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
package org.shredzone.flattr4j.exception;

/**
 * This exception is thrown when the user has no sufficient funds for the operation.
 * <p>
 * Note: {@link NoMoneyException} extends {@link NoMeansException} for downward
 * compatibility purposes. It will extend {@link FlattrServiceException} in a future
 * release when the deprecated {@link NoMeansException} is removed.
 *
 * @since 2.11
 * @author Richard "Shred" Körber
 */
@SuppressWarnings("deprecation")
public class NoMoneyException extends NoMeansException {
    private static final long serialVersionUID = -5034022662480993764L;

    public NoMoneyException(String code, String msg) {
        super(code, msg);
    }

}
