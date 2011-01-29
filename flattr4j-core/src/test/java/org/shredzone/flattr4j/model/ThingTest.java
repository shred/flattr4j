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

import java.util.Date;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test of the {@link Thing} class.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class ThingTest {

    @Test
    public void testProperty() {
        Thing thing = new Thing();

        Assert.assertNull(thing.getThingId());
        thing.setThingId("fed95464a0e8683364189118e0b521c6");
        Assert.assertEquals("fed95464a0e8683364189118e0b521c6", thing.getThingId());

        Assert.assertNull(thing.getInternalId());
        thing.setInternalId("123456");
        Assert.assertEquals("123456", thing.getInternalId());

        Date now = new Date();
        Assert.assertNull(thing.getCreated());
        thing.setCreated(now);
        Assert.assertEquals(now, thing.getCreated());

        Assert.assertEquals(0, thing.getClicks());
        thing.setClicks(133);
        Assert.assertEquals(133, thing.getClicks());

        Assert.assertNull(thing.getUser());
        thing.setUser(new User());
        
        Assert.assertNull(thing.getUser().getUserId());
        thing.getUser().setUserId("foobar");
        Assert.assertEquals("foobar", thing.getUser().getUserId());

        Assert.assertNull(thing.getUser().getUsername());
        thing.getUser().setUsername("Mr. Foo Bar");
        Assert.assertEquals("Mr. Foo Bar", thing.getUser().getUsername());

        Assert.assertNull(thing.getCategory());
        thing.setCategory(new Category());
        
        Assert.assertNull(thing.getCategory().getCategoryId());
        thing.getCategory().setCategoryId("image");
        Assert.assertEquals("image", thing.getCategory().getCategoryId());

        Assert.assertNull(thing.getStatus());
        thing.setStatus(ThingStatus.OWNER);
        Assert.assertEquals(ThingStatus.OWNER, thing.getStatus());
    }

    @Test
    public void testEquals() {
        Thing thing1 = new Thing();
        thing1.setTitle("Thing number one");
        thing1.setThingId("fed95464a0e8683364189118e0b521c6");
        thing1.setInternalId("13123");

        Thing thing2 = new Thing();
        thing2.setTitle("Thing number one");
        thing2.setThingId("fed95464a0e8683364189118e0b521c6");
        thing2.setInternalId("13123");

        Thing thing3 = new Thing();
        thing3.setTitle("Thing number three");
        thing3.setThingId("b99f4a1a9b8d4d7cb1b2848d8160ca1c");
        thing3.setInternalId("35893");

        Thing thing4 = new Thing();
        thing4.setThingId("fed95464a0e8683364189118e0b521c6");

        Assert.assertTrue("thing1 eq thing2", thing1.equals(thing2));
        Assert.assertTrue("thing2 eq thing1", thing2.equals(thing1));
        Assert.assertNotSame("thing1 !== thing2", thing2, thing1);

        Assert.assertFalse("thing1 ne thing3", thing1.equals(thing3));
        Assert.assertFalse("thing3 ne thing1", thing3.equals(thing1));
        Assert.assertNotSame("thing1 !== thing3", thing3, thing1);

        Assert.assertTrue("thing1 eq thing4", thing1.equals(thing4));
        Assert.assertTrue("thing4 eq thing1", thing4.equals(thing1));
        Assert.assertNotSame("thing1 !== thing4", thing4, thing1);
    }

    @Test
    public void testThingUrl() {
        Thing thing = new Thing();
        thing.setInternalId("123456");
        thing.setTitle("This is a  test title: hallå världen");

        String url = thing.getThingUrl();
        Assert.assertEquals("https://flattr.com/thing/123456/This-is-a-test-title-hall-vrlden", url);
        
        String pdfUrl = thing.getQrPdfUrl();
        Assert.assertEquals("https://flattr.com/things/show/id/123456/qrcode/true", pdfUrl);
    }

}
