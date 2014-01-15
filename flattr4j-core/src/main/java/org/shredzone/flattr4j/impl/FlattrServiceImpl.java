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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.connector.Connection;
import org.shredzone.flattr4j.connector.Connector;
import org.shredzone.flattr4j.connector.FlattrObject;
import org.shredzone.flattr4j.connector.RateLimit;
import org.shredzone.flattr4j.connector.RequestType;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Activity;
import org.shredzone.flattr4j.model.AutoSubmission;
import org.shredzone.flattr4j.model.Category;
import org.shredzone.flattr4j.model.Flattr;
import org.shredzone.flattr4j.model.Language;
import org.shredzone.flattr4j.model.MiniThing;
import org.shredzone.flattr4j.model.SearchQuery;
import org.shredzone.flattr4j.model.SearchResult;
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
 */
public class FlattrServiceImpl implements FlattrService {
    private final Connector connector;

    private RateLimit lastRateLimit = new RateLimit();
    private boolean fullMode = false;

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
    public void setFullMode(boolean full) {
        this.fullMode = full;
    }

    @Override
    public boolean isFullMode() {
        return fullMode;
    }

    @Override
    public ThingId create(Submission thing) throws FlattrException {
        if (thing == null)
            throw new IllegalArgumentException("thing is required");

        if (thing instanceof AutoSubmission && ((AutoSubmission) thing).getUser() != null)
            throw new IllegalArgumentException("cannot create a thing on behalf of a user");

        FlattrObject data = getConnector().create(RequestType.POST)
                .call("things")
                .data(thing.toFlattrObject())
                .rateLimit(lastRateLimit)
                .singleResult();

        return Thing.withId(data.get("id"));
    }

    @Override
    public void update(Thing thing) throws FlattrException {
        if (thing == null)
            throw new IllegalArgumentException("thing is required");

        FlattrObject update = thing.toUpdate();

        if (update != null) { // Thing was modified.
            getConnector().create(RequestType.PATCH)
                    .call("things/:id")
                    .parameter("id", thing.getThingId())
                    .rateLimit(lastRateLimit)
                    .data(update)
                    .result();
        }
    }

    @Override
    public void delete(ThingId thingId) throws FlattrException {
        if (thingId == null || thingId.getThingId().length() == 0)
            throw new IllegalArgumentException("thing id is required");

        getConnector().create(RequestType.DELETE)
                .call("things/:id")
                .parameter("id", thingId.getThingId())
                .rateLimit(lastRateLimit)
                .result();
    }

    @Override
    public void click(AutoSubmission submission) throws FlattrException {
        flattr(submission);
    }

    @Override
    public void click(String url) throws FlattrException {
        flattr(url);
    }

    @Override
    public void click(ThingId thingId) throws FlattrException {
        flattr(thingId);
    }

    @Override
    public MiniThing flattr(AutoSubmission submission) throws FlattrException {
        return flattr(submission.toUrl());
    }

    @Override
    public MiniThing flattr(String url) throws FlattrException {
        if (url == null || url.length() == 0)
            throw new IllegalArgumentException("url is required");

        FlattrObject data = new FlattrObject();
        data.put("url", url);

        FlattrObject result = getConnector().create(RequestType.POST)
                .call("flattr")
                .data(data)
                .rateLimit(lastRateLimit)
                .singleResult();

        return new MiniThing(result.getFlattrObject("thing"));
    }

    @Override
    public MiniThing flattr(ThingId thingId) throws FlattrException {
        if (thingId == null || thingId.getThingId().length() == 0)
            throw new IllegalArgumentException("thingId is required");

        FlattrObject result = getConnector().create(RequestType.POST)
                .call("things/:id/flattr")
                .parameter("id", thingId.getThingId())
                .rateLimit(lastRateLimit)
                .singleResult();

        return new MiniThing(result.getFlattrObject("thing"));
    }

    @Override
    public User getMyself() throws FlattrException {
        return new User(getConnector().create()
                .call("user")
                .rateLimit(lastRateLimit)
                .singleResult());
    }

    @Override
    public List<Thing> getMyThings() throws FlattrException {
        return getMyThings(null, null);
    }

