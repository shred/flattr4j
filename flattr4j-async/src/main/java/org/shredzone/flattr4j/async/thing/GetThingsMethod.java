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
package org.shredzone.flattr4j.async.thing;

import java.util.List;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.async.FlattrCallable;
import org.shredzone.flattr4j.async.PaginatedFlattrCallable;
import org.shredzone.flattr4j.model.Thing;
import org.shredzone.flattr4j.model.UserId;

/**
 * {@link FlattrCallable} for invoking {@link FlattrService#getThings(UserId)}
 *
 * @author Iulius Gutberlet
 * @author Richard "Shred" Körber
 */
public class GetThingsMethod extends PaginatedFlattrCallable<List<Thing>> {
    private static final long serialVersionUID = 863322551664804183L;

    private UserId userId;

    public GetThingsMethod() {
        // default constructor
    }

    public GetThingsMethod(UserId userId) {
        this.userId = userId;
    }

    public UserId getUserId()                   { return userId; }
    public void setUserId(UserId userId)        { this.userId = userId; }

    @Override
    public List<Thing> call(FlattrService service, Integer count, Integer page)
        throws Exception {
        return service.getThings(userId, count, page);
    }

}
