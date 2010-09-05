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
package org.shredzone.flattr4j.model;

import java.util.Date;

/**
 * A {@link Thing} that has been registered with Flattr and thus contains additional
 * information about the registration. Two {@link RegisteredThing} are considered equal if
 * they contain the same id.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class RegisteredThing extends Thing {
    private static final long serialVersionUID = 610493674068876984L;
    
    private String id;
    private Date created;
    private int clicks;
    private String userId;
    private String userName;
    private String categoryName;
    private ThingStatus status;

    /**
     * Thing's unique id at Flattr.
     */
    public String getId()                   { return id; }
    public void setId(String id)            { this.id = id; }

    /**
     * Creation date of the Thing.
     */
    public Date getCreated()                { return created; }
    public void setCreated(Date created)    { this.created = created; }

    /**
     * How many times this Thing was flattred.
     */
    public int getClicks()                  { return clicks; }
    public void setClicks(int clicks)       { this.clicks = clicks; }

    /**
     * Id of the user who owns this Thing.
     */
    public String getUserId()               { return userId; }
    public void setUserId(String userId)    { this.userId = userId; }

    /**
     * User name of the user who owns this Thing.
     */
    public String getUserName()             { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    /**
     * Name of the Category this thing is added to.
     */
    public String getCategoryName()         { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    /**
     * Status of this Thing.
     */
    public ThingStatus getStatus()          { return status; }
    public void setStatus(ThingStatus status) { this.status = status; }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof RegisteredThing)) {
            return false;
        }
        return id.equals(((RegisteredThing) obj).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}