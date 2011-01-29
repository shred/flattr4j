/**
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
package org.shredzone.flattr4j.impl.xml;

import java.io.InputStream;
import java.util.Date;

import javax.xml.namespace.QName;

import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.exception.FlattrServiceException;
import org.shredzone.flattr4j.model.ClickedThing;

/**
 * Parses an XML document for {@link ClickedThing} entries.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class ClickedThingXmlParser extends AbstractXmlParser<ClickedThing> {

    private final static QName QN_CLICK = new QName("click");
    private final static QName QN_ID = new QName("id");
    private final static QName QN_CLICK_TIME = new QName("click_time");
    private final static QName QN_THING = new QName("thing");
    private final static QName QN_TITLE = new QName("title");
    private final static QName QN_URL = new QName("url");

    private ClickedThing current = null;
    private boolean insideThing = false;

    public ClickedThingXmlParser(InputStream in) throws FlattrException {
        super(in);
    }

    @Override
    protected void parseStartElement(QName tag) throws FlattrException {
        if (QN_CLICK.equals(tag) && current == null) {
            current = new ClickedThing();
            
        } else if (QN_THING.equals(tag) && current != null) {
            insideThing = true;
        }
    }

    @Override
    protected ClickedThing parseEndElement(QName tag, String body) throws FlattrException {
        ClickedThing result = null;

        if (QN_CLICK.equals(tag) && current != null) {
            result = current;
            current = null;
            insideThing = false;

        } else if (QN_THING.equals(tag) && insideThing) {
            insideThing = false;
            
        } else if (QN_ID.equals(tag) && current != null && !insideThing) {
            current.setClickId(body);
            
        } else if (QN_CLICK_TIME.equals(tag) && current != null && !insideThing) {
            try {
                current.setTime(new Date(Long.parseLong(body) * 1000L));
            } catch (NumberFormatException ex) {
                throw new FlattrServiceException("Invalid click_time: " + body);
            }
            
        } else if (QN_ID.equals(tag) && current != null && insideThing) {
            current.setId(body);
            
        } else if (QN_TITLE.equals(tag) && current != null && insideThing) {
            current.setTitle(body);
            
        } else if (QN_URL.equals(tag) && current != null && insideThing) {
            current.setUrl(body);
        }
        
        return result;
    }

}
