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

import java.io.StringWriter;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Thing;

/**
 * Builds an XML document from a {@link Thing}.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public final class ThingXmlWriter {

    private final static String TAG_THING = "thing";
    private final static String TAG_TAGS = "tags";

    private final static XMLEventFactory FACTORY = XMLEventFactory.newInstance();

    private ThingXmlWriter() {
        // Private constructor
    }

    /**
     * Converts a {@link Thing} to its XML representation.
     * 
     * @param thing
     *            {@link Thing} to be converted
     * @return XML document representing that {@link Thing}
     */
    public static String write(Thing thing) throws FlattrException {
        try {
            StringWriter writer = new StringWriter();
            XMLEventWriter xml = XMLOutputFactory.newInstance().createXMLEventWriter(writer);

            xml.add(FACTORY.createStartElement("", "", TAG_THING));
            writeTag(xml, "url", thing.getUrl());
            writeCDataTag(xml, "title", thing.getTitle());
            writeTag(xml, "category", thing.getCategory());
            writeCDataTag(xml, "description", thing.getDescription());
            writeTag(xml, "language", thing.getLanguage());
            writeTag(xml, "hidden", thing.isHidden() ? "1" : "0");

            xml.add(FACTORY.createStartElement("", "", TAG_TAGS));
            for (String tag : thing.getTags()) {
                writeTag(xml, "tag", tag);
            }
            xml.add(FACTORY.createEndElement("", "", TAG_TAGS));

            xml.add(FACTORY.createEndElement("", "", TAG_THING));
            xml.flush();
            xml.close();
            
            return writer.toString();
        } catch (XMLStreamException ex) {
            throw new FlattrException("Could not generate XML", ex);
        }
    }

    /**
     * Writes a tag with a simple text body.
     * 
     * @param xml
     *            Writer to write to
     * @param tag
     *            tag to be created
     * @param body
     *            content of that element
     */
    private static void writeTag(XMLEventWriter xml, String tag, String body) throws XMLStreamException {
            xml.add(FACTORY.createStartElement("", "", tag));
            xml.add(FACTORY.createCharacters(body));
            xml.add(FACTORY.createEndElement("", "", tag));
    }

    /**
     * Writes a tag with a complex text body. The body is especially prepared for HTML
     * content.
     * 
     * @param xml
     *            Writer to write to
     * @param tag
     *            tag to be created
     * @param body
     *            content of that element
     */
    private static void writeCDataTag(XMLEventWriter xml, String tag, String body) throws XMLStreamException {
            xml.add(FACTORY.createStartElement("", "", tag));
            xml.add(FACTORY.createCData(body));
            xml.add(FACTORY.createEndElement("", "", tag));
    }

}
