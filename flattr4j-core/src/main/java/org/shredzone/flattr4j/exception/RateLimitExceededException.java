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
 * This exception is thrown when the rate limit was exceeded. The rate counter will be
 * reset after a while.
 * <p>
 * <em>Note:</em> The call limitation is posed by the Flattr API. This is not a limitation
 * of flattr4j.
 *
 * @author Richard "Shred" Körber
 */
public class RateLimitExceededException extends FlattrServiceException {
    private static final long serialVersionUID = 2052032965034468567L;

    public RateLimitExceededException(String code, String msg) {
        super(code, msg);
    }

}
