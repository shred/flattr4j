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
    
    private SSLSocketFactory sslSocketFactory;
    
    @Override
    protected ClientConnectionManager createClientConnectionManager() {
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", createSSLSocketFactory(), 443));
        return new SingleClientConnManager(getParams(), registry);
    }
    
    /**
     * Creates an SSLSocketFactory that accepts Flattr signatures.
     * 
     * @return SSLSocketFactory that was created
     */
    protected SSLSocketFactory createSSLSocketFactory() {
        synchronized (this) {
            if (sslSocketFactory == null) {
                KeyStore trustStore = null;
                try {
                    trustStore = KeyStore.getInstance("BKS");
                } catch (KeyStoreException ex) {
                    return SSLSocketFactory.getSocketFactory();
                }

                try {
                    InputStream in = null;
                    try {
                        in = FlattrHttpClient.class.getResourceAsStream("flattr.bks");
                        trustStore.load(in, "flattr4j".toCharArray());
                    } finally {
                        if (in != null) {
                            in.close();
                        }
                    }

                    sslSocketFactory = new SSLSocketFactory(trustStore);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    throw new RuntimeException("Could not create SSL Socket Factory", ex);
                }
            }
            return sslSocketFactory;
        }
    }
    
}
