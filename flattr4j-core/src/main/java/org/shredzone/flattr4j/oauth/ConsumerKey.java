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
 * The key and secret of the OAuth2 client. This is your application's key.
 *
 * @author Richard "Shred" Körber
 */
public class ConsumerKey implements Serializable {
    private static final long serialVersionUID = -2439158677542078353L;

    private String key;
    private String secret;

    /**
     * @deprecated {@link ConsumerKey} will be immutable in a future release. Do not use
     * the default constructor.
     */
    @Deprecated
    public ConsumerKey() {
        // Default constructor
    }

    public ConsumerKey(String key, String secret) {
        this.key = key;
        this.secret = secret;
    }

    public String getKey() {
        return key;
    }

    /**
     * @deprecated {@link ConsumerKey} will be immutable in a future release. Do not use
     * this setter, but create a new instance.
     */
    @Deprecated
    public void setKey(String key) {
        this.key = key;
    }

    public String getSecret() {
        return secret;
    }

    /**
     * @deprecated {@link ConsumerKey} will be immutable in a future release. Do not use
     * this setter, but create a new instance.
     */
    @Deprecated
    public void setSecret(String secret) {
        this.secret = secret;
    }

}
