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

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.shredzone.flattr4j.model.Category;
import org.shredzone.flattr4j.model.CategoryId;
import org.shredzone.flattr4j.model.Language;
import org.shredzone.flattr4j.model.LanguageId;
import org.shredzone.flattr4j.model.Submission;
import org.shredzone.flattr4j.model.Thing;
import org.shredzone.flattr4j.model.UserId;
import org.shredzone.flattr4j.web.ButtonType;

/**
 * Builds a Flattr button tag.
 * <p>
 * The builder uses sensible default settings that can be changed by using its methods.
 * All methods return a reference to the builder itself, so method calls can be
 * daisy-chained.
 * <p>
 * Example: <code>String button = new ButtonBuilder().url(myUrl).toString();</code>
 *
 * @author Richard "Shred" Körber
 */
public class ButtonBuilder implements Serializable {
    private static final long serialVersionUID = -3066039824282852155L;

    private static final int MIN_TITLE_LENGTH = 5;
    private static final int MAX_TITLE_LENGTH = 100;

    private static final int MIN_DESCRIPTION_LENGTH = 5;
    private static final int MAX_DESCRIPTION_LENGTH = 1000;

    private String url;
    private String uid;
    private String title;
    private String description;
    private String category;
    private String language;
    private ArrayList<String> tags = new ArrayList<String>();
    private String revsharekey;
    private ButtonType type;
    private boolean hidden = false;
    private Boolean popout = null;
    private String style;
    private String styleClass;
    private boolean html5 = false;
    private String prefix = "data-flattr";
    private TreeMap<String, String> attributes = new TreeMap<String, String>();

    /**
     * Unique URL to the thing. Always required!
     */
    public ButtonBuilder url(String url) {
        this.url = url;
        return this;
    }

    /**
     * Unique URL to the thing. Always required! This is a convenience method that accepts
     * {@link URL} objects.
     */
    public ButtonBuilder url(URL url) {
        return url(url.toString());
    }

    /**
     * User who published the thing. Required for autosubmit. Optional if the thing is
     * already published at Flattr.
     */
    public ButtonBuilder user(UserId user) {
        this.uid = user.getUserId();
        return this;
    }

    /**
     * Title of the thing. Required for autosubmit. If a title is set, it must currently
     * be between 5 and 100 characters long, and must not contain HTML.
     */
    public ButtonBuilder title(String title) {
        if (title.length() < MIN_TITLE_LENGTH) {
            throw new IllegalArgumentException("title must have at least "
                + MIN_TITLE_LENGTH + " characters.");
        }
        if (title.length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException("title must not exceed "
                + MAX_TITLE_LENGTH + " characters.");
        }
        this.title = title;
        return this;
    }

    /**
     * Description of the thing. Required for autosubmit. If a description is set, it must
     * currently be between 5 and 1000 characters long, and must not contain HTML (except
     * of &lt;br&gt; which is converted to newline).
     */
    public ButtonBuilder description(String description) {
        if (description.length() < MIN_DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException("description must have at least "
                + MIN_DESCRIPTION_LENGTH + " characters.");
        }
        if (description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException("description must not exceed "
                + MAX_DESCRIPTION_LENGTH + " characters.");
        }
        this.description = description;
        return this;
    }

    /**
     * Convenience method that automatically truncates the description to its maximum
     * accepted length.
     */
    public ButtonBuilder descriptionTruncate(String description) {
        String desc = description;
        if (desc.length() > MAX_DESCRIPTION_LENGTH) {
            desc = desc.substring(0, desc.length());
        }
        return description(desc);
    }

    /**
     * The category the thing is categorized with. Required for autosubmit.
     */
    public ButtonBuilder category(CategoryId category) {
        this.category = category.getCategoryId();
        return this;
    }

    /**
     * Specifies the language the thing is published in. Optional.
     */
    public ButtonBuilder language(LanguageId language) {
        this.language = language.getLanguageId();
        return this;
    }

