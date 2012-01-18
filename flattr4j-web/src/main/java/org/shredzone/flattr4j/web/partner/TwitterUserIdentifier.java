/*
 * flattr4j - A Java library for Flattr
 *
 * Copyright (C) 2012 Richard "Shred" Körber
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
package org.shredzone.flattr4j.web.partner;

import java.io.Serializable;

import org.shredzone.flattr4j.model.UserIdentifier;

/**
 * A {@link UserIdentifier} for Twitter accounts.
 *
 * @author Richard "Shred" Körber
 * @version $Revision:$
 * @since 2.0
 */
public class TwitterUserIdentifier implements UserIdentifier, Serializable {
    private static final long serialVersionUID = -1017781064729230054L;

    private final String identifier;

    /**
     * Creates a new {@link UserIdentifier} for the given twitter account name.
     *
     * @param twitterId
     *            Twitter account name
     */
    public TwitterUserIdentifier(String twitterId) {
        identifier = "twitter:user:id:" + twitterId;
    }

    @Override
    public String getUserIdentifier() {
        return identifier;
    }

}
