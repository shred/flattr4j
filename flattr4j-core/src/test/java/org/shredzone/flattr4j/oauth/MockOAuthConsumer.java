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

import junit.framework.Assert;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.signature.OAuthMessageSigner;
import oauth.signpost.signature.SigningStrategy;

/**
 * A mock implementation of {@link OAuthConsumer} that is used for unit testing.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class MockOAuthConsumer implements OAuthConsumer {
    private static final long serialVersionUID = 8761690732895044058L;

    private final ConsumerKey consumerKey;

    private String token;
    private String tokenSecret;

    public MockOAuthConsumer(ConsumerKey consumerKey) {
        Assert.assertNotNull(consumerKey);
        Assert.assertNotNull(consumerKey.getKey());
        Assert.assertNotNull(consumerKey.getSecret());
        this.consumerKey = consumerKey;
    }

    @Override
    public String getConsumerKey() {
        return consumerKey.getKey();
    }

    @Override
    public String getConsumerSecret() {
        return consumerKey.getSecret();
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public String getTokenSecret() {
        return tokenSecret;
    }

    @Override
    public void setTokenWithSecret(String token, String tokenSecret) {
        Assert.assertNotNull(token);
        Assert.assertNotNull(tokenSecret);

        this.token = token;
        this.tokenSecret = tokenSecret;
    }

    @Override
    public HttpParameters getRequestParameters() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAdditionalParameters(HttpParameters additionalParameters) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMessageSigner(OAuthMessageSigner messageSigner) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSendEmptyTokens(boolean enable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSigningStrategy(SigningStrategy signingStrategy) {
        throw new UnsupportedOperationException();
    }

    @Override
    public HttpRequest sign(HttpRequest request)
        throws OAuthMessageSignerException,
            OAuthExpectationFailedException,
            OAuthCommunicationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public HttpRequest sign(Object request)
        throws OAuthMessageSignerException,
            OAuthExpectationFailedException,
            OAuthCommunicationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String sign(String url)
        throws OAuthMessageSignerException,
            OAuthExpectationFailedException,
            OAuthCommunicationException {
        throw new UnsupportedOperationException();
    }

}
