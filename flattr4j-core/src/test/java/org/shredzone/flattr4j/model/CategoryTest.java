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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.shredzone.flattr4j.connector.FlattrObject;
import org.shredzone.flattr4j.exception.FlattrException;

/**
 * Unit test of the {@link Category} and {@link CategoryId} classes.
 *
 * @author Richard "Shred" Körber
 */
public class CategoryTest {

    @Test
    public void testId() throws FlattrException {
        CategoryId id = Category.withId("abc123");
        assertThat(id.getCategoryId(), is("abc123"));
    }

    @Test
    public void testModel() throws FlattrException {
        Category cat = new Category(new FlattrObject("{\"id\":\"images\",\"text\":\"Images\"}"));
        assertThat(cat.getCategoryId(), is("images"));
        assertThat(cat.getName(), is("Images"));
    }

    @Test
    public void testEquals() throws FlattrException {
        Category cat1 = new Category(new FlattrObject("{\"id\":\"images\",\"text\":\"Images\"}"));
        Category cat2 = new Category(new FlattrObject("{\"id\":\"images\",\"text\":\"Images\"}"));
        Category cat3 = new Category(new FlattrObject("{\"id\":\"audio\",\"text\":\"Audio\"}"));

        assertThat(cat1, is(equalTo(cat2)));
        assertThat(cat2, is(equalTo(cat1)));
        assertThat(cat1, not(equalTo(cat3)));
        assertThat(cat3, not(equalTo(cat1)));
    }

}
