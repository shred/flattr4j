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
package org.shredzone.flattr4j.impl.xml;

import java.util.Date;

import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.exception.FlattrServiceException;
import org.shredzone.flattr4j.model.Category;
import org.shredzone.flattr4j.model.Thing;
import org.shredzone.flattr4j.model.ThingStatus;
import org.shredzone.flattr4j.model.UserReference;

/**
 * Parses an XML document for {@link Thing} entries.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class ThingXmlParser extends AbstractXmlParser<Thing> {

    private final static String QN_THING = "thing";
    private final static String QN_ID = "id";
    private final static String QN_INT_ID = "int_id";
    private final static String QN_CREATED = "created";
    private final static String QN_LANGUAGE = "language";
    private final static String QN_URL = "url";
    private final static String QN_TITLE = "title";
    private final static String QN_STORY = "story";
    private final static String QN_CLICKS = "clicks";
    private final static String QN_USER = "user";
    private final static String QN_USERNAME = "username";
    private final static String QN_TAGS = "tags";
    private final static String QN_TAG = "tag";
    private final static String QN_CATEGORY = "category";
    private final static String QN_NAME = "name";
    private final static String QN_STATUS = "status";

    private Thing current = null;
    private UserReference user = null;
    private Category category = null;
    private boolean insideUser = false;
    private boolean insideTags = false;
    private boolean insideCategory = false;

    @Override
    protected void parseStartElement(String tag) throws FlattrException {
        if (QN_THING.equals(tag) && current == null) {
            current = new Thing();
       
        } else if (QN_USER.equals(tag) && current != null) {
            user = new UserReference();
            insideUser = true;
        
        } else if (QN_TAGS.equals(tag) && current != null) {
            insideTags = true;
        
        } else if (QN_CATEGORY.equals(tag) && current != null && category == null) {
            category = new Category();
            insideCategory = true;
        }
    }

    @Override
    protected Thing parseEndElement(String tag, String body) throws FlattrException {
        Thing result = null;

        if (QN_THING.equals(tag) && current != null) {
            result = current;
            current = null;

        } else if (QN_USER.equals(tag) && current != null) {
            current.setUser(user);
            user = null;
            insideUser = false;

        } else if (QN_TAGS.equals(tag) && current != null) {
            insideTags = false;
        
        } else if (QN_CATEGORY.equals(tag) && current != null && category != null) {
            current.setCategory(category);
            category = null;
            insideCategory = false;

        } else if (QN_ID.equals(tag) && current != null && !insideUser && !insideTags && !insideCategory) {
            current.setThingId(body);

        } else if (QN_INT_ID.equals(tag) && current != null) {
            current.setInternalId(body);
        
        } else if (QN_CREATED.equals(tag) && current != null) {
            try {
                current.setCreated(new Date(Long.parseLong(body) * 1000));
            } catch (NumberFormatException ex) {
                throw new FlattrServiceException("Invalid creation timestamp: " + body);
            }
        
        } else if (QN_LANGUAGE.equals(tag) && current != null) {
            current.setLanguageId(body);
        
        } else if (QN_URL.equals(tag) && current != null) {
            current.setUrl(body);
        
        } else if (QN_TITLE.equals(tag) && current != null) {
            current.setTitle(body);
        
        } else if (QN_STORY.equals(tag) && current != null) {
            current.setDescription(body);
        
        } else if (QN_CLICKS.equals(tag) && current != null) {
            try {
                current.setClicks(Integer.parseInt(body));
            } catch (NumberFormatException ex) {
                throw new FlattrServiceException("Invalid clicks: " + body);
            }
        
        } else if (QN_ID.equals(tag) && current != null && insideUser) {
            user.setUserId(body);
            
        } else if (QN_ID.equals(tag) && current != null && insideCategory) {
            category.setCategoryId(body);
        
        } else if (QN_USERNAME.equals(tag) && current != null && insideUser) {
            user.setUsername(body);
        
        } else if (QN_TAG.equals(tag) && current != null && insideTags) {
            current.getTags().add(body);
        
        } else if (QN_NAME.equals(tag) && current != null && insideCategory) {
            category.setName(body);
        
        } else if (QN_STATUS.equals(tag) && current != null) {
            try {
                current.setStatus(ThingStatus.valueOf(body.toUpperCase()));
            } catch (IllegalArgumentException ex) {
                throw new FlattrServiceException("Unknown thing status: " + body);
            }
        }
        
        return result;
    }

}
