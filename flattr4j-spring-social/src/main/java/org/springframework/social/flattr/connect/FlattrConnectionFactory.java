/*
 * flattr4j - A Java library for Flattr
 *
 * Copyright (C) 2012 Richard "Shred" Körber
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
package org.springframework.social.flattr.connect;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.oauth.ConsumerKey;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

/**
 * Creates an OAuth2 Connection to flattr.
 *
 * @author Richard "Shred" Körber
 * @since 2.4
 */
public class FlattrConnectionFactory extends OAuth2ConnectionFactory<FlattrService> {

    /**
     * The provider ID
     */
    public static final String PROVIDER_ID = "flattr";

    /**
     * Creates a new {@link FlattrConnectionFactory}.
     *
     * @param consumerKey
     *            OAuth2 consumer key
     * @param consumerSecret
     *            OAuth2 consumer secret
     */
    public FlattrConnectionFactory(String consumerKey, String consumerSecret) {
        this(new ConsumerKey(consumerKey, consumerSecret));
    }

    /**
     * Creates a new {@link FlattrConnectionFactory}.
     *
     * @param key
     *            {@link ConsumerKey}
     */
    public FlattrConnectionFactory(ConsumerKey key) {
        super(PROVIDER_ID, new FlattrServiceProvider(key), new FlattrAdapter());
    }

}
