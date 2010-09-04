/**
 * flattr4j - A Java library for Flattr
 *
 * Copyright (C) 2010 Richard "Shred" Körber
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
 *
 */
package org.shredzone.flattr4j.impl;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.shredzone.flattr4j.ThingService;
import org.shredzone.flattr4j.connector.FlattrConnector;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.exception.NotFoundException;
import org.shredzone.flattr4j.impl.xml.RegisteredThingXmlParser;
import org.shredzone.flattr4j.impl.xml.ThingXmlWriter;
import org.shredzone.flattr4j.model.RegisteredThing;
import org.shredzone.flattr4j.model.Thing;
import org.shredzone.flattr4j.model.User;

/**
 * Default implementation of {@link ThingService}.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class ThingServiceImpl implements ThingService {

    private final FlattrConnector connector;

    public ThingServiceImpl(FlattrConnector connector) {
        this.connector = connector;
    }

    @Override
    public RegisteredThing register(Thing thing) throws FlattrException {
        String data = ThingXmlWriter.write(thing);

        Reader reader = connector.post("thing/register", data);

        RegisteredThingXmlParser parser = new RegisteredThingXmlParser(reader);

        RegisteredThing result = parser.getNext();
        if (result == null) {
            throw new NotFoundException("unexpected empty result");
        }
        return result;
    }

    @Override
    public RegisteredThing get(String thingId) throws FlattrException {
        Reader reader = connector.call("thing/get/id/" + thingId);

        RegisteredThingXmlParser parser = new RegisteredThingXmlParser(reader);

        RegisteredThing thing = parser.getNext();
        if (thing == null) {
            throw new NotFoundException("unexpected empty result");
        }
        return thing;
    }

    @Override
    public void click(String thingId) throws FlattrException {
        connector.call("thing/click/id/" + thingId);
    }

    @Override
    public void click(RegisteredThing thing) throws FlattrException {
        click(thing.getId());
    }

    @Override
    public List<RegisteredThing> getThingList(User user) throws FlattrException {
        return getThingList(user.getId());
    }

    @Override
    public List<RegisteredThing> getThingList(String userId) throws FlattrException {
        Reader reader = connector.call("thing/listbyuser/id/" + userId);

        List<RegisteredThing> result = new ArrayList<RegisteredThing>();

        RegisteredThingXmlParser parser = new RegisteredThingXmlParser(reader);
        RegisteredThing thing;
        while ((thing = parser.getNext()) != null) {
            result.add(thing);
        }

        return Collections.unmodifiableList(result);
    }

}
