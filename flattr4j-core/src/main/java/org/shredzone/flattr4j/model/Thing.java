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
package org.shredzone.flattr4j.model;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.connector.FlattrObject;
import org.shredzone.flattr4j.exception.MarshalException;

/**
 * A {@link Thing} that has been registered with Flattr. Two {@link Thing} are considered
 * equal if they contain the same id. Some properties of a {@link Thing} can be modified
 * by setters. After that, invoke
 * {@link FlattrService#update(org.shredzone.flattr4j.model.Thing)} to persist the
 * changes.
 * <p>
 * This class is not threadsafe.
 *
 * @author Richard "Shred" Körber
 */
public class Thing extends Resource implements ThingId, UserId, CategoryId, LanguageId {
    private static final long serialVersionUID = 2822280427303390055L;

    private transient List<String> tags = null;
    private transient User user = null;
    private Set<String> updatedKeys = new HashSet<String>();

    /**
     * Returns a {@link ThingId} for the given Thing id.
     *
     * @param id
     *            Thing id
     * @return A {@link ThingId} object for this id
     */
    public static ThingId withId(final String id) {
        return new ThingId() {
            @Override
            public String getThingId() {
                return id;
            }
        };
    }

    public Thing(FlattrObject data) {
        super(data);
    }

    /**
     * Thing's unique id at Flattr.
     */
    @Override
    public String getThingId() {
        return String.valueOf(data.getInt("id"));
    }

    /**
     * URL that returns details of this resource as JSON.
     */
    public String getResource() {
        return data.get("resource");
    }

    /**
     * Human readable link to this resource at Flattr.
     */
    public String getLink() {
        return data.get("link");
    }

    /**
     * Creation date of the Thing.
     */
    public Date getCreated() {
        return data.getDate("created_at");
    }

    /**
     * How many times this Thing was flattred.
     */
    public int getClicks() {
        return data.getInt("flattrs");
    }

    /**
     * URL of the Thing.
     */
    public String getUrl() {
        return data.get("url");
    }

    /**
     * Title of the Thing.
     */
    public String getTitle() {
        return data.get("title");
    }

    public void setTitle(String title) {
        data.put("title", title);
        updatedKeys.add("title");
    }

    /**
     * URL of an image for this Thing. Empty string if there is none.
     *
     * @since 2.0
     */
    public String getImage() {
        return (data.has("image") ? data.get("image") : "");
    }

    /**
     * User this Thing belongs to.
     */
    @Override
    public String getUserId() {
        return data.getSubString("owner", "username");
    }

    /**
     * User this Thing belongs to.
     * <p>
     * All properties are only available when the service was set to full mode!
     *
     * @since 2.2
     */
    public User getUser() {
        if (user == null) {
            user = new User(data.getFlattrObject("owner"));
        }
        return user;
    }

    /**
     * Category this Thing belongs to.
     */
    @Override
    public String getCategoryId() {
        return data.get("category");
    }

    public void setCategory(CategoryId category) {
        data.put("category", (category != null ? category.getCategoryId() : null));
        updatedKeys.add("category");
    }

    /**
     * A descriptive text about the Thing.
     */
    public String getDescription() {
        return data.get("description");
    }

    public void setDescription(String description) {
        data.put("description", description);
        updatedKeys.add("description");
    }

    /**
     * Tags this Thing is tagged with.
     */
    public List<String> getTags() {
        if (tags == null) {
            tags = data.getStrings("tags");
        }
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
        updatedKeys.add("tags");
    }

    public void addTag(String tag) {
        if (tags == null) {
            tags = data.getStrings("tags");
        }
        tags.add(tag);
        updatedKeys.add("tags");
    }

    /**
     * Language id of the Thing.
     */
    @Override
    public String getLanguageId() {
        return data.get("language");
    }

    public void setLanguage(LanguageId language) {
        data.put("language", (language != null ? language.getLanguageId() : null));
        updatedKeys.add("language");
    }

