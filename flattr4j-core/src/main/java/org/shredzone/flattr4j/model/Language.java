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
 * A single Language that is available for Things. Two {@link Language} are considered
 * equal if they contain the same id.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class Language implements LanguageId, Serializable {
    private static final long serialVersionUID = 3004338782136430145L;
    
    private String id;
    private String name;

    /**
     * Returns a {@link LanguageId} for the given Language id.
     * 
     * @param id
     *            Language id
     * @return A {@link LanguageId} object for this id
     */
    public static LanguageId withId(final String id) {
        return new LanguageId() {
            @Override
            public String getLanguageId() {
                return id;
            }
        };
    }

    /**
     * Language id to be used with Flattr.
     */
    @Override
    public String getLanguageId()       { return id; }
    public void setLanguageId(String id)        { this.id = id; }

    /**
     * Language name to be used for humans.
     */
    public String getName()             { return name; }
    public void setName(String name)    { this.name = name; }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Language)) {
            return false;
        }
        return id.equals(((Language) obj).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
