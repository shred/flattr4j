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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.junit.Assert;
import org.shredzone.flattr4j.connector.Connector;
import org.shredzone.flattr4j.connector.Result;
import org.shredzone.flattr4j.exception.FlattrException;

/**
 * A mock {@link Connector}. It asserts that the correct URL and method is invoked,
 * compares an optional data text, and returns a given result.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class MockConnector implements Connector {

    private final String expectedCmd;

    private InputStream bodyInputStream;
    private CharSequence expectedData;
    private MockResult result;

    /**
     * Creates a new MockConnector. It expects the given command URL when invoked.
     * 
     * @param expectedCmd
     *            Command URL that is expected
     */
    public MockConnector(String expectedCmd) {
        this.expectedCmd = expectedCmd;
    }

    /**
     * Sets the result body to be used.
     * 
     * @param body
     *            Result body, as a text
     */
    public void setBody(CharSequence body) {
        try {
            bodyInputStream = new ByteArrayInputStream(body.toString().getBytes("iso-8859-1"));
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException("Unknown encoding");
        }
    }

    /**
     * Sets the resource of the result body to be used.
     * 
     * @param body
     *            Result body, as resource name
     */
    public void setBodyResource(String resource) {
        bodyInputStream = MockConnector.class.getResourceAsStream(resource);
    }

    /**
     * Expected data provided to {@link #post(java.lang.String, java.lang.String)}.
     * Invoking this method also requires post() to be invoked instead of call().
     * 
     * @param expectedData
     *            Expected data
     */
    public void setExpectedData(CharSequence expectedData) {
        this.expectedData = expectedData;
    }

    /**
     * Calls the command. Fails if a different command URL is given, or the expected data
     * property is set.
     * 
     * @param cmd
     *            Command to be invoked
     * @return {@link Result}
     */
    @Override
    public Result call(String cmd) throws FlattrException {
        if (expectedData != null) Assert.fail("post() expected");
        Assert.assertEquals(expectedCmd, cmd);
        result = new MockResult(bodyInputStream);
        return result;
    }

    /**
     * Calls the command with the given data. Fails if a different command URL is given,
     * or if the expected data property is unset.
     * 
     * @param cmd
     *            Command to be invoked
     * @param data
     *            Data to be passed to the server
     * @return {@link Result}
     */
    @Override
    public Result post(String cmd, String data) throws FlattrException {
        if (expectedData == null) Assert.fail("call() expected");
        Assert.assertEquals(expectedCmd, cmd);
        Assert.assertEquals(expectedData, data);
        result = new MockResult(bodyInputStream);
        return result;
    }

    /**
     * Asserts the state of the connector. Fails if neither call() nor post() were
     * invoked, or if the returned reader was not closed.
     */
    public void assertState() {
        Assert.assertNotNull("Connector was not invoked", result);
        result.assertReaderClosed();
    }

}
