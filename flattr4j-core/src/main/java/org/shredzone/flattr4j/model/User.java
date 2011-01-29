/**
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

import java.io.Serializable;

/**
 * A Flattr {@link User}. Two {@link User} are considered equal if they contain the same
 * id.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class User implements UserId, Serializable {

    private static final long serialVersionUID = 2406024500321708867L;
    
    private String id;
    private String username;

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

    /**
     * User id.
     */
    @Override
    public String getUserId()               { return id; }
    public void setUserId(String id)        { this.id = id; }

    /**
     * User name.
     */
    public String getUsername()             { return username; }
    public void setUsername(String username) { this.username = username; }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof User)) {
            return false;
        }
        return id.equals(((User) obj).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
