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
import org.shredzone.flattr4j.async.AbstractFlattrCallable;
import org.shredzone.flattr4j.async.FlattrCallable;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.AutoSubmission;
import org.shredzone.flattr4j.model.Thing;
import org.shredzone.flattr4j.model.ThingId;

/**
 * {@link FlattrCallable} for invoking {@link FlattrService#getThing(ThingId)},
 * {@link FlattrService#getThingByUrl(String)} or
 * {@link FlattrService#getThingBySubmission(AutoSubmission)}
 *
 * @author Iulius Gutberlet
 * @author Richard "Shred" Körber
 */
public class GetThingMethod extends AbstractFlattrCallable<Thing> {
    private static final long serialVersionUID = 3009657689598484152L;

    private final ThingId thingId;
    private final String url;
    private final AutoSubmission submission;

    public GetThingMethod(ThingId thingId) {
        this(thingId, null, null);
    }

    public GetThingMethod(String url) {
        this(null, url, null);
    }

    public GetThingMethod(AutoSubmission submission) {
        this(null, null, submission);
    }

    private GetThingMethod(ThingId thingId, String url, AutoSubmission submission) {
        if (thingId == null && url == null && submission == null) {
            throw new IllegalArgumentException("parameter must not be null");
        }
        this.thingId = thingId;
        this.url = url;
        this.submission = submission;
    }

    @Override
    public Thing call(FlattrService service) throws FlattrException {
        if (thingId != null) {
            return service.getThing(thingId);
        } else if (url != null) {
            return service.getThingByUrl(url);
        } else {
            return service.getThingBySubmission(submission);
        }
    }

}
