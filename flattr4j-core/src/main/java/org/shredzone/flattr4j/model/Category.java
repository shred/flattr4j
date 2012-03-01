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

import org.shredzone.flattr4j.connector.FlattrObject;

/**
 * A single Category that is available for Things. Two {@link Category} are considered
 * equal if they contain the same id.
 *
 * @author Richard "Shred" Körber
 */
public class Category extends Resource implements CategoryId {
    private static final long serialVersionUID = 6749493295567461888L;

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

    public Category(FlattrObject data) {
        super(data);
    }

    /**
     * Category id to be used with Flattr.
     */
    @Override
    public String getCategoryId() {
        return data.get("id");
    }

    /**
     * Category name to be used for humans.
     */
    public String getName() {
        return data.get("text");
    }

    @Override
    public boolean equals(Object obj) {
        String pk = getCategoryId();

        if (pk == null || obj == null || !(obj instanceof Category)) {
            return false;
        }

        return pk.equals(((Category) obj).getCategoryId());
    }

    @Override
    public int hashCode() {
        String pk = getCategoryId();
        return (pk != null ? pk.hashCode() : 0);
    }

}
