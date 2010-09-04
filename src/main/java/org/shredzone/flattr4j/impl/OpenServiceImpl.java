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
package org.shredzone.flattr4j.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.shredzone.flattr4j.OpenService;
import org.shredzone.flattr4j.connector.OpenConnector;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.exception.FlattrServiceException;
import org.shredzone.flattr4j.model.Category;
import org.shredzone.flattr4j.model.Language;

/**
 * Default implementation of {@link OpenService}.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class OpenServiceImpl implements OpenService {

    private final OpenConnector connector;

    public OpenServiceImpl(OpenConnector connector) {
        this.connector = connector;
    }

    @Override
    public List<Category> getCategoryList() throws FlattrException {
        try {
            List<Category> result = new ArrayList<Category>();
            BufferedReader reader = null;

            try {
                reader = new BufferedReader(connector.call("categories/text"));
    
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] split = line.split(";");
                    if (split.length != 2) {
                        throw new FlattrServiceException("Invalid line: '" + line + "'");
                    }
    
                    Category category = new Category();
                    category.setId(split[0]);
                    category.setName(split[1]);
                    result.add(category);
                }
    
                return Collections.unmodifiableList(result);
            } finally {
                if (reader != null) reader.close();
            }
        } catch (IOException ex) {
            throw new FlattrServiceException("Could not read from stream", ex);
        }
    }

    @Override
    public List<Language> getLanguageList() throws FlattrException {
        try {
            List<Language> result = new ArrayList<Language>();
            BufferedReader reader = null;

            try {
                reader = new BufferedReader(connector.call("languages/text"));

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] split = line.split(";");
                    if (split.length != 2) {
                        throw new FlattrServiceException("Invalid line: '" + line + "'");
                    }
    
                    Language language = new Language();
                    language.setId(split[0]);
                    language.setName(split[1]);
                    result.add(language);
                }
    
                return Collections.unmodifiableList(result);
            } finally {
                if (reader != null) reader.close();
            }
        } catch (IOException ex) {
            throw new FlattrServiceException("Could not read from stream", ex);
        }
    }

}
