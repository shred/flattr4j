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
package org.shredzone.flattr4j;

import org.shredzone.flattr4j.connector.impl.FlattrConnector;
import org.shredzone.flattr4j.impl.FlattrServiceImpl;
import org.shredzone.flattr4j.oauth.AccessToken;
import org.shredzone.flattr4j.oauth.ConsumerKey;

/**
 * A factory class that makes creation of the Flattr services as easy as possible.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public final class FlattrFactory {
    private static FlattrFactory instance = null;
    
    private FlattrFactory() {
        // Private constructor
    }

    /**
     * Retrieves an instance of the {@link FlattrFactory}. The factory is a singleton.
     * 
     * @return {@link FlattrFactory}
     */
    public static FlattrFactory getInstance() {
        if (instance == null) {
            instance = new FlattrFactory();
        }
        return instance;
    }

    /**
     * Creates a {@link FlattrService} for the given consumer and access token.
     * 
     * @param accessToken
     *            Access token
     * @return Created {@link FlattrService}
     * @since 2.0
     */
    public FlattrService createFlattrService(String accessToken) {
        return createFlattrService(new AccessToken(accessToken));
    }

    /**
     * Creates a {@link FlattrService} for the given consumer and access token.
     * 
     * @param accessToken
     *            {@link AccessToken} instance
     * @return Created {@link FlattrService}
     * @since 2.0
     */
    public FlattrService createFlattrService(AccessToken accessToken) {
        FlattrConnector connector = new FlattrConnector();
        connector.setAccessToken(accessToken);
        return new FlattrServiceImpl(connector);
    }
    
    /**
     * Creates a {@link FlattrService} for the given consumer and access token.
     * 
     * @param consumerKey
     *            Consumer key
     * @param consumerSecret
     *            Consumer secret
     * @param accessToken
     *            Access token
     * @param accessTokenSecret
     *            Access token secret
     * @return Created {@link FlattrService}
     * @deprecated Consumer key is not required any more. Use
     *             {@link #createFlattrService(org.shredzone.flattr4j.oauth.AccessToken)}
     *             instead.
     */
    @Deprecated
    public FlattrService createFlattrService(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
        return createFlattrService(new AccessToken(accessToken));
    }
 
    /**
     * Creates a {@link FlattrService} for the given consumer and access token.
     * 
     * @param consumerKey
     *            {@link ConsumerKey} instance
     * @param accessToken
     *            {@link AccessToken} instance
     * @return Created {@link FlattrService}
     * @deprecated Consumer key is not required any more. Use
     *             {@link #createFlattrService(org.shredzone.flattr4j.oauth.AccessToken)}
     *             instead.
     */
    @Deprecated
    public FlattrService createFlattrService(ConsumerKey consumerKey, AccessToken accessToken) {
        return createFlattrService(accessToken);
    }

    /**
     * Creates an {@link OpenService}.
     * 
     * @return Created {@link OpenService}
     */
    public OpenService createOpenService() {
        return new FlattrServiceImpl(new FlattrConnector());
    }

}
