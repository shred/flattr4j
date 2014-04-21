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

/**
 * Unit tests for {@link PaginatedFlattrCallable}.
 *
 * @author Richard "Shred" Körber
 */
public class PaginatedFlattrCallableTest {

    private boolean executed;

    @Test
    public void testNullValues() throws Exception {
        final FlattrService dummyService = DummyFlattrServiceFactory.create();
        final String expectedResult = "result321";
        executed = false;

        @SuppressWarnings("serial")
        PaginatedFlattrCallable<String> callable = new PaginatedFlattrCallable<String>() {
            @Override
            public String call(FlattrService service, Integer count, Integer page)
                throws Exception {
                Assert.assertSame(dummyService, service);
                Assert.assertNull(count);
                Assert.assertNull(page);
                executed = true;
                return expectedResult;
            }
        };

        Assert.assertNull(callable.getPage());
        Assert.assertNull(callable.getCount());

        String result = callable.call(dummyService);
        Assert.assertTrue(executed);
        Assert.assertSame(expectedResult, result);
        Assert.assertNull(callable.getPage());
        Assert.assertNull(callable.getCount());
    }

    @Test
    public void testSetValues() throws Exception {
        final FlattrService dummyService = DummyFlattrServiceFactory.create();
        final String expectedResult = "result321";
        final int expectedCount = 10;
        final int expectedPage = 5;
        executed = false;

        @SuppressWarnings("serial")
        PaginatedFlattrCallable<String> callable = new PaginatedFlattrCallable<String>() {
            @Override
            public String call(FlattrService service, Integer count, Integer page)
                throws Exception {
                Assert.assertSame(dummyService, service);
                Assert.assertEquals(expectedCount, count.intValue());
                Assert.assertEquals(expectedPage, page.intValue());
                executed = true;
                return expectedResult;
            }
        };

        callable.setPage(expectedPage);
        callable.setCount(expectedCount);
        Assert.assertEquals(expectedPage, callable.getPage().intValue());
        Assert.assertEquals(expectedCount, callable.getCount().intValue());

        String result = callable.call(dummyService);
        Assert.assertTrue(executed);
        Assert.assertSame(expectedResult, result);
        Assert.assertEquals(expectedPage, callable.getPage().intValue());
        Assert.assertEquals(expectedCount, callable.getCount().intValue());
    }

}
