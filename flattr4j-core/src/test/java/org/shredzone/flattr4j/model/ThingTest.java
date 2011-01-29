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
 * Unit test of the {@link ThingSubmission} class.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class ThingTest {

    @Test
    public void testProperty() {
        ThingSubmission thing = new ThingSubmission();

        Assert.assertNull(thing.getUrl());
        thing.setUrl("http://www.example.com/page.html");
        Assert.assertEquals("http://www.example.com/page.html", thing.getUrl());

        Assert.assertNull(thing.getTitle());
        thing.setTitle("an example page");
        Assert.assertEquals("an example page", thing.getTitle());

        Assert.assertNull(thing.getCategory());
        thing.setCategory(new Category());
        
        Assert.assertNull(thing.getCategory());
        thing.setCategory("text");
        Assert.assertEquals("text", thing.getCategory());

        Assert.assertNull(thing.getDescription());
        thing.setDescription("a long description of the example");
        Assert.assertEquals("a long description of the example", thing.getDescription());

        Assert.assertNull(thing.getLanguage());
        thing.setLanguage("en_US");
        Assert.assertEquals("en_US", thing.getLanguage());

        Assert.assertFalse(thing.isHidden());
        thing.setHidden(true);
        Assert.assertTrue(thing.isHidden());

        Assert.assertTrue(thing.getTags().isEmpty());
        thing.addTag("foo");
        thing.addTag("bar");
        thing.addTag("narf");
        Assert.assertEquals(3, thing.getTags().size());
        Assert.assertEquals("foo", thing.getTags().get(0));
        Assert.assertEquals("bar", thing.getTags().get(1));
        Assert.assertEquals("narf", thing.getTags().get(2));
    }

}