    /**
     * Is the Thing hidden from the public list of Things at Flattr?
     */
    public boolean isHidden() {
        return data.getBoolean("hidden");
    }

    public void setHidden(boolean hidden) {
        data.put("hidden", hidden);
        updatedKeys.add("hidden");
    }

    /**
     * Is this Thing flattred?
     *
     * @since 2.0
     */
    public boolean isFlattred() {
        return data.getBoolean("flattred");
    }

    /**
     * Is this Thing subscribed?
     *
     * @since 2.6
     */
    public boolean isSubscribed() {
        return data.getBoolean("subscribed");
    }

    /**
     * Date of last Flattr. Only available to the owner of this Thing.
     *
     * @since 2.0
     */
    public Date getLastFlattr() {
        return data.getDate("last_flattr_at");
    }

    /**
     * Date of last Update. Only available to the owner of this Thing.
     *
     * @since 2.0
     */
    public Date getUpdated() {
        return data.getDate("updated_at");
    }

    /**
     * Merges the contents of a submission to this {@link Thing}. This method is useful if
     * you want to modify an existing {@link Thing} by a {@link Submission} object.
     * <p>
     * For all unset properties of {@link Submission}, the {@link Thing} properties are
     * cleared. After merging, the {@link Thing} is in a state as if it was created with
     * the given {@link Submission}. If you only want to change single properties of
     * {@link Thing}, use the setters instead.
     * <p>
     * <em>NOTE:</em> The URL of a {@link Thing} cannot be changed. The {@link Submission}
     * object must contain either this {@link Thing}'s URL or {@code null} (for
     * convenience). Otherwise an {@link IllegalArgumentException} is thrown.
     *
     * @param submission
     *            {@link Submission} to merge
     * @since 2.0
     */
    public void merge(Submission submission) {
        if (submission.getUrl() != null && !this.getUrl().equals(submission.getUrl())) {
            throw new IllegalArgumentException("Thing URL '" + getUrl()
                            + "' cannot be changed to '" + submission.getUrl() + "'");
        }

        this.setCategory(submission.getCategory());
        this.setTitle(submission.getTitle());
        this.setDescription(submission.getDescription());
        this.setLanguage(submission.getLanguage());
        this.setTags(submission.getTags());

        if (submission.isHidden() != null) {
            this.setHidden(submission.isHidden());
        }
    }

    /**
     * Returns a {@link FlattrObject} for the updates that have been applied to this
     * Thing.
     *
     * @return {@link FlattrObject} for the updates, or {@code null} if this Thing was not
     *         modified.
     * @since 2.0
     */
    public FlattrObject toUpdate() {
        if (updatedKeys.isEmpty()) {
            return null;
        }

        data.putStrings("tags", tags);

        FlattrObject result = new FlattrObject();
        for (String key : updatedKeys) {
            if ("tags".equals(key)) {
                // Strangely, tags are expected to be joined in a comma separated string
                StringBuilder sb = new StringBuilder();
                for (String tag : tags) {
                    if (tag.indexOf(',') >= 0) {
                        throw new MarshalException("tag '" + tag + "' contains invalid character ','");
                    }
                    sb.append(',').append(tag);
                }
                if (sb.length() > 0) {
                    sb.deleteCharAt(0);
                }
                result.put(key, sb.toString());
            } else {
                if (data.has(key)) {
                    result.put(key, data.getObject(key));
                }
            }
        }
        return result;
    }

    /**
     * Returns the URL of a PDF document containing a QR code of the Thing. The PDF
     * can be printed, sticked on the wall, and then flattered using a mobile phone.
     */
    public String getQrPdfUrl() {
        return "https://flattr.com/thing/qr/" + getThingId();
    }

    @Override
    public boolean equals(Object obj) {
        String pk = getThingId();
        if (pk == null || obj == null || !(obj instanceof Thing)) {
            return false;
        }
        return pk.equals(((Thing) obj).getThingId());
    }

    @Override
    public int hashCode() {
        String pk = getThingId();
        return (pk != null ? pk.hashCode() : 0);
    }

}