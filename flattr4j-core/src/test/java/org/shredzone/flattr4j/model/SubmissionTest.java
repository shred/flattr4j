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
 * Unit test of the {@link Submission} class.
 *
 * @author Richard "Shred" Körber
 * @version $Revision: 596 $
 */
public class SubmissionTest {

    private void setupSubmission(Submission sub) throws FlattrException {
        Assert.assertNull("url", sub.getUrl());
        sub.setUrl("http://flattr4j.shredzone.org");
        Assert.assertEquals("url", "http://flattr4j.shredzone.org", sub.getUrl());

        Assert.assertEquals("{\"url\":\"http://flattr4j.shredzone.org\"}", sub.toFlattrObject().toString());

        Assert.assertNull("title", sub.getTitle());
        sub.setTitle("flattr4j");
        Assert.assertEquals("title", "flattr4j", sub.getTitle());

        Assert.assertNull("description", sub.getDescription());
        sub.setDescription("A Flattr library for Java");
        Assert.assertEquals("description", "A Flattr library for Java", sub.getDescription());

        Assert.assertNull("category", sub.getCategory());
        sub.setCategory(Category.withId("text"));
        Assert.assertEquals("category", "text", sub.getCategory().getCategoryId());

        Assert.assertNull("language", sub.getLanguage());
        sub.setLanguage(Language.withId("en_UK"));
        Assert.assertEquals("language", "en_UK", sub.getLanguage().getLanguageId());

        Assert.assertNull(sub.isHidden());
        sub.setHidden(false);
        Assert.assertFalse("hidden", sub.isHidden());

        Assert.assertTrue("tags", sub.getTags().isEmpty());
        sub.addTag("foo");
        sub.getTags().add("bar");
        Assert.assertEquals("tags", 2, sub.getTags().size());
        Assert.assertEquals("tags", "foo", sub.getTags().get(0));
        Assert.assertEquals("tags", "bar", sub.getTags().get(1));
    }

    @Test
    public void testModel() throws FlattrException, IOException {
        Submission sub = new Submission();
        setupSubmission(sub);

        FlattrObject data = sub.toFlattrObject();
        Assert.assertEquals("url", "http://flattr4j.shredzone.org", data.get("url"));
        Assert.assertEquals("title", "flattr4j", data.get("title"));
        Assert.assertEquals("description", "A Flattr library for Java", data.get("description"));
        Assert.assertEquals("category", "text", data.get("category"));
        Assert.assertEquals("language", "en_UK", data.get("language"));
        Assert.assertFalse("hidden", data.getBoolean("hidden"));
        Assert.assertEquals("tags", "foo,bar", data.get("tags"));
    }

    @Test
    public void testAutoSubmission() throws FlattrException {
        AutoSubmission sub = new AutoSubmission();
        setupSubmission(sub);

        Assert.assertNull("user", sub.getUser());
        sub.setUser(User.withId("scott"));
        Assert.assertEquals("user", "scott", sub.getUser().getUserId());

        String url = sub.toUrl();
        Assert.assertEquals("https://flattr.com/submit/auto" +
                "?user_id=scott" +
                "&url=http%3A%2F%2Fflattr4j.shredzone.org" +
                "&category=text" +
                "&language=en_UK" +
                "&title=flattr4j" +
                "&tags=foo%2Cbar" +
                "&description=A+Flattr+library+for+Java", url);
    }

    @Test(expected = MarshalException.class)
    public void testException() throws FlattrException {
        Submission sub = new Submission();
        sub.setUrl("http://flattr4j.shredzone.org");
        sub.addTag("foo,bar");
        sub.toFlattrObject();
    }

}
