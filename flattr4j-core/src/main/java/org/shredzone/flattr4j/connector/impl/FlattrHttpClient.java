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

import java.io.InputStream;
import java.security.KeyStore;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.http.HttpVersion;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

/**
 * A Flattr HTTP Client that also supports https connections on Android.
 *
 * @author Richard "Shred" Körber
 */
public class FlattrHttpClient extends DefaultHttpClient  {
    private static final int TIMEOUT_MS = 10000;

    private static final AtomicReference<SSLSocketFactory> sslSocketFactory = new AtomicReference<SSLSocketFactory>();
    private static final AtomicReference<SchemeRegistry> registry = new AtomicReference<SchemeRegistry>();

    /**
     * Gets a {@link SSLSocketFactory} suited for connecting to Flattr.
     *
     * @return {@link SSLSocketFactory}
     */
    public static SSLSocketFactory getSocketFactory() {
        SSLSocketFactory result = sslSocketFactory.get();
        if (result != null) {
            return result;
        }

        try {
            InputStream in = null;
            try {
                KeyStore trustStore = KeyStore.getInstance("BKS");
                in = FlattrHttpClient.class.getResourceAsStream("flattr.bks");
                trustStore.load(in, "flattr4j".toCharArray());
                result = new SSLSocketFactory(trustStore);
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        } catch (Exception ex) {
            // Fallback to the original SSL socket factory
            result = SSLSocketFactory.getSocketFactory();
        }

        if (sslSocketFactory.compareAndSet(null, result)) {
            return result;
        } else {
            return sslSocketFactory.get();
        }
    }

    /**
     * Gets a preconfigured {@link SchemeRegistry} suited for connecting to Flattr.
     *
     * @return {@link SchemeRegistry}
     */
    @SuppressWarnings("deprecation")
    public static SchemeRegistry getSchemeRegistry() {
        SchemeRegistry result = registry.get();
        if (result != null) {
            return result;
        }

        result = new SchemeRegistry();
        // The deprecated Scheme constructor must be used for Android compliancy!
        result.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        result.register(new Scheme("https", getSocketFactory(), 443));

        if (registry.compareAndSet(null, result)) {
            return result;
        } else {
            return registry.get();
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    protected ClientConnectionManager createClientConnectionManager() {
        HttpParams params = getParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_0);
        HttpConnectionParams.setSoTimeout(params, TIMEOUT_MS);
        HttpConnectionParams.setConnectionTimeout(params, TIMEOUT_MS);

        // The deprecated constructor must be used for Android compliancy!
        return new ThreadSafeClientConnManager(params, getSchemeRegistry());
    }

}
