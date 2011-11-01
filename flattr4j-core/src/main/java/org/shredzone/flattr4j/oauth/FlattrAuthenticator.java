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
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthException;

import org.apache.http.client.HttpClient;
import org.shredzone.flattr4j.connector.FlattrHttpClient;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.exception.FlattrServiceException;

/**
 * Helps through the OAuth authentication process at Flattr.
 * <p>
 * In API v2, OAuth2 is used, which drastically changes the authentication process.
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
    
    private String outOfBandUrl = OAuth.OUT_OF_BAND;
    
    private EnumSet<Scope> scope = EnumSet.of(Scope.READ);

    /**
     * The OAuth request token URL from Flattr.
     */
    @Deprecated
    public String getRequestTokenUrl()      { return requestTokenUrl; }

    @Deprecated
    public void setRequestTokenUrl(String requestTokenUrl) { this.requestTokenUrl = requestTokenUrl; }

    /**
     * The OAuth access token URL from Flattr.
     */
    @Deprecated
    public String getAccessTokenUrl()       { return accessTokenUrl; }
    
    @Deprecated
    public void setAccessTokenUrl(String accessTokenUrl) { this.accessTokenUrl = accessTokenUrl; }

    /**
     * The OAuth authorization URL from Flattr.
     */
    @Deprecated
    public String getAuthorizationUrl()     { return authorizationUrl; }
    
    @Deprecated
    public void setAuthorizationUrl(String authorizationUrl) { this.authorizationUrl = authorizationUrl; }

    /**
     * A callback URL. If set, it is invoked by Flattr in order to transport the PIN that
     * is required for the access token. {@code null} means that the user is required to
     * retrieve the PIN from a Flattr URL and enter it manually. Use {@code null} if you
     * cannot provide a callback URL, for example on a desktop or handheld device
     * application.
     * <p>
     * <em>NOTE:</em> If you registered your application as "client" type, you MUST NOT
     * invoke this method.
     * <p>
     * Defaults to {@code null}.
     */
    public String getCallbackUrl()          { return callbackUrl; }
    public void setCallbackUrl(String callbackUrl) { this.callbackUrl = callbackUrl; }

    /**
     * The access scope. This is a set of rights the consumer needs. The set of rights
     * is shown to the user on authentication. Defaults to {@link Scope#READ} only.
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
     * @deprecated Instead of fetching a {@link RequestToken}, the URL is returned
     *             directly by the autenticate() method. To identify the calling principal
     *             on the callback url, a "state" parameter may be passed as well.
     */
    @Deprecated
    public RequestToken fetchRequestToken() throws FlattrException {
        try {
            OAuthConsumer consumer = createConsumer(consumerKey);
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
     * @deprecated {@link RequestToken} is not used any more. Only the PIN (called code)
     *             is passed to fetchAccessToken().
     */
    @Deprecated
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
     * @deprecated {@link RequestToken} is not used any more. Only the PIN (called code)
     *             is passed to fetchAccessToken().
     */
    public AccessToken fetchAccessToken(String token, String secret, String pin) throws FlattrException {
        try {
            OAuthConsumer consumer = createConsumer(consumerKey);
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
     * @param ck
     *      {@link ConsumerKey} to be used
     * @return {@link OAuthConsumer} that was created
     * @deprecated Not used in OAuth2
     */
    @Deprecated
    protected OAuthConsumer createConsumer(ConsumerKey ck) {
        return new CommonsHttpOAuthConsumer(ck.getKey(), ck.getSecret());
    }

    /**
     * Creates a {@link OAuthProvider} that can be used for Flattr.
     * 
     * @return {@link OAuthProvider} that was created
     * @deprecated Not used in OAuth2
     */
    @Deprecated
    protected OAuthProvider createProvider() {
        OAuthProvider provider = new CommonsHttpOAuthProvider(
                        requestTokenUrl, accessTokenUrl, authorizationUrl,
                        createHttpClient());
        provider.setOAuth10a(true);
        return provider;
    }
    
    /**
     * Creates a {@link HttpClient} for sending the request.
     * 
     * @return {@link HttpClient}
     * @deprecated
     */
    @Deprecated
    protected HttpClient createHttpClient() {
        return new FlattrHttpClient();
    }

    /**
     * Builds a scope string for the scope flags set.
     * 
     * @return Scope string: scope texts, separated by comma.
     */
    protected String buildScopeString() {
        StringBuilder sb = new StringBuilder();
        
        if (scope.contains(Scope.READ))         sb.append(",read");
        if (scope.contains(Scope.EXTENDEDREAD)) sb.append(",extendedread");
        if (scope.contains(Scope.PUBLISH))      sb.append(",publish");
        if (scope.contains(Scope.CLICK))        sb.append(",click");
        
        if (sb.length() > 0) sb.deleteCharAt(0);
        
        return sb.toString();
    }

}
