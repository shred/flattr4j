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
package org.shredzone.flattr4j.web;

/**
 * Type of Flattr button to be used.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public enum ButtonType {

    /**
     * A button with default size (50 x 60)
     */
    DEFAULT(50, 60, "http://api.flattr.com/button/button-static-50x60.png"),

    /**
     * A button with compact size (100 x 17)
     */
    COMPACT(100, 17, "http://api.flattr.com/button/button-compact-static-100x17.png");

    private final int width;
    private final int height;
    private final String url;

    /**
     * Creates an enumeration object.
     */
    private ButtonType(int width, int height, String url) {
        this.width = width;
        this.height = height;
        this.url = url;
    }

    /**
     * Image width of the button.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Image height of the button.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the URL of a static button image.
     */
    public String getUrl() {
        return url;
    }

}
