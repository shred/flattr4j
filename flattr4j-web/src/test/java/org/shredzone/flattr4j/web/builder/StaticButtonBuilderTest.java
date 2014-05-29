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
import org.shredzone.flattr4j.model.AutoSubmission;
import org.shredzone.flattr4j.model.User;
import org.shredzone.flattr4j.web.BadgeType;

/**
 * Unit test of the {@link StaticButtonBuilder} class.
 *
 * @author Richard "Shred" Körber
 */
public class StaticButtonBuilderTest {

    @Test(expected = IllegalStateException.class)
    public void testNoArgBuilder() {
        StaticButtonBuilder builder = new StaticButtonBuilder();
        builder.toString();
        // throws IllegalStateException because thing url is missing
    }

    @Test
    public void testMinimalBuilder() {
        StaticButtonBuilder builder = new StaticButtonBuilder();
        builder.thing("https://flattr.com/thing/123546/a-demo-thing");

        assertThat(builder, hasToString(
                "<a href=\"https://flattr.com/thing/123546/a-demo-thing\">"
                + "<img src=\"http://api.flattr.com/button/flattr-badge-large.png\""
                + " width=\"93\" height=\"20\" alt=\"Flattr this\" title=\"Flattr this\" border=\"0\" />"
                + "</a>"));
    }

    @Test
    public void testCompactBuilder() {
        StaticButtonBuilder builder = new StaticButtonBuilder();
        builder.thing("https://flattr.com/thing/123546/a-demo-thing");
        builder.badge(BadgeType.SMALL);

        assertThat(builder, hasToString(
                "<a href=\"https://flattr.com/thing/123546/a-demo-thing\">"
                + "<img src=\"http://api.flattr.com/button/flattr-badge-small.png\""
                + " width=\"16\" height=\"16\" alt=\"Flattr this\" title=\"Flattr this\" border=\"0\" />"
                + "</a>"));
    }

    @Test
    public void testCssBuilder() {
        StaticButtonBuilder builder = new StaticButtonBuilder();
        builder.thing("https://flattr.com/thing/123546/a-demo-thing");
        builder.style("display:none;").styleClass("mybutton");

        assertThat(builder, hasToString(
                "<a class=\"mybutton\" style=\"display:none;\""
                + " href=\"https://flattr.com/thing/123546/a-demo-thing\">"
                + "<img src=\"http://api.flattr.com/button/flattr-badge-large.png\""
                + " width=\"93\" height=\"20\" alt=\"Flattr this\" title=\"Flattr this\" border=\"0\" />"
                + "</a>"));
    }

    @Test
    public void testAttributeBuilder() {
        StaticButtonBuilder builder = new StaticButtonBuilder();
        builder.thing("https://flattr.com/thing/123546/a-demo-thing");
        builder.attribute("target", "_blank");
        builder.attribute("onclick", "window.alert(\"Hello World\")");

        assertThat(builder, hasToString(
                "<a href=\"https://flattr.com/thing/123546/a-demo-thing\""
                + " onclick=\"window.alert(&quot;Hello World&quot;)\" target=\"_blank\">"
                + "<img src=\"http://api.flattr.com/button/flattr-badge-large.png\""
                + " width=\"93\" height=\"20\" alt=\"Flattr this\" title=\"Flattr this\" border=\"0\" />"
                + "</a>"));
    }

    @Test
    public void testAutoSubmissionBuilder() {
        AutoSubmission submission = new AutoSubmission();
        submission.setUrl("http://www.shredzone.org");
        submission.setUser(User.withId("shred"));
        submission.addTag("Site");

        StaticButtonBuilder builder = new StaticButtonBuilder();
        builder.thing(submission);

        assertThat(builder, hasToString(
                "<a href=\"https://flattr.com/submit/auto?user_id=shred&amp;" +
                "url=http%3A%2F%2Fwww.shredzone.org&amp;tags=Site\">" +
                "<img src=\"http://api.flattr.com/button/flattr-badge-large.png\"" +
                " width=\"93\" height=\"20\"" +
                " alt=\"Flattr this\" title=\"Flattr this\" border=\"0\" /></a>"));
    }

}
