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

import java.util.EnumSet;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthException;

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
    
    private String requestTokenUrl = "https://api.flattr.com/oauth/request_token";
    private String accessTokenUrl = "https://api.flattr.com/oauth/access_token";
    private String authorizationUrl = "https://api.flattr.com/oauth/authenticate";
    
    private String callbackUrl = null;
    
    // WORKAROUND: Usually this would be OAuth.OUT_OF_BAND, but Flattr does not accept it
    private String outOfBandUrl = "";
    
    private EnumSet<Scope> scope = EnumSet.of(Scope.READ);
    
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
     * The access scope. This is a set of rights the consumer needs. The set of rights
     * is shown to the user on authentication. Defaults to {@value Scope#READ} only.
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
     * <p>
     * Scope flags need to be properly set before invocation.
     * 
     * @return The generated {@link RequestToken}
     */
    public RequestToken fetchRequestToken() throws FlattrException {
        try {
            OAuthConsumer consumer = createConsumer();
            OAuthProvider provider = createProvider();
            String authUrl = provider.retrieveRequestToken(consumer,
                            (callbackUrl != null ? callbackUrl : outOfBandUrl));
            
            authUrl = OAuth.addQueryParameters(authUrl, "access_scope", buildScopeString());
            
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
     * <p>
     * Scope flags are ignored at this call.
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
            
            // WORKAROUND: OAUTH_VERIFIER is not sent in OAuth 1.0 mode, but required by Flattr
            provider.setOAuth10a(true);
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
        return new CommonsHttpOAuthConsumer(consumerKey.getKey(), consumerKey.getSecret());
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

    /**
     * Builds a scope string for the scope flags set.
     * 
     * @return Scope string: scope texts, separated by comma.
     */
    private String buildScopeString() {
        StringBuilder sb = new StringBuilder();
        
        if (scope.contains(Scope.READ))         sb.append(",read");
        if (scope.contains(Scope.EXTENDEDREAD)) sb.append(",extendedread");
        if (scope.contains(Scope.PUBLISH))      sb.append(",publish");
        if (scope.contains(Scope.CLICK))        sb.append(",click");
        
        if (sb.length() > 0) sb.deleteCharAt(0);
        
        return sb.toString();
    }

}
