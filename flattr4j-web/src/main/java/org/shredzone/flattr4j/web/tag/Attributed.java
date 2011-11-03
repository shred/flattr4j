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
package org.shredzone.flattr4j.web.tag;

/**
 * Interface for tags that accept nested {@link AttributeTag} for HTML attributes.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public interface Attributed {

    /**
     * Sets the HTML attribute from a sub {@link AttributeTag}.
     * 
     * @param name
     *            Attribute name
     * @param value
     *            Attribute value
     */
    void setAttribute(String name, String value);

}
