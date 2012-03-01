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
package org.shredzone.flattr4j.connector.impl;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.http.HttpVersion;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

/**
 * A Flattr HTTP Client for Apache HTTP Client 4.1 or higher.
 *
 * @author Richard "Shred" Körber
 * @since 2.2
 */
public class NewFlattrHttpClient extends DefaultHttpClient {
    private static final int TIMEOUT_MS = 10000;

    private static final AtomicReference<SchemeRegistry> registry = new AtomicReference<SchemeRegistry>();

    /**
     * Gets a preconfigured {@link SchemeRegistry} suited for connecting to Flattr.
     *
     * @return {@link SchemeRegistry}
     */
    public static SchemeRegistry getSchemeRegistry() {
        SchemeRegistry result = registry.get();
        if (result != null) {
            return result;
        }

        result = new SchemeRegistry();
        result.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        result.register(new Scheme("https", 443, FlattrHttpClient.getSocketFactory()));

        if (registry.compareAndSet(null, result)) {
            return result;
        } else {
            return registry.get();
        }
    }

    @Override
    protected ClientConnectionManager createClientConnectionManager() {
        return new ThreadSafeClientConnManager(getSchemeRegistry(), TIMEOUT_MS, TimeUnit.MILLISECONDS);
    }

    @Override
    protected HttpParams createHttpParams() {
        HttpParams params = super.createHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_0);
        return params;
    }

}
