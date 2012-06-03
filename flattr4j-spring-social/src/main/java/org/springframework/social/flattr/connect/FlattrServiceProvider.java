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

import org.shredzone.flattr4j.FlattrFactory;
import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.oauth.ConsumerKey;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2ServiceProvider;

/**
 * An {@link OAuth2ServiceProvider} implementation for flattr.
 *
 * @author Richard "Shred" Körber
 * @since 2.4
 */
public class FlattrServiceProvider extends AbstractOAuth2ServiceProvider<FlattrService> {

    /**
     * Creates a new {@link FlattrServiceProvider}.
     *
     * @param key
     *            {@link ConsumerKey} to be used
     */
    public FlattrServiceProvider(ConsumerKey key) {
        super(new FlattrOAuth2Operations(key));
    }

    @Override
    public FlattrService getApi(String accessToken) {
        return FlattrFactory.getInstance().createFlattrService(accessToken);
    }

}
