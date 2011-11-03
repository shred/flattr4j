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
package org.shredzone.flattr4j.connector.impl;

import org.shredzone.flattr4j.connector.Connection;
import org.shredzone.flattr4j.connector.Connector;
import org.shredzone.flattr4j.connector.RequestType;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.oauth.AccessToken;

/**
 * A {@link Connector} that connects to the Flattr web service.
 *
 * @author Richard "Shred" Körber
 * @version $Revision: 463 $
 */
public class FlattrConnector implements Connector {
    private String baseUrl = "https://api.flattr.com/rest/v2/";

    private AccessToken accessToken;

    @Override
    public Connection create() throws FlattrException {
        return create(RequestType.GET);
    }

    @Override
    public Connection create(RequestType type) throws FlattrException {
        FlattrConnection connection = new FlattrConnection(type);
        connection.url(baseUrl);
        if (accessToken != null) {
            connection.token(accessToken);
        }
        return connection;
    }

    /**
     * Base URL of the API. Must end with a trailing slash!
     */
    public String getBaseUrl()                  { return baseUrl; }
    public void setBaseUrl(String baseUrl)      { this.baseUrl = baseUrl; }
    
    /**
     * {@link AccessToken} to be used for authorized calls.
     */
    public AccessToken getAccessToken()         { return accessToken; }
    public void setAccessToken(AccessToken accessToken) { this.accessToken = accessToken; }
    
}
