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

    private final static String QN_USER = "user";
    private final static String QN_ID = "id";
    private final static String QN_USERNAME = "username";
    private final static String QN_FIRSTNAME = "firstname";
    private final static String QN_LASTNAME = "lastname";
    private final static String QN_CITY = "city";
    private final static String QN_COUNTRY = "country";
    private final static String QN_GRAVATAR = "gravatar";
    private final static String QN_EMAIL = "email";
    private final static String QN_DESCRIPTION = "description";
    private final static String QN_THINGCOUNT = "thingcount";

    private User current = null;

    public UserXmlParser(InputStream in) throws FlattrException {
        super(in);
    }

    @Override
    protected void parseStartElement(String tag) throws FlattrException {
        if (QN_USER.equals(tag) && current == null) {
            current = new User();
        }
    }

    @Override
    protected User parseEndElement(String tag, String body) throws FlattrException {
        User result = null;

        if (QN_USER.equals(tag) && current != null) {
            result = current;
            current = null;

        } else if (QN_ID.equals(tag) && current != null) {
            current.setUserId(body);
            
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
