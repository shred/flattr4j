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
 * A {@link Click}.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class Click implements Serializable {

    private static final long serialVersionUID = 7390599041226931054L;
    
    private String id;
    private Date clickTime;
    private String thingId;
    private String thingTitle;
    private String thingUrl;

    /**
     * Flattr internal Click ID. Can be used for abuse reporting.
     */
    public String getId()                       { return id; }
    public void setId(String id)                { this.id = id; }

    /**
     * Date and time of the click.
     */
    public Date getClickTime()                  { return clickTime; }
    public void setClickTime(Date clickTime)    { this.clickTime = clickTime; }

    /**
     * Thing ID of the Thing that was clicked.
     */
    public String getThingId()                  { return thingId; }
    public void setThingId(String thingId)      { this.thingId = thingId; }

    /**
     * Title of the Thing that was clicked.
     */
    public String getThingTitle()               { return thingTitle; }
    public void setThingTitle(String thingTitle) { this.thingTitle = thingTitle; }

    /**
     * URL of the actual Thing that was clicked.
     */
    public String getThingUrl()                 { return thingUrl; }
    public void setThingUrl(String thingUrl)    { this.thingUrl = thingUrl; }

}