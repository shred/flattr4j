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
 */
package org.shredzone.flattr4j;

import java.util.Calendar;
import java.util.List;

import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.BrowseTerm;
import org.shredzone.flattr4j.model.Category;
import org.shredzone.flattr4j.model.ClickCount;
import org.shredzone.flattr4j.model.ClickedThing;
import org.shredzone.flattr4j.model.Language;
import org.shredzone.flattr4j.model.Submission;
import org.shredzone.flattr4j.model.Thing;
import org.shredzone.flattr4j.model.ThingId;
import org.shredzone.flattr4j.model.User;
import org.shredzone.flattr4j.model.UserDetails;
import org.shredzone.flattr4j.model.UserId;
import org.shredzone.flattr4j.oauth.Scope;

/**
 * Service calls to the Flattr REST API that requires authorization.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public interface FlattrService{

    /**
     * Submits a new Thing to Flattr.
     * 
     * @param thing
     *            {@link Submission} to be submitted.
     * @return {@link Thing} as result from the submission.
     * @throws FlattrException
     *             when the submission failed
     */
    Thing submit(Submission thing) throws FlattrException;

    /**
     * Gets a {@link Thing} for the given {@link ThingId}.
     * 
     * @param thingId
     *            {@link ThingId} of the Thing to be fetched
     * @return {@link Thing}. Never {@code null}.
     * @throws FlattrException
     *             when no such thing was found
     */
    Thing getThing(ThingId thingId) throws FlattrException;

    /**
     * Clicks on a Thing. This means that the Thing is flattr-ed by the logged in user.
     * 
     * @param thing
     *            {@link ThingId} to flattr
     * @throws FlattrException
     *             when no such thing was found
     */
    void click(ThingId thingId) throws FlattrException;
    
    /**
     * Counts the number of clicks that the Thing received.
     * 
     * @param thing
     *            {@link Thing}
     * @return {@link ClickCount} containing the result
     * @throws FlattrException
     *             when no such thing was found
     */
    ClickCount countClicks(ThingId thingId) throws FlattrException;

    /**
     * Browses for Things and returns a list of matching ones.
     * 
     * @param term
     *            {@link BrowseTerm} that contains the browse terms
     * @return List of all matching {@link Thing}. May be empty but is never
     *         {@code null}.
     * @throws FlattrException
     *             when the browse operation failed
     */
    List<Thing> browse(BrowseTerm term) throws FlattrException;

    /**
     * Gets a list of all Flattr {@link Category}. The result is not cached.
     * 
     * @return List of Flattr {@link Category}.
     * @throws FlattrException
     *             when the operation failed
     */
    List<Category> getCategoryList() throws FlattrException;

    /**
     * Gets a list of all Flattr {@link Language}. The result is not cached.
     * 
     * @return List of Flattr {@link Language}.
     * @throws FlattrException
     *             when the operation failed
     */
    List<Language> getLanguageList() throws FlattrException;

    /**
     * Gets the {@link UserDetails} profile of the currently logged in user.
     * 
     * @return {@link UserDetails} profile of oneself. Never {@code null}.
     * @throws FlattrException
     *             when the operation failed
     */
    UserDetails getMyself() throws FlattrException;

    /**
     * Gets the {@link UserDetails} profile of the given {@link User}.
     * 
     * @param user
     *            {@link UserId} to get a profile for
     * @return {@link UserDetails} profile of that user. Never {@code null}.
     * @throws FlattrException
     *             when there is no such user
     */
    UserDetails getUser(UserId user) throws FlattrException;

    /**
     * Gets the {@link UserDetails} profile of the given user name.
     * 
     * @param name
     *            User name to get a profile for
     * @return {@link UserDetails} profile of that user. Never {@code null}.
     * @throws FlattrException
     *             when there is no such user
     */
    UserDetails getUserByName(String name) throws FlattrException;
    
    /**
     * Gets a list of {@link ClickedThing} made by the currently logged in user, starting at the
     * given period. Requires {@link Scope#EXTENDEDREAD} permission.
     * 
     * @param period
     *            Start of the evaluation period. Only the month and year is used.
     * @return List of {@link ClickedThing}.
     * @throws FlattrException
     *             when the list of clicks could not be fetched
     */
    List<ClickedThing> getClicks(Calendar period) throws FlattrException;

}