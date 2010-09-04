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
package org.shredzone.flattr4j.impl;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.shredzone.flattr4j.OpenService;
import org.shredzone.flattr4j.connection.FlattrConnector;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Category;
import org.shredzone.flattr4j.model.Language;

/**
 * Unit test of the {@link OpenServiceImpl} class.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class OpenServiceImplTest {

    @Test
    public void categoriesTest() throws FlattrException {
        FlattrConnector connector = new MockCategoriesConnector();
        OpenService service = new OpenServiceImpl(connector);

        List<Category> result = service.getCategoryList();

        Assert.assertEquals(3, result.size());

        Assert.assertEquals("text", result.get(0).getId());
        Assert.assertEquals("Written text", result.get(0).getName());

        Assert.assertEquals("images", result.get(1).getId());
        Assert.assertEquals("Images", result.get(1).getName());

        Assert.assertEquals("video", result.get(2).getId());
        Assert.assertEquals("Video", result.get(2).getName());

        // The list must not be modifiable!
        try {
            Category newcat = new Category();
            newcat.setId("foo");
            newcat.setName("Foo entry");
            result.add(newcat);
            Assert.fail("List is modifiable");
        } catch (UnsupportedOperationException ex) {
            // We expected this exception, so everything is fine!
        }
    }

    @Test
    public void languagesTest() throws FlattrException {
        FlattrConnector connector = new MockLanguagesConnector();
        OpenService service = new OpenServiceImpl(connector);

        List<Language> result = service.getLanguageList();

        Assert.assertEquals(3, result.size());

        Assert.assertEquals("en_GB", result.get(0).getId());
        Assert.assertEquals("English", result.get(0).getName());

        Assert.assertEquals("de_DE", result.get(1).getId());
        Assert.assertEquals("German", result.get(1).getName());

        Assert.assertEquals("sv_SE", result.get(2).getId());
        Assert.assertEquals("Swedish", result.get(2).getName());

        // The list must not be modifiable!
        try {
            Language newlang = new Language();
            newlang.setId("tlh_TLH");
            newlang.setName("Klingon");
            result.add(newlang);
            Assert.fail("List is modifiable");
        } catch (UnsupportedOperationException ex) {
            // We expected this exception, so everything is fine!
        }
    }

    public class MockCategoriesConnector implements FlattrConnector {
        @Override
        public Reader rawConnect(String url) throws FlattrException {
            Assert.assertEquals("categories/text", url);

            StringBuilder body = new StringBuilder();
            body.append("text;Written text\n");
            body.append("images;Images\n");
            body.append("video;Video\n");
            return new StringReader(body.toString());
        }

        @Override
        public Reader call(String url) throws FlattrException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Reader post(String url, String data) throws FlattrException {
            throw new UnsupportedOperationException();
        }
    }

    public class MockLanguagesConnector implements FlattrConnector {
        @Override
        public Reader rawConnect(String url) throws FlattrException {
            Assert.assertEquals("languages/text", url);

            StringBuilder body = new StringBuilder();
            body.append("en_GB;English\n");
            body.append("de_DE;German\n");
            body.append("sv_SE;Swedish\n");
            return new StringReader(body.toString());
        }

        @Override
        public Reader call(String url) throws FlattrException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Reader post(String url, String data) throws FlattrException {
            throw new UnsupportedOperationException();
        }
    }
}
