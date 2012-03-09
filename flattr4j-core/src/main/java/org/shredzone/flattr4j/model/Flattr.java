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

import java.util.Date;

import org.shredzone.flattr4j.connector.FlattrObject;

/**
 * A Flattr that was made to a thing.
 * <p>
 * This class is not threadsafe.
 *
 * @author Richard "Shred" Körber
 * @since 2.0
 */
public class Flattr extends Resource implements ThingId, UserId {
    private static final long serialVersionUID = 8013428651001009374L;

    private transient Thing thing = null;
    private transient User user = null;

    public Flattr(FlattrObject data) {
        super(data);
    }

    /**
     * The thing that was flattred.
     * <p>
     * All properties are only available when the service was set to full mode!
     */
    public Thing getThing() {
        if (thing == null) {
            thing = new Thing(data.getFlattrObject("thing"));
        }
        return thing;
    }

    public Date getCreated() {
        return data.getDate("created_at");
    }

    @Override
    public String getThingId() {
        return getThing().getThingId();
    }

    /**
     * UserID of the user who flattred the thing.
     */
    @Override
    public String getUserId() {
        return data.getSubString("owner", "username");
    }

    /**
     * User who flattred the thing.
     * <p>
     * All properties are only available when the service was set to full mode!
     *
     * @since 2.2
     */
    public User getUser() {
        if (user == null) {
            user = new User(data.getFlattrObject("owner"));
        }
        return user;
    }

}
