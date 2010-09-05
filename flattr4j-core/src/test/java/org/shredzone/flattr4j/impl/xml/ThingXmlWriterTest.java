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

import org.junit.Assert;
import org.junit.Test;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Thing;

/**
 * Unit test of the {@link ThingXmlWriter} class.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class ThingXmlWriterTest {

    @Test
    public void writerTest() throws FlattrException {
        Thing thing = new Thing();
        thing.setCategory("text");
        thing.setDescription("This is <em>a new Thing</em>");
        thing.setHidden(false);
        thing.setLanguage("en_UK");
        thing.setTitle("A thingy title");
        thing.setUrl("http://flattr4j.shredzone.org/thingy.html");
        thing.addTag("foo");
        thing.addTag("bar");
        thing.addTag("bla");

        String result = ThingXmlWriter.write(thing);

        Assert.assertEquals(createDocument(), result);
    }

    private String createDocument() {
        StringBuilder sb = new StringBuilder();
        sb.append("<thing>");
        sb.append("<url>http://flattr4j.shredzone.org/thingy.html</url>");
        sb.append("<title><![CDATA[A thingy title]]></title>");
        sb.append("<category>text</category>");
        sb.append("<description><![CDATA[This is <em>a new Thing</em>]]></description>");
        sb.append("<language>en_UK</language>");
        sb.append("<hidden>0</hidden>");
        sb.append("<tags>");
        sb.append("<tag>foo</tag>");
        sb.append("<tag>bar</tag>");
        sb.append("<tag>bla</tag>");
        sb.append("</tags>");
        sb.append("</thing>");
        return sb.toString();
    }

}
