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

/**
 * An enumeration of stati a thing can have.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 * @deprecated Not supported by API v2.
 */
@Deprecated
public enum ThingStatus {

    /**
     * The authenticated user may flattr the thing.
     */
    OK,

    /**
     * The authenticated user owns the thing and thus cannot flattr it.
     */
    OWNER,

    /**
     * The owner or the authenticated user is inactive and can't give or receive flattrs.
     */
    INACTIVE,

    /**
     * The authenticated user has already clicked.
     */
    CLICKED;

}
