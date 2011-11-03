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
import java.util.Date;

import org.shredzone.flattr4j.connector.FlattrObject;

/**
 * A Flattr that was made to a thing.
 * <p>
 * This class is not threadsafe.
 *
 * @author Richard "Shred" Körber
 * @version $Revision: 505 $
 * @since 2.0
 */
public class Flattr implements ThingId, UserId, Serializable {
    private static final long serialVersionUID = 8013428651001009374L;
    
    private FlattrObject data;
    private transient Thing thing;

    public Flattr(FlattrObject data) {
        this.data = data;
    }

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

}
