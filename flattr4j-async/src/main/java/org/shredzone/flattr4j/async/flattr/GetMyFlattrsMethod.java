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
package org.shredzone.flattr4j.async.flattr;

import java.util.List;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.async.FlattrCallable;
import org.shredzone.flattr4j.async.PaginatedFlattrCallable;
import org.shredzone.flattr4j.model.Flattr;

/**
 * {@link FlattrCallable} for invoking {@link FlattrService#getMyFlattrs()}
 *
 * @author Iulius Gutberlet
 * @author Richard "Shred" Körber
 */
public class GetMyFlattrsMethod extends PaginatedFlattrCallable<List<Flattr>> {
    private static final long serialVersionUID = -6494222228581076558L;

    @Override
    public List<Flattr> call(FlattrService service, Integer count, Integer page)
        throws Exception {
        return service.getMyFlattrs(count, page);
    }

}
