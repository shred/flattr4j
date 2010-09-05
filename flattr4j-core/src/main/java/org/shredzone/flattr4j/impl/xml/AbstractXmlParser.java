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

import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.exception.FlattrServiceException;

/**
 * An abstract, simple XML parser for parsing the responses of the Flattr API. The goal of
 * this class is to provide a lightweight instead of a full featured XML parser. The
 * document is not validated, but a correct document structure is just expected. Derived
 * classes need to implement {@link #parseStartElement(QName)} and
 * {@link #parseEndElement(QName, String)} to analyze the document structure and creating
 * result objects.
 * 
 * @param T
 *            Target object type that is created by the parser.
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public abstract class AbstractXmlParser<T> {

    private final XMLInputFactory factory = XMLInputFactory.newInstance();
    private XMLEventReader xmlReader;
    private List<StringBuilder> stringStack = new LinkedList<StringBuilder>();

    /**
     * Creates a new XML parser for the given XML document.
     * 
     * @param reader
     *            Reader for the XML document
     */
    public AbstractXmlParser(Reader reader) throws FlattrException {
        try {
            xmlReader = factory.createXMLEventReader(reader);
        } catch (XMLStreamException ex) {
            throw new FlattrServiceException("Could not parse XML response", ex);
        }
    }

    /**
     * Returns the next object that was found in the XML document.
     * 
     * @return The next Object that was created by the XML document data, or {@code null}
     *         if the end of the document was reached.
     */
    public T getNext() throws FlattrException {
        try {
            while (xmlReader.hasNext()) {
                XMLEvent event = xmlReader.nextEvent();

                if (event.isStartElement()) {
                    stringStack.add(0, new StringBuilder());
                    StartElement element = (StartElement) event;
                    parseStartElement(element.getName());

                } else if (event.isEndElement()) {
                    String str = stringStack.remove(0).toString().trim();
                    EndElement element = (EndElement) event;
                    T result = parseEndElement(element.getName(), str);
                    if (result != null) {
                        return result;
                    }

                } else if (event.isCharacters()) {
                    Characters characters = (Characters) event;
                    if (!stringStack.isEmpty()) {
                        stringStack.get(0).append(characters.getData());
                    }
                }
            }
        } catch (XMLStreamException ex) {
            throw new FlattrException("Bad XML", ex);
        }

        return null;
    }

    /**
     * The start of an XML element was found.
     * 
     * @param tag
     *            Start tag that was found
     */
    protected abstract void parseStartElement(QName tag) throws FlattrException;

    /**
     * The end of an XML element was found.
     * 
     * @param tag
     *            End tag that was found
     * @param body
     *            The textual content that was encloded in this tag.
     * @return Newly created object, or {@code null} if no new object could be created for
     *         now
     */
    protected abstract T parseEndElement(QName tag, String body) throws FlattrException;
    
}
