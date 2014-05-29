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
import static org.junit.Assert.*;
import static org.shredzone.flattr4j.model.FlattrObjectMatcher.*;

import java.io.IOException;

import org.junit.Test;
import org.shredzone.flattr4j.connector.FlattrObject;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.exception.MarshalException;

/**
 * Unit test of the {@link Thing} class.
 *
 * @author Richard "Shred" Körber
 */
public class ThingTest {

    @Test
    public void testId() throws FlattrException {
        ThingId id = Thing.withId("abc123");
        assertThat(id.getThingId(), is("abc123"));
    }

    @Test
    public void testModel() throws FlattrException, IOException {
        Thing thing = ModelGenerator.createThing();
        ModelGenerator.assertThing(thing);
    }

    @Test
    public void testUpdate() throws FlattrException, IOException {
        Thing thing = ModelGenerator.createThing();
        ModelGenerator.assertThing(thing);

        assertThat(thing.toUpdate(), nullValue());

        thing.setCategory(Category.withId("image"));
        assertThat(thing, hasProperty("categoryId", is("image")));

        thing.setDescription("foobar");
        assertThat(thing, hasProperty("description", is("foobar")));

        thing.setHidden(true);
        assertThat(thing, hasProperty("hidden", is(true)));

        thing.setLanguage(Language.withId("de_DE"));
        assertThat(thing, hasProperty("languageId", is("de_DE")));

        FlattrObject data1 = thing.toUpdate();
        assertThat(data1, jsonValue("category", is("image")));
        assertThat(data1, jsonValue("description", is("foobar")));
        assertThat(data1, jsonValue("hidden", is("true")));
        assertThat(data1, jsonValue("language", is("de_DE")));
        assertThat(data1, jsonHasNoValue("title"));
        assertThat(data1, jsonHasNoValue("tags"));

        thing.setTitle("footitle");
        assertThat(thing.getTitle(), is("footitle"));

        assertThat(thing.getTags(), contains("twitter"));
        thing.addTag("java");
        assertThat(thing.getTags(), contains("twitter", "java"));

        FlattrObject data2 = thing.toUpdate();
        assertThat(data2, jsonValue("category", is("image")));
        assertThat(data2, jsonValue("description", is("foobar")));
        assertThat(data2, jsonValue("hidden", is("true")));
        assertThat(data2, jsonValue("language", is("de_DE")));
        assertThat(data2, jsonValue("title", is("footitle")));
        assertThat(data2, jsonValue("tags", is("twitter,java")));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMerge() throws FlattrException, IOException {
        Thing thing = ModelGenerator.createThing();

        Submission sub = new Submission();
        sub.setTitle("example title");
        sub.setDescription("example description");
        sub.setCategory(Category.withId("text"));
        sub.setLanguage(Language.withId("es_ES"));
        sub.addTag("foo");
        sub.addTag("bar");
        sub.setHidden(true);

        // Merge with url == null
        thing.merge(sub);
        assertThat(thing, allOf(
            hasProperty("title", is("example title")),
            hasProperty("description", is("example description")),
            hasProperty("categoryId", is("text")),
            hasProperty("languageId", is("es_ES")),
            hasProperty("tags", contains("foo", "bar")),
            hasProperty("hidden", is(true)),
            hasProperty("url", is("http://twitter.com/#!/simongate"))
        ));

        // Merge with url == thing.url
        thing = ModelGenerator.createThing();
        sub.setUrl(thing.getUrl());
        thing.merge(sub);
        assertThat(thing, hasProperty("url", is("http://twitter.com/#!/simongate")));

        // Merge with different url
        thing = ModelGenerator.createThing();
        sub.setUrl("http://www.somewhe.re/over/the/rainbow");
        try {
            thing.merge(sub);
            fail("could use different url");
        } catch (IllegalArgumentException ex) {
            // Expected this exception, as the url is different to the thing's url
        }
    }

    @Test
    public void testEquals() throws FlattrException {
        Thing thing1 = new Thing(new FlattrObject("{\"id\":268185}"));
        Thing thing2 = new Thing(new FlattrObject("{\"id\":268185}"));
        Thing thing3 = new Thing(new FlattrObject("{\"id\":335823}"));

        assertThat(thing1, is(equalTo(thing2)));
        assertThat(thing2, is(equalTo(thing1)));
        assertThat(thing1, not(equalTo(thing3)));
        assertThat(thing3, not(equalTo(thing1)));
    }

    @Test(expected = MarshalException.class)
    public void testException() throws IOException, FlattrException {
        Thing thing = ModelGenerator.createThing();
        thing.addTag("foo,bar");
        thing.toUpdate();
    }

}
