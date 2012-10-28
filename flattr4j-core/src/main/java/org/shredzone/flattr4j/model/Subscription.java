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

import java.util.Date;

import org.shredzone.flattr4j.connector.FlattrObject;

/**
 * A Subscription that was made to a thing.
 * <p>
 * This class is not threadsafe.
 *
 * @author Richard "Shred" Körber
 * @since 2.6
 */
public class Subscription extends Resource implements ThingId {
    private static final long serialVersionUID = -6970294508136441692L;

    private transient Thing thing = null;

    public Subscription(FlattrObject data) {
        super(data);
    }

    /**
     * The thing that was flattred.
     */
    public Thing getThing() {
        if (thing == null) {
            thing = new Thing(data.getFlattrObject("thing"));
        }
        return thing;
    }

    @Override
    public String getThingId() {
        return getThing().getThingId();
    }

    /**
     * Creation date of the subscription.
     */
    public Date getCreated() {
        return data.getDate("created_at");
    }

    /**
     * Starting date of the subscription.
     */
    public Date getStarted() {
        return data.getDate("started_at");
    }

    /**
     * {@code true} if the subscription is currently active, {@code false} if it is
     * paused.
     */
    public boolean isActive() {
        return data.getBoolean("active");
    }

}
