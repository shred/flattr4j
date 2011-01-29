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
 *
 */
package org.shredzone.flattr4j.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test of the {@link UserDetails} class.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class UserTest {

    @Test
    public void testProperty() {
        UserDetails user = new UserDetails();

        Assert.assertNull(user.getUserId());
        user.setUserId("1701");
        Assert.assertEquals("1701", user.getUserId());

        Assert.assertNull(user.getUsername());
        user.setUsername("ncc");
        Assert.assertEquals("ncc", user.getUsername());

        Assert.assertNull(user.getFirstname());
        user.setFirstname("James T.");
        Assert.assertEquals("James T.", user.getFirstname());

        Assert.assertNull(user.getLastname());
        user.setLastname("Kirk");
        Assert.assertEquals("Kirk", user.getLastname());

        Assert.assertNull(user.getCity());
        user.setCity("Iowa");
        Assert.assertEquals("Iowa", user.getCity());

        Assert.assertNull(user.getCountry());
        user.setCountry("USA");
        Assert.assertEquals("USA", user.getCountry());

        Assert.assertNull(user.getGravatar());
        user.setGravatar("GRAVATARURL");
        Assert.assertEquals("GRAVATARURL", user.getGravatar());

        Assert.assertNull(user.getEmail());
        user.setEmail("kirk@enterprise.sfl");
        Assert.assertEquals("kirk@enterprise.sfl", user.getEmail());

        Assert.assertNull(user.getDescription());
        user.setDescription("captain of a famous starship");
        Assert.assertEquals("captain of a famous starship", user.getDescription());

        Assert.assertEquals(0, user.getThingcount());
        user.setThingcount(33);
        Assert.assertEquals(33, user.getThingcount());
    }

    @Test
    public void testEquals() {
        User user1 = new User();
        user1.setUserId("foo");

        User user2 = new User();
        user2.setUserId("foo");

        User user3 = new User();
        user3.setUserId("narf");

        Assert.assertTrue("user1 eq user2", user1.equals(user2));
        Assert.assertTrue("user2 eq user1", user2.equals(user1));
        Assert.assertNotSame("user1 !== user2", user2, user1);

        Assert.assertFalse("user1 eq user3", user1.equals(user3));
        Assert.assertFalse("user3 eq user1", user3.equals(user1));
        Assert.assertNotSame("user1 !== user3", user3, user1);
    }

}
