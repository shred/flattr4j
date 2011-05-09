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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.exception.FlattrServiceException;
import org.shredzone.flattr4j.exception.NotFoundException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * An abstract, simple XML parser for parsing the responses of the Flattr API. The goal of
 * this class is to provide a lightweight instead of a full featured XML parser. The
 * document is not validated, but a correct document structure is just expected. Derived
 * classes need to implement {@link #parseStartElement(String)} and
 * {@link #parseEndElement(String, String)} to analyze the document structure and creating
 * result objects.
 * 
 * @param T
 *            Target object type that is created by the parser.
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public abstract class AbstractXmlParser<T> extends DefaultHandler {

    private final SAXParserFactory factory = SAXParserFactory.newInstance();
    private final List<T> resultList = new ArrayList<T>();
    private final List<StringBuilder> stringStack = new LinkedList<StringBuilder>();

    /**
     * Parses the given XML document and adds the result to the result list.
     * 
     * @param in
     *            InputStream for the XML document
     */
    public void parse(InputStream in) throws FlattrException {
        try {
            SAXParser parser = factory.newSAXParser();
            parser.parse(in, this);
        } catch (SAXException ex) {
            if (ex.getCause() instanceof FlattrException) {
                throw (FlattrException) ex.getCause();
            } else {
                throw new FlattrServiceException("Could not parse XML response", ex);
            }
        } catch (Exception ex) {
            throw new FlattrServiceException("Could not parse XML response", ex);
        }
    }

    /**
     * Returns a list of objects that was found in the XML document.
     * 
     * @return A List of Object that was created by the XML document data. May
     *         be empty, but never {@code null}.
     */
    public List<T> getList() throws FlattrException {
        return resultList;
    }

    /**
     * Returns the only object of the XML document.
     * 
     * @return The object returned by the XML document data.
     * @throws NotFoundException
     *             if there was not even a single object in the result.
     * @throws FlattrServiceException
     *             if there was more than one object in the result.
     */
    public T getSingle() throws FlattrException {
        if (resultList.isEmpty()) {
            throw new NotFoundException("unexpected empty result");
        } else if (resultList.size() > 1) {
            throw new FlattrServiceException("unexpected result size: " + resultList.size() + " != 1");
        } else {
            return resultList.get(0);
        }
    }

    /**
     * The start of an XML element was found.
     * 
     * @param tag
     *            Start tag that was found
     */
    protected abstract void parseStartElement(String tag) throws FlattrException;

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
    protected abstract T parseEndElement(String tag, String body) throws FlattrException;
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            stringStack.add(0, new StringBuilder());
            parseStartElement(qName.toLowerCase());
        } catch (FlattrException ex) {
            throw new SAXException(ex);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        try {
            String str = stringStack.remove(0).toString().trim();
            T result = parseEndElement(qName.toLowerCase(), str);
            if (result != null) {
                resultList.add(result);
            }
        } catch (FlattrException ex) {
            throw new SAXException(ex);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (!stringStack.isEmpty()) {
            stringStack.get(0).append(ch, start, length);
        }
    }
    
}
