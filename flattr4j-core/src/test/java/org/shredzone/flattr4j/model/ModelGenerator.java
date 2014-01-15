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
 */
package org.shredzone.flattr4j.model;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Date;

import org.junit.Assert;
import org.shredzone.flattr4j.connector.FlattrObject;
import org.shredzone.flattr4j.connector.RateLimit;

/**
 * Generates and validates models.
 *
 * @author Richard "Shred" Körber
 */
public final class ModelGenerator {

    private ModelGenerator() {
        // utility class without constructor
    }

    /**
     * Creates a {@link Flattr} instance.
     *
     * @return {@link Flattr} instance
     */
    public static Flattr createFlattr() throws IOException {
        return new Flattr(new FlattrObject(resourceToString("/flattr.json")));
    }

    /**
     * Asserts that the given {@link Flattr} instance is equal to the one created by
     * {@link #createFlattr()}.
     *
     * @param flattr
     *            {@link Flattr} instance
     */
    public static void assertFlattr(Flattr flattr) {
        Assert.assertEquals("thingId", "313733", flattr.getThingId());
        Assert.assertEquals("userId", "qzio", flattr.getUserId());

        Thing thing = flattr.getThing();
        Assert.assertEquals("thing.resource", "https://api.flattr.local/rest/v2/things/313733", thing.getResource());
        Assert.assertEquals("thing.link", "https://flattr.local/things/313733", thing.getLink());
        Assert.assertEquals("thing.id", "313733", thing.getThingId());
        Assert.assertEquals("thing.url", "https://flattr.com/profile/gnuproject", thing.getUrl());
        Assert.assertEquals("thing.title", "GNU's not Unix!", thing.getTitle());
        Assert.assertEquals("thing.owner", "gnuproject", thing.getUserId());

        Date createdTest = new Date(1316697578000L);
        Assert.assertEquals("created", createdTest, flattr.getCreated());
    }

    /**
     * Creates a {@link Thing} instance.
     *
     * @return {@link Thing} instance
     */
    public static Thing createThing() throws IOException {
        return new Thing(new FlattrObject(resourceToString("/thing.json")));
    }

    /**
     * Asserts that the given {@link Thing} instance is equal to the one created by
     * {@link #createThing()}.
     *
     * @param thing
     *            {@link Thing} instance
     */
    public static void assertThing(Thing thing) {
        Assert.assertEquals("resource", "https://api.flattr.local/rest/v2/things/268185", thing.getResource());
        Assert.assertEquals("link", "https://flattr.local/things/268185", thing.getLink());
        Assert.assertEquals("id", "268185", thing.getThingId());
        Assert.assertEquals("url", "http://twitter.com/#!/simongate", thing.getUrl());
        Assert.assertEquals("image", "http://flattr.com/thing/image/4/2/3/4/0/5/medium.png", thing.getImage());
        Assert.assertEquals("language", "en_GB", thing.getLanguageId());
        Assert.assertEquals("category", "people", thing.getCategoryId());
        Assert.assertEquals("owner", "simon_g", thing.getUserId());
        Assert.assertEquals("hidden", false, thing.isHidden());
        Assert.assertEquals("flattred", false, thing.isFlattred());
        Assert.assertEquals("subscribed", true, thing.isSubscribed());
        Assert.assertEquals("created", new Date(1305018975000L), thing.getCreated());
        Assert.assertEquals("flattrs", 1, thing.getClicks());
        Assert.assertEquals("description", "Human", thing.getDescription());
        Assert.assertEquals("title", "simongate on Twitter", thing.getTitle());
        Assert.assertEquals("last_flattr_at", new Date(1305026796000L), thing.getLastFlattr());
        Assert.assertNull("updated_at", thing.getUpdated());
        Assert.assertEquals("qrPdfUrl", "https://flattr.com/thing/qr/268185", thing.getQrPdfUrl());
        Assert.assertEquals("tags.size", 1, thing.getTags().size());
        Assert.assertEquals("tags", "twitter", thing.getTags().get(0));
    }

    /**
     * Creates a {@link MiniThing} instance.
     *
     * @return {@link MiniThing} instance
     */
    public static MiniThing createMiniThing() throws IOException {
        return new MiniThing(new FlattrObject(resourceToString("/minithing.json")));
    }

    /**
     * Asserts that the given {@link MiniThing} instance is equal to the one created by
     * {@link #createMiniThing()}.
     *
     * @param thing
     *            {@link MiniThing} instance
     */
    public static void assertMiniThing(MiniThing thing) {
        Assert.assertEquals("resource", "https://api.flattr.com/rest/v2/things/423405", thing.getResource());
        Assert.assertEquals("link", "https://flattr.com/thing/423405", thing.getLink());
        Assert.assertEquals("id", "423405", thing.getThingId());
        Assert.assertEquals("url", "http://blog.flattr.net/2011/10/api-v2-beta-out-whats-changed/", thing.getUrl());
        Assert.assertEquals("flattrs", 3, thing.getClicks());
        Assert.assertEquals("title", "API v2 beta out - what's changed?", thing.getTitle());
        Assert.assertEquals("image", "https://flattr.com/thing/image/4/2/3/4/0/5/medium.png", thing.getImage());
    }

    /**
     * Creates a {@link User} instance.
     *
     * @return {@link User} instance
     */
    public static User createUser() throws IOException {
        return new User(new FlattrObject(resourceToString("/user.json")));
    }

