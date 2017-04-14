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
 * @since 2.0
 */
public class AutoSubmission extends Submission implements UserId, UserIdentifier {
    private static final long serialVersionUID = 469255989509420133L;

    private static final String ENCODING = "utf-8";

    private UserId user;
    private UserIdentifier identifier;

    /**
     * The user to submit this Submission on behalf of.
     */
    public UserId getUser()                     { return user; }
    public void setUser(UserId user)            { this.user = user; }

    @Override
    public String getUserId()                   { return user != null ? user.getUserId() : null; }

    /**
     * The user identifier of the local user
     * <p>
     * This is only available for Partner Site Integration. You need to register your
     * site with Flattr in order to use user identifiers.
     */
    public UserIdentifier getIdentifier()       { return identifier; }
    public void setIdentifier(UserIdentifier identifier) { this.identifier = identifier; }

    @Override
    public String getUserIdentifier()           { return identifier != null ? identifier.getUserIdentifier() : null; }

    /**
     * Returns a URL that can be used for submitting a Thing (for example in a link).
     * <p>
     * Use this URL as a fallback only if you can't use
     * {@link FlattrService#create(Submission)} or JavaScript. The length of an URL is
     * limited and depends on the browser and the server. The submission will be truncated
     * if the maximum length was exceeded.
     *
     * @return Submission URL
     */
    public String toUrl() {
        if (user == null && identifier == null) {
            throw new IllegalArgumentException("Anonymous submissions are not allowed");
        }
        if (user != null && identifier != null) {
            throw new IllegalArgumentException("Either user or identifier must be set, but not both");
        }

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("https://flattr.com/submit/auto");

            if (user != null) {
                sb.append("?user_id=").append(URLEncoder.encode(getUserId(), ENCODING));
            } else {
                sb.append("?owner=").append(URLEncoder.encode(getUserIdentifier(), ENCODING));
            }
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
            throw new IllegalStateException(ex);
        }
    }

}
