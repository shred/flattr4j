/*
 * flattr4j - A Java library for Flattr
 *
 * Copyright (C) 2011 Richard "Shred" Körber
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
package org.shredzone.flattr4j.model;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;
import org.shredzone.flattr4j.connector.FlattrObject;
import org.shredzone.flattr4j.exception.FlattrException;

/**
 * Unit test of the {@link User} class.
 *
 * @author Richard "Shred" Körber
 */
public class UserTest {

    @Test
    public void testId() throws FlattrException {
        UserId id = User.withId("abc123");
        assertThat(id.getUserId(), is("abc123"));
    }

    @Test
    public void testModel() throws FlattrException, IOException {
        User user = ModelGenerator.createUser();
        ModelGenerator.assertUser(user);
    }

    @Test
    public void testEquals() throws FlattrException {
        User user1 = new User(new FlattrObject("{\"username\":\"simon_g\"}"));
        User user2 = new User(new FlattrObject("{\"username\":\"simon_g\"}"));
        User user3 = new User(new FlattrObject("{\"username\":\"sherlock_h\"}"));

        assertThat(user1, is(equalTo(user2)));
        assertThat(user2, is(equalTo(user1)));
        assertThat(user1, not(equalTo(user3)));
        assertThat(user3, not(equalTo(user1)));
    }

}
