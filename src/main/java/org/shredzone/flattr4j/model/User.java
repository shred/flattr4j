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

import java.io.Serializable;

/**
 * A Flattr {@link User}. Two {@link User} are considered equal if they contain the same
 * id.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class User implements Serializable {
    private static final long serialVersionUID = -4198943870341187700L;
    
    private String id;
    private String username;
    private String firstname;
    private String lastname;
    private String city;
    private String country;
    private String gravatar;
    private String email;
    private String description;
    private int thingcount;

    /**
     * User id.
     */
    public String getId()                   { return id; }
    public void setId(String id)            { this.id = id; }

    /**
     * User name.
     */
    public String getUsername()             { return username; }
    public void setUsername(String username) { this.username = username; }

    /**
     * User's real first name.
     */
    public String getFirstname()            { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    /**
     * User's real last name.
     */
    public String getLastname()             { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    /**
     * City the user lives in.
     */
    public String getCity()                 { return city; }
    public void setCity(String city)        { this.city = city; }

    /**
     * Country the user lives in.
     */
    public String getCountry()              { return country; }
    public void setCountry(String country)  { this.country = country; }

    /**
     * URL of the user's picture at Gravatar.
     */
    public String getGravatar()             { return gravatar; }
    public void setGravatar(String gravatar) { this.gravatar = gravatar; }

    /**
     * User's email address.
     */
    public String getEmail()                { return email; }
    public void setEmail(String email)      { this.email = email; }

    /**
     * User's own description.
     */
    public String getDescription()          { return description; }
    public void setDescription(String description) { this.description = description; }

    /**
     * Number of things the user owns.
     */
    public int getThingcount()              { return thingcount; }
    public void setThingcount(int thingcount) { this.thingcount = thingcount; }

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
