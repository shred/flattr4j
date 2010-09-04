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

import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.User;

/**
 * Service calls to all user related functions.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public interface UserService {

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
    User get(String userId) throws FlattrException;

}
