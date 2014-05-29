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
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.shredzone.flattr4j.FlattrService;

/**
 * Unit tests for {@link PaginatedFlattrCallable}.
 *
 * @author Richard "Shred" Körber
 */
public class PaginatedFlattrCallableTest {

    private boolean executed;

    @Test
    public void testNullValues() throws Exception {
        final String expectedResult = "result321";

        final FlattrService mockService = mock(FlattrService.class);

        @SuppressWarnings("serial")
        PaginatedFlattrCallable<String> callable = new PaginatedFlattrCallable<String>() {
            @Override
            public String call(FlattrService service, Integer count, Integer page)
                throws Exception {
                assertThat(service, sameInstance(mockService));
                assertThat(count, nullValue());
                assertThat(page, nullValue());
                executed = true;
                return expectedResult;
            }
        };

        assertThat(callable, allOf(
            hasProperty("page", nullValue()),
            hasProperty("count", nullValue())
        ));

        executed = false;
        String result = callable.call(mockService);
        assertThat(executed, is(true));
        assertThat(result, sameInstance(expectedResult));
        assertThat(callable, allOf(
            hasProperty("page", nullValue()),
            hasProperty("count", nullValue())
        ));
    }

    @Test
    public void testSetValues() throws Exception {
        final String expectedResult = "result321";
        final int expectedCount = 10;
        final int expectedPage = 5;

        final FlattrService mockService = mock(FlattrService.class);

        @SuppressWarnings("serial")
        PaginatedFlattrCallable<String> callable = new PaginatedFlattrCallable<String>() {
            @Override
            public String call(FlattrService service, Integer count, Integer page)
                throws Exception {
                assertThat(service, sameInstance(mockService));
                assertThat(count, is(expectedCount));
                assertThat(page, is(expectedPage));
                executed = true;
                return expectedResult;
            }
        };

        callable.setPage(expectedPage);
        callable.setCount(expectedCount);
        assertThat(callable.getCount(), is(expectedCount));
        assertThat(callable.getPage(), is(expectedPage));

        executed = false;
        String result = callable.call(mockService);
        assertThat(executed, is(true));
        assertThat(result, sameInstance(expectedResult));
        assertThat(callable.getCount(), is(expectedCount));
        assertThat(callable.getPage(), is(expectedPage));
    }

}
