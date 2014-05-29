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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.connector.RateLimit;
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

        final FlattrService mockService = mock(FlattrService.class);
        when(mockService.getLastRateLimit()).thenReturn(new RateLimit());

        @SuppressWarnings("serial")
        final AbstractFlattrCallable<String> callable = new AbstractFlattrCallable<String>() {
            @Override
            public String call(FlattrService service) throws Exception {
                assertThat(service, sameInstance(mockService));
                executed = true;
                return expectedResult;
            }

            @Override
            protected FlattrService createFlattrService(AccessToken token) {
                assertThat(token, nullValue());
                return mockService;
            }
        };

        executed = false;
        String result = callable.call();
        assertThat(executed, is(true));
        assertThat(result, allOf(
            is(expectedResult),
            sameInstance(callable.getResult())
        ));
        verify(mockService).getLastRateLimit();
        assertThat(callable.getRateLimit(), allOf(
            notNullValue(),
            not(sameInstance(mockService.getLastRateLimit()))
        ));
    }

    @Test
    public void testAccessToken() throws Exception {
        final AccessToken accessToken = new AccessToken("token123");
        final String expectedResult = "result321";

        final FlattrService mockService = mock(FlattrService.class);
        when(mockService.getLastRateLimit()).thenReturn(new RateLimit());

        @SuppressWarnings("serial")
        final AbstractFlattrCallable<String> callable = new AbstractFlattrCallable<String>() {
            @Override
            public String call(FlattrService service) throws Exception {
                assertThat(service, sameInstance(mockService));
                executed = true;
                return expectedResult;
            }

            @Override
            protected FlattrService createFlattrService(AccessToken token) {
                assertThat(token, sameInstance(accessToken));
                return mockService;
            }
        };

        callable.setAccessToken(accessToken);
        executed = false;
        String result = callable.call();
        assertThat(executed, is(true));
        assertThat(result, allOf(
            is(expectedResult),
            sameInstance(callable.getResult())
        ));
        verify(mockService).getLastRateLimit();
        assertThat(callable.getRateLimit(), allOf(
            notNullValue(),
            not(sameInstance(mockService.getLastRateLimit()))
        ));
    }

    @Test
    public void testAccessTokenString() throws Exception {
        final String accessToken = "token123";
        final String expectedResult = "result321";

        final FlattrService mockService = mock(FlattrService.class);
        when(mockService.getLastRateLimit()).thenReturn(new RateLimit());

        @SuppressWarnings("serial")
        final AbstractFlattrCallable<String> callable = new AbstractFlattrCallable<String>() {
            @Override
            public String call(FlattrService service) throws Exception {
                assertThat(service, sameInstance(mockService));
                executed = true;
                return expectedResult;
            }

            @Override
            protected FlattrService createFlattrService(AccessToken token) {
                assertThat(token.getToken(), is(accessToken));
                return mockService;
            }
        };

        callable.setAccessToken(accessToken);
        executed = false;
        String result = callable.call();
        assertThat(executed, is(true));
        assertThat(result, allOf(
            is(expectedResult),
            sameInstance(callable.getResult())
        ));
        verify(mockService).getLastRateLimit();
        assertThat(callable.getRateLimit(), allOf(
            notNullValue(),
            not(sameInstance(mockService.getLastRateLimit()))
        ));
    }

    @Test
    public void testSetFullMode() throws Exception {
        final FlattrService mockService = mock(FlattrService.class);
        when(mockService.getLastRateLimit()).thenReturn(new RateLimit());

        @SuppressWarnings("serial")
        final AbstractFlattrCallable<String> callable = new AbstractFlattrCallable<String>() {
            @Override
            protected FlattrService createFlattrService(AccessToken token) {
                return mockService;
            }

            @Override
            public String call(FlattrService service) throws Exception {
                return null; // do nothing
            }
        };

        // default is false
        assertThat(callable.isFullMode(), is(false));
        callable.call();
        verify(mockService, times(1)).setFullMode(false);

        callable.setFullMode(true);
        assertThat(callable.isFullMode(), is(true));
        callable.call();
        verify(mockService, times(1)).setFullMode(true);

        callable.setFullMode(false);
        assertThat(callable.isFullMode(), is(false));
        callable.call();
        verify(mockService, times(2)).setFullMode(false);
    }

}
