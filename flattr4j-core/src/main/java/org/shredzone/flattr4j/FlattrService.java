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
package org.shredzone.flattr4j;

import java.util.List;

import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Activity;
import org.shredzone.flattr4j.model.AutoSubmission;
import org.shredzone.flattr4j.model.Flattr;
import org.shredzone.flattr4j.model.MiniThing;
import org.shredzone.flattr4j.model.Submission;
import org.shredzone.flattr4j.model.Subscription;
import org.shredzone.flattr4j.model.Thing;
import org.shredzone.flattr4j.model.ThingId;
import org.shredzone.flattr4j.model.User;
import org.shredzone.flattr4j.oauth.RequiredScope;
import org.shredzone.flattr4j.oauth.Scope;

/**
 * Service calls to the Flattr REST API which require authorization.
 * <p>
 * All calls will decrement the remaining rate by one, unless noted otherwise.
 *
 * @author Richard "Shred" Körber
 */
public interface FlattrService extends OpenService {

    /**
     * Creates a new Thing. The authenticated user will be the owner of the Thing.
     *
     * @param thing
     *            {@link Submission} to be submitted
     * @return {@link ThingId} of the {@link Thing} that was created
     * @since 2.0
     */
    @RequiredScope(Scope.THING)
    ThingId create(Submission thing) throws FlattrException;

    /**
     * Updates a Thing.
     *
     * @param thing
     *            {@link Thing} to be modified
     * @since 2.0
     */
    @RequiredScope(Scope.THING)
    void update(Thing thing) throws FlattrException;

    /**
     * Deletes a Thing.
     *
     * @param thingId
     *            {@link ThingId} to delete
     * @since 2.0
     */
    @RequiredScope(Scope.THING)
    void delete(ThingId thingId) throws FlattrException;

    /**
     * Flattrs a Thing. This means that the Thing is flattr-ed by the associated user.
     *
     * @param thingId
     *            {@link ThingId} to flattr
     * @deprecated use {@link #flattr(ThingId)} instead
     */
    @Deprecated
    @RequiredScope(Scope.FLATTR)
    void click(ThingId thingId) throws FlattrException;

    /**
     * Flattrs an {@link AutoSubmission}. If the submission has not been submitted to
     * Flattr yet, it will automatically be submitted before.
     *
     * @param submission
     *            {@link AutoSubmission} to flattr
     * @deprecated use {@link #flattr(AutoSubmission)} instead
     */
    @Deprecated
    @RequiredScope(Scope.FLATTR)
    void click(AutoSubmission submission) throws FlattrException;

    /**
     * Flattrs a URL.
     *
     * @param url
     *            URL to flattr
     * @deprecated use {@link #flattr(String)} instead
     */
    @Deprecated
    @RequiredScope(Scope.FLATTR)
    void click(String url) throws FlattrException;

    /**
     * Flattrs a Thing. This means that the Thing is flattr-ed by the associated user.
     *
     * @param thingId
     *            {@link ThingId} to flattr
     * @return {@link MiniThing} of the flattr-ed thing (containing an updated click
     *         count)
     * @since 2.9
     */
    @RequiredScope(Scope.FLATTR)
    MiniThing flattr(ThingId thingId) throws FlattrException;

    /**
     * Flattrs an {@link AutoSubmission}. If the submission has not been submitted to
     * Flattr yet, it will automatically be submitted before.
     *
     * @param submission
     *            {@link AutoSubmission} to flattr
     * @return {@link MiniThing} of the flattr-ed thing (containing an updated click
     *         count)
     * @since 2.9
     */
    @RequiredScope(Scope.FLATTR)
    MiniThing flattr(AutoSubmission submission) throws FlattrException;

    /**
     * Flattrs a URL.
     *
     * @param url
     *            URL to flattr
     * @return {@link MiniThing} of the flattr-ed thing (containing an updated click
     *         count)
     * @since 2.9
     */
    @RequiredScope(Scope.FLATTR)
    MiniThing flattr(String url) throws FlattrException;

    /**
     * Subscribes a {@link Thing}.
     *
     * @param thingId
     *            {@link ThingId} to subscribe
     * @since 2.6
     */
    @RequiredScope(Scope.FLATTR)
    void subscribe(ThingId thingId) throws FlattrException;

