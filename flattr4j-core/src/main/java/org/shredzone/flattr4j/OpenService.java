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

import java.util.Collection;
import java.util.List;

import org.shredzone.flattr4j.connector.RateLimit;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Activity;
import org.shredzone.flattr4j.model.AutoSubmission;
import org.shredzone.flattr4j.model.Category;
import org.shredzone.flattr4j.model.Flattr;
import org.shredzone.flattr4j.model.Language;
import org.shredzone.flattr4j.model.SearchQuery;
import org.shredzone.flattr4j.model.SearchResult;
import org.shredzone.flattr4j.model.Thing;
import org.shredzone.flattr4j.model.ThingId;
import org.shredzone.flattr4j.model.User;
import org.shredzone.flattr4j.model.UserId;

/**
 * Service calls to the Flattr REST API which do not require authorization. However, some
 * calls may return more details for authorized users.
 * <p>
 * All calls will decrement the remaining rate by one, unless noted otherwise.
 * <p>
 * <strong>Note:</strong> Distinguishing between {@link FlattrService} and
 * {@link OpenService} has historical reasons. In a future release, all
 * {@link OpenService} methods will move to {@link FlattrService}, and {@link OpenService}
 * will then be removed. To be prepared for this change, use {@link FlattrService} instead
 * of {@link OpenService}, and invoke {@link FlattrFactory#createFlattrService()} for
 * creating a {@link FlattrService} without access token.
 *
 * @author Richard "Shred" Körber
 */
public interface OpenService {

    /**
     * Sets the full mode. Defaults to {@code false}.
     *
     * @param full
     *            {@code true}: use full requests, {@code false}: use standard requests
     * @since 2.2
     */
    void setFullMode(boolean full);

    /**
     * Is the full mode currently enabled?
     *
     * @return {@code true}: full requests, {@code false}: standard requests
     * @since 2.2
     */
    boolean isFullMode();

    /**
     * Gets a {@link Thing} for the given {@link ThingId}.
     *
     * @param thingId
     *            {@link ThingId} of the Thing to be fetched
     * @return {@link Thing}
     */
    Thing getThing(ThingId thingId) throws FlattrException;

    /**
     * Gets a {@link Thing} by its registered URL.
     *
     * @param url
     *            Thing's URL
     * @return {@link Thing} of the URL that was found, {@code null} if nothing was found
     * @since 2.0
     */
    Thing getThingByUrl(String url) throws FlattrException;

    /**
     * Gets a {@link Thing} by its autosubmit URL.
     * <p>
     * Uses two rates!
     *
     * @param submission
     *            {@link AutoSubmission} to check for
     * @return {@link Thing} of the submission if found, {@code null} if nothing was found
     * @since 2.0
     */
    Thing getThingBySubmission(AutoSubmission submission) throws FlattrException;

    /**
     * Gets a list of {@link Thing} most recently submitted by the given user. This list
     * is limited to 30 entries.
     *
     * @param user
     *            {@link UserId} to find the Things of
     * @return List of {@link Thing} submitted by the user
     * @since 2.0
     */
    List<Thing> getThings(UserId user) throws FlattrException;

    /**
     * Gets a list of {@link Thing} most recently submitted by the given user.
     *
     * @param user
     *            {@link UserId} to find the Things of
     * @param count
     *            Number of entries per page, {@code null} defaults to 30 entries
     * @param page
     *            Page number (counted from 1), or {@code null} to turn off paging
     * @return List of {@link Thing} submitted by the user
     * @since 2.0
     */
    List<Thing> getThings(UserId user, Integer count, Integer page) throws FlattrException;

    /**
     * Gets a list of {@link Thing} by a collection of thing IDs.
     *
     * @param thingIds
     *            Collection of {@link ThingId}. The order of {@link Thing} returned may
     *            not match the order of the provided IDs.
     * @return List of {@link Thing} fetched
     * @since 2.0
     */
    List<Thing> getThings(Collection<? extends ThingId> thingIds) throws FlattrException;

