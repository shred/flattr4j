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

import java.io.InputStream;

import javax.xml.namespace.QName;

import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Category;

/**
 * Parses an XML document for {@link Category} entries.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class CategoryXmlParser extends AbstractXmlParser<Category> {

    private final static QName QN_CATEGORIES = new QName("categories");
    private final static QName QN_CATEGORY = new QName("category");
    private final static QName QN_ID = new QName("id");
    private final static QName QN_NAME = new QName("name");

    private boolean inside = false;
    private Category current = null;

    public CategoryXmlParser(InputStream in) throws FlattrException {
        super(in);
    }

    @Override
    protected void parseStartElement(QName tag) throws FlattrException {
        if (QN_CATEGORIES.equals(tag)) {
            inside = true;

        } else if (QN_CATEGORY.equals(tag) && inside) {
            current = new Category();
        }
    }

    @Override
    protected Category parseEndElement(QName tag, String body) throws FlattrException {
        Category result = null;
        
        if (QN_CATEGORIES.equals(tag)) {
            inside = false;
        
        } else if (QN_CATEGORY.equals(tag) && current != null) {
            result = current;
            current = null;
        
        } else if (QN_ID.equals(tag) && current != null) {
            current.setId(body);
        
        } else if (QN_NAME.equals(tag) && current != null) {
            current.setName(body);
        
        }
        
        return result;
    }

}
