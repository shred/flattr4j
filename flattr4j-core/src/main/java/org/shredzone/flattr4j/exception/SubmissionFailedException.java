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
package org.shredzone.flattr4j.exception;

/**
 * This exception is used when a Thing could not be registered.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class SubmissionFailedException extends FlattrException {
    private static final long serialVersionUID = 7637026385151047748L;

    public SubmissionFailedException() {
        super();
    }

    public SubmissionFailedException(String msg) {
        super(msg);
    }

}
