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
package org.springframework.social.flattr.connect;

import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.exception.FlattrServiceException;
import org.shredzone.flattr4j.exception.ForbiddenException;
import org.shredzone.flattr4j.exception.NoMoneyException;
import org.shredzone.flattr4j.exception.NotFoundException;
import org.shredzone.flattr4j.exception.RateLimitExceededException;
import org.springframework.social.ApiException;
import org.springframework.social.OperationNotPermittedException;
import org.springframework.social.ResourceNotFoundException;
import org.springframework.social.ServerException;
import org.springframework.social.SocialException;

/**
 * Provides some internal utility methods for Spring Social provider.
 *
 * @author Richard "Shred" Körber
 * @since 2.4
 */
public final class FlattrSocialUtils {

    private FlattrSocialUtils() {
        // utility class
    }

    /**
     * Returns a Spring {@link SocialException} that represents the
     * {@link FlattrException}.
     *
     * @param ex
     *            {@link FlattrException}
     * @return matching {@link SocialException}
     */
    public static SocialException socialFlattrException(FlattrException ex) {
        if (ex instanceof FlattrServiceException) {
            return new ServerException(ex.getMessage());

        } else if (ex instanceof ForbiddenException) {
            return new OperationNotPermittedException(ex.getMessage());

        } else if (ex instanceof NoMoneyException) {
            // a more specific Spring Social exception does not exist
            return new OperationNotPermittedException(ex.getMessage());

        } else if (ex instanceof NotFoundException) {
            return new ResourceNotFoundException(ex.getMessage());

        } else if (ex instanceof RateLimitExceededException) {
            return new org.springframework.social.RateLimitExceededException();

        } else {
            return new ApiException(ex.getMessage(), ex);
        }
    }

}
