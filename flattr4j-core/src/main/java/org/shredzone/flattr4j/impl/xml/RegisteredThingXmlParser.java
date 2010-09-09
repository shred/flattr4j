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

import java.io.Reader;
import java.util.Date;

import javax.xml.namespace.QName;

import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.exception.FlattrServiceException;
import org.shredzone.flattr4j.model.RegisteredThing;
import org.shredzone.flattr4j.model.ThingStatus;

/**
 * Parses an XML document for {@link RegisteredThing} entries.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class RegisteredThingXmlParser extends AbstractXmlParser<RegisteredThing> {

    private final static QName QN_THING = new QName("thing");
    private final static QName QN_ID = new QName("id");
    private final static QName QN_INT_ID = new QName("int_id");
    private final static QName QN_CREATED = new QName("created");
    private final static QName QN_LANGUAGE = new QName("language");
    private final static QName QN_URL = new QName("url");
    private final static QName QN_TITLE = new QName("title");
    private final static QName QN_STORY = new QName("story");
    private final static QName QN_CLICKS = new QName("clicks");
    private final static QName QN_USER = new QName("user");
    private final static QName QN_USERNAME = new QName("username");
    private final static QName QN_TAGS = new QName("tags");
    private final static QName QN_TAG = new QName("tag");
    private final static QName QN_CATEGORY = new QName("category");
    private final static QName QN_NAME = new QName("name");
    private final static QName QN_STATUS = new QName("status");

    private RegisteredThing current = null;
    private boolean insideUser = false;
    private boolean insideTags = false;
    private boolean insideCategory = false;

    public RegisteredThingXmlParser(Reader reader) throws FlattrException {
        super(reader);
    }

    @Override
    protected void parseStartElement(QName tag) throws FlattrException {
        if (tag.equals(QN_THING) && current == null) {
            current = new RegisteredThing();
        } else if (tag.equals(QN_USER) && current != null) {
            insideUser = true;
        } else if (tag.equals(QN_TAGS) && current != null) {
            insideTags = true;
        } else if (tag.equals(QN_CATEGORY) && current != null) {
            insideCategory = true;
        }
    }

    @Override
    protected RegisteredThing parseEndElement(QName tag, String body) throws FlattrException {
        RegisteredThing result = null;

        if (tag.equals(QN_THING) && current != null) {
            result = current;
            current = null;

        } else if (tag.equals(QN_USER) && current != null) {
            insideUser = false;
        } else if (tag.equals(QN_TAGS) && current != null) {
            insideTags = false;
        } else if (tag.equals(QN_CATEGORY) && current != null) {
            insideCategory = false;

        } else if (tag.equals(QN_ID) && current != null && !insideUser && !insideTags && !insideCategory) {
            current.setId(body);
        } else if (tag.equals(QN_INT_ID) && current != null) {
            current.setIntId(body);
        } else if (tag.equals(QN_CREATED) && current != null) {
            try {
                current.setCreated(new Date(Long.parseLong(body) * 1000));
            } catch (NumberFormatException ex) {
                throw new FlattrServiceException("Invalid creation timestamp: " + body);
            }
        } else if (tag.equals(QN_LANGUAGE) && current != null) {
            current.setLanguage(body);
        } else if (tag.equals(QN_URL) && current != null) {
            current.setUrl(body);
        } else if (tag.equals(QN_TITLE) && current != null) {
            current.setTitle(body);
        } else if (tag.equals(QN_STORY) && current != null) {
            current.setDescription(body);
        } else if (tag.equals(QN_CLICKS) && current != null) {
            try {
                current.setClicks(Integer.parseInt(body));
            } catch (NumberFormatException ex) {
                throw new FlattrServiceException("Invalid clicks: " + body);
            }
        } else if (tag.equals(QN_ID) && current != null) {
            if (insideUser) {
                current.setUserId(body);
            } else if (insideCategory) {
                current.setCategory(body);
            }
        } else if (tag.equals(QN_USERNAME) && current != null && insideUser) {
            current.setUserName(body);
        } else if (tag.equals(QN_TAG) && current != null && insideTags) {
            current.getTags().add(body);
        } else if (tag.equals(QN_NAME) && current != null && insideCategory) {
            current.setCategoryName(body);
        } else if (tag.equals(QN_STATUS) && current != null) {
            try {
                current.setStatus(ThingStatus.valueOf(body.toUpperCase()));
            } catch (IllegalArgumentException ex) {
                throw new FlattrServiceException("Unknown thing status: " + body);
            }
        }
        
        return result;
    }

}