    /**
     * Asserts that the given {@link User} instance is equal to the one created by
     * {@link #createUser()}.
     *
     * @param user
     *            {@link User} instance
     */
    public static void assertUser(User user) {
        Assert.assertEquals("resource", "https://api.flattr.local/rest/v2/users/simon_g", user.getResource());
        Assert.assertEquals("link", "https://flattr.local/user/simon_g", user.getLink());
        Assert.assertEquals("id", "3bpQBK", user.getIdentifier());
        Assert.assertEquals("username", "simon_g", user.getUserId());
        Assert.assertEquals("username", "simon_g", user.getUsername());
        Assert.assertEquals("firstname", "Simon", user.getFirstname());
        Assert.assertEquals("lastname", "Gate", user.getLastname());
        Assert.assertEquals("city", "Simcity", user.getCity());
        Assert.assertEquals("email", "simon@flattr.com", user.getEmail());
        Assert.assertEquals("about", "yes", user.getDescription());
        Assert.assertEquals("country", "Sweden", user.getCountry());
        Assert.assertTrue("active_supporter", user.isActiveSupporter());
        Assert.assertEquals("avatar", "", user.getGravatar());
        Assert.assertEquals("url", "http://flattr.com", user.getUrl());
        Assert.assertNull("registered_at", user.getRegisteredAt());
    }

    /**
     * Creates an {@link Activity} instance.
     *
     * @return {@link Activity} instance
     */
    public static Activity createActivity() throws IOException {
        return new Activity(new FlattrObject(resourceToString("/activity.json")));
    }

    /**
     * Asserts that the given {@link Activity} instance is equal to the one created by
     * {@link #createActivity()}.
     *
     * @param activity
     *            {@link Activity} instance
     */
    public static void assertActivity(Activity activity) {
        Assert.assertEquals("published", 1325668032000L, activity.getPublished().getTime());
        Assert.assertEquals("title", "pthulin flattred \"Acoustid\"", activity.getTitle());
        Assert.assertEquals("actor.displayName", "pthulin", activity.getActor("displayName"));
        Assert.assertEquals("actor.url", "https://flattr.dev/profile/pthulin", activity.getActor("url"));
        Assert.assertEquals("actor.objectType", "person", activity.getActor("objectType"));
        Assert.assertEquals("verb", "like", activity.getVerb());
        Assert.assertEquals("object.displayName", "Acoustid", activity.getObject("displayName"));
        Assert.assertEquals("object.url", "https://flattr.dev/thing/459394/Acoustid", activity.getObject("url"));
        Assert.assertEquals("object.objectType", "bookmark", activity.getObject("objectType"));
        Assert.assertEquals("activityId", "tag:flattr.com,2012-01-04:pthulin/flattr/459394", activity.getActivityId());
        Assert.assertNotNull("json", activity.toJSON());
        Assert.assertNotNull("jsonobject", activity.toFlattrObject());
    }

    /**
     * Creates a {@link Subscription} instance.
     *
     * @return {@link Subscription} instance
     */
    public static Subscription createSubscription() throws IOException {
        return new Subscription(new FlattrObject(resourceToString("/subscription.json")));
    }

    /**
     * Asserts that the given {@link Subscription} instance is equal to the one created by
     * {@link #createSubscription()}.
     *
     * @param subscription
     *            {@link Subscription} instance
     */
    public static void assertSubscription(Subscription subscription) {
        Assert.assertTrue("active", subscription.isActive());
        Assert.assertEquals("created", 1350654024000L, subscription.getCreated().getTime());
        Assert.assertEquals("started", 1351164964000L, subscription.getStarted().getTime());
        assertThing(subscription.getThing());
        Assert.assertNotNull("json", subscription.toJSON());
        Assert.assertNotNull("jsonobject", subscription.toFlattrObject());
    }

    /**
     * Creates a {@link SearchResult} instance.
     *
     * @return {@link SearchResult} instance
     */
    public static SearchResult createSearchResult() throws IOException {
        return new SearchResult(new FlattrObject(resourceToString("/searchresult.json")));
    }

    /**
     * Creates a {@link RateLimit} instance.
     *
     * @return {@link RateLimit} instance
     */
    public static RateLimit createRateLimit() throws IOException {
        return new RateLimit(new FlattrObject(resourceToString("/ratelimit.json")));
    }

    /**
     * Asserts that the given {@link RateLimit} instance is equal to the one created by
     * {@link #createRateLimit()}.
     *
     * @param ratelimit
     *            {@link RateLimit} instance
     */
    public static void assertRateLimit(RateLimit ratelimit) {
        Assert.assertEquals("hourly_limit", Long.valueOf(1000), ratelimit.getLimit());
        Assert.assertEquals("remaining_hits", Long.valueOf(986), ratelimit.getRemaining());
        Assert.assertEquals("current_hits", Long.valueOf(14), ratelimit.getCurrent());

        Date resetTest = new Date(1342521939000L);
        Assert.assertEquals("reset_time_in_seconds", resetTest, ratelimit.getReset());
    }


    /**
     * Reads a resource into a String.
     *
     * @param resource
     *            Resource to read
     * @return Resource content
     */
    private static String resourceToString(String resource) throws IOException {
        Charset charset = Charset.forName("utf-8");
        StringBuilder sb = new StringBuilder();
        InputStream in = ModelGenerator.class.getResourceAsStream(resource);
        byte[] data = new byte[2048];
        int len;
        while ((len = in.read(data)) >= 0) {
            sb.append(new String(data, 0, len, charset));
        }
        return sb.toString();
    }

}
