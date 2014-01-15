/*
 * flattr4j - A Java library for Flattr
 *
 * Copyright (C) 2014 Richard "Shred" Körber
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

import org.shredzone.flattr4j.connector.FlattrObject;

/**
 * A miniature representation of a {@link Thing}.
 * <p>
 * This class is not threadsafe.
 *
 * @author Richard "Shred" Körber
 * @since 2.9
 */
public class MiniThing extends Resource implements ThingId {
    private static final long serialVersionUID = 520173054571474737L;

    public MiniThing(FlattrObject data) {
        super(data);
    }

    /**
     * Thing's unique id at Flattr.
     */
    @Override
    public String getThingId() {
        return data.get("id");
    }

    /**
     * URL that returns details of this resource as JSON.
     */
    public String getResource() {
        return data.get("resource");
    }

    /**
     * Human readable link to this resource at Flattr.
     */
    public String getLink() {
        return data.get("link");
    }

    /**
     * How many times this Thing was flattred.
     */
    public int getClicks() {
        return data.getInt("flattrs");
    }

    /**
     * URL of the Thing.
     */
    public String getUrl() {
        return data.get("url");
    }

    /**
     * Title of the Thing.
     */
    public String getTitle() {
        return data.get("title");
    }

    /**
     * URL of an image for this Thing. Empty string if there is none.
     */
    public String getImage() {
        return (data.has("image") ? data.get("image") : "");
    }

}