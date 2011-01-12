/**
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
 *
 */
package org.shredzone.flattr4j.model;

import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test of the {@link BrowseTerm} class.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class BrowseTermTest {

    @Test
    public void testEmpty() {
        BrowseTerm term = new BrowseTerm();
        Assert.assertTrue(term.isEmpty());
        Assert.assertEquals("", term.toString());
    }

    @Test
    public void testAsBean() {
        Category cat = new Category();
        cat.setId("category3");

        Language lang = new Language();
        lang.setId("lang2");

        User user = new User();
        user.setId("444231");

        Set<String> langSet = new HashSet<String>();
        langSet.add("lang1");

        BrowseTerm term = new BrowseTerm();
        term.setQuery("aQuery");
        term.getTags().add("tag2");
        term.getTags().add("tag1");
        term.category(cat);
        term.getCategories().add("category2");
        term.getCategories().add("category1");
        term.setLanguages(langSet);
        term.language(lang);
        term.getUsers().add("user1");
        term.user(user);
        term.getUsers().add("123456");

        Assert.assertFalse(term.isEmpty());

        String expected = "query/aQuery/"
                + "tag/tag1,tag2/"
                + "category/category1,category2,category3/"
                + "language/lang1,lang2/"
                + "user/123456,444231,user1";

        Assert.assertEquals(expected, term.toString());
    }

    @Test
    public void testAsBuilder() {
        Category cat = new Category();
        cat.setId("category3");

        Language lang = new Language();
        lang.setId("lang2");

        User user = new User();
        user.setId("444231");

        BrowseTerm term = new BrowseTerm();
        term.query("aQuery");
        term.tag("tag2").tag("tag1").tag("tag2");
        term.category("category1").category(cat).category("category2");
        term.language("lang1").language(lang);
        term.user("user1").user(user).user("123456");

        Assert.assertFalse(term.isEmpty());

        String expected = "query/aQuery/"
                + "tag/tag1,tag2/"
                + "category/category1,category2,category3/"
                + "language/lang1,lang2/"
                + "user/123456,444231,user1";

        Assert.assertEquals(expected, term.toString());
    }

    @Test
    public void testEncoding() {
        BrowseTerm term = new BrowseTerm();
        term.query("aQuery/bQüery");
        term.tag("täg");
        term.getCategories().add("cätegory");
        term.language("läng");
        term.getUsers().add("üser");

        String expected = "query/aQuery%2FbQ%C3%BCery/"
                + "tag/t%C3%A4g/"
                + "category/c%C3%A4tegory/"
                + "language/l%C3%A4ng/"
                + "user/%C3%BCser";

        Assert.assertEquals(expected, term.toString());
    }

    @Test
    public void testQueryOnly() {
        BrowseTerm term = new BrowseTerm();
        term.query("aQuery");

        Assert.assertFalse(term.isEmpty());
        Assert.assertEquals("query/aQuery", term.toString());
    }

    @Test
    public void testTagOnly() {
        BrowseTerm term = new BrowseTerm();
        term.tag("tag1");

        Assert.assertFalse(term.isEmpty());
        Assert.assertEquals("tag/tag1", term.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultipleQuery() {
        BrowseTerm term = new BrowseTerm();
        term.query("aQuery");
        term.query("bQuery");
    }

    @Test
    public void testInvalidCharacters() {
        try {
            BrowseTerm term = new BrowseTerm();
            term.tag("tag,tag");
            term.toString();
            Assert.fail(); // Exception must have been thrown!
        } catch (IllegalStateException ex) {
            Assert.assertEquals("Tag 'tag,tag' must not contain ','", ex.getMessage());
        }

        try {
            BrowseTerm term = new BrowseTerm();
            term.category("category,category");
            term.toString();
            Assert.fail(); // Exception must have been thrown!
        } catch (IllegalStateException ex) {
            Assert.assertEquals("Category 'category,category' must not contain ','", ex.getMessage());
        }

        try {
            BrowseTerm term = new BrowseTerm();
            term.getLanguages().add("language,language");
            term.toString();
            Assert.fail(); // Exception must have been thrown!
        } catch (IllegalStateException ex) {
            Assert.assertEquals("Language 'language,language' must not contain ','", ex.getMessage());
        }

        try {
            BrowseTerm term = new BrowseTerm();
            term.getUsers().add("user,user");
            term.toString();
            Assert.fail(); // Exception must have been thrown!
        } catch (IllegalStateException ex) {
            Assert.assertEquals("User 'user,user' must not contain ','", ex.getMessage());
        }
    }

}
