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
 * A single {@link Subscription}.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class Subscription implements ThingId, Serializable {

    private static final long serialVersionUID = 1571544645265778892L;
    
    private String id;
    private String title;
    private String url;
    private int months;
    private int monthsLeft;
    private Date added;
    private String subscriptionId;
    
    /**
     * The Thing ID that is subscribed.
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
     * The number of months the Thing was subscribed.
     */
    public int getMonths()                      { return months; }
    public void setMonths(int months)           { this.months = months; }

    /**
     * The number of months left until this subscription terminates.
     */
    public int getMonthsLeft()                  { return monthsLeft; }
    public void setMonthsLeft(int monthsLeft)   { this.monthsLeft = monthsLeft; }

    /**
     * The date the subscription was added.
     */
    public Date getAdded()                      { return added; }
    public void setAdded(Date added)            { this.added = added; }

    /**
     * The subscription ID.
     */
    public String getSubscriptionId()           { return subscriptionId; }
    public void setSubscriptionId(String id)    { this.subscriptionId = id; }

}
