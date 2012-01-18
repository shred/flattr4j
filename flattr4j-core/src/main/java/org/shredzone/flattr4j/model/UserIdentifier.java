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
package org.shredzone.flattr4j.model;

/**
 * A {@link UserIdentifier} is used to identify a user of a web site. This user can
 * collect Flattr even when he is not registered there, and will be able to claim the
 * Flattr clicks he gained.
 * <p>
 * {@link UserIdentifier} is only useful for Partner Site Integration. You need to
 * register your site with Flattr before.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision:$
 * @since 2.0
 */
public interface UserIdentifier {

    /**
     * User identifier string.
     * <p>
     * Examples:
     * <ul>
     *   <li><tt>email:05f8b3480b04f6a516bb1a46e556323c</tt></li>
     *   <li><tt>twitter:user:id:123</tt></li>
     * </ul>
     */
    String getUserIdentifier();

}
