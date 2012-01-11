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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.shredzone.flattr4j.connector.Connection;

/**
 * Collects search parameters. Every parameter is optional.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 * @since 2.0
 */
public class SearchQuery implements Serializable {
    private static final long serialVersionUID = 8144711465654878363L;

    private String query;
    private Set<String> tags = new HashSet<String>();
    private LanguageId language;
    private CategoryId category;
    private UserId user;

    /**
     * Text to search for.
     */
    public String getQuery()                    { return query; }
    public void setQuery(String query)          { this.query = query; }

    /**
     * Tags to search for.
     */
    public Set<String> getTags()                { return tags; }
    public void setTags(Set<String> tags)       { this.tags = tags; }
    public void addTag(String tag)              { tags.add(tag); }

    /**
     * Language to search for.
     */
    public LanguageId getLanguage()             { return language; }
    public void setLanguage(LanguageId language) { this.language = language; }

    /**
     * Category to search for.
     */
    public CategoryId getCategory()             { return category; }
    public void setCategory(CategoryId category) { this.category = category; }

    /**
     * User to search for.
     */
    public UserId getUser()                     { return user; }
    public void setUser(UserId user)            { this.user = user; }

    /**
     * Text to search for.
     */
    public SearchQuery query(String query) {
        setQuery(query);
        return this;
    }

    /**
     * Adds a tag to search for.
     */
    public SearchQuery tag(String tag) {
        addTag(tag);
        return this;
    }

    /**
     * Language to search for.
     */
    public SearchQuery language(LanguageId language) {
        setLanguage(language);
        return this;
    }

    /**
     * Category to search for.
     */
    public SearchQuery category(CategoryId category) {
        setCategory(category);
        return this;
    }

    /**
     * User to search for.
     */
    public SearchQuery user(UserId user) {
        setUser(user);
        return this;
    }

    /**
     * Sets up a {@link Connection} with the current search parameters.
     *
     * @param conn
     *            {@link Connection} to set up
     */
    public void setupConnection(Connection conn) {
        if (query != null) {
            conn.query("query", query);
        }

        if (tags != null && !tags.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String tag : tags) {
                sb.append(',').append(tag);
            }
            conn.query("tags", sb.substring(1));
        }

        if (language != null) {
            conn.query("language", language.getLanguageId());
        }

        if (category != null) {
            conn.query("category", category.getCategoryId());
        }

        if (user != null) {
            conn.query("user", user.getUserId());
        }
    }

}
