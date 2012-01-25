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
package org.shredzone.flattr4j.model;

import java.io.Serializable;

import org.shredzone.flattr4j.connector.FlattrObject;

/**
 * A generic Flattr resource.
 *
 * @author Richard "Shred" Körber
 * @since 2.0
 */
public class Resource implements Serializable {
    private static final long serialVersionUID = 2052931614858694519L;

    protected FlattrObject data;

    /**
     * Creates a new resource.
     *
     * @param data
     *            {@link FlattrObject} containing the resource data
     */
    public Resource(FlattrObject data) {
        this.data = data;
    }

    /**
     * Returns the resource contents as JSON string.
     */
    public String toJSON() {
        return data.toString();
    }

    /**
     * Returns the resource as {@link FlattrObject}, for further processing.
     * <p>
     * Do not use the returned {@link FlattrObject} for modifying the resource contents.
     * The behaviour might be unpredictable.
     */
    public FlattrObject toFlattrObject() {
        return data;
    }

}
