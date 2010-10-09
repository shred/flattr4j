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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.connector.Connector;
import org.shredzone.flattr4j.connector.Result;
import org.shredzone.flattr4j.exception.ValidationException;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.exception.NotFoundException;
import org.shredzone.flattr4j.impl.xml.CategoryXmlParser;
import org.shredzone.flattr4j.impl.xml.LanguageXmlParser;
import org.shredzone.flattr4j.impl.xml.RegisteredThingXmlParser;
import org.shredzone.flattr4j.impl.xml.ThingXmlWriter;
import org.shredzone.flattr4j.impl.xml.UserXmlParser;
import org.shredzone.flattr4j.model.Category;
import org.shredzone.flattr4j.model.Language;
import org.shredzone.flattr4j.model.RegisteredThing;
import org.shredzone.flattr4j.model.Thing;
import org.shredzone.flattr4j.model.User;

/**
 * Default implementation of {@link FlattrService}.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class FlattrServiceImpl implements FlattrService {
    private final Connector connector;
    private String baseUrl = "http://api.flattr.com/rest/0.0.1/";

    public FlattrServiceImpl(Connector connector) {
        this.connector = connector;
    }
    
    public String getBaseUrl()              { return baseUrl; }
    public void setBaseUrl(String baseUrl)  { this.baseUrl = baseUrl; }

    @Override
    public RegisteredThing register(Thing thing) throws FlattrException {
        if (thing == null) throw new ValidationException("thing", "thing is required");
        thing.validate();

        String data = ThingXmlWriter.write(thing);
        Result result = connector.post(baseUrl + "thing/register", data).assertStatusOk();
        try {
            RegisteredThingXmlParser parser = new RegisteredThingXmlParser(result.openReader());
    
            RegisteredThing registered = parser.getNext();
            if (registered == null) {
                throw new NotFoundException("unexpected empty result");
            }
            return registered;
        } finally {
            result.closeReader();
        }
    }

    @Override
    public RegisteredThing getThing(String thingId) throws FlattrException {
        if (thingId == null || thingId.isEmpty()) throw new ValidationException("thingId", "thingId is required");

        Result result = connector.call(baseUrl + "thing/get/id/" + urlencode(thingId));
        result.assertStatusOk();
        try {
            RegisteredThingXmlParser parser = new RegisteredThingXmlParser(result.openReader());

            RegisteredThing thing = parser.getNext();
            if (thing == null) {
                throw new NotFoundException("unexpected empty result");
            }
            return thing;
        } finally {
            result.closeReader();
        }
    }

    @Override
    public void click(String thingId) throws FlattrException {
        if (thingId == null || thingId.isEmpty()) throw new ValidationException("thingId", "thingId is required");

        connector.call(baseUrl + "thing/click/id/" + urlencode(thingId)).assertStatusOk();
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
        if (userId == null || userId.isEmpty()) throw new ValidationException("userId", "userId is required");

        Result result = connector.call(baseUrl + "thing/listbyuser/id/" + urlencode(userId));
        result.assertStatusOk();
        try {
            List<RegisteredThing> list = new ArrayList<RegisteredThing>();

            RegisteredThingXmlParser parser = new RegisteredThingXmlParser(result.openReader());
            RegisteredThing thing;
            while ((thing = parser.getNext()) != null) {
                list.add(thing);
            }

            return Collections.unmodifiableList(list);
        } finally {
            result.closeReader();
        }
    }
    
    @Override
    public List<Category> getCategoryList() throws FlattrException {
        Result result = connector.call(baseUrl + "feed/categories").assertStatusOk();
        try {
            List<Category> list = new ArrayList<Category>();

            CategoryXmlParser parser = new CategoryXmlParser(result.openReader());
            Category category;
            while ((category = parser.getNext()) != null) {
                list.add(category);
            }
        
            return Collections.unmodifiableList(list);
        } finally {
            result.closeReader();
        }
    }

    @Override
    public List<Language> getLanguageList() throws FlattrException {
        Result result = connector.call(baseUrl + "feed/languages").assertStatusOk();
        try {
            List<Language> list = new ArrayList<Language>();
    
            LanguageXmlParser parser = new LanguageXmlParser(result.openReader());
            Language language;
            while ((language = parser.getNext()) != null) {
                list.add(language);
            }
    
            return Collections.unmodifiableList(list);
        } finally {
            result.closeReader();
        }
    }

    @Override
    public User getMyself() throws FlattrException {
        Result result = connector.call(baseUrl + "user/me").assertStatusOk();
        try {
            UserXmlParser parser = new UserXmlParser(result.openReader());

            User user = parser.getNext();
            if (user == null) {
                throw new NotFoundException("unexpected empty result");
            }
            return user;
        } finally {
            result.closeReader();
        }
    }

    @Override
    public User getUser(String userId) throws FlattrException {
        if (userId == null || userId.isEmpty()) throw new ValidationException("userId", "userId is required");

        Result result = connector.call(baseUrl + "user/get/id/" + urlencode(userId));
        result.assertStatusOk();
        try {
            UserXmlParser parser = new UserXmlParser(result.openReader());
    
            User user = parser.getNext();
            if (user == null) {
                throw new NotFoundException("unexpected empty result");
            }
            return user;
        } finally {
            result.closeReader();
        }
    }

    @Override
    public User getUserByName(String name) throws FlattrException {
        if (name == null || name.isEmpty()) throw new ValidationException("name", "name is required");

        Result result = connector.call(baseUrl + "user/get/name/" + urlencode(name));
        result.assertStatusOk();
        try {
            UserXmlParser parser = new UserXmlParser(result.openReader());

            User user = parser.getNext();
            if (user == null) {
                throw new NotFoundException("unexpected empty result");
            }
            return user;
        } finally {
            result.closeReader();
        }
    }

    /**
     * URL encodes the String using UTF-8. Convenience method for a blunder in the
     * original API.
     *
     * @param str String to encode
     * @return  Encoded string
     */
    private String urlencode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException("UTF-8 missing");
        }
    }

}
