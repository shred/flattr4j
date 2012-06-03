/*
 * flattr4j - A Java library for Flattr
 *
 * Copyright (C) 2012 Richard "Shred" Körber
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
package org.springframework.social.flattr.connect;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.social.connect.UserProfile;

/**
 * Unit test for {@link FlattrAdapter}.
 *
 * @author Richard "Shred" Körber
 */
public class FlattrAdapterTest {

    @Test
    public void testTestPositive() {
        DummyFlattrServiceFactory factory = new DummyFlattrServiceFactory();
        factory.enableGetMyself();

        FlattrAdapter adapter = new FlattrAdapter();
        Assert.assertTrue(adapter.test(factory.createFlattrService()));
    }

    @Test
    public void testTestNegative() {
        DummyFlattrServiceFactory factory = new DummyFlattrServiceFactory();

        FlattrAdapter adapter = new FlattrAdapter();
        Assert.assertFalse(adapter.test(factory.createFlattrService()));
    }

    @Test
    public void testSetConnectionValues() {
        DummyFlattrServiceFactory factory = new DummyFlattrServiceFactory();
        factory.enableGetMyself();

        SimpleConnectionValues cv = new SimpleConnectionValues();
        FlattrAdapter adapter = new FlattrAdapter();
        adapter.setConnectionValues(factory.createFlattrService(), cv);
        Assert.assertEquals("simon_g", cv.getProviderUserId());
        Assert.assertEquals("Simon Gate", cv.getDisplayName());
        Assert.assertEquals("https://flattr.local/user/simon_g", cv.getProfileUrl());
        Assert.assertNull(cv.getImageUrl());
    }

    @Test
    public void testFetchUserProfile() {
        DummyFlattrServiceFactory factory = new DummyFlattrServiceFactory();
        factory.enableGetMyself();

        FlattrAdapter adapter = new FlattrAdapter();
        UserProfile up = adapter.fetchUserProfile(factory.createFlattrService());
        Assert.assertEquals("simon_g", up.getUsername());
        Assert.assertEquals("Simon", up.getFirstName());
        Assert.assertEquals("Gate", up.getLastName());
        Assert.assertEquals("Simon Gate", up.getName());
        Assert.assertNull(up.getEmail());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUpdateStatus() {
        DummyFlattrServiceFactory factory = new DummyFlattrServiceFactory();

        FlattrAdapter adapter = new FlattrAdapter();
        adapter.updateStatus(factory.createFlattrService(), "foo");
    }

}
