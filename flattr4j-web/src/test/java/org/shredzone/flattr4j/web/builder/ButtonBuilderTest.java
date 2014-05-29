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
package org.shredzone.flattr4j.web.builder;

import static org.hamcrest.Matchers.hasToString;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.shredzone.flattr4j.model.Category;
import org.shredzone.flattr4j.model.Language;
import org.shredzone.flattr4j.model.Submission;
import org.shredzone.flattr4j.model.User;
import org.shredzone.flattr4j.web.ButtonType;

/**
 * Unit test of the {@link ButtonBuilder} class.
 *
 * @author Richard "Shred" Körber
 */
public class ButtonBuilderTest {

    @Test(expected = IllegalStateException.class)
    public void testNoArgBuilder() {
        ButtonBuilder builder = new ButtonBuilder();
        builder.toString();
        // throws IllegalStateException because url is missing
    }

    @Test
    public void testMinimalBuilder() {
        ButtonBuilder builder = new ButtonBuilder();
        builder.url("http://example.com/page/123");

        assertThat(builder, hasToString(
            "<a class=\"FlattrButton\" href=\"http://example.com/page/123\"></a>"));
    }

    @Test
    public void testCssBuilder() {
        ButtonBuilder builder = new ButtonBuilder();
        builder.url("http://example.com/page/123");
        builder.style("display:none;").styleClass("mybutton");

        assertThat(builder, hasToString(
                "<a class=\"FlattrButton mybutton\" style=\"display:none;\""
                + " href=\"http://example.com/page/123\"></a>"));
    }

    @Test
    public void testFullBuilder() {
        ButtonBuilder builder = new ButtonBuilder();
        builder.url("http://example.com/page/123");
        builder.style("display:none;").styleClass("mybutton");
        builder.button(ButtonType.COMPACT);
        builder.popout(true);
        builder.category(Category.withId("text")).language(Language.withId("en_UK"));
        builder.user(User.withId("123456"));
        builder.title("A Title");
        builder.description("A Description");
        builder.tag("def").tag("123").tag("abc");
        builder.revsharekey("f00b1337");
        builder.hidden();

        assertThat(builder, hasToString(
                "<a class=\"FlattrButton mybutton\" style=\"display:none;\""
                + " href=\"http://example.com/page/123\" title=\"A Title\""
                + " lang=\"en_UK\""
                + " rel=\"flattr;uid:123456;category:text;tags:def,123,abc;revsharekey:f00b1337;button:compact;popout:1;hidden:1;\">"
                + "A Description</a>"));
    }

    @Test
    public void testFullHtml5Builder() {
        ButtonBuilder builder = new ButtonBuilder();
        builder.url("http://example.com/page/123");
        builder.style("display:none;").styleClass("mybutton");
        builder.button(ButtonType.COMPACT);
        builder.popout(false);
        builder.category(Category.withId("text")).language(Language.withId("en_UK"));
        builder.user(User.withId("123456"));
        builder.title("A Title");
        builder.description("A Description");
        builder.tag("def").tag("123").tag("abc");
        builder.revsharekey("f00b1337");
        builder.hidden();
        builder.html5();

        assertThat(builder, hasToString(
                "<a class=\"FlattrButton mybutton\" style=\"display:none;\""
                + " href=\"http://example.com/page/123\" title=\"A Title\""
                + " lang=\"en_UK\""
                + " data-flattr-uid=\"123456\" data-flattr-category=\"text\""
                + " data-flattr-tags=\"def,123,abc\" data-flattr-revsharekey=\"f00b1337\""
                + " data-flattr-button=\"compact\" data-flattr-popout=\"0\""
                + " data-flattr-hidden=\"1\">"
                + "A Description</a>"));
    }

    @Test
    public void testFullPrefixHtml5Builder() {
        ButtonBuilder builder = new ButtonBuilder();
        builder.url("http://example.com/page/123");
        builder.style("display:none;").styleClass("mybutton");
        builder.button(ButtonType.COMPACT);
        builder.category(Category.withId("text")).language(Language.withId("en_UK"));
        builder.user(User.withId("123456"));
        builder.title("A Title");
        builder.description("A Description");
        builder.tag("def").tag("123").tag("abc");
        builder.revsharekey("f00b1337");
        builder.hidden();
        builder.html5().prefix("data-my");

        assertThat(builder, hasToString(
                "<a class=\"FlattrButton mybutton\" style=\"display:none;\""
                + " href=\"http://example.com/page/123\" title=\"A Title\""
                + " lang=\"en_UK\""
                + " data-my-uid=\"123456\" data-my-category=\"text\""
                + " data-my-tags=\"def,123,abc\" data-my-revsharekey=\"f00b1337\""
                + " data-my-button=\"compact\" data-my-hidden=\"1\">"
                + "A Description</a>"));
    }

    @Test
    public void testThingBuilder() {
        ButtonBuilder builder = new ButtonBuilder();
        builder.user(User.withId("123456")).thing(createSubmission());

        assertThat(builder, hasToString(
                "<a class=\"FlattrButton\" href=\"http://flattr4j.shredzone.org/thingy.html\""
                + " title=\"A thingy title\" lang=\"en_UK\""
                + " rel=\"flattr;uid:123456;category:text;tags:foo,bar,bla;\">"
                + "This is <em>a new Thing</em></a>"));
    }

    @Test
    public void testAttributeBuilder() {
        ButtonBuilder builder = new ButtonBuilder();
        builder.url("http://example.com/page/123");
        builder.attribute("target", "_blank");
        builder.attribute("onclick", "window.alert(\"Hello World\")");

        assertThat(builder, hasToString(
                "<a class=\"FlattrButton\" href=\"http://example.com/page/123\""
                + " onclick=\"window.alert(&quot;Hello World&quot;)\" target=\"_blank\"></a>"));
    }

    /**
     * Creates a filled {@link Submission}.
     *
     * @return {@link Submission}
     */
    public static Submission createSubmission() {
        Submission thing = new Submission();
        thing.setCategory(Category.withId("text"));
        thing.setDescription("This is <em>a new Thing</em>");
        thing.setHidden(false);
        thing.setLanguage(Language.withId("en_UK"));
        thing.setTitle("A thingy title");
        thing.setUrl("http://flattr4j.shredzone.org/thingy.html");
        thing.addTag("foo");
        thing.addTag("bar");
        thing.addTag("bla");
        return thing;
    }

}
