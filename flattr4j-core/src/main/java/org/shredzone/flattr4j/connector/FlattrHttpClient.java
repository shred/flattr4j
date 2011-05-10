/**
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
package org.shredzone.flattr4j.connector;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

/**
 * A Flattr HTTP Client that also supports https connections on Android.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class FlattrHttpClient extends DefaultHttpClient  {
    
    private static SSLSocketFactory sslSocketFactory;
    
    static {
        try {
            InputStream in = null;
            try {
                KeyStore trustStore = KeyStore.getInstance("BKS");
                in = FlattrHttpClient.class.getResourceAsStream("flattr.bks");
                trustStore.load(in, "flattr4j".toCharArray());
                sslSocketFactory = new SSLSocketFactory(trustStore);
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        } catch (Exception ex) {
            sslSocketFactory = SSLSocketFactory.getSocketFactory();
        }
    }
    
    /**
     * Gets a SSLSocketFactory suited for connecting to Flattr.
     * 
     * @return SSLSocketFactory
     */
    public static SSLSocketFactory getSocketFactory() {
        return sslSocketFactory;
    }
    
    @Override
    protected ClientConnectionManager createClientConnectionManager() {
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", FlattrHttpClient.getSocketFactory(), 443));
        return new SingleClientConnManager(getParams(), registry);
    }
    
}
