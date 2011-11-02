/**
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

import java.io.Serializable;
import java.util.Date;

/**
 * A {@link ClickedThing}.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class ClickedThing implements ThingId, Serializable {

    private static final long serialVersionUID = 7390599041226931054L;

    private String id;
    private String title;
    private String url;
    private Date time;
    private String clickId;
    
    /**
     * The Thing ID that was clicked on.
     */
    @Override
    public String getThingId()                  { return id; }
    public void setThingId(String id)           { this.id = id; }
    
    /**
     * The Thing title.
     */
    public String getTitle()                    { return title; }
    public void setTitle(String title)          { this.title = title; }

    /**
     * The Thing URL.
     */
    public String getUrl()                      { return url; }
    public void setUrl(String url)              { this.url = url; }

    /**
     * The time of the click.
     */
    public Date getTime()                       { return time; }
    public void setTime(Date time)              { this.time = time; }
    
    /**
     * A Flattr internal click ID. Can be used for abuse complaints.
     */
    public String getClickId()                  { return clickId; }
    public void setClickId(String clickId)      { this.clickId = clickId; }

}