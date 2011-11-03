/*
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
package org.shredzone.flattr4j.connector;

import java.util.Collection;

import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.oauth.AccessToken;
import org.shredzone.flattr4j.oauth.ConsumerKey;

/**
 * Prepares a connection against the Flattr API for a single call.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 448 $
 * @since 2.0
 */
public interface Connection {

    /**
     * Base URL to connect to. This must be an absolute URL.
     * 
     * @param url
     *            URL to connect to.
     * @return this
     */
    Connection url(String url);

    /**
     * Access token to be used for calls that require authentication. If no
     * {@link AccessToken} is set, the call will be placed anonymously.
     * 
     * @param token
     *            {@link AccessToken} to be used
     * @return this
     */
    Connection token(AccessToken token);

    /**
     * Consumer key to be used. Usually, this is only necessary in the OAuth2
     * authentication process.
     * 
     * @param key
     *            {@link ConsumerKey} to be used
     * @return this
     */
    Connection key(ConsumerKey key);

    /**
     * Call to be placed against the URL. The call string may contain placeholders that
     * are prepended by a colon (e.g. "users/:username/things").
     * <p>
     * If no call is set, the {@link #url(String)} is invoked unchanged.
     * 
     * @param call
     *            Signature of the call to be invoked
     * @return this
     */
    Connection call(String call);

    /**
     * A parameter to be used for a placeholder in the call string. If the call string
     * does not contain a matching placeholder, it will be silently ignored.
     * 
     * @param name
     *            Parameter name, without colon prepended
     * @param value
     *            Value to be used.
     * @return this
     */
    Connection parameter(String name, String value);

    /**
     * Query parameter to be used in the URL.
     * 
     * @param name
     *            Parameter name
     * @param value
     *            Parameter value
     * @return this
     */
    Connection query(String name, String value);

    /**
     * Data to be transported to the call, as {@link FlattrObject}. Requires
     * {@link RequestType#POST} or {@link RequestType#PATCH}, otherwise an exception
     * will be raised.
     * 
     * @param data
     *            Data to be sent
     * @return this
     */
    Connection data(FlattrObject data);

    /**
     * Form parameter to be sent. Requires {@link RequestType#POST}, otherwise an
     * exception will be raised. Do not mix with {@link #data(FlattrObject)}.
     * 
     * @param name
     *            Parameter name
     * @param value
     *            Parameter value
     * @return this
     */
    Connection form(String name, String value);

    /**
     * {@link RateLimit} object to keep updated.
     * 
     * @param limit
     *            {@link RateLimit} object
     * @return this
     */
    Connection rateLimit(RateLimit limit);

    /**
     * Invokes the call and returns an {@link FlattrObject} as response.
     * 
     * @return result
     * @throws FlattrException
     *             if the call could not be invoked or the web service returned an error
     */
    FlattrObject singleResult() throws FlattrException;

    /**
     * Invokes the call and returns a Collection of {@link FlattrObject} as response.
     * 
     * @return result
     * @throws FlattrException
     *             if the call could not be invoked or the web service returned an error
     */
    Collection<FlattrObject> result() throws FlattrException;

    /**
     * Closes the connection and releases all resources.
     */
    void close() throws FlattrException;

}
