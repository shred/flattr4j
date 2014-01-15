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

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.OpenService;
import org.shredzone.flattr4j.oauth.AccessToken;
import org.shredzone.flattr4j.oauth.FlattrAuthenticator;

/**
 * A service factory primarily made for Spring.
 * <p>
 * The factory implementation is preconfigured with the consumer key and consumer secret
 * on constuction time, optionally also with a default access token. When the
 * {@link FlattrServiceFactory} bean is injected, it is already properly configured and
 * ready to use.
 *
 * @author Richard "Shred" Körber
 */
public interface FlattrServiceFactory {

    /**
     * Creates a new {@link OpenService} instance.
     *
     * @return {@link OpenService} instance. Each invocation creates a new instance.
     * @deprecated Use {@link #getFlattrService()} instead
     */
    @Deprecated
    OpenService getOpenService();

    /**
     * Creates a new {@link FlattrService} instance, using a default access token. If no
     * default access token is set, an exception will be thrown.
     *
     * @return {@link FlattrService} instance. Each invocation creates a new instance.
     */
    FlattrService getFlattrService();

    /**
     * Creates a new {@link FlattrService} instance.
     *
     * @param accessToken
     *            {@link AccessToken} to be used by the {@link FlattrService}
     * @return {@link FlattrService} instance. Each invocation creates a new instance.
     */
    FlattrService getFlattrService(AccessToken accessToken);

    /**
     * Creates a new {@link FlattrAuthenticator}.
     *
     * @return {@link FlattrAuthenticator} instance. Each invocation creates a new
     *         instance.
     */
    FlattrAuthenticator getFlattrAuthenticator();

}
