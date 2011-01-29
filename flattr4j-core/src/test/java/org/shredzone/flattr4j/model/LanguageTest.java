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
 * Unit test of the {@link Language} class.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class LanguageTest {

    @Test
    public void testProperty() {
        Language language = new Language();

        Assert.assertNull(language.getLanguageId());
        language.setLanguageId("en_US");
        Assert.assertEquals("en_US", language.getLanguageId());

        Assert.assertNull(language.getName());
        language.setName("English");
        Assert.assertEquals("English", language.getName());
    }

    @Test
    public void testEquals() {
        Language lang1 = new Language();
        lang1.setLanguageId("de_DE");

        Language lang2 = new Language();
        lang2.setLanguageId("de_DE");

        Language lang3 = new Language();
        lang3.setLanguageId("es_ES");

        Assert.assertTrue("lang1 eq lang2", lang1.equals(lang2));
        Assert.assertTrue("lang2 eq lang1", lang2.equals(lang1));
        Assert.assertNotSame("lang1 !== lang2", lang2, lang1);

        Assert.assertFalse("lang1 eq lang3", lang1.equals(lang3));
        Assert.assertFalse("lang3 eq lang1", lang3.equals(lang1));
        Assert.assertNotSame("lang1 !== lang3", lang3, lang1);
    }

}
