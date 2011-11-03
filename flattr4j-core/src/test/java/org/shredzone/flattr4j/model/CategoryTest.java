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

import org.junit.Assert;
import org.junit.Test;
import org.shredzone.flattr4j.connector.FlattrObject;
import org.shredzone.flattr4j.exception.FlattrException;

/**
 * Unit test of the {@link Category} and {@link CategoryId} classes.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 596 $
 */
public class CategoryTest {

    @Test
    public void testId() throws FlattrException {
        CategoryId id = Category.withId("abc123");
        Assert.assertEquals("abc123", id.getCategoryId());
    }

    @Test
    public void testModel() throws FlattrException {
        Category cat = new Category(new FlattrObject("{\"id\":\"images\",\"text\":\"Images\"}"));
        Assert.assertEquals("images", cat.getCategoryId());
        Assert.assertEquals("Images", cat.getName());
    }

    @Test
    public void testEquals() throws FlattrException {
        Category cat1 = new Category(new FlattrObject("{\"id\":\"images\",\"text\":\"Images\"}"));
        Category cat2 = new Category(new FlattrObject("{\"id\":\"images\",\"text\":\"Images\"}"));
        Category cat3 = new Category(new FlattrObject("{\"id\":\"audio\",\"text\":\"Audio\"}"));
        
        Assert.assertTrue(cat1.equals(cat2));
        Assert.assertTrue(cat2.equals(cat1));
        Assert.assertFalse(cat1.equals(cat3));
        Assert.assertFalse(cat3.equals(cat1));
    }
    
}
