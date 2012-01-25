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

import java.util.ArrayList;
import java.util.List;

import org.shredzone.flattr4j.connector.FlattrObject;

/**
 * Contains the search result.
 * <p>
 * This class is not threadsafe.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 * @since 2.0
 */
public class SearchResult extends Resource {
    private static final long serialVersionUID = -3762044230769599498L;

    private transient ArrayList<Thing> result = null;

    /**
     * Creates a new {@link SearchResult}.
     */
    public SearchResult(FlattrObject data) {
        super(data);
    }

    /**
     * Returns the total number of results.
     */
    public int getTotalCount() {
        return data.getInt("total_items");
    }

    /**
     * Returns the number of items.
     */
    public int getItemCount() {
        return data.getInt("items");
    }

    /**
     * Returns the current page.
     */
    public int getPage() {
        return data.getInt("page");
    }

    /**
     * Returns the result list of {@link Thing}.
     */
    public List<Thing> getThings() {
        if (result == null) {
            List<FlattrObject> objects = data.getObjects("things");
            result = new ArrayList<Thing>(objects.size());
            for (FlattrObject obj : objects) {
                result.add(new Thing(obj));
            }
        }
        return result;
    }

}
