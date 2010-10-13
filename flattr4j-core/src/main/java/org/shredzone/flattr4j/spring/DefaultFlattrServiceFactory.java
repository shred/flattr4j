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
package org.shredzone.flattr4j.spring;

import org.shredzone.flattr4j.FlattrFactory;
import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.OpenService;
import org.shredzone.flattr4j.oauth.AccessToken;
import org.shredzone.flattr4j.oauth.ConsumerKey;
import org.shredzone.flattr4j.oauth.FlattrAuthenticator;

/**
 * Default implementation of {@link FlattrServiceFactory}.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class DefaultFlattrServiceFactory implements FlattrServiceFactory {

    private final ConsumerKey consumerKey;
    private AccessToken accessToken;

    /**
     * Creates a new {@link DefaultFlattrServiceFactory} with the given
     * {@link ConsumerKey}.
     * 
     * @param consumerKey
     *            {@link ConsumerKey} to be used by this factory
     */
    public DefaultFlattrServiceFactory(ConsumerKey consumerKey) {
        this.consumerKey = consumerKey;
    }

    /**
     * Sets an optional {@link AccessToken} to be used by the factory. You may want to use
     * this method if the application only deals with a single Flattr user.
     * 
     * @param accessToken
     *            {@link AccessToken} to be used by this factory
     */
    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public OpenService getOpenService() {
        return FlattrFactory.newInstance().createOpenService();
    }

    @Override
    public FlattrService getFlattrService() {
        return getFlattrService(accessToken);
    }

    @Override
    public FlattrService getFlattrService(AccessToken at) {
        if (at == null) {
            throw new IllegalStateException("An AccessToken is required");
        }
        return FlattrFactory.newInstance().createFlattrService(consumerKey, at);
    }

    @Override
    public FlattrAuthenticator getFlattrAuthenticator() {
        return new FlattrAuthenticator(consumerKey);
    }

}
