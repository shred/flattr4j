/*
 * flattr4j - A Java library for Flattr
 *
 * Copyright (C) 2014 Richard "Shred" Körber
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
package org.shredzone.flattr4j.async;

import org.shredzone.flattr4j.FlattrService;

/**
 * A {@link FlattrCallable} that does not return a result.
 *
 * @author Iulius Gutberlet
 * @author Richard "Shred" Körber
 */
public abstract class VoidFlattrCallable extends AbstractFlattrCallable<Void> {
    private static final long serialVersionUID = 2581148424236392276L;

    /**
     * Calls the appropriate method at the service.
     *
     * @param service
     *            Preconfigured {@link FlattrService} to be invoked
     */
    public abstract void execute(FlattrService service) throws Exception;

    @Override
    public Void call(FlattrService service) throws Exception {
        execute(service);
        return null;
    }

}
