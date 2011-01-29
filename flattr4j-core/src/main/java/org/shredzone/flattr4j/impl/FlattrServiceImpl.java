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

import static org.shredzone.flattr4j.util.ServiceUtils.urlencode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.connector.Connector;
import org.shredzone.flattr4j.connector.Result;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.exception.NotFoundException;
import org.shredzone.flattr4j.exception.ValidationException;
import org.shredzone.flattr4j.impl.xml.CategoryXmlParser;
import org.shredzone.flattr4j.impl.xml.ClickCountXmlParser;
import org.shredzone.flattr4j.impl.xml.ClickXmlParser;
import org.shredzone.flattr4j.impl.xml.LanguageXmlParser;
import org.shredzone.flattr4j.impl.xml.RegisteredThingXmlParser;
import org.shredzone.flattr4j.impl.xml.SubmissionXmlWriter;
import org.shredzone.flattr4j.impl.xml.UserXmlParser;
import org.shredzone.flattr4j.model.BrowseTerm;
import org.shredzone.flattr4j.model.Category;
import org.shredzone.flattr4j.model.Click;
import org.shredzone.flattr4j.model.ClickCount;
import org.shredzone.flattr4j.model.Language;
import org.shredzone.flattr4j.model.Thing;
import org.shredzone.flattr4j.model.ThingSubmission;
import org.shredzone.flattr4j.model.User;
import org.shredzone.flattr4j.model.UserDetails;

