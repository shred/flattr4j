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
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.shredzone.flattr4j.connector.Result;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.exception.FlattrServiceException;
import org.shredzone.flattr4j.exception.ForbiddenException;
import org.shredzone.flattr4j.exception.NotFoundException;
import org.shredzone.flattr4j.exception.SubmissionFailedException;

/**
 * Default implementation of {@link Result}.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class DefaultResult implements Result {
    private static final String RESULT_ENCODING = "iso-8859-1";
    
    private final HttpResponse response;
    private InputStream stream = null;

    /**
     * Creates a new {@link Result} object from a {@link HttpResponse}.
     * 
     * @param response
     *            {@link HttpResponse}
     */
    public DefaultResult(HttpResponse response) {
        this.response = response;
    }
    
    @Override
    public Result assertStatusOk() throws FlattrException {
        StatusLine line = response.getStatusLine();
        String message = line.getStatusCode() + ": " + line.getReasonPhrase();
        
        switch (line.getStatusCode()) {
            case HttpStatus.SC_OK:                      // 200
                return this;

            case HttpStatus.SC_UNAUTHORIZED:            // 401
            case HttpStatus.SC_FORBIDDEN:               // 403
                throw new ForbiddenException(message);
                
            case HttpStatus.SC_NOT_FOUND:               // 404
                throw new NotFoundException(message);
                
            case HttpStatus.SC_INTERNAL_SERVER_ERROR:   // 500
                throw new SubmissionFailedException(message);
                
            default:
                throw new FlattrServiceException(message);
        } 
    }
    
    @Override
    public InputStream openInputStream() throws FlattrException {
        if (stream == null) {
            try {
                HttpEntity entity = response.getEntity();
                stream = entity.getContent();
            } catch (IOException ex) {
                throw new FlattrServiceException("Could not read response stream", ex);
            }
        }
        
        return stream;
    }
    
    @Override
    public void closeInputStream() throws FlattrException {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException ex) {
                throw new FlattrServiceException("Could not close stream", ex);
            } finally {
                stream = null;
            }
        }
    }
    
    @Override
    public String getEncoding() throws FlattrException {
        HttpEntity entity = response.getEntity();
        if (entity.getContentEncoding() != null) {
            return entity.getContentEncoding().getValue();
        } else {
            return RESULT_ENCODING;
        }
    }

}
