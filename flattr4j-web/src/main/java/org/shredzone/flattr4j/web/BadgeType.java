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
package org.shredzone.flattr4j.web;

/**
 * Enumeration of static Flattr badges that can be used.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public enum BadgeType {

    /**
     * A static badge with default size (93 x 20)
     */
    DEFAULT(93, 20, "http://api.flattr.com/button/flattr-badge-large.png"),

    /**
     * A static badge with small size (16 x 16)
     */
    SMALL(16, 16, "http://api.flattr.com/button/flattr-badge-small.png");

    private final int width;
    private final int height;
    private final String url;

    /**
     * Creates an enumeration object.
     */
    private BadgeType(int width, int height, String url) {
        this.width = width;
        this.height = height;
        this.url = url;
    }

    /**
     * Image width of the badge.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Image height of the badge.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the URL of a static badge image.
     */
    public String getUrl() {
        return url;
    }

}
