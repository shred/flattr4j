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
import org.shredzone.flattr4j.model.Flattr;
import org.shredzone.flattr4j.model.Submission;
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
 * @version $Revision$
 */
public interface FlattrService extends OpenService {

    /**
     * Creates a new Thing.
     * 
     * @param thing
     *            {@link Submission} to be submitted
     * @return {@link ThingId} of the {@link Thing} that was created
     * @since 2.0
     */
    @RequiredScope(Scope.THING)
    ThingId create(Submission thing) throws FlattrException;

    /**
     * Submits a new Thing to Flattr.
     * <p>
     * Uses two rates.
     * 
     * @param thing
     *            {@link Submission} to be submitted
     * @return {@link Thing} as result from the submission.
     * @deprecated Use {@link #create(org.shredzone.flattr4j.model.Submission)} instead
     */
    @Deprecated
    @RequiredScope(Scope.THING)
    Thing submit(Submission thing) throws FlattrException;
    
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
     * Clicks on a Thing. This means that the Thing is flattr-ed by the associated user.
     * 
     * @param thingId
     *            {@link ThingId} to flattr
     */
    @RequiredScope(Scope.FLATTR)
    void click(ThingId thingId) throws FlattrException;

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
    
}
