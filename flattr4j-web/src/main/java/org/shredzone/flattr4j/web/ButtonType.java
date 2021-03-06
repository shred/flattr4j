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
 * Type of Flattr button to be used.
 *
 * @author Richard "Shred" Körber
 */
public enum ButtonType {

    /**
     * A button with default size (55 x 62)
     */
    DEFAULT(55, 62),

    /**
     * A button with compact size (110 x 20)
     */
    COMPACT(110, 20);

    private final int width;
    private final int height;

    /**
     * Creates an enumeration object.
     */
    private ButtonType(int width, int height) {
        this.width = width;
        this.height = height;
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

}
