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
package org.shredzone.flattr4j.oauth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.EnumSet;

import org.shredzone.flattr4j.connector.Connection;
import org.shredzone.flattr4j.connector.Connector;
import org.shredzone.flattr4j.connector.FlattrObject;
import org.shredzone.flattr4j.connector.RequestType;
import org.shredzone.flattr4j.connector.impl.FlattrConnector;
import org.shredzone.flattr4j.exception.FlattrException;

/**
 * Helps through the OAuth2 authentication process at Flattr.
 *
 * @author Richard "Shred" Körber
 * @see <a href="http://tools.ietf.org/html/draft-ietf-oauth-v2-21">IETF OAuth V2</a>
 */
public class FlattrAuthenticator {
    private static final String ENCODING = "utf-8";

    private final ConsumerKey consumerKey;

    private String requestTokenUrl = "https://flattr.com/oauth/authorize";
    private String accessTokenUrl = "https://flattr.com/oauth/token";
    private String responseType = "code";

    private String callbackUrl = null;

    private EnumSet<Scope> scope = EnumSet.noneOf(Scope.class);

   /**
     * The request token url
     * <p>
     * <em>NOTE:</em> This request token url must match the url defined at http://developers.flattr.net/api/#authorization
     * @return the defined request token url
     */
    public String getRequestTokenUrl() { return requestTokenUrl; }
    public void setRequestTokenUrl(String requestTokenUrl) { this.requestTokenUrl = requestTokenUrl; }

    /**
     * The access token url required to retrieve the access token for flattr.com
     * <p>
     * <em>NOTE:</em> This access token url must match the url defined at http://developers.flattr.net/api/#authorization
     * @return the defined access token url
     */
    public String getAccessTokenUrl() { return accessTokenUrl; }
    public void setAccessTokenUrl(String accessTokenUrl) { this.accessTokenUrl = accessTokenUrl; }

    /**
     * A callback URL. If set, the user is forwarded to this URL in order to pass the
     * valication code. {@code null} means that the user is required to retrieve the code
     * from Flattr and enter it manually. Use {@code null} if you cannot provide a
     * callback URL, for example on a desktop or handheld device application.
     * <p>
     * <em>NOTE:</em> This callback URL must <em>exactly</em> match the URL that was used
     * on registration. Otherwise the authentication process will fail.
     * <em>NOTE:</em> If you registered your application as "client" type, callback url
     * <em>must be</em> {@code null}.
     * <p>
     * Defaults to {@code null}.
     */
    public String getCallbackUrl()          { return callbackUrl; }
    public void setCallbackUrl(String callbackUrl) { this.callbackUrl = callbackUrl; }

    /**
     * The access scope. This is a set of rights the consumer needs. The set of rights
     * is shown to the user on authentication. Defaults to an empty set.
     */
    public EnumSet<Scope> getScope()        { return scope; }
    public void setScope(EnumSet<Scope> scope) { this.scope = scope; }

    /**
     * The OAuth response type. This is {@code code} or {@code token}, with the former
     * being the default.
     *
     * @since 2.3
     */
    public void setResponseType(String responseType) { this.responseType = responseType; }
    public String getResponseType()         { return responseType; }

    /**
     * Constructs a new instance with the given {@link ConsumerKey}.
     *
     * @param consumerKey {@link ConsumerKey}
     */
    public FlattrAuthenticator(ConsumerKey consumerKey) {
        this.consumerKey = consumerKey;
    }

    /**
     * Constructs a new instance with the given consumer key and secret.
     *
     * @param key
     *            Consumer key
     * @param secret
     *            Consumer secret
     */
    public FlattrAuthenticator(String key, String secret) {
        this(new ConsumerKey(key, secret));
    }

    /**
     * Authenticates this application against Flattr. The user is required to visit the
     * returned url and retrieve a code. The code needs to be passed to
     * {@link #fetchAccessToken(String)} in order to complete the authorization.
     * <p>
     * When a callback url was set, Flattr will forward the user to this url with the
     * following GET parameter:
     * <ul>
     * <li>{@code code}: the code</li>
     * </ul>
     * This way the user only needs to log in at Flattr, but does not need to copy a code
     * to complete the authorization.
     * <p>
     * Scope flags need to be set properly before invocation.
     *
     * @return The authentication URL that the user must visit
     * @since 2.0
     */
    public String authenticate() throws FlattrException {
        return authenticate(null);
    }