    /**
     * A tag that further describes the thing. Optional. Tags must not contain ',', ';'
     * and other non-word characters.
     */
    public ButtonBuilder tag(String tag) {
        if (tag.indexOf(',') >= 0 || tag.indexOf(';') >= 0) {
            throw new IllegalArgumentException("illegal character in tag \"" + tag + '"');
        }
        tags.add(tag);
        return this;
    }

    /**
     * Convenience method that adds a collection of tags.
     */
    public ButtonBuilder tags(Collection<String> tags) {
        for (String tag : tags) {
            tag(tag);
        }
        return this;
    }

    /**
     * Sets the revenue share key to be used.
     *
     * @since 2.5
     */
    public ButtonBuilder revsharekey(String key) {
        this.revsharekey = key;
        return this;
    }

    /**
     * Selects the button type to be used. Optional, defaults to the default button.
     */
    public ButtonBuilder button(ButtonType type) {
        this.type = type;
        return this;
    }

    /**
     * Sets whether to override the popout default and show a popout when hovering with
     * the mouse over the button.
     *
     * @param popout
     *            {@code true}: always show a popout, {@code false}: never show a popout
     * @since 2.2
     */
    public ButtonBuilder popout(boolean popout) {
        this.popout = popout;
        return this;
    }

    /**
     * The thing shall not be listed at Flattr. Only used on autosubmit.
     */
    public ButtonBuilder hidden() {
        this.hidden = true;
        return this;
    }

    /**
     * Initializes the builder based on the given {@link Thing}. This is a
     * convenience method to prepare a link to an existing Thing.
     */
    public ButtonBuilder thing(Thing thing) {
        url(thing.getUrl());
        title(thing.getTitle());
        description(thing.getDescription());
        category(Category.withId(thing.getCategoryId()));
        language(Language.withId(thing.getLanguageId()));
        tags(thing.getTags());
        if (thing.isHidden()) hidden();
        return this;
    }

    /**
     * Initializes the builder based on the given {@link Submission}. This is a
     * convenience method to prepare an autosubmit of a thing. Invoke
     * {@link #user(UserId)} together with this method for a successful autosubmission.
     */
    public ButtonBuilder thing(Submission thing) {
        url(thing.getUrl());
        title(thing.getTitle());
        description(thing.getDescription());
        category(thing.getCategory());
        language(thing.getLanguage());
        tags(thing.getTags());
        if (thing.isHidden()) hidden();
        return this;
    }

    /**
     * CSS style to be used.
     */
    public ButtonBuilder style(String style) {
        this.style = style;
        return this;
    }

