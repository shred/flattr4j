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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.connector.FlattrObject;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.User;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * Unit test for {@link FlattrAdapter}.
 *
 * @author Richard "Shred" Körber
 */
public class FlattrAdapterTest {

    private User testUser;

    @Before
    public void init() {
        testUser = new User(new FlattrObject("{\"type\":\"user\"," +
                        "\"link\":\"https://flattr.local/user/simon_g\"," +
                        "\"username\":\"simon_g\","+
                        "\"firstname\":\"Simon\"," +
                        "\"lastname\":\"Gate\"," +
                        "\"avatar\":\"\"}"));
    }

    @Test
    public void testTestPositive() throws FlattrException {
        FlattrService mockService = mock(FlattrService.class);
        when(mockService.getMyself()).thenReturn(testUser);

        FlattrAdapter adapter = new FlattrAdapter();
        assertThat(adapter.test(mockService), is(true));
    }

    @Test
    public void testTestNegative() throws FlattrException {
        FlattrService mockService = mock(FlattrService.class);
        when(mockService.getMyself()).thenThrow(new FlattrException("no connection"));

        FlattrAdapter adapter = new FlattrAdapter();
        assertThat(adapter.test(mockService), is(false));
    }

    @Test
    public void testSetConnectionValues() throws FlattrException {
        FlattrService mockService = mock(FlattrService.class);
        when(mockService.getMyself()).thenReturn(testUser);

        ConnectionValues mockCv = mock(ConnectionValues.class);

        FlattrAdapter adapter = new FlattrAdapter();
        adapter.setConnectionValues(mockService, mockCv);

        verify(mockCv).setProviderUserId("simon_g");
        verify(mockCv).setDisplayName("Simon Gate");
        verify(mockCv).setProfileUrl("https://flattr.local/user/simon_g");
        verify(mockCv, never()).setImageUrl(null);
    }

    @Test
    public void testFetchUserProfile() throws FlattrException {
        FlattrService mockService = mock(FlattrService.class);
        when(mockService.getMyself()).thenReturn(testUser);

        FlattrAdapter adapter = new FlattrAdapter();
        UserProfile up = adapter.fetchUserProfile(mockService);
        assertThat(up, allOf(
            hasProperty("username", is("simon_g")),
            hasProperty("firstName", is("Simon")),
            hasProperty("lastName", is("Gate")),
            hasProperty("name", is("Simon Gate")),
            hasProperty("email", nullValue())
        ));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUpdateStatus() throws FlattrException {
        FlattrService mockService = mock(FlattrService.class);
        when(mockService.getMyself()).thenThrow(new FlattrException("no connection"));

        FlattrAdapter adapter = new FlattrAdapter();
        adapter.updateStatus(mockService, "foo");
    }

}
