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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthException;
import oauth.signpost.signature.HmacSha1MessageSigner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.shredzone.flattr4j.connector.FlattrConnector;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.exception.FlattrServiceException;
import org.shredzone.flattr4j.exception.InactiveException;
import org.shredzone.flattr4j.exception.NotFoundException;
import org.shredzone.flattr4j.exception.RegistrationFailedException;
import org.shredzone.flattr4j.oauth.AccessToken;
import org.shredzone.flattr4j.oauth.ConsumerKey;

/**
 * A {@link FlattrConnector} that uses Oauth for authorization.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class OauthConnector implements FlattrConnector {

    private static final String REST_VERSION = "0.0.1";
    private static final String RESULT_ENCODING = "iso-8859-1";
    private static final String POST_ENCODING = "utf-8";
    
    private String baseUrl = "http://api.flattr.com/rest/" + REST_VERSION + "/";
    
    private ConsumerKey consumerKey;
    private AccessToken accessToken;
    
    public ConsumerKey getConsumerKey()         { return consumerKey; }
    public void setConsumerKey(ConsumerKey consumerKey) { this.consumerKey = consumerKey; }
    
    public AccessToken getAccessToken()         { return accessToken; }
    public void setAccessToken(AccessToken accessToken) { this.accessToken = accessToken; }

    public String getBaseUrl()          { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }

    @Override
    public Reader call(String url) throws FlattrException {
        return execute(new HttpGet(baseUrl + url));
    }

    @Override
    public Reader post(String url, String data) throws FlattrException {
        try {
            HttpPost request = new HttpPost(baseUrl + url);
            
            StringEntity body = new StringEntity("data=" + URLEncoder.encode(data, POST_ENCODING));
            body.setContentType("application/x-www-form-urlencoded");
            request.setEntity(body);
            
            return execute(request);
        } catch (UnsupportedEncodingException ex) {
            throw new FlattrServiceException("Unknown encoding: " + POST_ENCODING);
        }
    }

    /**
     * Executes the request and handles the response.
     * 
     * @param request
     *            Request to be executed
     * @return {@link Reader} containing the response body
     * @throws FlattrException
     *             when the request could not be executed
     */
    private Reader execute(HttpUriRequest request) throws FlattrException {
        try {
            createConsumer().sign(request);
            
            HttpResponse response = createHttpClient().execute(request);
            assertStatusOk(response);
            return openReader(response);
        } catch (OAuthException ex) {
            throw new FlattrServiceException("Signing failed", ex);
        } catch (IOException ex) {
            throw new FlattrServiceException("HTTP access failed", ex);
        }
    }

    /**
     * Asserts that the HTTP status is OK. Throws a matching {@link FlattrException} if
     * not.
     * 
     * @param response
     *            Http Response to assert
     * @throws FlattrException
     *             if the HTTP status was not OK
     */
    private void assertStatusOk(HttpResponse response) throws FlattrException {
        StatusLine line = response.getStatusLine();
        String message = line.getStatusCode() + ": " + line.getReasonPhrase();
        
        switch (line.getStatusCode()) {
            case HttpStatus.SC_OK:                      // 200
                return;
                
            case HttpStatus.SC_FORBIDDEN:               // 403
                throw new InactiveException(message);
                
            case HttpStatus.SC_NOT_FOUND:               // 404
                throw new NotFoundException(message);
                
            case HttpStatus.SC_INTERNAL_SERVER_ERROR:   // 500
                throw new RegistrationFailedException(message);
                
            default:
                throw new FlattrServiceException(message);
        } 
    }
    
    /**
     * Opens a Reader to the response content.
     * 
     * @param response Response to be accessed by the reader.
     * @return Reader
     */
    private Reader openReader(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        
        String encoding = RESULT_ENCODING;
        if (entity.getContentEncoding() != null) {
            encoding = entity.getContentEncoding().getValue();
        }
        
        return new InputStreamReader(entity.getContent(), encoding);
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

    /**
     * Creates a {@link HttpClient} for sending the request.
     * 
     * @return {@link HttpClient}
     */
    protected HttpClient createHttpClient() {
        return new DefaultHttpClient();
    }
    
}
