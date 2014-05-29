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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

        assertThat(sq.getQuery(), is("foo"));
        assertThat(sq.getUser().getUserId(), is("shred"));
        assertThat(sq.getTags(), is("foobar & blafoo"));
        assertThat(sq.getUrl(), is("http://flattr4j.shredzone.org"));
        assertThat(sq.getSort(), is(Order.TREND));

        Iterator<CategoryId> itC = sq.getCategories().iterator();
        assertThat(itC.next().getCategoryId(), is("text"));
        assertThat(itC.next().getCategoryId(), is("image"));
        assertThat(itC.hasNext(), is(false));

        Iterator<LanguageId> itL = sq.getLanguages().iterator();
        assertThat(itL.next().getLanguageId(), is("en_UK"));
        assertThat(itL.next().getLanguageId(), is("no_NO"));
        assertThat(itL.hasNext(), is(false));

        TestConnection conn = new TestConnection();
        sq.setupConnection(conn);
        assertThat(conn.getQuery("query"), is("foo"));
        assertThat(conn.getQuery("category"), is("text,image"));
        assertThat(conn.getQuery("language"), is("en_UK,no_NO"));
        assertThat(conn.getQuery("user"), is("shred"));
        assertThat(conn.getQuery("tags"), is("foobar & blafoo"));
        assertThat(conn.getQuery("url"), is("http://flattr4j.shredzone.org"));
        assertThat(conn.getQuery("sort"), is("trend"));
    }

    @Test
    public void testResultModel() throws FlattrException, IOException {
        SearchResult result = ModelGenerator.createSearchResult();

        assertThat(result, allOf(
            hasProperty("totalCount", is(5)),
            hasProperty("itemCount", is(1)),
            hasProperty("page", is(2)),
            hasProperty("things", iterableWithSize(1))
        ));

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
