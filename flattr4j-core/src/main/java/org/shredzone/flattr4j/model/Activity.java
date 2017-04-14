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
 *
 */
package org.shredzone.flattr4j.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.shredzone.flattr4j.connector.FlattrObject;
import org.shredzone.flattr4j.connector.impl.Logger;
import org.shredzone.flattr4j.exception.MarshalException;

/**
 * Represents a single activity.
 * <p>
 * Handling Activity Streams is beyond the scope of this library. However, this class
 * gives basic access to the Activity Stream item.
 *
 * @see <a href="http://activitystrea.ms/specs/json/1.0/">Activity Streams Specs</a>
 * @author Richard "Shred" Körber
 * @since 2.0
 */
public class Activity extends Resource {
    private static final Logger LOG = new Logger("flattr4j", Activity.class.getName());
    private static final long serialVersionUID = -7610676384296279814L;

    /**
     * Available activity types.
     */
    public static enum Type {
        OUTGOING, INCOMING;
    }

    public Activity(FlattrObject data) {
        super(data);
    }

    /**
     * Returns the activity publication date.
     */
    public Date getPublished() {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            String pub = data.get("published");
            return fmt.parse(pub.replaceAll("(\\d\\d):(\\d\\d)$", "$1$2"));
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Returns the activity title.
     */
    public String getTitle() {
        return data.get("title");
    }

    /**
     * Returns the activity verb.
     */
    public String getVerb() {
        return data.get("verb");
    }

    /**
     * Returns a unique activity ID.
     */
    public String getActivityId() {
        return data.get("id");
    }

    /**
     * Gets a property of the "Actor" object.
     *
     * @param key
     *            property name (e.g. "objectType", "url", "displayName")
     * @return Property value, or {@code null} if there is no such property
     * @see <a href="http://activitystrea.ms/specs/json/1.0/#object">Activity Stream
     *      Objects</a>
     */
    public String getActor(String key) {
        try {
            return data.getSubString("actor", key);
        } catch (MarshalException ex) {
            LOG.debug("actor", ex);
            return null;
        }
    }

    /**
     * Gets a property of the "Object" object.
     *
     * @param key
     *            property name (e.g. "objectType", "url", "displayName")
     * @return Property value, or {@code null} if there is no such property
     * @see <a href="http://activitystrea.ms/specs/json/1.0/#object">Activity Stream
     *      Objects</a>
     */
    public String getObject(String key) {
        try {
            return data.getSubString("object", key);
        } catch (MarshalException ex) {
            LOG.debug("object", ex);
            return null;
        }
    }

}
