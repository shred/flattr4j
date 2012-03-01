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

import org.shredzone.flattr4j.oauth.Scope;

/**
 * This exception is used when the operation is forbidden. There are several causes for
 * this:
 * <ul>
 * <li>insufficient permissions, see {@link Scope}</li>
 * <li>the operation is not allowed (e.g. flattering an own thing)</li>
 * </ul>
 * The message contains the reason for the exception.
 *
 * @author Richard "Shred" Körber
 */
public class ForbiddenException extends FlattrServiceException {
    private static final long serialVersionUID = 1279509373771421366L;

    public ForbiddenException(String code, String msg) {
        super(code, msg);
    }

}
