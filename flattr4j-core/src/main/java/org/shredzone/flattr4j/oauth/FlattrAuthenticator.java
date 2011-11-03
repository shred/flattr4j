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
import java.util.UUID;

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
 * @version $Revision$
 * @see <a href="http://tools.ietf.org/html/draft-ietf-oauth-v2-21">IETF OAuth V2</a>
 */
public class FlattrAuthenticator {
    private static final String ENCODING = "utf-8";

    private final ConsumerKey consumerKey;
    
    private String requestTokenUrl = "https://flattr.com/oauth/authorize";
    private String accessTokenUrl = "http://flattr.com/oauth/token";
    
    private String callbackUrl = null;
    
    private EnumSet<Scope> scope = EnumSet.noneOf(Scope.class);

    /**
     * A callback URL. If set, it is invoked by Flattr in order to transport the PIN that
     * is required for the access token. {@code null} means that the user is required to
     * retrieve the PIN from a Flattr URL and enter it manually. Use {@code null} if you
     * cannot provide a callback URL, for example on a desktop or handheld device
     * application.
     * <p>
     * <em>NOTE:</em> This callback URL must <em>exactly</em> match the URL that was used
     * on registration, or the authentication process will fail.
     * <em>NOTE:</em> If you registered your application as "client" type, callback url
     * <em>must be</em> {@code null}.
     * <p>
     * Defaults to {@code null}.
     */
    public String getCallbackUrl()          { return callbackUrl; }
    public void setCallbackUrl(String callbackUrl) { this.callbackUrl = callbackUrl; }

    /**
     * The access scope. This is a set of rights the consumer needs. The set of rights
     * is shown to the user on authentication. Defaults to none.
     */
    public EnumSet<Scope> getScope()        { return scope; }
    public void setScope(EnumSet<Scope> scope) { this.scope = scope; }

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
     * following GET parameters:
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
     * <li>{@code state}: the value of the state parameter, if it was set</li>
     * </ul>
     * This way the user only needs to log in at Flattr, but does not need to copy a code
     * to complete the authorization.
     * <p>
     * Scope flags need to be set properly before invocation.
     * 
     * @param state
     *          A value that is passed to the callback URL, to maintain state between
     *          request and callback. Optional, may be {@code null}.
     * @return The authentication URL that the user must visit
     * @since 2.0
     */
    public String authenticate(String state) throws FlattrException {
        try {
            StringBuilder url = new StringBuilder();
            url.append(requestTokenUrl);
            url.append("?response_type=code");
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
     * The returned access token can be serialized or its contents persisted in a
     * database. It is needed to access the Flattr API with a valid authentication until
     * revoked by the user.
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

        try {
            FlattrObject response = conn.singleResult();
            String accessToken = response.get("access_token");
            String tokenType = response.get("token_type");

            if (!"bearer".equals(tokenType)) {
                throw new FlattrException("Unknown token type " + tokenType);
            }
            
            return new AccessToken(accessToken);
        } finally {
            conn.close();
        }
    }

    /**
     * Creates a {@link Connector} for sending requests with basic authentication.
     * 
     * @return {@link Connector}
     */
    protected Connector createConnector() {
        return new FlattrConnector();
    }

    /**
     * Builds a scope string for the scope flags set.
     * 
     * @return Scope string: scope texts, separated by comma.
     */
    @SuppressWarnings("deprecation")
    protected String buildScopeString() {
        StringBuilder sb = new StringBuilder();
        
        if (scope.contains(Scope.FLATTR) || scope.contains(Scope.CLICK)) sb.append(" flattr");
        if (scope.contains(Scope.THING) || scope.contains(Scope.PUBLISH)) sb.append(" thing");
        if (scope.contains(Scope.EXTENDEDREAD)) sb.append(" extendedread");
        
        return sb.toString().trim();
    }
    
    /**
     * Fetches a {@link RequestToken} from Flattr. When this method returns, the user is
     * required to visit the url given in {@link RequestToken#getAuthUrl()} and retrieve a
     * PIN. The PIN needs to be passed to
     * {@link #fetchAccessToken(String, String, String)} in order to complete the
     * authorization.
     * <p>
     * <em>NOTE:</em> Since 2.0, the following parameters are passed to the callback url:
     * <ul>
     * <li>{@code code}: the code (was {@code oauth_verifier})</li>
     * <li>{@code state}: the request token (was {@code oauth_token}), for your internal
     * use to find the matching principal for the access token.</li>
     * </ul>
     * This way the user only needs to log in at Flattr, but does not need to enter a PIN
     * to complete the authorization.
     * <p>
     * Scope flags need to be properly set before invocation.
     * 
     * @return The generated {@link RequestToken}
     * @deprecated Use {@link #authenticate(java.lang.String)} instead. Do not use in new
     *             code!
     */
    @Deprecated
    public RequestToken fetchRequestToken() throws FlattrException {
        RequestToken result = new RequestToken();
        String code = UUID.randomUUID().toString();
        result.setToken(code);
        result.setSecret(code);
        result.setAuthUrl(authenticate(code));
        return result;
    }
    
    /**
     * Fetches an {@link AccessToken} from Flattr. This method is just a convenience call
     * that gets the request token and secret from {@link RequestToken} and invokes
     * {@link #fetchAccessToken(String, String, String)}. It can be used when the
     * {@link RequestToken} instance from {@link #fetchRequestToken()} is still available
     * or was serialized.
     * 
     * @param token
     *            {@link RequestToken} from {@link #fetchRequestToken()}. It is ignored
     *            since 2.0.
     * @param pin
     *            The PIN that was returned from Flattr
     * @return {@link AccessToken} that allows access to the Flattr API
     * @see #fetchAccessToken(String, String, String)
     * @deprecated Use {@link #fetchAccessToken(java.lang.String)} instead. Do not use in
     *             new code!
     */
    @Deprecated
    public AccessToken fetchAccessToken(RequestToken token, String pin) throws FlattrException {
        return fetchAccessToken(pin);
    }
    
    /**
     * Fetches an {@link AccessToken} that gives access to the Flattr API. After the user
     * entered the PIN (or when the callback url was invoked), this method is invoked to
     * complete the authorization process.
     * <p>
     * The returned access token can be serialized or its contents persisted in a
     * database. It is needed to access the Flattr API with a valid authentication until
     * revoked by the user.
     * <p>
     * Scope flags are ignored at this call.
     * 
     * @param token
     *            The request token. It is ignored since 2.0.
     * @param secret
     *            The request token secret. It is ignored since 2.0.
     * @param pin
     *            The PIN that was returned from Flattr
     * @return {@link AccessToken} giving access to the Flattr API for the authenticated
     *         user
     * @deprecated Use {@link #fetchAccessToken(java.lang.String)} instead. Do not use in
     *             new code!
     */
    @Deprecated
    public AccessToken fetchAccessToken(String token, String secret, String pin) throws FlattrException {
        return fetchAccessToken(pin);
    }

}
