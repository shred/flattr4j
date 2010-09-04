/**
 * flattr4j - A Java library for Flattr
 *
 * Copyright (C) 2010 Richard "Shred" Körber
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

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthException;
import oauth.signpost.signature.HmacSha1MessageSigner;

import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.exception.FlattrServiceException;

/**
 * Helps through the OAuth authentication process at Flattr.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class FlattrAuthenticator {
    private final ConsumerKey consumerKey;
    
    private String requestTokenUrl = "http://api.testweb.flattr.com/oauth/request_token";
    private String accessTokenUrl = "http://api.testweb.flattr.com/oauth/access_token";
    private String authorizationUrl = "http://api.testweb.flattr.com/oauth/authenticate";
    
    private String callbackUrl = null;
    
    private String accessScope = "read,publish";
    
    /**
     * The OAuth request token URL from Flattr.
     */
    public String getRequestTokenUrl()      { return requestTokenUrl; }
    public void setRequestTokenUrl(String requestTokenUrl) { this.requestTokenUrl = requestTokenUrl; }

    /**
     * The OAuth access token URL from Flattr.
     */
    public String getAccessTokenUrl()       { return accessTokenUrl; }
    public void setAccessTokenUrl(String accessTokenUrl) { this.accessTokenUrl = accessTokenUrl; }

    /**
     * The OAuth authorization URL from Flattr.
     */
    public String getAuthorizationUrl()     { return authorizationUrl; }
    public void setAuthorizationUrl(String authorizationUrl) { this.authorizationUrl = authorizationUrl; }

    /**
     * A callback URL. If set, it is invoked by Flattr in order to transport the PIN that
     * is required for the access token. {@code null} means that the user is required to
     * retrieve the PIN from a Flattr URL and enter it manually. Use {@code null} if you
     * cannot provide a callback URL, for example on a desktop or handheld device
     * application.
     * <p>
     * Defaults to {@code null}.
     */
    public String getCallbackUrl()          { return callbackUrl; }
    public void setCallbackUrl(String callbackUrl) { this.callbackUrl = callbackUrl; }

    /**
     * The access scope. Defaults to {@code "read,publish"}.
     */
    public String getAccessScope()          { return accessScope; }
    public void setAccessScope(String accessScope) { this.accessScope = accessScope; }

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
        consumerKey = new ConsumerKey();
        consumerKey.setKey(key);
        consumerKey.setSecret(secret);
    }
    
    /**
     * Fetches a {@link RequestToken} from Flattr. When this method returns, the user is
     * required to visit the url given in {@link RequestToken#getAuthUrl()} and retrieve a
     * PIN. The PIN needs to be passed to
     * {@link #fetchAccessToken(String, String, String)} in order to complete the
     * authorization.
     * <p>
     * When a callback url was set, Flattr will invoke this url with these parameters:
     * <ul>
     * <li>{@code oauth_token}: the request token</li>
     * <li>{@code oauth_token_secret}: the request token secret</li>
     * <li>{@code oauth_verifier} the PIN</li>
     * </ul>
     * This way the user only needs to log in at Flattr, but does not need to enter a PIN
     * to complete the authorization.
     * 
     * @return The generated {@link RequestToken}
     */
    public RequestToken fetchRequestToken() throws FlattrException {
        try {
            OAuthConsumer consumer = createConsumer();
            OAuthProvider provider = createProvider();
            String authUrl = provider.retrieveRequestToken(consumer,
                            (callbackUrl != null ? callbackUrl : OAuth.OUT_OF_BAND));
            authUrl = OAuth.addQueryParameters(authUrl, "access_scope", accessScope);
            
            RequestToken result = new RequestToken();
            result.setToken(consumer.getToken());
            result.setSecret(consumer.getTokenSecret());
            result.setAuthUrl(authUrl);
            return result;
        } catch (OAuthException ex) {
            throw new FlattrServiceException("OAuth request token failed", ex);
        }
    }

    /**
     * Fetches an {@link AccessToken} from Flattr. This method is just a convenience call
     * that gets the request token and secret from {@link RequestToken} and invokes
     * {@link #fetchAccessToken(String, String, String)}. It can be used when the
     * {@link RequestToken} instance from {@link #fetchRequestToken()} is still available
     * or was serialized.
     * 
     * @param token
     *            {@link RequestToken} from {@link #fetchRequestToken()}
     * @param pin
     *            The PIN that was returned from Flattr
     * @return {@link AccessToken} that allows access to the Flattr API
     * @see #fetchAccessToken(String, String, String)
     */
    public AccessToken fetchAccessToken(RequestToken token, String pin) throws FlattrException {
        return fetchAccessToken(token.getToken(), token.getSecret(), pin);
    }

    /**
     * Fetches an {@link AccessToken} that gives access to the Flattr API. After the user
     * entered the PIN (or when the callback url was invoked), this method is invoked to
     * complete the authorization process.
     * <p>
     * The returned access token can be serialized or its contents persisted in a
     * database. It is needed to access the Flattr API with a valid authentication until
     * revoked by the user.
     * 
     * @param token
     *            The request token
     * @param secret
     *            The request token secret
     * @param pin
     *            The PIN that was returned from Flattr
     * @return {@link AccessToken} giving access to the Flattr API for the authenticated
     *         user
     */
    public AccessToken fetchAccessToken(String token, String secret, String pin) throws FlattrException {
        try {
            OAuthConsumer consumer = createConsumer();
            OAuthProvider provider = createProvider();
            consumer.setTokenWithSecret(token, secret);
            provider.retrieveAccessToken(consumer, pin);
            
            AccessToken result = new AccessToken();
            result.setToken(consumer.getToken());
            result.setSecret(consumer.getTokenSecret());
            return result;
        } catch (OAuthException ex) {
            throw new FlattrServiceException("OAuth request token failed", ex);
        }
    }

    /**
     * Creates a {@link OAuthConsumer} that can be used for Flattr.
     * 
     * @return {@link OAuthConsumer} that was created
     */
    protected OAuthConsumer createConsumer() {
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(consumerKey.getKey(), consumerKey.getSecret());
        // Flattr requires this signer!
        consumer.setMessageSigner(new HmacSha1MessageSigner());
        return consumer;
    }

    /**
     * Creates a {@link OAuthProvider} that can be used for Flattr.
     * 
     * @return {@link OAuthProvider} that was created
     */
    protected OAuthProvider createProvider() {
        OAuthProvider provider = new DefaultOAuthProvider(requestTokenUrl, accessTokenUrl, authorizationUrl);
        return provider;
    }
    
}
