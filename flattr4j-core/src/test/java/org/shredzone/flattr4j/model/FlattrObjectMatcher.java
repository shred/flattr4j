/*
 * flattr4j - A Java library for Flattr
 *
 * Copyright (C) 2014 Richard "Shred" Körber
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

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.shredzone.flattr4j.connector.FlattrObject;

/**
 * A hamcrest matcher for matching keys from a {@link FlattrObject}.
 *
 * @author Richard "Shred" Körber
 */
public class FlattrObjectMatcher<T> extends TypeSafeMatcher<FlattrObject> {

    private final String key;
    private final Matcher<T> matcher;

    /**
     * Create a new {@link FlattrObjectMatcher} that matches if the {@link FlattrObject}
     * does <em>not</em> contain the given key.
     *
     * @param key
     *            Key to match
     */
    public FlattrObjectMatcher(String key) {
        this.key = key;
        this.matcher = null;
    }

    /**
     * Create a new {@link FlattrObjectMatcher} that matches the value of the
     * {@link FlattrObject} key.
     *
     * @param key
     *            Key to match
     * @param matcher
     *            Matcher for the value
     */
    public FlattrObjectMatcher(String key, Matcher<T> matcher) {
        this.key = key;
        this.matcher = matcher;
    }

    @Override
    protected boolean matchesSafely(FlattrObject fo) {
        if (matcher != null) {
            return matcher.matches(fo.get(key));
        } else {
            return !fo.has(key);
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("[").appendText(key).appendText("] ");
        if (matcher != null) {
            description.appendText("is ").appendDescriptionOf(matcher);
        } else {
            description.appendText("does not exist");
        }
    }

    /**
     * Matches when the {@link FlattrObject} does not contain the given key.
     *
     * @param key
     *            Key to match
     */
    @Factory
    public static <T> Matcher<FlattrObject> jsonHasNoValue(String key) {
        return new FlattrObjectMatcher<T>(key);
    }

    /**
     * Matches the value of the {@link FlattrObject} key.
     *
     * @param key
     *            Key to match
     * @param matcher
     *            Matcher for the value
     */
    @Factory
    public static <T> Matcher<FlattrObject> jsonValue(String key, Matcher<T> matcher) {
        return new FlattrObjectMatcher<T>(key, matcher);
    }

}
