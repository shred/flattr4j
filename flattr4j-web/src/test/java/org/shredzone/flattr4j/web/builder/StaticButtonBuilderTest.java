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
package org.shredzone.flattr4j.web.builder;

import org.junit.Assert;
import org.junit.Test;
import org.shredzone.flattr4j.web.ButtonType;

/**
 * Unit test of the {@link StaticButtonBuilder} class.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
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

        Assert.assertEquals(
                "<a href=\"https://flattr.com/thing/123546/a-demo-thing\">"
                + "<img src=\"http://api.flattr.com/button/button-static-50x60.png\""
                + " width=\"50\" height=\"60\" alt=\"Flattr this\" title=\"Flattr this\" border=\"0\" />"
                + "</a>",
                builder.toString()
        );
    }

    @Test
    public void testCompactBuilder() {
        StaticButtonBuilder builder = new StaticButtonBuilder();
        builder.thing("https://flattr.com/thing/123546/a-demo-thing");
        builder.button(ButtonType.COMPACT);

        Assert.assertEquals(
                "<a href=\"https://flattr.com/thing/123546/a-demo-thing\">"
                + "<img src=\"http://api.flattr.com/button/button-compact-static-100x17.png\""
                + " width=\"100\" height=\"17\" alt=\"Flattr this\" title=\"Flattr this\" border=\"0\" />"
                + "</a>",
                builder.toString()
        );
    }

    @Test
    public void testCssBuilder() {
        StaticButtonBuilder builder = new StaticButtonBuilder();
        builder.thing("https://flattr.com/thing/123546/a-demo-thing");
        builder.style("display:none;").styleClass("mybutton");

        Assert.assertEquals(
                "<a class=\"mybutton\" style=\"display:none;\""
                + " href=\"https://flattr.com/thing/123546/a-demo-thing\">"
                + "<img src=\"http://api.flattr.com/button/button-static-50x60.png\""
                + " width=\"50\" height=\"60\" alt=\"Flattr this\" title=\"Flattr this\" border=\"0\" />"
                + "</a>",
                builder.toString()
        );
    }

    @Test
    public void testAttributeBuilder() {
        StaticButtonBuilder builder = new StaticButtonBuilder();
        builder.thing("https://flattr.com/thing/123546/a-demo-thing");
        builder.attribute("target", "_blank");
        builder.attribute("onclick", "window.alert(\"Hello World\")");

        Assert.assertEquals(
                "<a href=\"https://flattr.com/thing/123546/a-demo-thing\""
                + " onclick=\"window.alert(&quot;Hello World&quot;)\" target=\"_blank\">"
                + "<img src=\"http://api.flattr.com/button/button-static-50x60.png\""
                + " width=\"50\" height=\"60\" alt=\"Flattr this\" title=\"Flattr this\" border=\"0\" />"
                + "</a>",
                builder.toString()
        );
    }

}
