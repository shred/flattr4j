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

import java.io.Serializable;
import java.util.concurrent.Callable;

import org.shredzone.flattr4j.oauth.AccessToken;

/**
 * A {@link Callable} for a Flattr method.
 *
 * @author Iulius Gutberlet
 * @author Richard "Shred" Körber
 */
public interface FlattrCallable<R> extends Callable<R>, Serializable {

    /**
     * Sets the {@link AccessToken} to be used if the call requires authorization.
     *
     * @param token
     *            {@link AccessToken}, or {@code null} if no authorization is needed
     */
    void setAccessToken(AccessToken token);

    /**
     * Shortcut to set an {@link AccessToken}.
     *
     * @param token
     *            Access token, or {@code null} if no authorization is needed
     */
    void setAccessToken(String token);

    /**
     * Gets the result of the last call.
     */
    R getResult();

}
