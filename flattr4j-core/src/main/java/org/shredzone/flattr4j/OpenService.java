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

import org.shredzone.flattr4j.connector.RateLimit;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Category;
import org.shredzone.flattr4j.model.Flattr;
import org.shredzone.flattr4j.model.Language;
import org.shredzone.flattr4j.model.Thing;
import org.shredzone.flattr4j.model.ThingId;
import org.shredzone.flattr4j.model.User;
import org.shredzone.flattr4j.model.UserId;

/**
 * Service calls to the Flattr REST API which do not require authorization. However, some
 * calls may return more details for authorized users.
 * <p>
 * All calls will decrement the remaining rate by one, unless noted otherwise.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public interface OpenService {
    
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
     * <p>
     * Uses two rates!
     * 
     * @param url
     *            Thing's URL
     * @return {@link Thing} of the URL that was found, {@code null} if nothing was found
     * @since 2.0
     */
    Thing getThingByUrl(String url) throws FlattrException;

    /**
     * Gets all {@link Thing} submitted by the given user.
     * 
     * @param user
     *            {@link UserId} to find the Things of
     * @return List of {@link Thing} submitted by the user
     * @since 2.0
     */
    List<Thing> getThings(UserId user) throws FlattrException;

    /**
     * Gets all {@link Thing} submitted by the given user.
     * 
     * @param user
     *            {@link UserId} to find the Things of
     * @param count
     *            Number of entries per page, or {@code null} to turn off paging
     * @param page
     *            Page number, or {@code null} to turn off paging
     * @return List of {@link Thing} submitted by the user
     * @since 2.0
     */
    List<Thing> getThings(UserId user, Long count, Long page) throws FlattrException;

    /**
     * Gets the {@link User} profile of the given user ID.
     * 
     * @param user
     *            {@link UserId} to get a profile for
     * @return {@link User} profile of that user
     */
    User getUser(UserId user) throws FlattrException;

    /**
     * Gets all {@link Flattr} posted by the given user ID.
     * 
     * @param user
     *            {@link UserId} to get the result for
     * @return List of {@link Flattr} posted by the user
     * @since 2.0
     */
    List<Flattr> getFlattrs(UserId user) throws FlattrException;

    /**
     * Gets all {@link Flattr} posted by the given user ID.
     * 
     * @param user
     *            {@link UserId} to get the result for
     * @param count
     *            Number of entries per page, or {@code null} to turn off paging
     * @param page
     *            Page number, or {@code null} to turn off paging
     * @return List of {@link Flattr} posted by the user
     * @since 2.0
     */
    List<Flattr> getFlattrs(UserId user, Long count, Long page) throws FlattrException;

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
     * Gets the rate limit and remaining rate returned by the last API call.
     * 
     * @return Rate limit.
     * @since 2.0
     */
    RateLimit getLastRateLimit();

}
