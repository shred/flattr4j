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
package org.shredzone.flattr4j.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.shredzone.flattr4j.FlattrService;

/**
 * An {@link AutoSubmission} is used to generate a URL for auto submission. A user must
 * be set, which will be the owner of the Thing.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 * @since 2.0
 */
public class AutoSubmission extends Submission {
    private static final long serialVersionUID = 469255989509420133L;

    private static final String ENCODING = "utf-8";

    private UserId user;

    /**
     * The user to submit this Submission on behalf of.
     */
    public UserId getUser()                     { return user; }
    public void setUser(UserId user)            { this.user = user; }

    /**
     * Returns a URL that can be used for submitting a Thing (for example in a link).
     * <p>
     * Use this URL as a fallback only if you can't use
     * {@link FlattrService#create(Submission)} or JavaScript. The length of an URL is
     * limited and depends on the browser and the server. The submission will be truncated
     * if the maximum length was exceeded.
     *
     * @param user
     *            {@link UserId} of the user to register the submission with. Required,
     *            must not be {@code null}.
     * @return Submission URL
     * @since 2.0
     */
    public String toUrl() {
        if (user == null) {
            throw new IllegalArgumentException("Anonymous submissions are not allowed");
        }

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("https://flattr.com/submit/auto");

            sb.append("?user_id=").append(URLEncoder.encode(user.getUserId(), ENCODING));
            sb.append("&url=").append(URLEncoder.encode(getUrl(), ENCODING));

            if (getCategory() != null) {
                sb.append("&category=").append(URLEncoder.encode(getCategory().getCategoryId(), ENCODING));
            }

            if (getLanguage() != null) {
                sb.append("&language=").append(URLEncoder.encode(getLanguage().getLanguageId(), ENCODING));
            }

            if (getTitle() != null) {
                sb.append("&title=").append(URLEncoder.encode(getTitle(), ENCODING));
            }

            if (isHidden() != null && isHidden().booleanValue() == true) {
                sb.append("&hidden=1");
            }

            if (getTags() != null && !getTags().isEmpty()) {
                sb.append("&tags=").append(URLEncoder.encode(getTagsAsString(), ENCODING));
            }

            if (getDescription() != null) {
                sb.append("&description=").append(URLEncoder.encode(getDescription(), ENCODING));
            }

            return sb.toString();
        } catch (UnsupportedEncodingException ex) {
            throw new InternalError(ENCODING);
        }
    }

}
