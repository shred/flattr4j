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

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.shredzone.flattr4j.FeedService;
import org.shredzone.flattr4j.connector.FlattrConnector;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.impl.xml.CategoryXmlParser;
import org.shredzone.flattr4j.impl.xml.LanguageXmlParser;
import org.shredzone.flattr4j.model.Category;
import org.shredzone.flattr4j.model.Language;

/**
 * Default implementation of {@link FeedService}.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class FeedServiceImpl implements FeedService {

    private final FlattrConnector connector;

    public FeedServiceImpl(FlattrConnector connector) {
        this.connector = connector;
    }

    @Override
    public List<Category> getCategoryList() throws FlattrException {
        Reader reader = connector.call("feed/categories");

        List<Category> result = new ArrayList<Category>();

        CategoryXmlParser parser = new CategoryXmlParser(reader);
        Category category;
        while ((category = parser.getNext()) != null) {
            result.add(category);
        }
        
        return Collections.unmodifiableList(result);
    }

    @Override
    public List<Language> getLanguageList() throws FlattrException {
        Reader reader = connector.call("feed/languages");

        List<Language> result = new ArrayList<Language>();

        LanguageXmlParser parser = new LanguageXmlParser(reader);
        Language language;
        while ((language = parser.getNext()) != null) {
            result.add(language);
        }

        return Collections.unmodifiableList(result);
    }

}
