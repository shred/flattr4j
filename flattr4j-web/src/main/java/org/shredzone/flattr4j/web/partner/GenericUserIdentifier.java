/*
 * flattr4j - A Java library for Flattr
 *
 * Copyright (C) 2012 Richard "Shred" Körber
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
package org.shredzone.flattr4j.web.partner;

import java.io.Serializable;

import org.shredzone.flattr4j.model.UserIdentifier;

/**
 * A generic {@link UserIdentifier} implementation. The identifier string is passed
 * through unmodified.
 *
 * @author Richard "Shred" Körber
 * @since 2.5
 */
public class GenericUserIdentifier implements UserIdentifier, Serializable {
    private static final long serialVersionUID = 3030205670078497897L;

    private final String identifier;

    /**
     * Creates a new, generic {@link UserIdentifier}.
     *
     * @param identifier
     *            Full Identifier
     */
    public GenericUserIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getUserIdentifier() {
        return identifier;
    }

}
