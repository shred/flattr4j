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

import java.util.List;

/**
 * Contains the number of clicks to a Thing, along with a list of {@link UserReference}
 * who clicked the Thing.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class ClickCount {
    
    private int anonymousCount;
    private int publicCount;
    private List<UserReference> users;

    /**
     * Number of anonymous clicks.
     */
    public int getAnonymousCount()                  { return anonymousCount; }
    public void setAnonymousCount(int anonymousCount) { this.anonymousCount = anonymousCount; }

    /**
     * Number of public clicks.
     */
    public int getPublicCount()                     { return publicCount; }
    public void setPublicCount(int publicCount)     { this.publicCount = publicCount; }

    /**
     * List of {@link UserReference} who clicked.
     */
    public List<UserReference> getUsers()                    { return users; }
    public void setUsers(List<UserReference> users)          { this.users = users; }

}
