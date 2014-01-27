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
package org.shredzone.flattr4j.async.activity;

import java.util.List;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.async.AbstractFlattrCallable;
import org.shredzone.flattr4j.async.FlattrCallable;
import org.shredzone.flattr4j.model.Activity;

/**
 * {@link FlattrCallable} for invoking
 * {@link FlattrService#getMyActivities(org.shredzone.flattr4j.model.Activity.Type)}
 *
 * @author Iulius Gutberlet
 * @author Richard "Shred" Körber
 */
public class GetMyActivitiesMethod extends AbstractFlattrCallable<List<Activity>> {
    private static final long serialVersionUID = 963490622912496314L;

    private Activity.Type type;

    public GetMyActivitiesMethod() {
        // default constructor
    }

    public GetMyActivitiesMethod(Activity.Type type) {
        this.type = type;
    }

    public Activity.Type getType()              { return type; }
    public void setType(Activity.Type type)     { this.type = type; }

    @Override
    public List<Activity> call(FlattrService service) throws Exception {
        return service.getMyActivities(type);
    }

}
