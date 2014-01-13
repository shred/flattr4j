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

/**
 * {@link FlattrCallable} for invoking {@link FlattrService#getMyThings()}
 *
 * @author Iulius Gutberlet
 * @author Richard "Shred" Körber
 */
public class GetMyThingsMethod extends PaginatedFlattrCallable<List<Thing>> {
    private static final long serialVersionUID = -9087582562478239181L;

    @Override
    public List<Thing> call(FlattrService service, Integer count, Integer page)
        throws Exception {
        return service.getMyThings(count, page);
    }

}
