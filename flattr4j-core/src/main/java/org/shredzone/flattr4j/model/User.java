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
package org.shredzone.flattr4j.model;

import java.util.Date;

import org.shredzone.flattr4j.connector.FlattrObject;
import org.shredzone.flattr4j.oauth.RequiredScope;
import org.shredzone.flattr4j.oauth.Scope;

/**
 * A {@link User}. Two {@link User} are considered equal if they contain the same id.
 *
 * @author Richard "Shred" Körber
 */
public class User extends Resource implements UserId {
    private static final long serialVersionUID = 594781523400164895L;

    /**
     * Returns a {@link UserId} for the given User id.
     *
     * @param id
     *            User id
     * @return A {@link UserId} object for this id
     */
    public static UserId withId(final String id) {
        return new UserId() {
            @Override
            public String getUserId() {
                return id;
            }
        };
    }

    public User(FlattrObject data) {
        super(data);
    }

    /**
     * User id.
     */
    @Override
    public String getUserId() {
        return data.get("username");
    }

    /**
     * URL that returns details of this resource as JSON.
     */
    public String getResource() {
        return data.get("resource");
    }

    /**
     * Human readable link to this resource at Flattr.
     */
    public String getLink() {
        return data.get("link");
    }

    /**
     * User's real first name.
     */
    public String getFirstname() {
        return data.get("firstname");
    }

    /**
     * User's real last name.
     */
    public String getLastname() {
        return data.get("lastname");
    }

    /**
     * City the user lives in.
     */
    public String getCity() {
        return data.get("city");
    }

    /**
     * Country the user lives in.
     */
    public String getCountry() {
        return data.get("country");
    }

    /**
     * URL of the user's home page.
     *
     * @since 2.5
     */
    public String getUrl() {
        return data.get("url");
    }

    /**
     * User's email address.
     */
    @RequiredScope(Scope.EXTENDEDREAD)
    public String getEmail() {
        return data.get("email");
    }

    /**
     * User's own description.
     */
    public String getDescription() {
        return data.get("about");
    }

    /**
     * URL of the user's picture at Gravatar.
     */
    public String getGravatar() {
        return data.get("avatar");
    }

    /**
     * Registration date.
     *
     * @since 2.0
     */
    @RequiredScope(Scope.EXTENDEDREAD)
    public Date getRegisteredAt() {
        return data.getDate("registered_at");
    }

    @Override
    public boolean equals(Object obj) {
        String pk = getUserId();
        if (pk == null || obj == null || !(obj instanceof User)) {
            return false;
        }
        return pk.equals(((User) obj).getUserId());
    }

    @Override
    public int hashCode() {
        String pk = getUserId();
        return (pk != null ? pk.hashCode() : 0);
    }

}
