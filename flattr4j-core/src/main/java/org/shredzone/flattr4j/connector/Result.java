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
package org.shredzone.flattr4j.connector;

import java.io.InputStream;

import org.shredzone.flattr4j.exception.FlattrException;

/**
 * The result of a connector call.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 * @deprecated Major changes of the connection handling in V2
 */
@Deprecated
public interface Result {
    
    /**
     * Asserts that the result is OK. Throws a matching {@link FlattrException} if it was
     * not.
     * 
     * @return itself
     * @throws FlattrException
     *             if the result was not OK
     */
    Result assertStatusOk() throws FlattrException;
    
    /**
     * Opens an {@link InputStream} to the result content.
     * 
     * @return {@link InputStream}
     */
    InputStream openInputStream() throws FlattrException;

    /**
     * Closes a previously opened {@link InputStream}. If no {@link InputStream} was
     * openend, nothing happens.
     */
    void closeInputStream() throws FlattrException;
    
    /**
     * Gets the encoding of the result content.
     * 
     * @return Encoding
     */
    String getEncoding() throws FlattrException;

}
