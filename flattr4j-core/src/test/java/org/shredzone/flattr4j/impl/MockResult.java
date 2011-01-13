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
package org.shredzone.flattr4j.impl;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.shredzone.flattr4j.connector.Result;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.exception.FlattrServiceException;

/**
 * A mock {@link Result} that returns a prepared {@link InputStream}.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class MockResult implements Result {
    private final InputStream stream;
    private boolean opened = false;

    /**
     * Creates a new {@link MockResult} that provides the content of the given
     * {@link InputStream}.
     * 
     * @param stream
     *            {@link InputStream} that provides the content
     */
    public MockResult(InputStream stream) {
        this.stream = stream;
    }

    @Override
    public Result assertStatusOk() throws FlattrException {
        // Status is always OK in unit tests
        return this;
    }

    @Override
    public InputStream openInputStream() throws FlattrException {
        if (opened) {
            throw new IllegalStateException("Stream is already open!");
        }
        opened = true;
        return stream;
    }

    @Override
    public void closeInputStream() throws FlattrException {
        try {
            stream.close();
        } catch (IOException ex) {
            throw new FlattrServiceException("Could not close stream", ex);
        } finally {
            opened = false;
        }
    }
    
    @Override
    public String getEncoding() throws FlattrException {
        return "iso-8859-1";
    }

    /**
     * Fails if the reader was not closed.
     */
    public void assertReaderClosed() {
        if (opened) Assert.fail("Reader was not closed!");
    }

}
