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
import org.shredzone.flattr4j.connector.Connector;
import org.shredzone.flattr4j.connector.Result;
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
        MockCategoriesConnector connector = new MockCategoriesConnector();
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
        
        connector.assertReaderClosed();
    }

    @Test
    public void languagesTest() throws FlattrException {
        MockLanguagesConnector connector = new MockLanguagesConnector();
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
        
        connector.assertReaderClosed();
    }

    public class MockCategoriesConnector implements Connector {
        private MockResult result;
        
        @Override
        public Result call(String cmd) throws FlattrException {
            Assert.assertEquals("http://api.flattr.com/odapi/categories/text", cmd);

            StringBuilder body = new StringBuilder();
            body.append("text;Written text\n");
            body.append("images;Images\n");
            body.append("video;Video\n");
            result = new MockResult(body.toString());
            return result;
        }

        @Override
        public Result post(String url, String data) throws FlattrException {
            throw new UnsupportedOperationException();
        }
        
        public void assertReaderClosed() {
            if (result != null) {
                result.assertReaderClosed();
            }
        }
    }

    public class MockLanguagesConnector implements Connector {
        private MockResult result;

        @Override
        public Result call(String cmd) throws FlattrException {
            Assert.assertEquals("http://api.flattr.com/odapi/languages/text", cmd);

            StringBuilder body = new StringBuilder();
            body.append("en_GB;English\n");
            body.append("de_DE;German\n");
            body.append("sv_SE;Swedish\n");
            result = new MockResult(body.toString());
            return result;
        }

        @Override
        public Result post(String url, String data) throws FlattrException {
            throw new UnsupportedOperationException();
        }

        public void assertReaderClosed() {
            if (result != null) {
                result.assertReaderClosed();
            }
        }
    }
    
    public class MockResult implements Result {
        private final String body;
        private boolean opened = false;

        public MockResult(String body) {
            this.body = body;
        }
        
        @Override
        public Result assertStatusOk() throws FlattrException {
            return this;
        }

        @Override
        public Reader openReader() throws FlattrException {
            if (opened) {
                throw new IllegalStateException("Reader is already open!");
            }
            opened = true;
            return new StringReader(body);
        }

        @Override
        public void closeReader() throws FlattrException {
            opened = false;
        }
        
        public void assertReaderClosed() {
            if (opened) Assert.fail("Reader was not closed!");
        }
        
    }
}
