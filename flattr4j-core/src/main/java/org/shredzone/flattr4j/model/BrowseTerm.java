/**
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
package org.shredzone.flattr4j.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Collection;

import static org.shredzone.flattr4j.util.ServiceUtils.urlencode;

/**
 * Contains and assembles the browse terms that are passed to
 * {@link org.shredzone.flattr4j.FlattrService#browse(org.shredzone.flattr4j.model.BrowseTerm)}.
 * <p>
 * This class can be used in two ways.
 * <p>
 * One way is to use it like a POJO bean, with its getters and setters. This is useful if
 * the browse terms are to be filled by a GUI or web frontend.
 * <p>
 * Another way is to use it like a builder, e.g.:
 * <pre>
 * new BrowseTerm().query(&quot;flattr4j&quot;).tag(&quot;java&quot;).tag(&quot;api&quot;).category(textCategory);
 * </pre>
 * <p>
 * <em>Note</em> that all tags, categories, languages and users must not contain comma
 * characters (','), as it is a reserved character.
 * <p>
 * <em>Note</em> that even though this class accepts an "unlimited" number of tags,
 * categories, languages and users, the actual browse query will impose limits to the
 * number and length of the terms. This is because the browse request is actually a HTTP
 * GET request with a limited length.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision$
 * @deprecated Not supported by API v2.
 */
@Deprecated
public class BrowseTerm implements Serializable {
    private static final long serialVersionUID = 6549162014192571214L;
    
    private String query = null;
    private Set<String> tags = new HashSet<String>();
    private Set<String> categories = new HashSet<String>();
    private Set<String> languages = new HashSet<String>();
    private Set<String> users = new HashSet<String>();

    public String getQuery()            { return query; }
    public void setQuery(String query)  { this.query = query; }

    public Set<String> getTags()        { return tags; }
    public void setTags(Set<String> tags) {
        if (tags == null) throw new IllegalArgumentException("set must not be null");
        this.tags = tags;
    }

    public Set<String> getCategories()  { return categories; }
    public void setCategories(Set<String> categories) {
        if (categories == null) throw new IllegalArgumentException("set must not be null");
        this.categories = categories;
    }

    public Set<String> getLanguages()   { return languages; }
    public void setLanguages(Set<String> languages) {
        if (languages == null) throw new IllegalArgumentException("set must not be null");
        this.languages = languages;
    }

    public Set<String> getUsers()       { return users; }
    public void setUsers(Set<String> users) {
        if (users == null) throw new IllegalArgumentException("set must not be null");
        this.users = users;
    }

    /**
     * Sets the query term to be used. Only one query term can be set.
     * 
     * @param val
     *            Query term.
     */
    public BrowseTerm query(String val) {
        if (query != null) throw new IllegalArgumentException("query is already set");
        setQuery(val);
        return this;
    }

    /**
     * Adds a tag to restrict the result with. Multiple tags can be set.
     * 
     * @param val
     *            Tag to be set.
     */
    public BrowseTerm tag(String val) {
        getTags().add(val);
        return this;
    }

    /**
     * Adds a {@link CategoryId} to restrict the result with. Multiple categories can be
     * set.
     * 
     * @param val
     *            {@link CategoryId} to be set.
     */
    public BrowseTerm category(CategoryId val) {
        getCategories().add(val.getCategoryId());
        return this;
    }

    /**
     * Adds a {@link LanguageId} to restrict the result with. Multiple languages can be
     * set.
     * 
     * @param val
     *            {@link LanguageId} to be set.
     */
    public BrowseTerm language(LanguageId val) {
        getLanguages().add(val.getLanguageId());
        return this;
    }

    /**
     * Adds a user to restrict the result with. Both usernames and user IDs are accepted.
     * Multiple users can be set.
     * 
     * @param val
     *            User string to be set.
     */
    public BrowseTerm user(String val) {
        getUsers().add(val);
        return this;
    }

    /**
     * Adds a {@link UserId} to restrict the result with. Multiple users can be set.
     * 
     * @param val
     *            {@link UserId} to be set.
     */
    public BrowseTerm user(UserId val) {
        return user(val.getUserId());
    }

    /**
     * Checks if the current browse term is empty.
     * 
     * @return {@code true} if no term has been set.
     */
    public boolean isEmpty() {
        return (getQuery() == null && getTags().isEmpty() && getCategories().isEmpty() && getLanguages().isEmpty() && getUsers().isEmpty());
    }

    /**
     * Returns the contents of the object as a query string.
     * 
     * @return Query string
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        if (query != null) {
            result.append("/query/").append(urlencode(query));
        }

        if (!tags.isEmpty()) {
            result.append("/tag/");
            appendSet("Tag", tags, result);
        }

        if (!categories.isEmpty()) {
            result.append("/category/");
            appendSet("Category", categories, result);
        }

        if (!languages.isEmpty()) {
            result.append("/language/");
            appendSet("Language", languages, result);
        }

        if (!users.isEmpty()) {
            result.append("/user/");
            appendSet("User", users, result);
        }

        if (result.length() > 0 && result.charAt(0) == '/') {
            result.deleteCharAt(0);
        }

        return result.toString();
    }

    /**
     * Appends the entries of a String Collection to the target StringBuilder, separated
     * by comma.
     * 
     * @param type
     *            Type of the Flattr collection (e.g. "Category")
     * @param set
     *            Collection to be appended
     * @param target
     *            StringBuilder to append to
     */
    private void appendSet(String type, Collection<String> set, StringBuilder target) {
        // Sort alphabetically
        String[] entries = set.toArray(new String[set.size()]);
        Arrays.sort(entries);

        boolean commaRequired = false;
        for (String value : entries) {
            if (value == null || value.length() == 0) {
                continue;
            }

            if (value.indexOf(',') >= 0) {
                throw new IllegalStateException(type + " '" + value + "' must not contain ','");
            }

            if (commaRequired) {
                target.append(',');
            }
            commaRequired = true;

            target.append(urlencode(value));
        }
    }

}