    @Override
    public List<Thing> getMyThings(Integer count, Integer page) throws FlattrException {
        Connection conn = getConnector().create()
                .call("user/things")
                .rateLimit(lastRateLimit);

        setupFullMode(conn);

        if (count != null) {
            conn.query("count", count.toString());
        }

        if (page != null) {
            conn.query("page", page.toString());
        }

        List<Thing> list = new ArrayList<Thing>();
        for (FlattrObject data : conn.result()) {
            list.add(new Thing(data));
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public List<Flattr> getMyFlattrs() throws FlattrException {
        return getMyFlattrs(null, null);
    }

    @Override
    public List<Flattr> getMyFlattrs(Integer count, Integer page) throws FlattrException {
        Connection conn = getConnector().create()
                .call("user/flattrs")
                .rateLimit(lastRateLimit);

        setupFullMode(conn);

        if (count != null) {
            conn.query("count", count.toString());
        }

        if (page != null) {
            conn.query("page", page.toString());
        }

        List<Flattr> list = new ArrayList<Flattr>();
        for (FlattrObject data : conn.result()) {
            list.add(new Flattr(data));
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public Thing getThing(ThingId thingId) throws FlattrException {
        if (thingId == null || thingId.getThingId().length() == 0)
            throw new IllegalArgumentException("thingId is required");

        Connection conn = getConnector().create()
                .call("things/:id")
                .parameter("id", thingId.getThingId())
                .rateLimit(lastRateLimit);

        setupFullMode(conn);

        return new Thing(conn.singleResult());
    }

    @Override
    public Thing getThingByUrl(String url) throws FlattrException {
        if (url == null || url.length() == 0)
            throw new IllegalArgumentException("url is required");

        FlattrObject data = getConnector().create()
                .call("things/lookup/")
                .query("url", url)
                .rateLimit(lastRateLimit)
                .singleResult();

        if (data.has("message") && "not_found".equals(data.get("message"))) {
            return null;
        }

        return new Thing(data);
    }

    @Override
    public Thing getThingBySubmission(AutoSubmission submission) throws FlattrException {
        return getThingByUrl(submission.toUrl());
    }

    @Override
    public List<Thing> getThings(UserId user) throws FlattrException {
        return getThings(user, null, null);
    }

    @Override
    public List<Thing> getThings(UserId user, Integer count, Integer page) throws FlattrException {
        if (user == null || user.getUserId().length() == 0)
            throw new IllegalArgumentException("user is required");

        Connection conn = getConnector().create()
                .call("users/:username/things")
                .parameter("username", user.getUserId())
                .rateLimit(lastRateLimit);

        setupFullMode(conn);

        if (count != null) {
            conn.query("count", count.toString());
        }

        if (page != null) {
            conn.query("page", page.toString());
        }

        List<Thing> list = new ArrayList<Thing>();
        for (FlattrObject data : conn.result()) {
            list.add(new Thing(data));
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public List<Thing> getThings(Collection<? extends ThingId> thingIds) throws FlattrException {
        if (thingIds.size() == 0) {
            // No IDs, so the result will be empty anyways
            return Collections.emptyList();
        }

        String[] params = new String[thingIds.size()];
        int ix = 0;
        for (ThingId thingId : thingIds) {
            params[ix++] = thingId.getThingId();
        }

        Connection conn = getConnector().create()
                        .call("things/:ids")
                        .parameterArray("ids", params)
                        .rateLimit(lastRateLimit);

        setupFullMode(conn);

        List<Thing> list = new ArrayList<Thing>();
        for (FlattrObject data : conn.result()) {
            list.add(new Thing(data));
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public SearchResult searchThings(SearchQuery query, Integer count, Integer page) throws FlattrException {
        Connection conn = getConnector().create()
                        .call("things/search")
                        .rateLimit(lastRateLimit);

        if (query != null) {
            query.setupConnection(conn);
        }

        setupFullMode(conn);

        if (count != null) {
            conn.query("count", count.toString());
        }

        if (page != null) {
            conn.query("page", page.toString());
        }

        return new SearchResult(conn.singleResult());
    }

    @Override
    public User getUser(UserId user) throws FlattrException {
        if (user == null || user.getUserId().length() == 0)
            throw new IllegalArgumentException("user is required");

        return new User(getConnector().create()
                .call("users/:username")
                .parameter("username", user.getUserId())
                .rateLimit(lastRateLimit)
                .singleResult());
    }

    @Override
    public List<Flattr> getFlattrs(UserId user) throws FlattrException {
        return getFlattrs(user, null, null);
    }

    @Override
    public List<Flattr> getFlattrs(UserId userId, Integer count, Integer page) throws FlattrException {
        if (userId == null || userId.getUserId().length() == 0)
            throw new IllegalArgumentException("userId is required");

        Connection conn = getConnector().create()
                .call("users/:username/flattrs")
                .parameter("username", userId.getUserId())
                .rateLimit(lastRateLimit);

        setupFullMode(conn);

        if (count != null) {
            conn.query("count", count.toString());
        }

        if (page != null) {
            conn.query("page", page.toString());
        }

        List<Flattr> list = new ArrayList<Flattr>();
        for (FlattrObject data : conn.result()) {
            list.add(new Flattr(data));
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public List<Flattr> getFlattrs(ThingId thingId) throws FlattrException {
        return getFlattrs(thingId, null, null);
    }

    @Override
    public List<Flattr> getFlattrs(ThingId thingId, Integer count, Integer page) throws FlattrException {
        if (thingId == null || thingId.getThingId().length() == 0)
            throw new IllegalArgumentException("thingId is required");

        Connection conn = getConnector().create()
                .call("things/:id/flattrs")
                .parameter("id", thingId.getThingId())
                .rateLimit(lastRateLimit);

        setupFullMode(conn);

        if (count != null) {
            conn.query("count", count.toString());
        }

        if (page != null) {
            conn.query("page", page.toString());
        }

        List<Flattr> list = new ArrayList<Flattr>();
        for (FlattrObject data : conn.result()) {
            list.add(new Flattr(data));
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public List<Activity> getActivities(UserId user, Activity.Type type) throws FlattrException {
        if (user == null || user.getUserId().length() == 0)
            throw new IllegalArgumentException("userId is required");

        Connection conn = getConnector().create()
                        .call("users/:username/activities.as")
                        .parameter("username", user.getUserId())
                        .rateLimit(lastRateLimit);

        if (type != null) {
            conn.query("type", type.name().toLowerCase());
        }

        FlattrObject data = conn.singleResult();
        List<Activity> list = new ArrayList<Activity>();
        for (FlattrObject item : data.getObjects("items")) {
            list.add(new Activity(item));
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public List<Activity> getMyActivities(Activity.Type type) throws FlattrException {
        Connection conn = getConnector().create()
                        .call("user/activities.as")
                        .rateLimit(lastRateLimit);

        if (type != null) {
            conn.query("type", type.name().toLowerCase());
        }

        FlattrObject data = conn.singleResult();
        List<Activity> list = new ArrayList<Activity>();
        for (FlattrObject item : data.getObjects("items")) {
            list.add(new Activity(item));
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public List<Subscription> getMySubscriptions() throws FlattrException {
        Connection conn = getConnector().create()
                        .call("user/subscriptions")
                        .rateLimit(lastRateLimit);

        List<Subscription> list = new ArrayList<Subscription>();
        for (FlattrObject item : conn.result()) {
            list.add(new Subscription(item));
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public Subscription getSubscription(ThingId thingId) throws FlattrException {
        if (thingId == null || thingId.getThingId().length() == 0) {
            throw new IllegalArgumentException("thingId is required");
        }

        Connection conn = getConnector().create()
                        .call("user/subscriptions")
                        .rateLimit(lastRateLimit);

        for (FlattrObject item : conn.result()) {
            if (thingId.getThingId().equals(item.getFlattrObject("thing").get("id"))) {
                return new Subscription(item);
            }
        }

        return null;
    }

    @Override
    public void subscribe(ThingId thingId) throws FlattrException {
        if (thingId == null || thingId.getThingId().length() == 0)
            throw new IllegalArgumentException("thingId is required");

        getConnector().create(RequestType.POST)
                        .call("things/:id/subscriptions")
                        .parameter("id", thingId.getThingId())
                        .rateLimit(lastRateLimit)
                        .result();
    }

    @Override
    public void unsubscribe(ThingId thingId) throws FlattrException {
        if (thingId == null || thingId.getThingId().length() == 0)
            throw new IllegalArgumentException("thingId is required");

        getConnector().create(RequestType.DELETE)
                        .call("things/:id/subscriptions")
                        .parameter("id", thingId.getThingId())
                        .rateLimit(lastRateLimit)
                        .result();
    }

    @Override
    public boolean toggleSubscription(ThingId thingId) throws FlattrException {
        if (thingId == null || thingId.getThingId().length() == 0)
            throw new IllegalArgumentException("thingId is required");

        FlattrObject data = getConnector().create(RequestType.PUT)
                        .call("things/:id/subscriptions")
                        .parameter("id", thingId.getThingId())
                        .rateLimit(lastRateLimit)
                        .singleResult();

        return "paused".equals(data.get("message"));
    }

    @Override
    public void pauseSubscription(ThingId thingId, boolean paused) throws FlattrException {
        boolean current = toggleSubscription(thingId);

        if (current != paused) {
            toggleSubscription(thingId);
        }
    }

    @Override
    public List<Category> getCategories() throws FlattrException {
        Connection conn = getConnector().create()
                .call("categories")
                .rateLimit(lastRateLimit);

        List<Category> list = new ArrayList<Category>();
        for (FlattrObject data : conn.result()) {
            list.add(new Category(data));
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public List<Language> getLanguages() throws FlattrException {
        Connection conn = getConnector().create()
                .call("languages")
                .rateLimit(lastRateLimit);

        List<Language> list = new ArrayList<Language>();
        for (FlattrObject data : conn.result()) {
            list.add(new Language(data));
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public RateLimit getCurrentRateLimit() throws FlattrException {
        Connection conn = getConnector().create()
                        .call("rate_limit");

        return new RateLimit(conn.singleResult());
    }

    @Override
    public RateLimit getLastRateLimit() {
        return lastRateLimit;
    }

    /**
     * Sets the {@link Connection} according to the current full mode.
     *
     * @param conn
     *            {@link Connection} to set
     */
    protected void setupFullMode(Connection conn) {
        if (fullMode) {
            conn.query("full", "1");
        }
    }

}
