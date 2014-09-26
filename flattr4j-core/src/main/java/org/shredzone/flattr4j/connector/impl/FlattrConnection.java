/*
 * flattr4j - A Java library for Flattr
 *
 * Copyright (C) 2011 Richard "Shred" Körber
 *   http://flattr4j.shredzone.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License / GNU Lesser
 * General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.shredzone.flattr4j.connector.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.shredzone.flattr4j.connector.Connection;
import org.shredzone.flattr4j.connector.FlattrObject;
import org.shredzone.flattr4j.connector.RateLimit;
import org.shredzone.flattr4j.connector.RequestType;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.exception.FlattrServiceException;
import org.shredzone.flattr4j.exception.ForbiddenException;
import org.shredzone.flattr4j.exception.MarshalException;
import org.shredzone.flattr4j.exception.NoMoneyException;
import org.shredzone.flattr4j.exception.NotFoundException;
import org.shredzone.flattr4j.exception.RateLimitExceededException;
import org.shredzone.flattr4j.exception.ValidationException;
import org.shredzone.flattr4j.oauth.AccessToken;
import org.shredzone.flattr4j.oauth.ConsumerKey;

/**
 * Default implementation of {@link Connection}.
 *
 * @author Richard "Shred" Körber
 */
public class FlattrConnection implements Connection {
    private static final Logger LOG = new Logger("flattr4j", FlattrConnection.class.getName());
    private static final String ENCODING = "utf-8";

    private static final boolean NEW_API;
    static {
        boolean newApi = false;

        // Android safe way to find out if Apache HTTP Client 4.1 or higher is available
        for (Constructor<?> c : Scheme.class.getConstructors()) {
            Class<?>[] types = c.getParameterTypes();
            if (types.length == 3
                    && types[0].isAssignableFrom(String.class)
                    && types[1].isAssignableFrom(int.class)) {
                newApi = true;
                break;
            }
        }

        NEW_API = newApi;
    }

    private HttpRequestBase request;
    private String baseUrl;
    private String call;
    private RequestType type;
    private ConsumerKey key;
    private List<NameValuePair> queryParam;
    private List<NameValuePair> formParam;
    private RateLimit limit;

    /**
     * Creates a new {@link FlattrConnection} for the given {@link RequestType}.
     *
     * @param type
     *            {@link RequestType} to be used
     */
    public FlattrConnection(RequestType type) {
        this.type = type;
    }

    @Override
    public Connection url(String url) {
        this.baseUrl = url;
        LOG.verbose("-> baseUrl {0}", url);
        return this;
    }

    @Override
    public Connection call(String call) {
        this.call = call;
        LOG.verbose("-> call {0}", call);
        return this;
    }

    @Override
    public Connection token(AccessToken token) {
        createRequest();
        request.setHeader("Authorization", "Bearer " + token.getToken());
        return this;
    }

    @Override
    public Connection key(ConsumerKey key) {
        this.key = key;
        return this;
    }

