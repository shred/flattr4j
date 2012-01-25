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
 * A single Language that is available for Things. Two {@link Language} are considered
 * equal if they contain the same id.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class Language extends Resource implements LanguageId {
    private static final long serialVersionUID = -2166187856968632922L;

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

    public Language(FlattrObject data) {
        super(data);
    }

    /**
     * Language id to be used with Flattr.
     */
    @Override
    public String getLanguageId() {
        return data.get("id");
    }

    /**
     * Language name to be used for humans.
     */
    public String getName() {
        return data.get("text");
    }

    @Override
    public boolean equals(Object obj) {
        String pk = getLanguageId();

        if (pk == null || obj == null || !(obj instanceof Language)) {
            return false;
        }

        return pk.equals(((Language) obj).getLanguageId());
    }

    @Override
    public int hashCode() {
        String pk = getLanguageId();
        return (pk != null ? pk.hashCode() : 0);
    }

}
