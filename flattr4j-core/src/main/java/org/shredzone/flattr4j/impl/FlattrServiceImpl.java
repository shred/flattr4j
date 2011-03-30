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
import org.shredzone.flattr4j.impl.xml.ClickedThingXmlParser;
import org.shredzone.flattr4j.impl.xml.LanguageXmlParser;
import org.shredzone.flattr4j.impl.xml.SubmissionXmlWriter;
import org.shredzone.flattr4j.impl.xml.SubscriptionXmlParser;
import org.shredzone.flattr4j.impl.xml.ThingXmlParser;
import org.shredzone.flattr4j.impl.xml.UserXmlParser;
import org.shredzone.flattr4j.model.BrowseTerm;
import org.shredzone.flattr4j.model.Category;
import org.shredzone.flattr4j.model.ClickCount;
import org.shredzone.flattr4j.model.ClickedThing;
import org.shredzone.flattr4j.model.Language;
import org.shredzone.flattr4j.model.Submission;
import org.shredzone.flattr4j.model.Subscription;
import org.shredzone.flattr4j.model.Thing;
import org.shredzone.flattr4j.model.ThingId;
import org.shredzone.flattr4j.model.User;
import org.shredzone.flattr4j.model.UserId;

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
    public Thing submit(Submission thing) throws FlattrException {
        if (thing == null) throw new ValidationException("thing", "thing is required");
        thing.validate();

        String data = SubmissionXmlWriter.write(thing);
        Result result = connector.post(baseUrl + "thing/register", data).assertStatusOk();
        try {
            ThingXmlParser parser = new ThingXmlParser(result.openInputStream());
    
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
    public Thing getThing(ThingId thingId) throws FlattrException {
        if (thingId == null || thingId.getThingId().isEmpty()) throw new ValidationException("thingId", "thing id is required");

        Result result = connector.call(baseUrl + "thing/get/id/" + urlencode(thingId.getThingId()));
        result.assertStatusOk();
        try {
            ThingXmlParser parser = new ThingXmlParser(result.openInputStream());

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
    public Thing getThingByUrl(String url) throws FlattrException {
        if (url == null || url.isEmpty()) throw new ValidationException("url", "url is required");

        Result result = connector.call(baseUrl + "thing/get/url/" + urlencode(url));
        result.assertStatusOk();
        try {
            ThingXmlParser parser = new ThingXmlParser(result.openInputStream());

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
    public void click(ThingId thingId) throws FlattrException {
        if (thingId == null || thingId.getThingId().isEmpty()) throw new ValidationException("thingId", "thingId is required");

        connector.call(baseUrl + "thing/click/id/" + urlencode(thingId.getThingId())).assertStatusOk();
    }

    @Override
    public ClickCount countClicks(ThingId thingId) throws FlattrException {
        if (thingId == null || thingId.getThingId().isEmpty()) throw new ValidationException("thingId", "thingId is required");
        
        Result result = connector.call(baseUrl + "thing/clicks/thing/" + urlencode(thingId.getThingId())).assertStatusOk();
        
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

            ThingXmlParser parser = new ThingXmlParser(result.openInputStream());
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
    public List<Category> getCategories() throws FlattrException {
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
    public List<Language> getLanguages() throws FlattrException {
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
    public User getMyself() throws FlattrException {
        Result result = connector.call(baseUrl + "user/me").assertStatusOk();
        try {
            UserXmlParser parser = new UserXmlParser(result.openInputStream());

            User user = parser.getNext();
            if (user == null) {
                throw new NotFoundException("unexpected empty result");
            }
            return user;
        } finally {
            result.closeInputStream();
        }
    }

    @Override
    public User getUser(UserId userId) throws FlattrException {
        if (userId == null || userId.getUserId().isEmpty()) throw new ValidationException("userId", "userId is required");

        Result result = connector.call(baseUrl + "user/get/id/" + urlencode(userId.getUserId()));
        result.assertStatusOk();
        try {
            UserXmlParser parser = new UserXmlParser(result.openInputStream());
    
            User user = parser.getNext();
            if (user == null) {
                throw new NotFoundException("unexpected empty result");
            }
            return user;
        } finally {
            result.closeInputStream();
        }
    }
    
    @Override
    public User getUserByName(String name) throws FlattrException {
        if (name == null || name.isEmpty()) throw new ValidationException("name", "name is required");

        Result result = connector.call(baseUrl + "user/get/name/" + urlencode(name));
        result.assertStatusOk();
        try {
            UserXmlParser parser = new UserXmlParser(result.openInputStream());

            User user = parser.getNext();
            if (user == null) {
                throw new NotFoundException("unexpected empty result");
            }
            return user;
        } finally {
            result.closeInputStream();
        }
    }

    @Override
    public List<ClickedThing> getClicks(Calendar period) throws FlattrException {
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
            List<ClickedThing> list = new ArrayList<ClickedThing>();
            
            ClickedThingXmlParser parser = new ClickedThingXmlParser(result.openInputStream());
            ClickedThing click;
            while ((click = parser.getNext()) != null) {
                list.add(click);
            }
    
            return Collections.unmodifiableList(list);
        } finally {
            result.closeInputStream();
        }
    }
    
    @Override
    public List<Subscription> getSubscriptions() throws FlattrException {
        Result result = connector.call(baseUrl + "subscription/list");
        result.assertStatusOk();
        try {
            List<Subscription> list = new ArrayList<Subscription>();
            
            SubscriptionXmlParser parser = new SubscriptionXmlParser(result.openInputStream());
            Subscription sub;
            while ((sub = parser.getNext()) != null) {
                list.add(sub);
            }
            
            return Collections.unmodifiableList(list);
        } finally {
            result.closeInputStream();
        }
    }

}
