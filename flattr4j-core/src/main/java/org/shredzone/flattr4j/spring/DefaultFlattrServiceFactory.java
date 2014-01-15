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
 */
public class DefaultFlattrServiceFactory implements FlattrServiceFactory {

    private final ConsumerKey consumerKey;
    private final AccessToken accessToken;

    /**
     * Creates a new {@link DefaultFlattrServiceFactory} with the given
     * {@link ConsumerKey}.
     *
     * @param consumerKey
     *            {@link ConsumerKey} to be used by this factory
     */
    public DefaultFlattrServiceFactory(ConsumerKey consumerKey) {
        this(consumerKey, null);
    }

    /**
     * Creates a new {@link DefaultFlattrServiceFactory} with the given
     * {@link ConsumerKey} and {@link AccessToken}.
     *
     * @param consumerKey
     *            {@link ConsumerKey} to be used by this factory
     * @param accessToken
     *            {@link AccessToken} to be used by this factory
     */
    public DefaultFlattrServiceFactory(ConsumerKey consumerKey, AccessToken accessToken) {
        if (consumerKey == null) {
            throw new IllegalArgumentException("A ConsumerKey is required");
        }
        this.consumerKey = consumerKey;
        this.accessToken = accessToken;
    }

    @Override
    @Deprecated
    public OpenService getOpenService() {
        return FlattrFactory.getInstance().createFlattrService();
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
        return FlattrFactory.getInstance().createFlattrService(at);
    }

    @Override
    public FlattrAuthenticator getFlattrAuthenticator() {
        return new FlattrAuthenticator(consumerKey);
    }

}
