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
 * A single Category that is available for Things. Two {@link Category} are considered
 * equal if they contain the same id.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class Category implements CategoryId, Serializable {
    private static final long serialVersionUID = 7142046970430415794L;
    
    private String id;
    private String name;

    /**
     * Returns a {@link CategoryId} for the given Category id.
     * 
     * @param id
     *            Category id
     * @return A {@link CategoryId} object for this id
     */
    public static CategoryId withId(final String id) {
        return new CategoryId() {
            @Override
            public String getCategoryId() {
                return id;
            }
        };
    }

    /**
     * Category id to be used with Flattr.
     */
    @Override
    public String getCategoryId()       { return id; }
    public void setCategoryId(String id){ this.id = id; }

    /**
     * Category name to be used for humans.
     */
    public String getName()             { return name; }
    public void setName(String name)    { this.name = name; }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Category)) {
            return false;
        }
        return id.equals(((Category) obj).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
