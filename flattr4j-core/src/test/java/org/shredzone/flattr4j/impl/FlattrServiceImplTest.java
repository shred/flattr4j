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

import java.util.List;
import org.junit.Assert;

import org.junit.Test;
import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.impl.xml.MockDataHelper;
import org.shredzone.flattr4j.model.BrowseTerm;
import org.shredzone.flattr4j.model.Category;
import org.shredzone.flattr4j.model.Language;
import org.shredzone.flattr4j.model.Thing;
import org.shredzone.flattr4j.model.Submission;
import org.shredzone.flattr4j.model.UserDetails;

/**
 * Unit test of the {@link FlattrServiceImpl} class.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class FlattrServiceImplTest {

    private static final String BASE = "http://api.flattr.com/rest/0.5/";

    @Test
    public void testRegister() throws FlattrException {
        MockConnector connector = new MockConnector(BASE + "thing/register");
        connector.setBodyResource("/org/shredzone/flattr4j/impl/xml/Thing.xml");
        connector.setExpectedData(MockDataHelper.createThingXml());

        Submission thing = MockDataHelper.createThing();

        FlattrService service = new FlattrServiceImpl(connector);
        Thing result = service.submit(thing);

        MockDataHelper.assertThingResource(result);

        connector.assertState();
    }

    @Test
    public void testGetThing() throws FlattrException {
        MockConnector connector = new MockConnector(BASE + "thing/get/id/12345");
        connector.setBodyResource("/org/shredzone/flattr4j/impl/xml/Thing.xml");

        FlattrService service = new FlattrServiceImpl(connector);
        Thing result = service.getThing("12345");

        MockDataHelper.assertThingResource(result);

        connector.assertState();
    }

    @Test
    public void testClick() throws FlattrException {
        MockConnector connector = new MockConnector(BASE + "thing/click/id/12345");

        FlattrService service = new FlattrServiceImpl(connector);
        service.click("12345");

        connector.assertState();
    }

    @Test
    public void testSearchThing() throws FlattrException {
        MockConnector connector = new MockConnector(BASE + "thing/browse/query/foo/tag/bar,bla");
        connector.setBodyResource("/org/shredzone/flattr4j/impl/xml/Thing.xml");

        BrowseTerm term = new BrowseTerm().query("foo").tag("bar").tag("bla");

        FlattrService service = new FlattrServiceImpl(connector);
        List<Thing> result = service.browse(term);

        Assert.assertEquals(1, result.size());
        MockDataHelper.assertThingResource(result.get(0));

        connector.assertState();
    }

    @Test
    public void testGetCategoryList() throws FlattrException {
        MockConnector connector = new MockConnector(BASE + "feed/categories");
        connector.setBodyResource("/org/shredzone/flattr4j/impl/xml/Category.xml");

        FlattrService service = new FlattrServiceImpl(connector);
        List<Category> result = service.getCategoryList();

        Assert.assertEquals(2, result.size());

        Category category1 = result.get(0);
        Category category2 = result.get(1);
        MockDataHelper.assertCategoryResource(category1, category2);

        connector.assertState();
    }

    @Test
    public void testGetLanguageList() throws FlattrException {
        MockConnector connector = new MockConnector(BASE + "feed/languages");
        connector.setBodyResource("/org/shredzone/flattr4j/impl/xml/Language.xml");

        FlattrService service = new FlattrServiceImpl(connector);
        List<Language> result = service.getLanguageList();

        Assert.assertEquals(2, result.size());

        Language language1 = result.get(0);
        Language language2 = result.get(1);
        MockDataHelper.assertLanguageResource(language1, language2);

        connector.assertState();
    }

    @Test
    public void testGetMyself() throws FlattrException {
        MockConnector connector = new MockConnector(BASE + "user/me");
        connector.setBodyResource("/org/shredzone/flattr4j/impl/xml/User.xml");

        FlattrService service = new FlattrServiceImpl(connector);
        UserDetails result = service.getMyself();

        MockDataHelper.assertUserResource(result);

        connector.assertState();
    }

    @Test
    public void testGetUser() throws FlattrException {
        MockConnector connector = new MockConnector(BASE + "user/get/id/98765");
        connector.setBodyResource("/org/shredzone/flattr4j/impl/xml/User.xml");

        FlattrService service = new FlattrServiceImpl(connector);
        UserDetails result = service.getUser("98765");

        MockDataHelper.assertUserResource(result);

        connector.assertState();
    }

    @Test
    public void testGetUserByName() throws FlattrException {
        MockConnector connector = new MockConnector(BASE + "user/get/name/blafoo");
        connector.setBodyResource("/org/shredzone/flattr4j/impl/xml/User.xml");

        FlattrService service = new FlattrServiceImpl(connector);
        UserDetails result = service.getUserByName("blafoo");

        MockDataHelper.assertUserResource(result);

        connector.assertState();
    }

}
