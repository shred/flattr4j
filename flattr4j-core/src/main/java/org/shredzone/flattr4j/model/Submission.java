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
package org.shredzone.flattr4j.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.shredzone.flattr4j.exception.ValidationException;

/**
 * A {@link Submission}.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class Submission implements Serializable {
    private static final long serialVersionUID = -6684005944290342599L;
    
    private String url;
    private String title;
    private String description;
    private List<String> tags = new ArrayList<String>();
    private CategoryId category;
    private LanguageId language;
    private boolean hidden = false;

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
    public boolean isHidden()                   { return hidden; }
    public void setHidden(boolean hidden)       { this.hidden = hidden; }

    /**
     * Validates the content of this Thing.
     *
     * @throws ValidationException Validation failed. The exception contains the property
     *          name and a reason message.
     */
    public void validate() throws ValidationException {
        if (url == null || url.length() == 0)
            throw new ValidationException("url", "url required");

        if (title == null)
            throw new ValidationException("title", "title required");
        
        if (title.length() < 5)
            throw new ValidationException("title", "title too short (< 5 characters)");
        
        if (title.length() > 100)
            throw new ValidationException("title", "title too long (> 100 characters)");

        if (description == null)
            throw new ValidationException("description", "description is required");

        if (description.length() < 5)
            throw new ValidationException("description", "description too short (< 5 characters)");

        if (description.length() > 1000)
            throw new ValidationException("description", "description too long (> 1000 characters)");

        if (category == null || category.getCategoryId().length() == 0)
            throw new ValidationException("category", "category required");

        for (String tag : tags) {
            if (tag == null || tag.length() == 0)
                throw new ValidationException("tags", "tags contains empty tag");

            if (tag.indexOf(',') >= 0)
                throw new ValidationException("tags", "tag '" + tag + "' contains invalid ','");
        }
    }

}