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
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

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
    private static final int TIMEOUT = 10000;
    private static final Pattern CHARSET = Pattern.compile(".*?charset=\"?(.*?)\"?\\s*(;.*)?", Pattern.CASE_INSENSITIVE);
    private static final String BASE64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

    private String baseUrl;
    private String call;
    private RequestType type;
    private ConsumerKey key;
    private AccessToken token;
    private FlattrObject data;
    private StringBuilder queryParams;
    private StringBuilder formParams;
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
        this.token = token;
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
        if (queryParams == null) {
            queryParams = new StringBuilder();
        }
        appendParam(queryParams, name, value);
        LOG.verbose("-> query {0} = {1}", name, value);
        return this;
    }

    @Override
    public Connection data(FlattrObject data) {
        if (formParams != null) {
            throw new IllegalArgumentException("no data permitted when form is used");
        }
        this.data = data;
        LOG.verbose("-> JSON body: {0}", data);
        return this;
    }

    @Override
    public Connection form(String name, String value) {
        if (data != null) {
            throw new IllegalArgumentException("no form permitted when data is used");
        }
        if (formParams == null) {
            formParams = new StringBuilder();
        }
        appendParam(formParams, name, value);
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
        try {
            String queryString = (queryParams != null ? "?" + queryParams : "");

            URL url;
            if (call != null) {
                url = new URI(baseUrl).resolve(call + queryString).toURL();
            } else {
                url = new URI(baseUrl + queryString).toURL();
            }

            HttpURLConnection conn = createConnection(url);
            conn.setRequestMethod(type.name());
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Accept-Charset", ENCODING);
            conn.setRequestProperty("Accept-Encoding", "gzip");

            if (token != null) {
                conn.setRequestProperty("Authorization", "Bearer " + token.getToken());
            } else if (key != null) {
                conn.setRequestProperty("Authorization", "Basic " +
                                base64(key.getKey() + ':' +  key.getSecret()));
            }

            byte[] outputData = null;
            if (data != null) {
                outputData = data.toString().getBytes(ENCODING);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setFixedLengthStreamingMode(outputData.length);
            } else if (formParams != null) {
                outputData = formParams.toString().getBytes(ENCODING);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setFixedLengthStreamingMode(outputData.length);
            }

            LOG.info("Sending Flattr request: {0}", call);
            conn.connect();

            if (outputData != null) {
                OutputStream out = conn.getOutputStream();
                try {
                    out.write(outputData);
                } finally {
                    out.close();
                }
            }

            if (limit != null) {
                String remainingHeader = conn.getHeaderField("X-RateLimit-Remaining");
                if (remainingHeader != null) {
                    limit.setRemaining(Long.parseLong(remainingHeader));
                } else {
                    limit.setRemaining(null);
                }

                String limitHeader = conn.getHeaderField("X-RateLimit-Limit");
                if (limitHeader != null) {
                    limit.setLimit(Long.parseLong(limitHeader));
                } else {
                    limit.setLimit(null);
                }

                String currentHeader = conn.getHeaderField("X-RateLimit-Current");
                if (currentHeader != null) {
                    limit.setCurrent(Long.parseLong(currentHeader));
                } else {
                    limit.setCurrent(null);
                }

                String resetHeader = conn.getHeaderField("X-RateLimit-Reset");
                if (resetHeader != null) {
                    limit.setReset(new Date(Long.parseLong(resetHeader) * 1000L));
                } else {
                    limit.setReset(null);
                }
            }

            List<FlattrObject> result;

            if (assertStatusOk(conn)) {
                // Status is OK and there is content
                Object resultData = new JSONTokener(readResponse(conn)).nextValue();
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
     * Reads the returned HTTP response as string.
     *
     * @param conn
     *            {@link HttpURLConnection} to read from
     * @return Response read
     */
    private String readResponse(HttpURLConnection conn) throws IOException {
        InputStream in = null;

        try {
            in = conn.getErrorStream();
            if (in == null) {
                in = conn.getInputStream();
            }

            if ("gzip".equals(conn.getContentEncoding())) {
                in = new GZIPInputStream(in);
            }

            Charset charset = getCharset(conn.getContentType());
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
            if (in != null) {
                in.close();
            }
        }
    }

    /**
     * Assert that the HTTP result is OK, otherwise generate and throw an appropriate
     * {@link FlattrException}.
     *
     * @param conn
     *            {@link HttpURLConnection} to assert
     * @return {@code true} if the status is OK and there is a content, {@code false} if
     *         the status is OK but there is no content. (If the status is not OK, an
     *         exception is thrown.)
     */
    private boolean assertStatusOk(HttpURLConnection conn) throws FlattrException {
        String error = null, desc = null, httpStatus = null;

        try {
            int statusCode = conn.getResponseCode();

            if (statusCode == HttpURLConnection.HTTP_OK || statusCode == HttpURLConnection.HTTP_CREATED) {
                return true;
            }
            if (statusCode == HttpURLConnection.HTTP_NO_CONTENT) {
                return false;
            }

            httpStatus = "HTTP " + statusCode + ": " + conn.getResponseMessage();

            JSONObject errorData = (JSONObject) new JSONTokener(readResponse(conn)).nextValue();
            LOG.verbose("<- ERROR {0}: {1}", statusCode, errorData);

            error = errorData.optString("error");
            desc = errorData.optString("error_description");
            LOG.error("Flattr ERROR {0}: {1}", error, desc);
        } catch (HttpRetryException ex) {
            // Could not read error response because HttpURLConnection sucketh
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

        LOG.error("Flattr {0}", httpStatus);
        throw new FlattrException(httpStatus);
    }

    /**
     * Creates a {@link HttpURLConnection} to the given url. Override to configure the
     * connection.
     *
     * @param url
     *            {@link URL} to connect to
     * @return {@link HttpURLConnection} that is connected to the url and is
     *         preconfigured.
     */
    protected HttpURLConnection createConnection(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(TIMEOUT);
        conn.setReadTimeout(TIMEOUT);
        conn.setUseCaches(false);
        conn.setRequestProperty("User-Agent", "flattr4j");
        return conn;
    }

    /**
     * Appends a HTTP parameter to a string builder.
     *
     * @param builder
     *            {@link StringBuffer} to append to
     * @param key
     *            parameter key
     * @param value
     *            parameter value
     */
    private void appendParam(StringBuilder builder, String key, String value) {
        try {
            if (builder.length() > 0) {
                builder.append('&');
            }
            builder.append(URLEncoder.encode(key, ENCODING));
            builder.append('=');
            builder.append(URLEncoder.encode(value, ENCODING));
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalArgumentException("Unknown encoding " + ENCODING);
        }
    }

    /**
     * Gets the {@link Charset} from the content-type header. If there is no charset, the
     * default charset is returned instead.
     *
     * @param contentType
     *            content-type header, may be {@code null}
     * @return {@link Charset}
     */
    protected Charset getCharset(String contentType) {
        Charset charset = Charset.forName(ENCODING);
        if (contentType != null) {
            Matcher m = CHARSET.matcher(contentType);
            if (m.matches()) {
                try {
                    charset = Charset.forName(m.group(1));
                } catch (UnsupportedCharsetException ex) {
                    // ignore and return default charset
                }
            }
        }
        return charset;
    }

    /**
     * Base64 encodes a string.
     *
     * @param str
     *            String to encode
     * @return Encoded string
     */
    protected String base64(String str) {
        // There is no common Base64 encoder in Java and Android. Sometimes I hate Java.
        try {
            byte[] data = str.getBytes(ENCODING);

            StringBuilder sb = new StringBuilder();
            for (int ix = 0; ix < data.length; ix += 3) {
                int triplet = (data[ix] & 0xFF) << 16;
                if (ix + 1 < data.length) {
                    triplet |= (data[ix+1] & 0xFF) << 8;
                }
                if (ix + 2 < data.length) {
                    triplet |= (data[ix+2] & 0xFF);
                }

                for (int iy = 0; iy < 4; iy++) {
                    if (ix + iy <= data.length) {
                        int ch = (triplet & 0xFC0000) >> 18;
                        sb.append(BASE64.charAt(ch));
                        triplet <<= 6;
                    } else {
                        sb.append('=');
                    }
                }
            }

            return sb.toString();
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalArgumentException(ENCODING, ex);
        }
    }

}
