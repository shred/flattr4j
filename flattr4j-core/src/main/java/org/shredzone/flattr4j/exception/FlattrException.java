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
package org.shredzone.flattr4j.exception;

/**
 * A generic Flattr exception, like a network error or an unexpected response from the
 * Flattr web service. More detailed exceptions are derived from this.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class FlattrException extends Exception {
    private static final long serialVersionUID = 3095863989605383892L;

    public FlattrException() {
        super();
    }

    public FlattrException(String msg) {
        super(msg);
    }

    public FlattrException(Throwable cause) {
        super(cause);
    }

    public FlattrException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
}
