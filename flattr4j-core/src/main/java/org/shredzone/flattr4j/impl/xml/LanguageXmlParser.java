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
package org.shredzone.flattr4j.impl.xml;

import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Language;

/**
 * Parses an XML document for {@link Language} entries.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class LanguageXmlParser extends AbstractXmlParser<Language> {

    private final static String QN_LANGUAGES = "languages";
    private final static String QN_LANGUAGE = "language";
    private final static String QN_ID = "id";
    private final static String QN_NAME = "name";

    private boolean inside = false;
    private Language current = null;

    @Override
    protected void parseStartElement(String tag) throws FlattrException {
        if (QN_LANGUAGES.equals(tag)) {
            inside = true;
            
        } else if (QN_LANGUAGE.equals(tag) && inside) {
            current = new Language();
        }
    }

    @Override
    protected Language parseEndElement(String tag, String body) throws FlattrException {
        Language result = null;
        
        if (QN_LANGUAGES.equals(tag)) {
            inside = false;
            
        } else if (QN_LANGUAGE.equals(tag) && current != null) {
            result = current;
            current = null;
            
        } else if (QN_ID.equals(tag) && current != null) {
            current.setLanguageId(body);
            
        } else if (QN_NAME.equals(tag) && current != null) {
            current.setName(body);
        }
        
        return result;
    }

}
