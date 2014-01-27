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

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.async.FlattrCallable;
import org.shredzone.flattr4j.async.VoidFlattrCallable;
import org.shredzone.flattr4j.model.Thing;

/**
 * {@link FlattrCallable} for invoking {@link FlattrService#update(Thing)}
 *
 * @author Iulius Gutberlet
 * @author Richard "Shred" Körber
 */
public class UpdateMethod extends VoidFlattrCallable {
    private static final long serialVersionUID = 5092656849988342360L;

    private Thing thing;

    public UpdateMethod() {
        // default constructor
    }

    public UpdateMethod(Thing thing) {
        this.thing = thing;
    }

    public Thing getThing()                     { return thing; }
    public void setThing(Thing thing)           { this.thing = thing; }

    @Override
    public void execute(FlattrService service) throws Exception {
        service.update(thing);
    }

}
