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
 */
package org.shredzone.flattr4j.impl.xml;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.junit.Assert;
import org.junit.Test;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.RegisteredThing;

/**
 * Unit test of the {@link RegisteredThingXmlParser} class.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class RegisteredThingXmlParserTest {

    @Test
    public void testParser() throws FlattrException, UnsupportedEncodingException {
        InputStream in = RegisteredThingXmlParserTest.class.getResourceAsStream("/org/shredzone/flattr4j/impl/xml/Thing.xml");
        RegisteredThingXmlParser parser = new RegisteredThingXmlParser(new InputStreamReader(in, "UTF-8"));

        RegisteredThing thing;

        thing = parser.getNext();
        MockDataHelper.assertThingResource(thing);

        thing = parser.getNext();
        Assert.assertNull(thing);
    }

}
