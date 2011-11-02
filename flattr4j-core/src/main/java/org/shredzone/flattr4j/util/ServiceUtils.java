/**
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
package org.shredzone.flattr4j.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Utility methods used by the services.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public final class ServiceUtils {

    private ServiceUtils() {
        // Service class without constructor
    }

    /**
     * URL encodes the String using UTF-8. Convenience method for a blunder in the
     * original API.
     * 
     * @param str
     *            String to encode
     * @return Encoded string
     */
    public static String urlencode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException("UTF-8 missing");
        }
    }
    
}
