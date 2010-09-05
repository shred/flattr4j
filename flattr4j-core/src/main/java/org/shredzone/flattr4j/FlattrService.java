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

import java.util.List;

import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Category;
import org.shredzone.flattr4j.model.Language;
import org.shredzone.flattr4j.model.RegisteredThing;
import org.shredzone.flattr4j.model.Thing;
import org.shredzone.flattr4j.model.User;

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
     * Gets a list of all Things of the given user.
     * 
     * @param user
     *            {@link User} to fetch a list of Things for
     * @return List of all {@link RegisteredThing} of that user
     */
    List<RegisteredThing> getThingList(User user) throws FlattrException;

    /**
     * Gets a list of all Things of the given user.
     * 
     * @param userId
     *            User id to fetch a list of Things for
     * @return List of all {@link RegisteredThing} of that user
     */
    List<RegisteredThing> getThingList(String userId) throws FlattrException;

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
     * Gets the {@link User} profile of the currently logged in user.
     * 
     * @return {@link User} profile of oneself. Never {@code null}.
     */
    User getMyself() throws FlattrException;

    /**
     * Gets the {@link User} profile of the given user id.
     * 
     * @param userId
     *            User id to get a profile for
     * @return {@link User} profile of that user. Never {@code null}. An exception is
     *         thrown when there was no such user.
     */
    User getUser(String userId) throws FlattrException;

}