    @Override
    public Connection parameter(String name, String value) {
        try {
            call = call.replace(":" + name, URLEncoder.encode(value, ENCODING));
            LOG.verbose("-> param {0} = {1}", name, value);
            return this;
        } catch (UnsupportedEncodingException ex) {
            // should never be thrown, as "utf-8" encoding is available on any VM
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Connection parameterArray(String name, String[] value) {
        try {
            StringBuilder sb = new StringBuilder();
            for (int ix = 0; ix < value.length; ix++) {
                if (ix > 0) {
                    // Is it genius or madness, but the Flattr server does not accept
                    // URL encoded ','!
                    sb.append(',');
                }
                sb.append(URLEncoder.encode(value[ix], ENCODING));
            }
            call = call.replace(":" + name, sb.toString());
            LOG.verbose("-> param {0} = [{1}]", name, sb.toString());
            return this;
        } catch (UnsupportedEncodingException ex) {
            // should never be thrown, as "utf-8" encoding is available on any VM
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Connection query(String name, String value) {
        if (queryParam == null) {
            queryParam = new ArrayList<NameValuePair>();
        }
        queryParam.add(new BasicNameValuePair(name, value));
        LOG.verbose("-> query {0} = {1}", name, value);
        return this;
    }

    @Override
    public Connection data(FlattrObject data) {
        createRequest();

        if (!(request instanceof HttpEntityEnclosingRequestBase)) {
            throw new IllegalArgumentException("No data allowed for RequestType " + type);
        }

        try {
            StringEntity body = new StringEntity(data.toString(), ENCODING);
            body.setContentType("application/json");
            body.setContentEncoding(ENCODING);
            ((HttpEntityEnclosingRequestBase) request).setEntity(body);
        } catch (UnsupportedEncodingException ex) {
            // should never be thrown, as "utf-8" encoding is available on any VM
            throw new RuntimeException(ex);
        }

        LOG.verbose("-> JSON body: {0}", data);
        return this;
    }

    @Override
    public Connection form(String name, String value) {
        createRequest();

        if (!(request instanceof HttpPost)) {
            throw new IllegalArgumentException("No form allowed for RequestType " + type);
        }

        if (formParam == null) {
            formParam = new ArrayList<NameValuePair>();
        }
        formParam.add(new BasicNameValuePair(name, value));

        LOG.verbose("-> form {0} = {1}", name, value);
        return this;
    }

    @Override
    public Connection rateLimit(RateLimit limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public Collection<FlattrObject> result() throws FlattrException {
        AbstractHttpClient client = null;

        try {
            createRequest();

            String queryString = "";
            if (queryParam != null) {
                queryString = '?' + URLEncodedUtils.format(queryParam, ENCODING);
            }

            if (call != null) {
                request.setURI(new URI(baseUrl).resolve(call + queryString));
            } else {
                request.setURI(new URI(baseUrl + queryString));
            }

            request.setHeader("Accept", "application/json");
            request.setHeader("Accept-Encoding", "gzip");

            if (formParam != null) {
                UrlEncodedFormEntity body = new UrlEncodedFormEntity(formParam, ENCODING);
                body.setContentType("application/x-www-form-urlencoded");
                body.setContentEncoding(ENCODING);
                ((HttpPost) request).setEntity(body);
            }

            client = createHttpClient();

            if (key != null) {
                client.getCredentialsProvider().setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(key.getKey(), key.getSecret())
                );
            }

            LOG.info("Sending Flattr request: {0}", call);

            HttpResponse response = client.execute(request);

            if (limit != null) {
                Header remainingHeader = response.getFirstHeader("X-RateLimit-Remaining");
                if (remainingHeader != null) {
                    limit.setRemaining(Long.parseLong(remainingHeader.getValue()));
                } else {
                    limit.setRemaining(null);
                }

                Header limitHeader = response.getFirstHeader("X-RateLimit-Limit");
                if (limitHeader != null) {
                    limit.setLimit(Long.parseLong(limitHeader.getValue()));
                } else {
                    limit.setLimit(null);
                }

                Header currentHeader = response.getFirstHeader("X-RateLimit-Current");
                if (currentHeader != null) {
                    limit.setCurrent(Long.parseLong(currentHeader.getValue()));
                } else {
                    limit.setCurrent(null);
                }

                Header resetHeader = response.getFirstHeader("X-RateLimit-Reset");
                if (resetHeader != null) {
                    limit.setReset(new Date(Long.parseLong(resetHeader.getValue()) * 1000L));
                } else {
                    limit.setReset(null);
                }
            }

            List<FlattrObject> result;

            if (assertStatusOk(response)) {
                // Status is OK and there is content
                Object resultData = new JSONTokener(readResponse(response)).nextValue();
                if (resultData instanceof JSONArray) {
                    JSONArray array = (JSONArray) resultData;
                    result = new ArrayList<FlattrObject>(array.length());
                    for (int ix = 0; ix < array.length(); ix++) {
                        FlattrObject fo = new FlattrObject(array.getJSONObject(ix));
                        result.add(fo);
                        LOG.verbose("<- JSON result: {0}", fo);
                    }
                    LOG.verbose("<-   {0} rows", array.length());
                } else if (resultData instanceof JSONObject) {
                    FlattrObject fo = new FlattrObject((JSONObject) resultData);
                    result = Collections.singletonList(fo);
                    LOG.verbose("<- JSON result: {0}", fo);
                } else {
                    throw new MarshalException("unexpected result type " + resultData.getClass().getName());
                }
            } else {
                // Status was OK, but there is no content
                result = Collections.emptyList();
            }

            return result;
        } catch (URISyntaxException ex) {
            throw new IllegalStateException("bad baseUrl");
        } catch (IOException ex) {
            throw new FlattrException("API access failed: " + call, ex);
        } catch (JSONException ex) {
            throw new MarshalException(ex);
        } catch (ClassCastException ex) {
            throw new FlattrException("Unexpected result type", ex);
        } finally {
            if (client != null) {
                disposeHttpClient(client);
            }
            request = null;
        }
    }

    @Override
    public FlattrObject singleResult() throws FlattrException {
        Collection<FlattrObject> result = result();
        if (result.size() == 1) {
            return result.iterator().next();
        } else {
            throw new MarshalException("Expected 1, but got " + result.size() + " result rows");
        }
    }

    /**
     * Creates a new http request for the given {@link RequestType}. Does nothing if a
     * request has already been created.
     */
    private void createRequest() {
        if (request == null) {
            switch (type) {
                case GET:
                    request = new HttpGet();
                    break;

                case POST:
                    request = new HttpPost();
                    break;

                case PUT:
                    request = new HttpPut();
                    break;

                case DELETE:
                    request = new HttpDelete();
                    break;

                case PATCH:
                    request = new HttpPost() {
                        @Override
                        public String getMethod() {
                            return "PATCH";
                        }
                    };
                    break;

                default:
                    throw new IllegalArgumentException("Unknown type " + type);
            }
        }
    }

    /**
     * Reads the returned HTTP response as string.
     *
     * @param response
     *            {@link HttpResponse} to read from
     * @return Response read
     */
    private String readResponse(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();

        Charset charset = Charset.forName(ENCODING);
        Header contentType = entity.getContentType();
        if (contentType != null) {
            for (HeaderElement elem : contentType.getElements()) {
                if ("charset".equals(elem.getName())) {
                    charset = Charset.forName(elem.getValue());
                    break;
                }
            }
        }

        InputStream in = entity.getContent();

        Header encoding = entity.getContentEncoding();
        if (encoding != null) {
            if ("gzip".equals(encoding.getValue())) {
                in = new GZIPInputStream(in);
            }
        }

        try {
            Reader reader = new InputStreamReader(in, charset);

            // Sadly, the Android API does not offer a JSONTokener for a Reader.
            char[] buffer = new char[1024];
            StringBuilder sb = new StringBuilder();

            int len;
            while ((len = reader.read(buffer)) >= 0) {
                sb.append(buffer, 0, len);
            }

            return sb.toString();
        } finally {
            in.close();
        }
    }

    /**
     * Assert that the HTTP result is OK, otherwise generate and throw an appropriate
     * {@link FlattrException}.
     *
     * @param response
     *            {@link HttpResponse} to assert
     * @return {@code true} if the status is OK and there is a content, {@code false} if
     *         the status is OK but there is no content. (If the status is not OK, an
     *         exception is thrown.)
     */
    private boolean assertStatusOk(HttpResponse response) throws FlattrException {
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_CREATED) {
            return true;
        }
        if (statusCode == HttpStatus.SC_NO_CONTENT) {
            return false;
        }

        String error = null, desc = null;

        try {
            JSONObject errorData = (JSONObject) new JSONTokener(readResponse(response)).nextValue();
            LOG.verbose("<- ERROR {0}: {1}", statusCode, errorData);

            error = errorData.optString("error");
            desc = errorData.optString("error_description");
            LOG.error("Flattr ERROR {0}: {1}", error, desc);
        } catch (IOException ex) {
            throw new FlattrException("Could not read response", ex);
        } catch (ClassCastException ex) {
            // An unexpected JSON type was returned, just throw a generic error
        } catch (JSONException ex) {
            // No valid error message was returned, just throw a generic error
        }

        if (error != null && desc != null) {
            if (   "flattr_once".equals(error)
                || "flattr_owner".equals(error)
                || "thing_owner".equals(error)
                || "forbidden".equals(error)
                || "insufficient_scope".equals(error)
                || "unauthorized".equals(error)
                || "subscribed".equals(error)) {
                throw new ForbiddenException(error, desc);

            } else if ("no_means".equals(error)
                || "no_money".equals(error)) {
                throw new NoMoneyException(error, desc);

            } else  if ("not_found".equals(error)) {
                throw new NotFoundException(error, desc);

            } else if ("rate_limit_exceeded".equals(error)) {
                throw new RateLimitExceededException(error, desc);

            } else if ("invalid_parameters".equals(error)
                || "invalid_scope".equals(error)
                || "validation".equals(error)) {
                throw new ValidationException(error, desc);
            }

            // "not_acceptable", "server_error", "invalid_request", everything else...
            throw new FlattrServiceException(error, desc);
        }

        StatusLine line = response.getStatusLine();
        String msg = "HTTP " + line.getStatusCode() + ": " + line.getReasonPhrase();
        LOG.error("Flattr {0}", msg);
        throw new FlattrException(msg);
    }

    /**
     * Creates a {@link AbstractHttpClient} for sending the request.
     *
     * @return {@link AbstractHttpClient}
     */
    protected AbstractHttpClient createHttpClient() {
        if (NEW_API) {
            return new NewFlattrHttpClient();
        } else {
            return new FlattrHttpClient();
        }
    }

    /**
     * Disposes a {@link AbstractHttpClient}, releasing all resources.
     *
     * @param cl
     *            {@link AbstractHttpClient} to release
     */
    protected void disposeHttpClient(AbstractHttpClient cl) {
        cl.getConnectionManager().shutdown();
    }

}
