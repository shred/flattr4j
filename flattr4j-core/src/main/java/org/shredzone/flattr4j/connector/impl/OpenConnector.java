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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.shredzone.flattr4j.connector.Connector;
import org.shredzone.flattr4j.connector.FlattrHttpClient;
import org.shredzone.flattr4j.connector.Result;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.exception.FlattrServiceException;

/**
 * The default implementation of {@link Connector}.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class OpenConnector implements Connector {
    private static final String POST_ENCODING = "utf-8";

    @Override
    public Result call(String url) throws FlattrException {
        HttpGet request = new HttpGet(url);
        return execute(request);
    }

    @Override
    public Result post(String url, String data) throws FlattrException {
        try {
            HttpPost request = new HttpPost(url);
            
            StringEntity body = new StringEntity("data=" + URLEncoder.encode(data, POST_ENCODING));
            body.setContentType("application/x-www-form-urlencoded");
            request.setEntity(body);
            
            return execute(request);
        } catch (UnsupportedEncodingException ex) {
            throw new FlattrServiceException("Unknown encoding: " + POST_ENCODING);
        }
    }

    /**
     * Creates a {@link HttpClient} for sending the request.
     * 
     * @return {@link HttpClient}
     */
    protected HttpClient createHttpClient() {
        return new FlattrHttpClient();
    }

    /**
     * Executes the request and handles the response.
     * 
     * @param request
     *            Request to be executed
     * @return {@link Result} containing the result of the request
     * @throws FlattrException
     *             when the request could not be executed
     */
    protected Result execute(HttpUriRequest request) throws FlattrException {
        try {
            return new DefaultResult(createHttpClient().execute(request));
        } catch (IOException ex) {
            throw new FlattrServiceException("HTTP access failed", ex);
        }
    }

}