    /**
     * Cancels subscription of a {@link Thing}.
     *
     * @param thingId
     *            {@link ThingId} to unsubscribed
     * @since 2.6
     */
    @RequiredScope(Scope.FLATTR)
    void unsubscribe(ThingId thingId) throws FlattrException;

    /**
     * Gets the {@link User} profile of the associated user.
     *
     * @return {@link User} profile of oneself
     */
    @RequiredScope()
    User getMyself() throws FlattrException;

    /**
     * Returns all {@link Thing} submitted by the associated user. Limited to 30 entries.
     *
     * @return List of {@link Thing}
     * @since 2.0
     */
    @RequiredScope()
    List<Thing> getMyThings() throws FlattrException;

    /**
     * Returns all {@link Thing} submitted by the associated user.
     *
     * @param count
     *            Number of entries per page, {@code null} defaults to 30 entries
     * @param page
     *            Page number (counted from 1), or {@code null} to turn off paging
     * @return List of {@link Thing}
     * @since 2.0
     */
    @RequiredScope()
    List<Thing> getMyThings(Integer count, Integer page) throws FlattrException;

    /**
     * Returns all {@link Flattr} submitted by the associated user. Limited to 30 entries.
     *
     * @return List of {@link Flattr}
     * @since 2.0
     */
    @RequiredScope()
    List<Flattr> getMyFlattrs() throws FlattrException;

    /**
     * Returns all {@link Flattr} submitted by the associated user.
     *
     * @param count
     *            Number of entries per page, {@code null} defaults to 30 entries
     * @param page
     *            Page number (counted from 1), or {@code null} to turn off paging
     * @return List of {@link Flattr}
     * @since 2.0
     */
    @RequiredScope()
    List<Flattr> getMyFlattrs(Integer count, Integer page) throws FlattrException;

    /**
     * Returns all {@link Activity} of the associated user.
     *
     * @param type
     *            activity type. {@code null} defaults to {@link Activity.Type#OUTGOING}.
     * @return List of {@link Activity}
     * @since 2.0
     */
    @RequiredScope()
    List<Activity> getMyActivities(Activity.Type type) throws FlattrException;

    /**
     * Returns all {@link Subscription} of the associated user.
     *
     * @return List of {@link Subscription}
     * @since 2.6
     */
    @RequiredScope(Scope.FLATTR)
    List<Subscription> getMySubscriptions() throws FlattrException;

    /**
     * Returns the {@link Subscription} of the given {@link Thing}. Only the subscriptions
     * of the associated user are accessible.
     * <p>
     * Note: This call is emulated by flattr4j. Depending on the number of subscriptions
     * of the associated user, this call may take some time and cause increased network
     * traffic. It may also increment the rate counter by more than 1.
     *
     * @param thingId
     *            {@link ThingId} to get the subscription of
     * @return {@link Subscription} of this thing, or {@code null} if there is no such
     *         subscription
     * @since 2.6
     */
    @RequiredScope(Scope.FLATTR)
    Subscription getSubscription(ThingId thingId) throws FlattrException;

    /**
     * Toggles the pause state of the subscription of the given {@link Thing}.
     *
     * @param thingId
     *            {@link ThingId} of the thing to toggle the pause state of
     * @return {@code true} if the subscription is now paused, {@code false} if the
     *         subscription was resumed.
     * @since 2.6
     */
    @RequiredScope(Scope.FLATTR)
    boolean toggleSubscription(ThingId thingId) throws FlattrException;

    /**
     * Pauses or resumes a {@link Subscription}. If the subscription is already in the
     * desired state, nothing will happen.
     * <p>
     * Note: There is currently no way to explicitely set the pause state of a
     * subscription via Flattr API. flattr4j emulates this call by toggling the pause
     * state to get the current state, and if necessary, toggling it again to set the
     * subscription to the desired state (which means that the rate counter is incremented
     * by 2). This call is not atomic. If the second toggle call should fail, it will
     * leave the subscription in the opposite state.
     *
     * @param thingId
     *            {@link ThingId} of the thing to set the pause state
     * @param paused
     *            {@code true}: pause subscription, {@code false}: resume subscription
     * @since 2.6
     */
    @RequiredScope(Scope.FLATTR)
    void pauseSubscription(ThingId thingId, boolean paused) throws FlattrException;

}
