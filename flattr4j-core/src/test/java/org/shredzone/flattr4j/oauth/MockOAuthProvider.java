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

import java.util.Map;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.OAuthProviderListener;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import oauth.signpost.http.HttpParameters;

import org.junit.Assert;

/**
 * A mock implementation of {@link OAuthProvider} that is used for unit testing.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class MockOAuthProvider implements OAuthProvider {
    private static final long serialVersionUID = 5924541372865269685L;
    
    private String requestUrl;
    private boolean oauth10a = false;

    public MockOAuthProvider(String requestUrl, String accessUrl, String authorizeUrl) {
        Assert.assertEquals("https://api.flattr.com/oauth/request_token", requestUrl);
        Assert.assertEquals("https://api.flattr.com/oauth/access_token", accessUrl);
        Assert.assertEquals("https://api.flattr.com/oauth/authenticate", authorizeUrl);
        this.requestUrl = requestUrl;
    }

    @Override
    public String retrieveRequestToken(OAuthConsumer consumer, String callbackUrl)
    throws OAuthMessageSignerException, OAuthNotAuthorizedException, OAuthExpectationFailedException, OAuthCommunicationException {
        Assert.assertNotNull(consumer);
        Assert.assertTrue(consumer instanceof MockOAuthConsumer);
        Assert.assertEquals("ck-abc", consumer.getConsumerKey());
        Assert.assertEquals("ck-123", consumer.getConsumerSecret());

        Assert.assertNotNull(callbackUrl);
        Assert.assertTrue(callbackUrl.equals("oob") || callbackUrl.startsWith("http"));

        Assert.assertTrue(oauth10a);

        consumer.setTokenWithSecret("req-abc", "req-123");
        return requestUrl + "?mock=true";
    }

    @Override
    public void retrieveAccessToken(OAuthConsumer consumer, String oauthVerifier)
    throws OAuthMessageSignerException, OAuthNotAuthorizedException, OAuthExpectationFailedException, OAuthCommunicationException {
        Assert.assertNotNull(consumer);
        Assert.assertTrue(consumer instanceof MockOAuthConsumer);
        Assert.assertEquals("ck-abc", consumer.getConsumerKey());
        Assert.assertEquals("ck-123", consumer.getConsumerSecret());

        Assert.assertEquals("req-abc", consumer.getToken());
        Assert.assertEquals("req-123", consumer.getTokenSecret());

        Assert.assertEquals("654321", oauthVerifier);

        Assert.assertTrue(oauth10a);

        consumer.setTokenWithSecret("acc-abc", "acc-123");
    }

    @Override
    public void setOAuth10a(boolean isOAuth10aProvider) {
        oauth10a = isOAuth10aProvider;
    }

    @Override
    public boolean isOAuth10a() {
        return oauth10a;
    }

    @Override
    public String getAccessTokenEndpointUrl() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getAuthorizationWebsiteUrl() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getRequestTokenEndpointUrl() {
        throw new UnsupportedOperationException();
    }

    @Override
    public HttpParameters getResponseParameters() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setResponseParameters(HttpParameters parameters) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setListener(OAuthProviderListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeListener(OAuthProviderListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public void setRequestHeader(String header, String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public Map<String, String> getRequestHeaders() {
        throw new UnsupportedOperationException();
    }

}
