/**
 * flattr4j - A Java library for Flattr
 *
 * Copyright (C) 2010 Richard "Shred" Körber
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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.shredzone.flattr4j.connector.OpenConnector;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.exception.FlattrServiceException;

/**
 * The default implementation of {@link OpenConnector}.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class DefaultOpenConnector implements OpenConnector {
    
    private String baseUrl = "http://api.flattr.com/odapi/";
    
    public String getBaseUrl()          { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }

    @Override
    public Reader call(String cmd) throws FlattrException {
        try {
            URL url = new URL(baseUrl + cmd);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new FlattrServiceException(connection.getResponseCode() + ": " + connection.getResponseMessage());
            }
            
            String encoding = connection.getContentEncoding();
            if (encoding == null) encoding = "ISO-8859-1";
            
            return new InputStreamReader(connection.getInputStream(), encoding);
        } catch (IOException ex) {
            throw new FlattrServiceException("Request failed", ex);
        }
    }

}
