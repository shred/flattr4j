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
import static org.shredzone.flattr4j.model.FlattrObjectMatcher.jsonValue;

import java.io.IOException;

import org.junit.Test;
import org.shredzone.flattr4j.connector.FlattrObject;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.exception.MarshalException;

/**
 * Unit test of the {@link Submission} class.
 *
 * @author Richard "Shred" Körber
 */
public class SubmissionTest {

    private void setupSubmission(Submission sub) throws FlattrException {
        assertThat(sub.getUrl(), nullValue());
        sub.setUrl("http://flattr4j.shredzone.org");
        assertThat(sub.getUrl(), is("http://flattr4j.shredzone.org"));
        assertThat(sub.toFlattrObject(), hasToString("{\"url\":\"http://flattr4j.shredzone.org\"}"));

        assertThat(sub.getTitle(), nullValue());
        sub.setTitle("flattr4j");
        assertThat(sub.getTitle(), is("flattr4j"));

        assertThat(sub.getDescription(), nullValue());
        sub.setDescription("A Flattr library for Java");
        assertThat(sub.getDescription(), is("A Flattr library for Java"));

        assertThat(sub.getCategory(), nullValue());
        sub.setCategory(Category.withId("text"));
        assertThat(sub.getCategory().getCategoryId(), is("text"));

        assertThat(sub.getLanguage(), nullValue());
        sub.setLanguage(Language.withId("en_UK"));
        assertThat(sub.getLanguage().getLanguageId(), is("en_UK"));

        assertThat(sub.isHidden(), nullValue());
        sub.setHidden(false);
        assertThat(sub.isHidden(), is(false));

        assertThat(sub.getTags(), is(empty()));
        sub.addTag("foo");
        sub.getTags().add("bar");
        assertThat(sub.getTags(), contains("foo", "bar"));
    }

    @Test
    public void testModel() throws FlattrException, IOException {
        Submission sub = new Submission();
        setupSubmission(sub);

        FlattrObject data = sub.toFlattrObject();
        assertThat(data, jsonValue("url", is("http://flattr4j.shredzone.org")));
        assertThat(data, jsonValue("title", is("flattr4j")));
        assertThat(data, jsonValue("description", is("A Flattr library for Java")));
        assertThat(data, jsonValue("category", is("text")));
        assertThat(data, jsonValue("language", is("en_UK")));
        assertThat(data, jsonValue("hidden", is("false")));
        assertThat(data, jsonValue("tags", is("foo,bar")));
    }

    @Test
    public void testAutoSubmission() throws FlattrException {
        AutoSubmission sub = new AutoSubmission();
        setupSubmission(sub);

        assertThat(sub.getUser(), nullValue());
        sub.setUser(User.withId("scott"));
        assertThat(sub.getUser().getUserId(), is("scott"));

        assertThat(sub.toUrl(), is("https://flattr.com/submit/auto" +
                "?user_id=scott" +
                "&url=http%3A%2F%2Fflattr4j.shredzone.org" +
                "&category=text" +
                "&language=en_UK" +
                "&title=flattr4j" +
                "&tags=foo%2Cbar" +
                "&description=A+Flattr+library+for+Java"));
    }

    @Test(expected = MarshalException.class)
    public void testException() throws FlattrException {
        Submission sub = new Submission();
        sub.setUrl("http://flattr4j.shredzone.org");
        sub.addTag("foo,bar");
        sub.toFlattrObject();
    }

}
