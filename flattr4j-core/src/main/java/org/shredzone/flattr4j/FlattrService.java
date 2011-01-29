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
import org.shredzone.flattr4j.model.Click;
import org.shredzone.flattr4j.model.ClickCount;
import org.shredzone.flattr4j.model.Language;
import org.shredzone.flattr4j.model.RegisteredThing;
import org.shredzone.flattr4j.model.Thing;
import org.shredzone.flattr4j.model.User;
import org.shredzone.flattr4j.model.UserDetails;
import org.shredzone.flattr4j.oauth.Scope;

/**
 * Service calls to the Flattr REST API that requires authorization.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public interface FlattrService{

    /**
     * Registers a new {@link Thing} at Flattr.
     * 
     * @param thing
     *            {@link Thing} to be registered.
     * @return {@link RegisteredThing} as result from the registration.
     */
    RegisteredThing register(Thing thing) throws FlattrException;

    /**
     * Gets a {@link RegisteredThing} with the given Thing id.
     * 
     * @param thingId
     *            id of the Thing to be fetched
     * @return {@link RegisteredThing}. Never {@code null}. An exception is thrown when no
     *         such Thing was found.
     */
    RegisteredThing getThing(String thingId) throws FlattrException;
    
    /**
     * Gets a {@link RegisteredThing} for the given {@link Click}.
     * 
     * @param click
     *            {@link Click} to get the {@link RegisteredThing} for
     * @return {@link RegisteredThing}. Never {@code null}.
     */
    RegisteredThing getThing(Click click) throws FlattrException;

    /**
     * Clicks on a Thing. This means that the Thing is flattr-ed by the logged in user.
     * 
     * @param thingId
     *            id of the Thing to flattr
     */
    void click(String thingId) throws FlattrException;

    /**
     * Clicks on a Thing. This means that the Thing is flattr-ed by the logged in user.
     * 
     * @param thing
     *            {@link RegisteredThing} to flattr
     */
    void click(RegisteredThing thing) throws FlattrException;
    
    /**
     * Counts the number of clicks that the Thing received.
     * 
     * @param thingId
     *            id of the Thing
     * @return {@link ClickCount} containing the result
     */
    ClickCount countClicks(String thingId) throws FlattrException;

    /**
     * Counts the number of clicks that the Thing received.
     * 
     * @param thing
     *            {@link RegisteredThing}
     * @return {@link ClickCount} containing the result
     */
    ClickCount countClicks(RegisteredThing thing) throws FlattrException;

    /**
     * Browses for Things and returns a list of matching ones.
     * 
     * @param term
     *            {@link BrowseTerm} that contains the browse terms
     * @return List of all matching {@link RegisteredThing}. May be empty but is never
     *         {@code null}.
     */
    List<RegisteredThing> browse(BrowseTerm term) throws FlattrException;

    /**
     * Gets a list of all Flattr {@link Category}. The result is not cached.
     * 
     * @return List of Flattr {@link Category}.
     */
    List<Category> getCategoryList() throws FlattrException;

    /**
     * Gets a list of all Flattr {@link Language}. The result is not cached.
     * 
     * @return List of Flattr {@link Language}.
     */
    List<Language> getLanguageList() throws FlattrException;

    /**
     * Gets the {@link UserDetails} profile of the currently logged in user.
     * 
     * @return {@link UserDetails} profile of oneself. Never {@code null}.
     */
    UserDetails getMyself() throws FlattrException;

    /**
     * Gets the {@link UserDetails} profile of the given user id.
     * 
     * @param userId
     *            User id to get a profile for
     * @return {@link UserDetails} profile of that user. Never {@code null}. An exception
     *         is thrown when there was no such user.
     */
    UserDetails getUser(String userId) throws FlattrException;

    /**
     * Gets the {@link UserDetails} profile of the given {@link User}.
     * 
     * @param user
     *            {@link User} to get a profile for
     * @return {@link UserDetails} profile of that user. Never {@code null}. An exception
     *         is thrown when there was no such user.
     */
    UserDetails getUser(User user) throws FlattrException;

    /**
     * Gets the {@link UserDetails} profile of the given user name.
     * 
     * @param name
     *            User name to get a profile for
     * @return {@link UserDetails} profile of that user. Never {@code null}. An exception
     *         is thrown when there was no such user.
     */
    UserDetails getUserByName(String name) throws FlattrException;
    
    /**
     * Gets a list of {@link Click} made by the currently logged in user, starting at the
     * given period. Requires {@link Scope#EXTENDEDREAD} permission.
     * 
     * @param period
     *            Start of the evaluation period. Only the month and year is used.
     * @return List of {@link Click}.
     */
    List<Click> getClicks(Calendar period) throws FlattrException;

}