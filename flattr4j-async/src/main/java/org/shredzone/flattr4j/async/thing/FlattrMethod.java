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
import org.shredzone.flattr4j.model.AutoSubmission;
import org.shredzone.flattr4j.model.MiniThing;
import org.shredzone.flattr4j.model.ThingId;

/**
 * {@link FlattrCallable} for invoking {@link FlattrService#flattr(ThingId)},
 * {@link FlattrService#flattr(String)} or {@link FlattrService#flattr(AutoSubmission)}
 *
 * @author Iulius Gutberlet
 * @author Richard "Shred" Körber
 */
public class FlattrMethod extends AbstractFlattrCallable<MiniThing> {
    private static final long serialVersionUID = 8054668403264619223L;

    private ThingId thingId;
    private String url;
    private AutoSubmission submission;

    public FlattrMethod() {
        // default constructor
    }

    public FlattrMethod(ThingId thingId) {
        setThingId(thingId);
    }

    public FlattrMethod(String url) {
        setUrl(url);
    }

    public FlattrMethod(AutoSubmission submission) {
        setAutoSubmission(submission);
    }

    public ThingId getThingId()                 { return thingId; }
    public void setThingId(ThingId thingId) {
        this.thingId = thingId;
        this.url = null;
        this.submission = null;
    }

    public String getUrl()                      { return url; }
    public void setUrl(String url) {
        this.url = url;
        this.submission = null;
        this.thingId = null;
    }

    public AutoSubmission getAutoSubmission()   { return submission; }
    public void setAutoSubmission(AutoSubmission submission) {
        this.submission = submission;
        this.thingId = null;
        this.url = null;
    }

    @Override
    public MiniThing call(FlattrService service) throws Exception {
        if (thingId != null) {
            return service.flattr(thingId);
        } else if (url != null) {
            return service.flattr(url);
        } else {
            return service.flattr(submission);
        }
    }

}
