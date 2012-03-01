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

import org.shredzone.flattr4j.connector.Connection;

/**
 * Collects search parameters. Every parameter is optional.
 *
 * @author Richard "Shred" Körber
 * @since 2.0
 */
public class SearchQuery implements Serializable {
    private static final long serialVersionUID = 8144711465654878363L;

    /**
     * Enumeration of available sort orders.
     */
    public static enum Order {
        RELEVANCE, TREND, FLATTRS, FLATTRS_MONTH, FLATTRS_WEEK, FLATTRS_DAY;
    }

    private String query;
    private String tags;
    private String url;
    private LanguageId language;
    private CategoryId category;
    private UserId user;
    private Order sort;

    /**
     * Text to search for.
     */
    public String getQuery()                    { return query; }
    public void setQuery(String query)          { this.query = query; }

    /**
     * Tags to search for. Allows logical operators: '|' (or), '&' (and), '!' (not). For
     * example, "game | games ! software" means: search for "game" or "games", but not
     * "software".
     */
    public String getTags()                     { return tags; }
    public void setTags(String tags)            { this.tags = tags; }

    /**
     * URL to search for.
     */
    public String getUrl()                      { return url; }
    public void setUrl(String url)              { this.url = url; }

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
     * Sort order. Defaults to {@link Order#RELEVANCE}.
     */
    public Order getSort()                      { return sort; }
    public void setSort(Order sort)             { this.sort = sort; }

    /**
     * Text to search for.
     */
    public SearchQuery query(String query) {
        setQuery(query);
        return this;
    }

    /**
     * Sets a tag expression to search for.
     */
    public SearchQuery tags(String tags) {
        setTags(tags);
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
     * Order to sort the result by.
     */
    public SearchQuery sort(Order order) {
        setSort(order);
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

        if (tags != null) {
            conn.query("tags", tags);
        }

        if (url != null) {
            conn.query("url", url);
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

        if (sort != null) {
            conn.query("sort", sort.name().toLowerCase());
        }
    }

}
