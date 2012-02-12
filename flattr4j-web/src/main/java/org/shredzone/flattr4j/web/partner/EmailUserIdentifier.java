/*
 * flattr4j - A Java library for Flattr
 *
 * Copyright (C) 2012 Richard "Shred" Körber
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
package org.shredzone.flattr4j.web.partner;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.shredzone.flattr4j.model.UserIdentifier;

/**
 * A {@link UserIdentifier} for identifying a user by email address. The email address is
 * stored as md5 hash.
 *
 * @author Richard "Shred" Körber
 * @version $Revision:$
 * @since 2.0
 */
public class EmailUserIdentifier implements UserIdentifier, Serializable {
    private static final long serialVersionUID = 201195102581924021L;

    private final String identifier;

    /**
     * Creates a new {@link UserIdentifier} for email.
     *
     * @param email
     *            User's email address
     */
    public EmailUserIdentifier(String email) {
        identifier = "email:" + computeHash(email);
    }

    @Override
    public String getUserIdentifier() {
        return identifier;
    }

    /**
     * Converts an email address to a md5 hash.
     *
     * @param email
     *            email address to convert
     * @return md5 hash
     */
    public static String computeHash(String email) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(email.trim().toLowerCase().getBytes("UTF-8"));

            StringBuilder digest = new StringBuilder();
            for (byte b : md5.digest()) {
                digest.append(String.format("%02x", b & 0xFF));
            }

            return digest.toString();
        } catch (NoSuchAlgorithmException ex) {
            // should never happen since MD5 is a standard digester
            throw new InternalError("no md5 hashing");
        } catch (UnsupportedEncodingException ex) {
            // should never happen since UTF-8 is a standard encoding
            throw new InternalError("no utf-8 encoding");
        }
    }

}
