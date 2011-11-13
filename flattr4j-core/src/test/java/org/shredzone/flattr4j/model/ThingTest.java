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
import org.shredzone.flattr4j.exception.MarshalException;

/**
 * Unit test of the {@link Thing} class.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 596 $
 */
public class ThingTest {

    @Test
    public void testId() throws FlattrException {
        ThingId id = Thing.withId("abc123");
        Assert.assertEquals("abc123", id.getThingId());
    }

    @Test
    public void testModel() throws FlattrException, IOException {
        Thing thing = ModelGenerator.createThing();
        ModelGenerator.assertThing(thing);
    }

    @Test
    public void testUpdate() throws FlattrException, IOException {
        Thing thing = ModelGenerator.createThing();
        ModelGenerator.assertThing(thing);
        
        Assert.assertNull(thing.toUpdate());
        
        thing.setCategory(Category.withId("image"));
        Assert.assertEquals("category", "image", thing.getCategoryId());
        
        thing.setDescription("foobar");
        Assert.assertEquals("description", "foobar", thing.getDescription());
        
        thing.setHidden(true);
        Assert.assertTrue("hidden", thing.isHidden());
        
        thing.setLanguage(Language.withId("de_DE"));
        Assert.assertEquals("language", "de_DE", thing.getLanguageId());

        FlattrObject data1 = thing.toUpdate();
        Assert.assertEquals("category", "image", data1.get("category"));
        Assert.assertEquals("description", "foobar", data1.get("description"));
        Assert.assertEquals("hidden", true, data1.getBoolean("hidden"));
        Assert.assertEquals("language", "de_DE", data1.get("language"));
        try {
            data1.get("title");
            Assert.fail("title");
        } catch (MarshalException ex) {
            // Expected
        }
        try {
            data1.getObject("tags");
            Assert.fail("tags");
        } catch (MarshalException ex) {
            // Expected
        }

        thing.setTitle("footitle");
        Assert.assertEquals("title", "footitle", thing.getTitle());
        
        Assert.assertEquals("tags", 1, thing.getTags().size());
        thing.addTag("java");
        Assert.assertEquals("tags", 2, thing.getTags().size());
        Assert.assertEquals("tags", "java", thing.getTags().get(1));
        
        FlattrObject data2 = thing.toUpdate();
        Assert.assertEquals("category", "image", data2.get("category"));
        Assert.assertEquals("description", "foobar", data2.get("description"));
        Assert.assertEquals("hidden", true, data2.getBoolean("hidden"));
        Assert.assertEquals("language", "de_DE", data2.get("language"));
        Assert.assertEquals("title", "footitle", data2.get("title"));
        Assert.assertEquals("tags", "twitter,java", data2.get("tags"));
    }
    
    @Test
    public void testMerge() throws FlattrException, IOException {
        Thing thing = ModelGenerator.createThing();
        
        Submission sub = new Submission();
        sub.setTitle("example title");
        sub.setDescription("example description");
        sub.setCategory(Category.withId("text"));
        sub.setLanguage(Language.withId("es_ES"));
        sub.addTag("foo");
        sub.addTag("bar");
        sub.setHidden(true);
        
        // Merge with url == null
        thing.merge(sub);
        Assert.assertEquals("example title", thing.getTitle());
        Assert.assertEquals("example description", thing.getDescription());
        Assert.assertEquals("text", thing.getCategoryId());
        Assert.assertEquals("es_ES", thing.getLanguageId());
        Assert.assertNotNull(thing.getTags());
        Assert.assertEquals(2, thing.getTags().size());
        Assert.assertEquals("foo", thing.getTags().get(0));
        Assert.assertEquals("bar", thing.getTags().get(1));
        Assert.assertTrue(thing.isHidden());
        Assert.assertEquals("http://twitter.com/#!/simongate", thing.getUrl());
        
        // Merge with url == thing.url
        thing = ModelGenerator.createThing();
        sub.setUrl(thing.getUrl());
        thing.merge(sub);
        Assert.assertEquals("http://twitter.com/#!/simongate", thing.getUrl());
        
        // Merge with different url
        thing = ModelGenerator.createThing();
        sub.setUrl("http://www.somewhe.re/over/the/rainbow");
        try {
            thing.merge(sub);
            Assert.fail("could use different url");
        } catch (IllegalArgumentException ex) {
            // Expected this exception, as the url is different to the thing's url
        }
    }

    @Test
    public void testEquals() throws FlattrException {
        Thing thing1 = new Thing(new FlattrObject("{\"id\":268185}"));
        Thing thing2 = new Thing(new FlattrObject("{\"id\":268185}"));
        Thing thing3 = new Thing(new FlattrObject("{\"id\":335823}"));
        
        Assert.assertTrue(thing1.equals(thing2));
        Assert.assertTrue(thing2.equals(thing1));
        Assert.assertFalse(thing1.equals(thing3));
        Assert.assertFalse(thing3.equals(thing1));
    }
    
    @Test(expected = MarshalException.class)
    public void testException() throws IOException, FlattrException {
        Thing thing = ModelGenerator.createThing();
        thing.addTag("foo,bar");
        thing.toUpdate();
    }

}
