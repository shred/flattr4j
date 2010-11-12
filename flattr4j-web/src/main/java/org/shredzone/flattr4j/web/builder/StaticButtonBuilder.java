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

import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

import org.shredzone.flattr4j.model.RegisteredThing;
import org.shredzone.flattr4j.web.BadgeType;

/**
 * Builds a static Flattr button tag. A static button works without JavaScript, but does
 * not offer enhanced features like a click counter.
 * <p>
 * The builder uses sensible default settings that can be changed by using its methods.
 * All methods return a reference to the builder itself, so method calls can be
 * daisy-chained.
 * <p>
 * Example:
 * <code>String button = new StaticButtonBuilder().url(thingUrl).toString();</code>
 * 
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class StaticButtonBuilder {
    private String url;
    private String badgeUrl;
    private BadgeType type;
    private String style;
    private String styleClass;
    private Map<String, String> attributes = new TreeMap<String, String>();

    /**
     * Link to the Thing page at Flattr.
     */
    public StaticButtonBuilder thing(String url) {
        this.url = url;
        return this;
    }

    /**
     * Link to the Thing page at Flattr.
     */
    public StaticButtonBuilder thing(RegisteredThing thing) {
        return thing(thing.getThingUrl());
    }

    /**
     * Selects the badge type to be used. Optional, defaults to the default badge.
     */
    public StaticButtonBuilder badge(BadgeType type) {
        this.type = type;
        return this;
    }

    /**
     * A URL to a custom badge image. If this url is given, an invocation of the
     * {@link #badge(org.shredzone.flattr4j.web.BadgeType)} method is ignored.
     * 
     * @param url
     *            Button image URL
     */
    public StaticButtonBuilder badgeUrl(String url) {
        this.badgeUrl = url;
        return this;
    }

    /**
     * A URL to a custom badge image. Convenience method that takes an URL object.
     */
    public StaticButtonBuilder badgeUrl(URL url) {
        return badgeUrl(url.toString());
    }

    /**
     * CSS style to be used.
     */
    public StaticButtonBuilder style(String style) {
        this.style = style;
        return this;
    }

    /**
     * CSS class to be used.
     */
    public StaticButtonBuilder styleClass(String styleClass) {
        this.styleClass = styleClass;
        return this;
    }

    /**
     * Adds a custom HTML attribute to the generated link tag. If an attribute has already
     * been added, its value will be replaced. Attributes are written to the tag in
     * alphabetical order.
     * <p>
     * Attributes are added without further checks. It is your responsibility to take care
     * for HTML compliance.
     * 
     * @param attribute
     *            HTML attribute to be added
     * @param value
     *            Value of that attribute. The builder takes care for proper HTML
     *            escaping.
     */
    public StaticButtonBuilder attribute(String attribute, String value) {
        String check = attribute.trim().toLowerCase();
        if ("class".equals(check) || "style".equals(check) || "href".equals(check)) {
            throw new IllegalArgumentException("attribute \"" + check + "\" is reserved");
        }

        this.attributes.put(attribute, value);
        return this;
    }

    /**
     * Builds a static button tag of the current setup.
     */
    @Override
    public String toString() {
        if (url == null) {
            throw new IllegalStateException("thing url is required, but missing");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<a");

        if (styleClass != null) {
            sb.append(" class=\"").append(escape(styleClass)).append('"');
        }

        if (style != null) {
            sb.append(" style=\"").append(escape(style)).append('"');
        }

        sb.append(" href=\"").append(escape(url)).append('"');

        for (String attr : attributes.keySet()) {
            sb.append(' ').append(attr).append("=\"");
            sb.append(escape(attributes.get(attr)));
            sb.append('"');
        }

        sb.append('>');

        if (badgeUrl != null) {
            sb.append("<img src=\"").append(escape(badgeUrl)).append('"')
                            .append(" alt=\"Flattr this\"")
                            .append(" title=\"Flattr this\"")
                            .append(" border=\"0\"")
                            .append(" />");
            
        } else {
            BadgeType useType = (type != null ? type : BadgeType.DEFAULT);
            sb.append("<img src=\"").append(useType.getUrl()).append('"')
                            .append(" width=\"").append(useType.getWidth()).append('"')
                            .append(" height=\"").append(useType.getHeight()).append('"')
                            .append(" alt=\"Flattr this\"")
                            .append(" title=\"Flattr this\"")
                            .append(" border=\"0\"")
                            .append(" />");
        }

        sb.append("</a>");

        return sb.toString();
    }

    /**
     * Escapes a string for use in HTML attributes.
     * 
     * @param str
     *            Attribute to be escaped
     * @return Escaped attribute
     */
    private String escape(String str) {
        return str.replace("&", "&amp;").replace("<", "&lt;").replace("\"", "&quot;");
    }

}
