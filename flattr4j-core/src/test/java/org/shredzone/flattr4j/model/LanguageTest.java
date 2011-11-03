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
 * Unit test of the {@link Language} and {@link LanguageId} classes.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 596 $
 */
public class LanguageTest {

    @Test
    public void testId() throws FlattrException {
        LanguageId id = Language.withId("abc123");
        Assert.assertEquals("abc123", id.getLanguageId());
    }

    @Test
    public void testModel() throws FlattrException {
        Language lang = new Language(new FlattrObject("{\"id\":\"en_GB\",\"text\":\"English\"}"));
        Assert.assertEquals("en_GB", lang.getLanguageId());
        Assert.assertEquals("English", lang.getName());
    }

    @Test
    public void testEquals() throws FlattrException {
        Language lang1 = new Language(new FlattrObject("{\"id\":\"en_GB\",\"text\":\"English\"}"));
        Language lang2 = new Language(new FlattrObject("{\"id\":\"en_GB\",\"text\":\"English\"}"));
        Language lang3 = new Language(new FlattrObject("{\"id\":\"sq_AL\",\"text\":\"Albanian\"}"));
        
        Assert.assertTrue(lang1.equals(lang2));
        Assert.assertTrue(lang2.equals(lang1));
        Assert.assertFalse(lang1.equals(lang3));
        Assert.assertFalse(lang3.equals(lang1));
    }
    
}
