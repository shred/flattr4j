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
import org.shredzone.flattr4j.exception.FlattrServiceException;
import org.shredzone.flattr4j.model.UserDetails;

/**
 * Parses an XML document for {@link UserDetails} entries.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class UserXmlParser extends AbstractXmlParser<UserDetails> {

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

    private UserDetails current = null;

    public UserXmlParser(InputStream in) throws FlattrException {
        super(in);
    }

    @Override
    protected void parseStartElement(QName tag) throws FlattrException {
        if (QN_USER.equals(tag) && current == null) {
            current = new UserDetails();
        }
    }

    @Override
    protected UserDetails parseEndElement(QName tag, String body) throws FlattrException {
        UserDetails result = null;

        if (QN_USER.equals(tag) && current != null) {
            result = current;
            current = null;

        } else if (QN_ID.equals(tag) && current != null) {
            current.setId(body);
            
        } else if (QN_USERNAME.equals(tag) && current != null) {
            current.setUsername(body);

        } else if (QN_FIRSTNAME.equals(tag) && current != null) {
            current.setFirstname(body);
        
        } else if (QN_LASTNAME.equals(tag) && current != null) {
            current.setLastname(body);
        
        } else if (QN_CITY.equals(tag) && current != null) {
            current.setCity(body);
        
        } else if (QN_COUNTRY.equals(tag) && current != null) {
            current.setCountry(body);
        
        } else if (QN_GRAVATAR.equals(tag) && current != null) {
            current.setGravatar(body);
        
        } else if (QN_EMAIL.equals(tag) && current != null) {
            current.setEmail(body);
        
        } else if (QN_DESCRIPTION.equals(tag) && current != null) {
            current.setDescription(body);
        
        } else if (QN_THINGCOUNT.equals(tag) && current != null) {
            try {
                current.setThingcount(Integer.parseInt(body));
            } catch (NumberFormatException ex) {
                throw new FlattrServiceException("Invalid thingcount: " + body);
            }
        }
        
        return result;
    }

}
