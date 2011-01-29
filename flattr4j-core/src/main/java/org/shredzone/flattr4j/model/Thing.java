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
package org.shredzone.flattr4j.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A {@link Thing} that has been registered with Flattr. Two {@link Thing} are considered
 * equal if they contain the same id.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class Thing implements ThingId, Serializable {
    private static final long serialVersionUID = 610493674068876984L;

    private static final String BASE_URL = "https://flattr.com/thing/";

    private String id;
    private String intId;
    private Date created;
    private int clicks;
    private String url;
    private String title;
    private User user;
    private ThingStatus status;
    private Category category;
    private String description;
    private List<String> tags = new ArrayList<String>();
    private String language;
    private boolean hidden = false;


    /**
     * Thing's unique id at Flattr.
     */
    @Override
    public String getId()                   { return id; }
    public void setId(String id)            { this.id = id; }

    /**
     * Internal Flattr Thing's unique id.
     */
    public String getIntId()                { return intId; }
    public void setIntId(String intId)      { this.intId = intId; }

    /**
     * Creation date of the Thing.
     */
    public Date getCreated()                { return created; }
    public void setCreated(Date created)    { this.created = created; }

    /**
     * How many times this Thing was flattred.
     */
    public int getClicks()                  { return clicks; }
    public void setClicks(int clicks)       { this.clicks = clicks; }

    /**
     * The {@link User} who owns this Thing.
     */
    public User getUser()                   { return user; }
    public void setUser(User user)          { this.user = user; }
    
    /**
     * Status of this Thing.
     */
    public ThingStatus getStatus()          { return status; }
    public void setStatus(ThingStatus status) { this.status = status; }

    /**
     * URL of the Thing.
     */
    public String getUrl()                      { return url; }
    public void setUrl(String url)              { this.url = url; }

    /**
     * Title of the Thing.
     */
    public String getTitle()                    { return title; }
    public void setTitle(String title)          { this.title = title; }

    /**
     * Category this Thing belongs to.
     */
    public Category getCategory()               { return category; }
    public void setCategory(Category category)  { this.category = category; }

    /**
     * A descriptive text about the Thing.
     */
    public String getDescription()              { return description; }
    public void setDescription(String description) { this.description = description; }

    /**
     * Tags this Thing is tagged with.
     */
    public List<String> getTags()               { return tags; }
    public void setTags(List<String> tags)      { this.tags = tags; }
    public void addTag(String tag)              { tags.add(tag); }

    /**
     * Language id of the Thing.
     */
    public String getLanguage()                 { return language; }
    public void setLanguage(String language)    { this.language = language; }

    /**
     * Is the Thing hidden from the public list of Things at Flattr?
     */
    public boolean isHidden()                   { return hidden; }
    public void setHidden(boolean hidden)       { this.hidden = hidden; }

    /**
     * Returns the URL to the thing's page at Flattr.
     * <p>
     * Currently this value is computed by flattr4j. The returned link may be different to
     * the official link shown by Flattr. The composition of this link may change in
     * future releases.
     */
    public String getThingUrl() {
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_URL).append(intId).append('/');
        String title = getTitle();
        if (title != null) {
            sb.append(title.replaceAll("\\s+", "-").replaceAll("[^a-zA-Z0-9_-]", ""));
        }
        return sb.toString();
    }

    /**
     * Returns the URL of a PDF document containing a QR code of the Thing. The PDF
     * can be printed, sticked on the wall, and then flattered using a mobile phone.
     */
    public String getQrPdfUrl() {
        return "https://flattr.com/things/show/id/" + intId + "/qrcode/true";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Thing)) {
            return false;
        }
        return id.equals(((Thing) obj).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}