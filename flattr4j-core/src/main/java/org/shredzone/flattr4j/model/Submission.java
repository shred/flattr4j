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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.connector.FlattrObject;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.exception.MarshalException;

/**
 * A {@link Submission} is used for creating new Things.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class Submission implements Serializable {
    private static final long serialVersionUID = -6684005944290342599L;

    private static final String ENCODING = "utf-8";

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
     * Returns a URL that can be used for submitting a Thing (for example in a link).
     * <p>
     * Use this URL as a fallback only if you can't use
     * {@link FlattrService#create(Submission)} or JavaScript. The length of an URL is
     * limited and depends on the browser and the server. The submission will be truncated
     * if the maximum length was exceeded.
     *
     * @param user
     *            {@link UserId} of the user to register the submission with. Required,
     *            must not be {@code null}.
     * @return Submission URL
     * @since 2.0
     */
    public String toUrl(UserId user) {
        if (user == null) {
            throw new IllegalArgumentException("Anonymous submissions are not allowed");
        }

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("https://flattr.com/submit/auto");

            sb.append("?user_id=").append(URLEncoder.encode(user.getUserId(), ENCODING));
            sb.append("&url=").append(URLEncoder.encode(url, ENCODING));

            if (category != null) {
                sb.append("&category=").append(URLEncoder.encode(category.getCategoryId(), ENCODING));
            }

            if (language != null) {
                sb.append("&language=").append(URLEncoder.encode(language.getLanguageId(), ENCODING));
            }

            if (title != null) {
                sb.append("&title=").append(URLEncoder.encode(title, ENCODING));
            }

            if (hidden != null && hidden.booleanValue() == true) {
                sb.append("&hidden=1");
            }

            String tagPrefix = "&tags=";
            for (String tag : tags) {
                if (tag.indexOf(',') >= 0) {
                    throw new MarshalException("tag '" + tag + "' contains invalid character ','");
                }
                sb.append(tagPrefix).append(URLEncoder.encode(tag, ENCODING));
                tagPrefix = ",";
            }

            if (description != null) {
                sb.append("&description=").append(URLEncoder.encode(description, ENCODING));
            }

            return sb.toString();
        } catch (UnsupportedEncodingException ex) {
            throw new InternalError(ENCODING);
        }
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

        StringBuilder sb = new StringBuilder();
        for (String tag : tags) {
            if (tag.indexOf(',') >= 0) {
                throw new MarshalException("tag '" + tag + "' contains invalid character ','");
            }
            sb.append(',').append(tag);
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(0);
            result.put("tags", sb.toString());
        }

        return result;
    }
    
}