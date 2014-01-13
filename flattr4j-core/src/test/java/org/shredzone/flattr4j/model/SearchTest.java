/*
 * flattr4j - A Java library for Flattr
 *
 * Copyright (C) 2012 Richard "Shred" Körber
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.shredzone.flattr4j.connector.Connection;
import org.shredzone.flattr4j.connector.FlattrObject;
import org.shredzone.flattr4j.connector.RateLimit;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.SearchQuery.Order;
import org.shredzone.flattr4j.oauth.AccessToken;
import org.shredzone.flattr4j.oauth.ConsumerKey;

/**
 * Unit test of the {@link SearchQuery} and {@link SearchResult} class.
 *
 * @author Richard "Shred" Körber
 */
public class SearchTest {

    @Test
    public void testQueryModel() {
        SearchQuery sq = new SearchQuery();
        sq.setQuery("foo");
        sq.getCategories().add(Category.withId("text"));
        sq.addCategory(Category.withId("image"));
        sq.getLanguages().add(Language.withId("en_UK"));
        sq.addLanguage(Language.withId("no_NO"));
        sq.setUser(User.withId("shred"));
        sq.setTags("foobar & blafoo");
        sq.setUrl("http://flattr4j.shredzone.org");
        sq.setSort(Order.TREND);

        Assert.assertEquals("foo", sq.getQuery());
        Assert.assertEquals("shred", sq.getUser().getUserId());
        Assert.assertEquals("foobar & blafoo", sq.getTags());
        Assert.assertEquals("http://flattr4j.shredzone.org", sq.getUrl());
        Assert.assertEquals(Order.TREND, sq.getSort());

        Iterator<CategoryId> itC = sq.getCategories().iterator();
        Assert.assertEquals("text", itC.next().getCategoryId());
        Assert.assertEquals("image", itC.next().getCategoryId());

        Iterator<LanguageId> itL = sq.getLanguages().iterator();
        Assert.assertEquals("en_UK", itL.next().getLanguageId());
        Assert.assertEquals("no_NO", itL.next().getLanguageId());

        TestConnection conn = new TestConnection();
        sq.setupConnection(conn);
        Assert.assertEquals("foo", conn.getQuery("query"));
        Assert.assertEquals("text,image", conn.getQuery("category"));
        Assert.assertEquals("en_UK,no_NO", conn.getQuery("language"));
        Assert.assertEquals("shred", conn.getQuery("user"));
        Assert.assertEquals("foobar & blafoo", conn.getQuery("tags"));
        Assert.assertEquals("http://flattr4j.shredzone.org", conn.getQuery("url"));
        Assert.assertEquals("trend", conn.getQuery("sort"));
    }

    @Test
    public void testResultModel() throws FlattrException, IOException {
        SearchResult result = ModelGenerator.createSearchResult();

        Assert.assertEquals("totalCount", 5, result.getTotalCount());
        Assert.assertEquals("itemCount", 1, result.getItemCount());
        Assert.assertEquals("page", 2, result.getPage());
        Assert.assertNotNull("things", result.getThings());
        Assert.assertEquals("things.size", 1, result.getThings().size());

        ModelGenerator.assertThing(result.getThings().get(0));
    }

    /**
     * Dummy {@link Connection} that collects query parameters.
     */
    private static class TestConnection implements Connection {
        private Map<String, String> query = new HashMap<String, String>();

        @Override
        public Connection url(String url) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Connection token(AccessToken token) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Connection key(ConsumerKey key) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Connection call(String call) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Connection parameter(String name, String value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Connection parameterArray(String name, String[] value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Connection query(String name, String value) {
            query.put(name, value);
            return this;
        }

        @Override
        public Connection data(FlattrObject data) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Connection form(String name, String value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Connection rateLimit(RateLimit limit) {
            throw new UnsupportedOperationException();
        }

        @Override
        public FlattrObject singleResult() throws FlattrException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Collection<FlattrObject> result() throws FlattrException {
            throw new UnsupportedOperationException();
        }

        public String getQuery(String name) {
            return query.get(name);
        }
    }

}
