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
 *
 */
package org.shredzone.flattr4j.impl;

import java.io.Reader;

import org.shredzone.flattr4j.UserService;
import org.shredzone.flattr4j.connector.FlattrConnector;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.exception.NotFoundException;
import org.shredzone.flattr4j.impl.xml.UserXmlParser;
import org.shredzone.flattr4j.model.User;

/**
 * Default implementation of {@link UserService}.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class UserServiceImpl implements UserService {

    private final FlattrConnector connector;

    public UserServiceImpl(FlattrConnector connector) {
        this.connector = connector;
    }

    @Override
    public User getMyself() throws FlattrException {
        Reader reader = connector.call("user/me");

        UserXmlParser parser = new UserXmlParser(reader);

        User user = parser.getNext();
        if (user == null) {
            throw new NotFoundException("unexpected empty result");
        }
        return user;
    }

    @Override
    public User get(String userId) throws FlattrException {
        Reader reader = connector.call("user/get/id/" + userId);

        UserXmlParser parser = new UserXmlParser(reader);

        User user = parser.getNext();
        if (user == null) {
            throw new NotFoundException("unexpected empty result");
        }
        return user;
    }

}