/**
 * Default implementation of {@link FlattrService}.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class FlattrServiceImpl implements FlattrService {
    private final Connector connector;
    private String baseUrl = "http://api.flattr.com/rest/0.5/";

    public FlattrServiceImpl(Connector connector) {
        this.connector = connector;
    }
    
    public String getBaseUrl()              { return baseUrl; }
    public void setBaseUrl(String baseUrl)  { this.baseUrl = baseUrl; }

    @Override
    public Thing submit(ThingSubmission thing) throws FlattrException {
        if (thing == null) throw new ValidationException("thing", "thing is required");
        thing.validate();

        String data = SubmissionXmlWriter.write(thing);
        Result result = connector.post(baseUrl + "thing/register", data).assertStatusOk();
        try {
            RegisteredThingXmlParser parser = new RegisteredThingXmlParser(result.openInputStream());
    
            Thing registered = parser.getNext();
            if (registered == null) {
                throw new NotFoundException("unexpected empty result");
            }
            return registered;
        } finally {
            result.closeInputStream();
        }
    }

    @Override
    public Thing getThing(String thingId) throws FlattrException {
        if (thingId == null || thingId.isEmpty()) throw new ValidationException("thingId", "thingId is required");

        Result result = connector.call(baseUrl + "thing/get/id/" + urlencode(thingId));
        result.assertStatusOk();
        try {
            RegisteredThingXmlParser parser = new RegisteredThingXmlParser(result.openInputStream());

            Thing thing = parser.getNext();
            if (thing == null) {
                throw new NotFoundException("unexpected empty result");
            }
            return thing;
        } finally {
            result.closeInputStream();
        }
    }

    @Override
    public Thing getThing(Click click) throws FlattrException {
        if (click == null) throw new ValidationException("click", "click is required");
        return getThing(click.getThingId());
    }
    
    @Override
    public void click(String thingId) throws FlattrException {
        if (thingId == null || thingId.isEmpty()) throw new ValidationException("thingId", "thingId is required");

        connector.call(baseUrl + "thing/click/id/" + urlencode(thingId)).assertStatusOk();
    }

    @Override
    public void click(Thing thing) throws FlattrException {
        click(thing.getId());
    }
    
    @Override
    public ClickCount countClicks(Thing thing) throws FlattrException {
        if (thing == null) throw new ValidationException("thing", "thing is required");
        return countClicks(thing.getId());
    }
    
    @Override
    public ClickCount countClicks(String thingId) throws FlattrException {
        if (thingId == null || thingId.isEmpty()) throw new ValidationException("thingId", "thingId is required");
        
        Result result = connector.call(baseUrl + "thing/clicks/thing/" + urlencode(thingId)).assertStatusOk();
        
        try {
            ClickCountXmlParser parser = new ClickCountXmlParser(result.openInputStream());

            ClickCount count = parser.getNext();
            if (count == null) {
                throw new NotFoundException("unexpected empty result");
            }
            return count;
        } finally {
            result.closeInputStream();
        }
    }

    @Override
    public List<Thing> browse(BrowseTerm term) throws FlattrException {
        if (term == null || term.isEmpty()) throw new ValidationException("term", "Browse term must not be empty");
        
        Result result = connector.call(baseUrl + "thing/browse/" + term.toString());
        result.assertStatusOk();
        try {
            List<Thing> list = new ArrayList<Thing>();

            RegisteredThingXmlParser parser = new RegisteredThingXmlParser(result.openInputStream());
            Thing thing;
            while ((thing = parser.getNext()) != null) {
                list.add(thing);
            }

            return Collections.unmodifiableList(list);
        } finally {
            result.closeInputStream();
        }
    }
    
    @Override
    public List<Category> getCategoryList() throws FlattrException {
        Result result = connector.call(baseUrl + "feed/categories").assertStatusOk();
        try {
            List<Category> list = new ArrayList<Category>();

            CategoryXmlParser parser = new CategoryXmlParser(result.openInputStream());
            Category category;
            while ((category = parser.getNext()) != null) {
                list.add(category);
            }
        
            return Collections.unmodifiableList(list);
        } finally {
            result.closeInputStream();
        }
    }

    @Override
    public List<Language> getLanguageList() throws FlattrException {
        Result result = connector.call(baseUrl + "feed/languages").assertStatusOk();
        try {
            List<Language> list = new ArrayList<Language>();
    
            LanguageXmlParser parser = new LanguageXmlParser(result.openInputStream());
            Language language;
            while ((language = parser.getNext()) != null) {
                list.add(language);
            }
    
            return Collections.unmodifiableList(list);
        } finally {
            result.closeInputStream();
        }
    }

    @Override
    public UserDetails getMyself() throws FlattrException {
        Result result = connector.call(baseUrl + "user/me").assertStatusOk();
        try {
            UserXmlParser parser = new UserXmlParser(result.openInputStream());

            UserDetails user = parser.getNext();
            if (user == null) {
                throw new NotFoundException("unexpected empty result");
            }
            return user;
        } finally {
            result.closeInputStream();
        }
    }

    @Override
    public UserDetails getUser(User user) throws FlattrException {
        if (user == null) throw new ValidationException("user", "user is required");
        return getUser(user.getId());
    }

    @Override
    public UserDetails getUser(String userId) throws FlattrException {
        if (userId == null || userId.isEmpty()) throw new ValidationException("userId", "userId is required");

        Result result = connector.call(baseUrl + "user/get/id/" + urlencode(userId));
        result.assertStatusOk();
        try {
            UserXmlParser parser = new UserXmlParser(result.openInputStream());
    
            UserDetails user = parser.getNext();
            if (user == null) {
                throw new NotFoundException("unexpected empty result");
            }
            return user;
        } finally {
            result.closeInputStream();
        }
    }
    
    @Override
    public UserDetails getUserByName(String name) throws FlattrException {
        if (name == null || name.isEmpty()) throw new ValidationException("name", "name is required");

        Result result = connector.call(baseUrl + "user/get/name/" + urlencode(name));
        result.assertStatusOk();
        try {
            UserXmlParser parser = new UserXmlParser(result.openInputStream());

            UserDetails user = parser.getNext();
            if (user == null) {
                throw new NotFoundException("unexpected empty result");
            }
            return user;
        } finally {
            result.closeInputStream();
        }
    }

    @Override
    public List<Click> getClicks(Calendar period) throws FlattrException {
        if (period == null || !period.isSet(Calendar.YEAR) || !period.isSet(Calendar.MONTH))
            throw new ValidationException("period month and year is required");
        
        String pstr = String.format(
                        "%04d%02d",
                        period.get(Calendar.YEAR),
                        period.get(Calendar.MONTH) + 1
        );
        
        Result result = connector.call(baseUrl + "user/clicks/period/" + urlencode(pstr));
        result.assertStatusOk();
        try {
            List<Click> list = new ArrayList<Click>();
            
            ClickXmlParser parser = new ClickXmlParser(result.openInputStream());
            Click click;
            while ((click = parser.getNext()) != null) {
                list.add(click);
            }
    
            return Collections.unmodifiableList(list);
        } finally {
            result.closeInputStream();
        }
    }

}
