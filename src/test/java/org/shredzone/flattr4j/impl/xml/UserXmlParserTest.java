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
package org.shredzone.flattr4j.impl.xml;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import junit.framework.Assert;

import org.junit.Test;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.User;

/**
 * Unit test of the {@link UserXmlParser} class.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class UserXmlParserTest {

    @Test
    public void parserTest() throws FlattrException, UnsupportedEncodingException {
        InputStream in = UserXmlParserTest.class.getResourceAsStream("/org/shredzone/flattr4j/impl/xml/User.xml");
        UserXmlParser parser = new UserXmlParser(new InputStreamReader(in, "UTF-8"));

        User user;

        user = parser.getNext();
        Assert.assertEquals("244", user.getId());
        Assert.assertEquals("Bomelin", user.getUsername());
        Assert.assertEquals("Mattias", user.getFirstname());
        Assert.assertEquals("Bomelin", user.getLastname());
        Assert.assertEquals("Skurup", user.getCity());
        Assert.assertEquals("Sweden", user.getCountry());
        Assert.assertEquals("https://secure.gravatar.com/avatar/59bc275a1d17a4f2ec448538426803bf?s=120&r=pg", user.getGravatar());
        Assert.assertEquals("mattias@flattr.com.invalid", user.getEmail());
        Assert.assertEquals("Flattr meee", user.getDescription());
        Assert.assertEquals(10, user.getThingcount());

        user = parser.getNext();
        Assert.assertNull(user);
    }

}
