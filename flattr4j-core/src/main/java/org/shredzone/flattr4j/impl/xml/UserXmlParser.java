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

import javax.xml.namespace.QName;

import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.exception.FlattrServiceException;
import org.shredzone.flattr4j.model.User;

/**
 * Parses an XML document for {@link User} entries.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class UserXmlParser extends AbstractXmlParser<User> {

    private final static QName QN_USER = new QName("user");
    private final static QName QN_ID = new QName("id");
    private final static QName QN_USERNAME = new QName("username");
    private final static QName QN_FIRSTNAME = new QName("firstname");
    private final static QName QN_LASTNAME = new QName("lastname");
    private final static QName QN_CITY = new QName("city");
    private final static QName QN_COUNTRY = new QName("country");
    private final static QName QN_GRAVATAR = new QName("gravatar");
    private final static QName QN_EMAIL = new QName("email");
    private final static QName QN_DESCRIPTION = new QName("description");
    private final static QName QN_THINGCOUNT = new QName("thingcount");

    private User current = null;

    public UserXmlParser(Reader reader) throws FlattrException {
        super(reader);
    }

    @Override
    protected void parseStartElement(QName tag) throws FlattrException {
        if (tag.equals(QN_USER) && current == null) {
            current = new User();
        }
    }

    @Override
    protected User parseEndElement(QName tag, String body) throws FlattrException {
        User result = null;

        if (tag.equals(QN_USER) && current != null) {
            result = current;
            current = null;

        } else if (tag.equals(QN_ID) && current != null) {
            current.setId(body);
        } else if (tag.equals(QN_USERNAME) && current != null) {
            current.setUsername(body);
        } else if (tag.equals(QN_FIRSTNAME) && current != null) {
            current.setFirstname(body);
        } else if (tag.equals(QN_LASTNAME) && current != null) {
            current.setLastname(body);
        } else if (tag.equals(QN_CITY) && current != null) {
            current.setCity(body);
        } else if (tag.equals(QN_COUNTRY) && current != null) {
            current.setCountry(body);
        } else if (tag.equals(QN_GRAVATAR) && current != null) {
            current.setGravatar(body);
        } else if (tag.equals(QN_EMAIL) && current != null) {
            current.setEmail(body);
        } else if (tag.equals(QN_DESCRIPTION) && current != null) {
            current.setDescription(body);
        } else if (tag.equals(QN_THINGCOUNT) && current != null) {
            try {
                current.setThingcount(Integer.parseInt(body));
            } catch (NumberFormatException ex) {
                throw new FlattrServiceException("Invalid thingcount: " + body);
            }
        }
        
        return result;
    }

}
