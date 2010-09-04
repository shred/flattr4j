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
package org.shredzone.flattr4j;

import java.util.List;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Category;
import org.shredzone.flattr4j.model.Language;

/**
 * Offers service calls for open functions. Unlike all other services, these service calls
 * do not need a valid login at Flattr, but can be used anonymously.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public interface OpenService {

    /**
     * Gets a list of all Flattr {@link Category}. The result is not cached.
     * 
     * @return List of Flattr {@link Category}.
     */
    List<Category> getCategoryList() throws FlattrException;

    /**
     * Gets a list of all Flattr {@link Language}. The result is not cached.
     * 
     * @return List of Flattr {@link Language}.
     */
    List<Language> getLanguageList() throws FlattrException;

}