    /**
     * Authenticates this application against Flattr. The user is required to visit the
     * returned url and retrieve a code. The code needs to be passed to
     * {@link #fetchAccessToken(String)} in order to complete the authorization.
     * <p>
     * When a callback url was set, Flattr will forward the user to this url with the
     * following GET parameters:
     * <ul>
     * <li>{@code code}: the code</li>
     * <li>{@code state}: the value of the state parameter that was given</li>
     * </ul>
     * This way the user only needs to log in at Flattr, but does not need to copy a code
     * to complete the authorization.
     * <p>
     * Scope flags need to be set properly before invocation.
     *
     * @param state
     *          A value that is passed to the callback URL, to maintain state and
     *          reidentify the user between request and callback. Optional, may be
     *          {@code null}.
     * @return The authentication URL the user must visit
     * @since 2.0
     */
    public String authenticate(String state) throws FlattrException {
        try {
            StringBuilder url = new StringBuilder();
            url.append(requestTokenUrl);
            url.append("?response_type=").append(URLEncoder.encode(responseType, ENCODING));
            url.append("&client_id=").append(URLEncoder.encode(consumerKey.getKey(), ENCODING));

            if (callbackUrl != null) {
                url.append("&redirect_uri=").append(URLEncoder.encode(callbackUrl, ENCODING));
            }

            if (!scope.isEmpty()) {
                url.append("&scope=").append(URLEncoder.encode(buildScopeString(), ENCODING));
            }

            if (state != null) {
                url.append("&state=").append(URLEncoder.encode(state, ENCODING));
            }

            return url.toString();
        } catch (UnsupportedEncodingException ex) {
            // should never be thrown, as "utf-8" encoding is available on any VM
            throw new RuntimeException(ex);
        }
    }

    /**
     * Fetches an {@link AccessToken} that gives access to the Flattr API. After the user
     * entered the code (or when the callback url was invoked), this method is invoked to
     * complete the authorization process.
     * <p>
     * The returned access token can be serialized or the {@link AccessToken#getToken()}
     * persisted in a database. It is needed to access the Flattr API with a valid
     * authentication. The token is valid until revoked by the user.
     *
     * @param code
     *            The code that was returned from Flattr
     * @return {@link AccessToken} giving access to the Flattr API for the authenticated
     *         user
     */
    public AccessToken fetchAccessToken(String code) throws FlattrException {
        Connector connector = createConnector();

        Connection conn = connector.create(RequestType.POST)
                .url(accessTokenUrl)
                .key(consumerKey)
                .form("code", code)
                .form("grant_type", "authorization_code");

        if (callbackUrl != null) {
            conn.form("redirect_uri", callbackUrl);
        }

        FlattrObject response = conn.singleResult();
        String accessToken = response.get("access_token");
        String tokenType = response.get("token_type");

        if (!"bearer".equals(tokenType)) {
            throw new FlattrException("Unknown token type " + tokenType);
        }

        return new AccessToken(accessToken);
    }

    /**
     * Creates a {@link Connector} for sending requests.
     *
     * @return {@link Connector}
     */
    protected Connector createConnector() {
        return new FlattrConnector();
    }

    /**
     * Builds a scope string for the scope flags set.
     *
     * @return Scope string: scope texts, separated by spaces.
     */
    protected String buildScopeString() {
        StringBuilder sb = new StringBuilder();

        if (scope.contains(Scope.FLATTR      )) sb.append(" flattr");
        if (scope.contains(Scope.THING       )) sb.append(" thing");
        if (scope.contains(Scope.EMAIL       )) sb.append(" email");
        if (scope.contains(Scope.EXTENDEDREAD)) sb.append(" extendedread");

        return sb.toString().trim();
    }

}
