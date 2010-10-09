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
 * Unit test of the {@link Category} class.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class CategoryTest {

    @Test
    public void testProperty() {
        Category category = new Category();

        Assert.assertNull(category.getId());
        category.setId("text");
        Assert.assertEquals("text", category.getId());

        Assert.assertNull(category.getName());
        category.setName("Texts");
        Assert.assertEquals("Texts", category.getName());
    }

    @Test
    public void testEquals() {
        Category cat1 = new Category();
        cat1.setId("text");

        Category cat2 = new Category();
        cat2.setId("text");

        Category cat3 = new Category();
        cat3.setId("image");

        Assert.assertTrue("cat1 eq cat2", cat1.equals(cat2));
        Assert.assertTrue("cat2 eq cat1", cat2.equals(cat1));
        Assert.assertNotSame("cat1 !== cat2", cat2, cat1);

        Assert.assertFalse("cat1 eq cat3", cat1.equals(cat3));
        Assert.assertFalse("cat3 eq cat1", cat3.equals(cat1));
        Assert.assertNotSame("cat1 !== cat3", cat3, cat1);
    }

}
