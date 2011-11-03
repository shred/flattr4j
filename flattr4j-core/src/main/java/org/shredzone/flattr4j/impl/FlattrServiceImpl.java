/*
 * flattr4j - A Java library for Flattr
 *
 * Copyright (C) 2011 Richard "Shred" Körber
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.connector.Connection;
import org.shredzone.flattr4j.connector.Connector;
import org.shredzone.flattr4j.connector.FlattrObject;
import org.shredzone.flattr4j.connector.RateLimit;
import org.shredzone.flattr4j.connector.RequestType;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Category;
import org.shredzone.flattr4j.model.Flattr;
import org.shredzone.flattr4j.model.Language;
import org.shredzone.flattr4j.model.Submission;
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
    private static final Pattern ID_PATTERN = Pattern.compile(".*?(\\d+)$");
    
    private final Connector connector;
   
    private RateLimit lastRateLimit = new RateLimit();

    public FlattrServiceImpl(Connector connector) {
        this.connector = connector;
    }
    
    /**
     * Returns the {@link Connector} used for calling the API.
     * 
     * @return {@link Connector}
     */
    protected Connector getConnector() {
        return connector;
    }

    @Override
    @Deprecated
    public Thing submit(Submission thing) throws FlattrException {
        return getThing(create(thing));
    }

    @Override
    public ThingId create(Submission thing) throws FlattrException {
        if (thing == null)
            throw new IllegalArgumentException("thing is required");
        
        Connection conn = getConnector().create(RequestType.POST)
                .call("things")
                .data(thing.toFlattrObject())
                .rateLimit(lastRateLimit);

        try {
            FlattrObject data = conn.singleResult();
            return Thing.withId(data.get("id"));
        } finally {
            conn.close();
        }
    }

    @Override
    public void update(Thing thing) throws FlattrException {
        if (thing == null)
            throw new IllegalArgumentException("thing is required");

        FlattrObject update = thing.toUpdate();
        
        if (update != null) { // Thing was modified.
            Connection conn = getConnector().create(RequestType.PATCH)
                    .call("things/:id")
                    .parameter("id", thing.getThingId())
                    .rateLimit(lastRateLimit);
    
            try {
                conn.data(update).result();
            } finally {
                conn.close();
            }
        }
    }
    
    @Override
    public void delete(ThingId thingId) throws FlattrException {
        if (thingId == null || thingId.getThingId().length() == 0)
            throw new IllegalArgumentException("thing id is required");
        
        Connection conn = getConnector().create(RequestType.DELETE)
                .call("things/:id")
                .parameter("id", thingId.getThingId())
                .rateLimit(lastRateLimit);
        
        try {
            conn.result();
        } finally {
            conn.close();
        }
    }

    @Override
    public void click(ThingId thingId) throws FlattrException {
        if (thingId == null || thingId.getThingId().length() == 0)
            throw new IllegalArgumentException("thingId is required");

        Connection conn = getConnector().create(RequestType.POST)
                .call("things/:id/flattr")
                .parameter("id", thingId.getThingId())
                .rateLimit(lastRateLimit);
        
        try {
             conn.result();
        } finally {
            conn.close();
        }
    }
    
    @Override
    public User getMyself() throws FlattrException {
        Connection conn = getConnector().create()
                .call("user")
                .rateLimit(lastRateLimit);
        
        try {
            return new User(conn.singleResult());
        } finally {
            conn.close();
        }
    }

    @Override
    public List<Thing> browseByMyself() throws FlattrException {
        return browseByMyself(null, null);
    }

    @Override
    public List<Thing> browseByMyself(Long count, Long page) throws FlattrException {
        Connection conn = getConnector().create()
                .call("user/things")
                .rateLimit(lastRateLimit);
        
        if (count != null) {
            conn.query("count", count.toString());
        }
        
        if (page != null) {
            conn.query("page", page.toString());
        }
            
        try {
            List<Thing> list = new ArrayList<Thing>();
            for (FlattrObject data : conn.result()) {
                list.add(new Thing(data));
            }
            return Collections.unmodifiableList(list);
        } finally {
            conn.close();
        }
    }

    @Override
    public List<Flattr> getMyFlattrs() throws FlattrException {
        return getMyFlattrs(null, null);
    }

    @Override
    public List<Flattr> getMyFlattrs(Long count, Long page) throws FlattrException {
        Connection conn = getConnector().create()
                .call("user/flattrs")
                .rateLimit(lastRateLimit);
        
        if (count != null) {
            conn.query("count", count.toString());
        }
        
        if (page != null) {
            conn.query("page", page.toString());
        }
        
        try {
            List<Flattr> list = new ArrayList<Flattr>();
            for (FlattrObject data : conn.result()) {
                list.add(new Flattr(data));
            }
            return Collections.unmodifiableList(list);
        } finally {
            conn.close();
        }
    }

    @Override
    public Thing getThing(ThingId thingId) throws FlattrException {
        if (thingId == null || thingId.getThingId().length() == 0)
            throw new IllegalArgumentException("thingId is required");

        Connection conn = getConnector().create()
                .call("things/:id")
                .parameter("id", thingId.getThingId())
                .rateLimit(lastRateLimit);
        
        try {
            return new Thing(conn.singleResult());
        } finally {
            conn.close();
        }
    }

    @Override
    public Thing getThingByUrl(String url) throws FlattrException {
        if (url == null || url.length() == 0)
            throw new IllegalArgumentException("url is required");
        
        Connection conn = getConnector().create()
                .call("things/lookup/?q=:url")
                .parameter("url", url)
                .rateLimit(lastRateLimit);
        
        try {
            FlattrObject data = conn.singleResult();
            if (data.get("message").equals("found")) {
                String loc = data.get("location");
                Matcher m = ID_PATTERN.matcher(loc);
                if (m.matches()) {
                    return getThing(Thing.withId(m.group(1)));
                } else {
                    throw new FlattrException("unexpected location " + loc);
                }
            } else {
                return null;
            }
        } finally {
            conn.close();
        }
    }

    @Override
    public List<Thing> browseByUser(UserId user) throws FlattrException {
        return browseByUser(user, null, null);
    }

    @Override
    public List<Thing> browseByUser(UserId user, Long count, Long page) throws FlattrException {
        if (user == null || user.getUserId().length() == 0)
            throw new IllegalArgumentException("user is required");

        Connection conn = getConnector().create()
                .call("users/:username/things")
                .parameter("username", user.getUserId())
                .rateLimit(lastRateLimit);
        
        if (count != null) {
            conn.query("count", count.toString());
        }
        
        if (page != null) {
            conn.query("page", page.toString());
        }
        
        try {
            List<Thing> list = new ArrayList<Thing>();
            for (FlattrObject data : conn.result()) {
                list.add(new Thing(data));
            }
            return Collections.unmodifiableList(list);
        } finally {
            conn.close();
        }
    }

    @Override
    public User getUser(UserId user) throws FlattrException {
        if (user == null || user.getUserId().length() == 0)
            throw new IllegalArgumentException("user is required");
        
        Connection conn = getConnector().create()
                .call("users/:username")
                .parameter("username", user.getUserId())
                .rateLimit(lastRateLimit);
        
        try {
            return new User(conn.singleResult());
        } finally {
            conn.close();
        }
    }

    @Override
    public List<Flattr> getFlattrs(UserId user) throws FlattrException {
        return getFlattrs(user, null, null);
    }

    @Override
    public List<Flattr> getFlattrs(UserId userId, Long count, Long page) throws FlattrException {
        if (userId == null || userId.getUserId().length() == 0)
            throw new IllegalArgumentException("userId is required");

        Connection conn = getConnector().create()
                .call("users/:username/flattrs")
                .parameter("username", userId.getUserId())
                .rateLimit(lastRateLimit);
        
        if (count != null) {
            conn.query("count", count.toString());
        }
        
        if (page != null) {
            conn.query("page", page.toString());
        }
        
        try {
            List<Flattr> list = new ArrayList<Flattr>();
            for (FlattrObject data : conn.result()) {
                list.add(new Flattr(data));
            }
            return Collections.unmodifiableList(list);
        } finally {
            conn.close();
        }
    }
    
    @Override
    public List<Category> getCategories() throws FlattrException {
        Connection conn = getConnector().create()
                .call("categories")
                .rateLimit(lastRateLimit);
        
        try {
            List<Category> list = new ArrayList<Category>();
            for (FlattrObject data : conn.result()) {
                list.add(new Category(data));
            }
            return Collections.unmodifiableList(list);
        } finally {
            conn.close();
        }
    }

    @Override
    public List<Language> getLanguages() throws FlattrException {
        Connection conn = getConnector().create()
                .call("languages")
                .rateLimit(lastRateLimit);

        try {
            List<Language> list = new ArrayList<Language>();
            for (FlattrObject data : conn.result()) {
                list.add(new Language(data));
            }
            return Collections.unmodifiableList(list);
        } finally {
            conn.close();
        }
    }

    @Override
    public RateLimit getLastRateLimit() {
        return lastRateLimit;
    }

}