    /**
     * CSS class to be used. The class "FlattrButton" is always used.
     */
    public ButtonBuilder styleClass(String styleClass) {
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
    public ButtonBuilder attribute(String attribute, String value) {
        String check = attribute.trim().toLowerCase();
        if ("class".equals(check) || "style".equals(check) || "title".equals(check)
            || "rel".equals(check) || "href".equals(check) || "lang".equals(check)
            || check.startsWith(prefix)) {
            throw new IllegalArgumentException("attribute \"" + check + "\" is reserved");
        }

        this.attributes.put(attribute, value);
        return this;
    }

    /**
     * Generate a HTML5 compliant button.
     */
    public ButtonBuilder html5() {
        this.html5 = true;
        return this;
    }

    /**
     * Sets a HTML5 key prefix. By default "data-flattr" is used. The setting is only used
     * in HTML5 mode.
     *
     * @param prefix
     *            HTML5 key prefix. The string must start with "data-"
     */
    public ButtonBuilder prefix(String prefix) {
        if (!prefix.startsWith("data-")) {
            throw new IllegalArgumentException("prefix must start with \"data-\"");
        }
        this.prefix = prefix;
        return this;
    }

    /**
     * Builds a button of the current setup.
     */
    @Override
    public String toString() {
        if (url == null) {
            throw new IllegalStateException("url is required, but missing");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<a");

        sb.append(" class=\"FlattrButton");
        if (styleClass != null) {
            sb.append(' ').append(escape(styleClass));
        }
        sb.append('"');

        if (style != null) {
            sb.append(" style=\"").append(escape(style)).append('"');
        }

        sb.append(" href=\"").append(escape(url)).append('"');

        if (title != null) {
            sb.append(" title=\"").append(escape(title)).append('"');
        }

        if (language != null) {
            sb.append(" lang=\"").append(escape(language)).append('"');
        }

        if (html5) {
            appendHtml5(sb);
        } else {
            appendAttributes(sb);
        }

        if (!attributes.isEmpty()) {
            for (Map.Entry<String, String> entry : attributes.entrySet()) {
                sb.append(' ').append(entry.getKey()).append("=\"");
                sb.append(escape(entry.getValue()));
                sb.append('"');
            }
        }

        sb.append('>');

        if (description != null) {
            sb.append(description);
        }

        sb.append("</a>");

        return sb.toString();
    }

    /**
     * Appends thing attributes to the {@link StringBuilder}.
     *
     * @param sb
     *            {@link StringBuilder} to append the attributes to
     */
    private void appendAttributes(StringBuilder sb) {
        boolean header = false;

        if (uid != null) {
            appendAttributesHeader(sb);
            header = true;
            sb.append("uid:").append(escape(uid)).append(';');
        }

        if (category != null) {
            if (!header) appendAttributesHeader(sb);
            header = true;
            sb.append("category:").append(escape(category)).append(';');
        }

        if (!tags.isEmpty()) {
            if (!header) appendAttributesHeader(sb);
            header = true;
            sb.append("tags:");
            appendTagList(sb);
            sb.append(';');
        }

        if (revsharekey != null) {
            if (!header) appendAttributesHeader(sb);
            header = true;
            sb.append("revsharekey:").append(escape(revsharekey)).append(';');
        }

        if (type != null && type == ButtonType.COMPACT) {
            if (!header) appendAttributesHeader(sb);
            header = true;
            sb.append("button:compact;");
        }

        if (popout != null) {
            if (!header) appendAttributesHeader(sb);
            header = true;
            sb.append("popout:").append(popout.booleanValue() ? 1 : 0).append(';');
        }

        if (hidden) {
            if (!header) appendAttributesHeader(sb);
            header = true;
            sb.append("hidden:1;");
        }

        if (header) {
            sb.append('"');
        }
    }

    /**
     * Appends the header for non-http5 attributes.
     *
     * @param sb
     *            {@link StringBuilder} to append the attributes to
     */
    private void appendAttributesHeader(StringBuilder sb) {
        sb.append(" rel=\"flattr;");
    }

    /**
     * Appends thing attributes as HTML 5 attributes {@link StringBuilder}.
     *
     * @param sb
     *            {@link StringBuilder} to append the attributes to
     */
    private void appendHtml5(StringBuilder sb) {
        if (uid != null) {
            appendHtml5Attribute(sb, "uid", uid);
        }

        if (category != null) {
            appendHtml5Attribute(sb, "category", category);
        }

        if (!tags.isEmpty()) {
            sb.append(' ').append(prefix).append("-tags=\"");
            appendTagList(sb);
            sb.append('"');
        }

        if (revsharekey != null) {
            appendHtml5Attribute(sb, "revsharekey", revsharekey);
        }

        if (type != null && type == ButtonType.COMPACT) {
            appendHtml5Attribute(sb, "button", "compact");
        }

        if (popout != null) {
            appendHtml5Attribute(sb, "popout", popout.booleanValue() ? "1" : "0");
        }

        if (hidden) {
            appendHtml5Attribute(sb, "hidden", "1");
        }
    }

    /**
     * Appends a single HTML5 attribute.
     *
     * @param sb
     *            {@link StringBuilder} to append the attribute to
     * @param key
     *            Attribute key
     * @param value
     *            Value (unescaped)
     */
    private void appendHtml5Attribute(StringBuilder sb, String key, String value) {
        sb.append(' ').append(prefix).append('-').append(key).append("=\"");
        sb.append(escape(value)).append('"');
    }

    /**
     * Appends a list of all tags, separated by comma.
     *
     * @param sb
     *            {@link StringBuilder} to append the tags to
     */
    private void appendTagList(StringBuilder sb) {
        boolean needsSeparator = false;
        for (String tag : tags) {
            if (needsSeparator) sb.append(',');
            sb.append(escape(tag));
            needsSeparator = true;
        }
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
