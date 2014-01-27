/*
 * flattr4j - A Java library for Flattr
 *
 * Copyright (C) 2014 Richard "Shred" Körber
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
package org.shredzone.flattr4j.async;

import org.shredzone.flattr4j.FlattrFactory;
import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.connector.RateLimit;
import org.shredzone.flattr4j.oauth.AccessToken;

/**
 * Abstract implementation of {@link FlattrCallable}.
 *
 * @author Iulius Gutberlet
 * @author Richard "Shred" Körber
 */
public abstract class AbstractFlattrCallable<R> implements FlattrCallable<R> {
    private static final long serialVersionUID = 4300158666615391413L;

    private AccessToken token;
    private boolean full;
    private RateLimit rateLimit;
    private R result;

    @Override
    public void setAccessToken(AccessToken token) {
        this.token = token;
    }

    @Override
    public void setAccessToken(String token) {
        setAccessToken(new AccessToken(token));
    }

    /**
     * Returns the {@link RateLimit} state after this callable was successfully executed,
     * or {@code null} if the call failed or was not invoked yet.
     */
    public RateLimit getRateLimit() {
        return rateLimit;
    }

    /**
     * Sets the full mode to be used when the call is executed.
     *
     * @param full
     *            full mode
     */
    public void setFullMode(boolean full)       { this.full = full; }
    public boolean isFullMode()                 { return full; }

    /**
     * Creates a new {@link FlattrService} for the given {@link AccessToken}. Can be
     * overridden by subclasses if the {@link FlattrService} needs a special
     * configuration.
     *
     * @param token
     *            {@link AccessToken}
     * @return {@link FlattrService} that was created
     */
    protected FlattrService createFlattrService(AccessToken token) {
        return FlattrFactory.getInstance().createFlattrService(token);
    }

    /**
     * Calls the appropriate method at the service and returns the result.
     *
     * @param service
     *            Preconfigured {@link FlattrService} to be invoked
     * @return Result returned from the Flattr method.
     */
    public abstract R call(FlattrService service) throws Exception;

    @Override
    public R call() throws Exception {
        rateLimit = null;
        FlattrService service = createFlattrService(token);
        service.setFullMode(full);
        result = call(service);
        rateLimit = new RateLimit(service.getLastRateLimit());
        return result;
    }

    @Override
    public R getResult() {
        return result;
    };

}
