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
package org.shredzone.flattr4j.connector.impl;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthException;
import oauth.signpost.signature.HmacSha1MessageSigner;

import org.apache.http.client.methods.HttpUriRequest;
import org.shredzone.flattr4j.connector.Connector;
import org.shredzone.flattr4j.connector.Result;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.exception.FlattrServiceException;
import org.shredzone.flattr4j.oauth.AccessToken;
import org.shredzone.flattr4j.oauth.ConsumerKey;

/**
 * A {@link Connector} that uses OAuth for authorization.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class OAuthConnector extends OpenConnector {

    private ConsumerKey consumerKey;
    private AccessToken accessToken;
    
    public ConsumerKey getConsumerKey()         { return consumerKey; }
    public void setConsumerKey(ConsumerKey consumerKey) { this.consumerKey = consumerKey; }
    
    public AccessToken getAccessToken()         { return accessToken; }
    public void setAccessToken(AccessToken accessToken) { this.accessToken = accessToken; }

    @Override
    protected Result execute(HttpUriRequest request) throws FlattrException {
        try {
            createConsumer().sign(request);
        } catch (OAuthException ex) {
            throw new FlattrServiceException("Signing failed", ex);
        }
        
        return super.execute(request);
    }
    
    /**
     * Creates an {@link OAuthConsumer} that can be used for signing requests.
     * 
     * @return {@link OAuthConsumer}
     */
    protected OAuthConsumer createConsumer() {
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(consumerKey.getKey(), consumerKey.getSecret());
        consumer.setMessageSigner(new HmacSha1MessageSigner());
        consumer.setTokenWithSecret(accessToken.getToken(), accessToken.getSecret());
        return consumer;
    }
    
}
