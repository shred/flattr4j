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
package org.shredzone.flattr4j.oauth;

import java.io.Serializable;

/**
 * An access token that gives access to the Flattr account.
 *
 * @author Richard "Shred" Körber
 */
public class AccessToken implements Serializable {
    private static final long serialVersionUID = 7715751842047101911L;

    private String token;

    /**
     * @deprecated {@link AccessToken} will be immutable in a future release. Do not use
     * the default constructor.
     */
    @Deprecated
    public AccessToken() {
        // Default constructor
    }

    public AccessToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    /**
     * @deprecated {@link AccessToken} will be immutable in a future release. Do not use
     * this setter, but create a new instance.
     */
    @Deprecated
    public void setToken(String token) {
        this.token = token;
    }

}
