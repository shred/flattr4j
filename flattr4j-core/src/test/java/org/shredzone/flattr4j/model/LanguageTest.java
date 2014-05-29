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
 * Unit test of the {@link Language} and {@link LanguageId} classes.
 *
 * @author Richard "Shred" Körber
 */
public class LanguageTest {

    @Test
    public void testId() throws FlattrException {
        LanguageId id = Language.withId("abc123");
        assertThat(id.getLanguageId(), is("abc123"));
    }

    @Test
    public void testModel() throws FlattrException {
        Language lang = new Language(new FlattrObject("{\"id\":\"en_GB\",\"text\":\"English\"}"));
        assertThat(lang.getLanguageId(), is("en_GB"));
        assertThat(lang.getName(), is("English"));
    }

    @Test
    public void testEquals() throws FlattrException {
        Language lang1 = new Language(new FlattrObject("{\"id\":\"en_GB\",\"text\":\"English\"}"));
        Language lang2 = new Language(new FlattrObject("{\"id\":\"en_GB\",\"text\":\"English\"}"));
        Language lang3 = new Language(new FlattrObject("{\"id\":\"sq_AL\",\"text\":\"Albanian\"}"));

        assertThat(lang1, is(equalTo(lang2)));
        assertThat(lang2, is(equalTo(lang1)));
        assertThat(lang1, not(equalTo(lang3)));
        assertThat(lang3, not(equalTo(lang1)));
    }

}
