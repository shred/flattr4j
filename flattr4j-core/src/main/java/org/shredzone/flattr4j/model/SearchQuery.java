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
import java.util.ArrayList;
import java.util.Collection;

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
    private Collection<LanguageId> languageList = new ArrayList<LanguageId>();
    private Collection<CategoryId> categoryList = new ArrayList<CategoryId>();
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
     * Adds a language to search for.
     * <p>
     * If the collection has been changed via {@link #setLanguage(LanguageId)}, it
     * must be modifiable.
     *
     * @since 2.5
     */
    public void addLanguage(LanguageId language) {
        if (language != null) {
            languageList.add(language);
        }
    }

    /**
     * Returns the collection of languages to search for. The default collection is
     * modifiable.
     *
     * @since 2.5
     */
    public Collection<LanguageId> getLanguages() {
        return languageList;
    }

    /**
     * Sets a collection of languages to search for.
     *
     * @param languages
     *            Collection of {@link LanguageId} to be used for searching. Must not be
     *            {@code null}.
     * @since 2.5
     */
    public void setLanguages(Collection<LanguageId> languages) {
        if (languages == null) {
            throw new IllegalArgumentException("languages list must not be null");
        }
        languageList = languages;
    }

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
     * Adds a category to search for.
     * <p>
     * If the collection has been changed via {@link #setCategories(Collection)}, it
     * must be modifiable.
     *
     * @since 2.2
     */
    public void addCategory(CategoryId category) {
        if (category != null) {
            categoryList.add(category);
        }
    }

    /**
     * Returns the collection of categories to search for.
     * <p>
     * Since 2.5, the default collection is modifiable.
     *
     * @since 2.2
     */
    public Collection<CategoryId> getCategories() {
        return categoryList;
    }

    /**
     * Sets a collection of categories to search for.
     *
     * @param categories
     *            Collection of {@link CategoryId} to be used for searching. Must not be
     *            {@code null}.
     * @since 2.5
     */
    public void setCategories(Collection<CategoryId> categories) {
        if (categories == null) {
            throw new IllegalArgumentException("categories list must not be null");
        }
        categoryList = categories;
    }


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
     * <p>
     * Since version 2.5, multiple languages can be set by invoking this call multiple
     * times.
     */
    public SearchQuery language(LanguageId language) {
        addLanguage(language);
        return this;
    }

    /**
     * Category to search for.
     * <p>
     * Since version 2.2, multiple categories can be set by invoking this call multiple
     * times.
     */
    public SearchQuery category(CategoryId category) {
        addCategory(category);
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

        if (!languageList.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (LanguageId lid : languageList) {
                sb.append(',').append(lid.getLanguageId());
            }
            if (sb.length() > 0) {
                conn.query("language", sb.substring(1));
            }
        }

        if (!categoryList.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (CategoryId cid : categoryList) {
                sb.append(',').append(cid.getCategoryId());
            }
            if (sb.length() > 0) {
                conn.query("category", sb.substring(1));
            }
        }

        if (user != null) {
            conn.query("user", user.getUserId());
        }

        if (sort != null) {
            conn.query("sort", sort.name().toLowerCase());
        }
    }

}
