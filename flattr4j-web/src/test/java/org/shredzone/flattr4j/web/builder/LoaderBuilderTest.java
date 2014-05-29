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
import org.shredzone.flattr4j.model.User;
import org.shredzone.flattr4j.web.ButtonType;

/**
 * Unit test of the {@link LoaderBuilder} class.
 *
 * @author Richard "Shred" Körber
 */
public class LoaderBuilderTest {

    @Test
    public void testMinimalBuilder() {
        LoaderBuilder builder = new LoaderBuilder();

        assertThat(builder, hasToString(
                "<script type=\"text/javascript\">/* <![CDATA[ */\n"
                + "(function() {var s = document.createElement('script'),"
                + "t = document.getElementsByTagName('script')[0];"
                + "s.type = 'text/javascript';"
                + "s.async = true;"
                + "s.src = 'http://api.flattr.com/js/0.6/load.js?mode=auto';"
                + "t.parentNode.insertBefore(s, t);})();\n"
                + "/* ]]> */</script>"));
    }

    @Test
    public void testManualBuilder() {
        LoaderBuilder builder = new LoaderBuilder();
        builder.manual();

        assertThat(builder, hasToString(
                "<script type=\"text/javascript\">/* <![CDATA[ */\n"
                + "(function() {var s = document.createElement('script'),"
                + "t = document.getElementsByTagName('script')[0];"
                + "s.type = 'text/javascript';"
                + "s.async = true;"
                + "s.src = 'http://api.flattr.com/js/0.6/load.js';"
                + "t.parentNode.insertBefore(s, t);})();\n"
                + "/* ]]> */</script>"));
    }

    @Test
    public void testBareBuilder() {
        LoaderBuilder builder = new LoaderBuilder();
        builder.bare();

        assertThat(builder, hasToString(
                "(function() {var s = document.createElement('script'),"
                + "t = document.getElementsByTagName('script')[0];"
                + "s.type = 'text/javascript';"
                + "s.async = true;"
                + "s.src = 'http://api.flattr.com/js/0.6/load.js?mode=auto';"
                + "t.parentNode.insertBefore(s, t);})();"));
    }

    @Test
    public void testHttpsBuilder() {
        LoaderBuilder builder = new LoaderBuilder();
        builder.https();

        assertThat(builder, hasToString(
                "<script type=\"text/javascript\">/* <![CDATA[ */\n"
                + "(function() {var s = document.createElement('script'),"
                + "t = document.getElementsByTagName('script')[0];"
                + "s.type = 'text/javascript';"
                + "s.async = true;"
                + "s.src = 'https://api.flattr.com/js/0.6/load.js?mode=auto';"
                + "t.parentNode.insertBefore(s, t);})();\n"
                + "/* ]]> */</script>"));
    }

    @Test
    public void testFullBuilder() {
        LoaderBuilder builder = new LoaderBuilder();
        builder.button(ButtonType.COMPACT);
        builder.popout(false);
        builder.category(Category.withId("image")).language(Language.withId("fr_FR"));
        builder.user(User.withId("123456"));
        builder.prefix("data-my");

        assertThat(builder, hasToString(
                "<script type=\"text/javascript\">/* <![CDATA[ */\n"
                + "(function() {var s = document.createElement('script'),"
                + "t = document.getElementsByTagName('script')[0];"
                + "s.type = 'text/javascript';"
                + "s.async = true;"
                + "s.src = 'http://api.flattr.com/js/0.6/load.js?mode=auto&popout=0&uid=123456&button=compact&language=fr_FR&category=image&html5-key-prefix=data-my';"
                + "t.parentNode.insertBefore(s, t);})();\n"
                + "/* ]]> */</script>"));
    }

    @Test
    public void testAlternateBuilder() {
        LoaderBuilder builder = new LoaderBuilder();
        builder.baseUrl("http://localhost").version("0.3");

        assertThat(builder, hasToString(
                "<script type=\"text/javascript\">/* <![CDATA[ */\n"
                + "(function() {var s = document.createElement('script'),"
                + "t = document.getElementsByTagName('script')[0];"
                + "s.type = 'text/javascript';"
                + "s.async = true;"
                + "s.src = 'http://localhost/js/0.3/load.js?mode=auto';"
                + "t.parentNode.insertBefore(s, t);})();\n"
                + "/* ]]> */</script>"));
    }

}
