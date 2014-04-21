/*
 * flattr4j - A Java library for Flattr
 *
 * Copyright (C) 2014 Richard "Shred" Körber
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
package org.shredzone.flattr4j.async;

import org.junit.Assert;
import org.junit.Test;
import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.oauth.AccessToken;

/**
 * Unit tests for {@link AbstractFlattrCallable}.
 *
 * @author Richard "Shred" Körber
 */
public class AbstractFlattrCallableTest {

    private boolean executed;

    @Test
    public void testNoAccessToken() throws Exception {
        final String expectedResult = "result321";
        final FlattrService dummyService = DummyFlattrServiceFactory.create();
        executed = false;

        @SuppressWarnings("serial")
        final AbstractFlattrCallable<String> callable = new AbstractFlattrCallable<String>() {
            @Override
            public String call(FlattrService service) throws Exception {
                Assert.assertSame(dummyService, service);
                executed = true;
                return expectedResult;
            }

            @Override
            protected FlattrService createFlattrService(AccessToken token) {
                Assert.assertNull(token);
                return dummyService;
            }
        };

        String result = callable.call();
        Assert.assertTrue(executed);
        Assert.assertEquals(expectedResult, result);
        Assert.assertSame(result, callable.getResult());
        Assert.assertNotNull(callable.getRateLimit());
        Assert.assertNotSame(dummyService.getLastRateLimit(), callable.getRateLimit());
    }

    @Test
    public void testAccessToken() throws Exception {
        final AccessToken accessToken = new AccessToken("token123");
        final String expectedResult = "result321";
        final FlattrService dummyService = DummyFlattrServiceFactory.create();
        executed = false;

        @SuppressWarnings("serial")
        final AbstractFlattrCallable<String> callable = new AbstractFlattrCallable<String>() {
            @Override
            public String call(FlattrService service) throws Exception {
                Assert.assertSame(dummyService, service);
                executed = true;
                return expectedResult;
            }

            @Override
            protected FlattrService createFlattrService(AccessToken token) {
                Assert.assertNotNull(token);
                Assert.assertSame(accessToken, token);
                return dummyService;
            }
        };

        callable.setAccessToken(accessToken);
        String result = callable.call();
        Assert.assertTrue(executed);
        Assert.assertEquals(expectedResult, result);
        Assert.assertSame(result, callable.getResult());
        Assert.assertNotNull(callable.getRateLimit());
        Assert.assertNotSame(dummyService.getLastRateLimit(), callable.getRateLimit());
    }

    @Test
    public void testAccessTokenString() throws Exception {
        final String accessToken = "token123";
        final String expectedResult = "result321";
        final FlattrService dummyService = DummyFlattrServiceFactory.create();
        executed = false;

        @SuppressWarnings("serial")
        final AbstractFlattrCallable<String> callable = new AbstractFlattrCallable<String>() {
            @Override
            public String call(FlattrService service) throws Exception {
                Assert.assertSame(dummyService, service);
                executed = true;
                return expectedResult;
            }

            @Override
            protected FlattrService createFlattrService(AccessToken token) {
                Assert.assertNotNull(token);
                Assert.assertEquals(accessToken, token.getToken());
                return dummyService;
            }
        };

        callable.setAccessToken(accessToken);
        String result = callable.call();
        Assert.assertTrue(executed);
        Assert.assertEquals(expectedResult, result);
        Assert.assertSame(result, callable.getResult());
        Assert.assertNotNull(callable.getRateLimit());
        Assert.assertNotSame(dummyService.getLastRateLimit(), callable.getRateLimit());
    }

    @Test
    public void testSetFullMode() {
        @SuppressWarnings("serial")
        final AbstractFlattrCallable<String> callable = new AbstractFlattrCallable<String>() {
            @Override
            public String call(FlattrService service) throws Exception {
                throw new UnsupportedOperationException();
            }
        };

        // default is false
        Assert.assertFalse(callable.isFullMode());

        callable.setFullMode(true);
        Assert.assertTrue(callable.isFullMode());

        callable.setFullMode(false);
        Assert.assertFalse(callable.isFullMode());
    }

}
