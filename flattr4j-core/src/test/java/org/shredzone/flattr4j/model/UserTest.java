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

import java.io.IOException;

import org.junit.Assert;
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
        Assert.assertEquals("abc123", id.getUserId());
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

        Assert.assertTrue(user1.equals(user2));
        Assert.assertTrue(user2.equals(user1));
        Assert.assertFalse(user1.equals(user3));
        Assert.assertFalse(user3.equals(user1));
    }

}