    /**
     * Searches for {@link Thing}.
     *
     * @param query
     *            {@link SearchQuery}, or {@code null} to search for everything
     * @param count
     *            Number of entries per page, {@code null} defaults to 30 entries
     * @param page
     *            Page number (counted from 1), or {@code null} to turn off paging
     * @return {@link SearchResult}
     * @since 2.0
     */
    SearchResult searchThings(SearchQuery query, Integer count, Integer page) throws FlattrException;

    /**
     * Gets the {@link User} profile of the given user ID.
     *
     * @param user
     *            {@link UserId} to get a profile for
     * @return {@link User} profile of that user
     */
    User getUser(UserId user) throws FlattrException;

    /**
     * Gets all {@link Flattr} most recently posted by the given user ID. Limited to 30
     * results.
     *
     * @param user
     *            {@link UserId} to get the result for
     * @return List of {@link Flattr} posted by the user
     * @since 2.0
     */
    List<Flattr> getFlattrs(UserId user) throws FlattrException;

    /**
     * Gets all {@link Flattr} most recently posted by the given user ID.
     *
     * @param user
     *            {@link UserId} to get the result for
     * @param count
     *            Number of entries per page, {@code null} defaults to 30 entries
     * @param page
     *            Page number (counted from 1), or {@code null} to turn off paging
     * @return List of {@link Flattr} posted by the user
     * @since 2.0
     */
    List<Flattr> getFlattrs(UserId user, Integer count, Integer page) throws FlattrException;

    /**
     * Gets all {@link Flattr} most recently posted for the given thing ID.
     *
     * @param thing
     *            {@link ThingId} to get the result for
     * @return List of {@link Flattr} posted by the user
     * @since 2.0
     */
    List<Flattr> getFlattrs(ThingId thing) throws FlattrException;

    /**
     * Gets all {@link Flattr} most recently posted for the given thing ID.
     *
     * @param thing
     *            {@link ThingId} to get the result for
     * @param count
     *            Number of entries per page, {@code null} defaults to 30 entries
     * @param page
     *            Page number (counted from 1), or {@code null} to turn off paging
     * @return List of {@link Flattr} posted by the user
     * @since 2.0
     */
    List<Flattr> getFlattrs(ThingId thing, Integer count, Integer page) throws FlattrException;

    /**
     * Returns all {@link Activity} of the given user ID.
     *
     * @param user
     *            {@link UserId} to get the result for
     * @param type
     *            activity type. {@code null} defaults to {@link Activity.Type#OUTGOING}.
     * @return List of {@link Activity}
     * @since 2.0
     */
    List<Activity> getActivities(UserId user, Activity.Type type) throws FlattrException;

    /**
     * Gets a list of all Flattr {@link Category}.
     * <p>
     * <em>Note:</em> The result is not cached.
     *
     * @return List of Flattr {@link Category}.
     */
    List<Category> getCategories() throws FlattrException;

    /**
     * Gets a list of all Flattr {@link Language}.
     * <p>
     * <em>Note:</em> The result is not cached.
     *
     * @return List of Flattr {@link Language}.
     */
    List<Language> getLanguages() throws FlattrException;

    /**
     * Gets the current rate limit from the server.
     * <p>
     * Unlike {@link #getLastRateLimit()}, this call actively queries the current rate
     * limits from the server.
     * <p>
     * {@link #getLastRateLimit()} is untouched by this call.
     *
     * @return Rate limit.
     * @since 2.5
     */
    RateLimit getCurrentRateLimit() throws FlattrException;

    /**
     * Gets a {@link RateLimit} instance that reflects the rate limit and remaining rate
     * returned by the last API call.
     * <p>
     * This method does not block and does not connect to the network.
     *
     * @return Rate limit.
     * @since 2.0
     */
    RateLimit getLastRateLimit();

}
