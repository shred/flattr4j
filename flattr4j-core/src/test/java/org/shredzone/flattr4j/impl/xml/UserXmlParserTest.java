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
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.junit.Assert;
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
    public void testParser() throws FlattrException, UnsupportedEncodingException {
        InputStream in = UserXmlParserTest.class.getResourceAsStream("/org/shredzone/flattr4j/impl/xml/User.xml");
        UserXmlParser parser = new UserXmlParser();
        parser.parse(in);

        List<User> result = parser.getList();
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());

        User user = result.get(0);
        MockDataHelper.assertUserResource(user);
    }

}
