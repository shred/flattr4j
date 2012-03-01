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
package org.shredzone.flattr4j.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.shredzone.flattr4j.connector.FlattrObject;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.exception.MarshalException;

/**
 * A {@link Submission} is used for creating new Things.
 *
 * @author Richard "Shred" Körber
 */
public class Submission implements Serializable {
    private static final long serialVersionUID = -6684005944290342599L;

    private String url;
    private String title;
    private String description;
    private List<String> tags = new ArrayList<String>();
    private CategoryId category;
    private LanguageId language;
    private Boolean hidden;

    /**
     * URL of the Thing.
     */
    public String getUrl()                      { return url; }
    public void setUrl(String url)              { this.url = url; }

    /**
     * Title of the Thing.
     */
    public String getTitle()                    { return title; }
    public void setTitle(String title)          { this.title = title; }

    /**
     * A descriptive text about the Thing.
     */
    public String getDescription()              { return description; }
    public void setDescription(String description) { this.description = description; }

    /**
     * Tags this Thing is tagged with.
     */
    public List<String> getTags()               { return tags; }
    public void setTags(List<String> tags)      { this.tags = tags; }
    public void addTag(String tag)              { tags.add(tag); }

    /**
     * The category this Thing belongs to.
     */
    public CategoryId getCategory()             { return category; }
    public void setCategory(CategoryId category) { this.category = category; }

    /**
     * The language this Thing is written in.
     */
    public LanguageId getLanguage()             { return language; }
    public void setLanguage(LanguageId language) { this.language = language; }

    /**
     * Is the Thing hidden from the public list of Things at Flattr?
     */
    public Boolean isHidden()                   { return hidden; }
    public void setHidden(Boolean hidden)       { this.hidden = hidden; }

    /**
     * Returns the set of tags as a comma separated list.
     *
     * @return tags
     * @since 2.0
     */
    public String getTagsAsString() {
        StringBuilder sb = new StringBuilder();
        for (String tag : tags) {
            if (tag.indexOf(',') >= 0) {
                throw new MarshalException("tag '" + tag + "' contains invalid character ','");
            }
            sb.append(',').append(tag);
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    /**
     * Returns the submission as {@link FlattrObject}.
     *
     * @return {@link FlattrObject} of this submission.
     * @since 2.0
     */
    public FlattrObject toFlattrObject() throws FlattrException {
        FlattrObject result = new FlattrObject();
        result.put("url", url);

        if (hidden != null) {
            result.put("hidden", hidden);
        }

        if (title != null) {
            result.put("title", title);
        }

        if (description != null) {
            result.put("description", description);
        }

        if (category != null) {
            result.put("category", category.getCategoryId());
        }

        if (language != null) {
            result.put("language", language.getLanguageId());
        }

        if (tags != null && !tags.isEmpty()) {
            result.put("tags", getTagsAsString());
        }

        return result;
    }

}
