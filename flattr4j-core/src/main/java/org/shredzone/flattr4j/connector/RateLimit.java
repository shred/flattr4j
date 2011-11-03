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
package org.shredzone.flattr4j.connector;

/**
 * Keeps the maximum rate of allowed calls and the remaining number of calls.
 * <p>
 * <em>Note:</em> The call limitation is posed by the Flattr API. This is not a limitation
 * of flattr4j.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: -1 $
 */
public class RateLimit {
    private Long limit;
    private Long remaining;
    
    /**
     * The maximum rate of allowed calls per time span. {@code null} if there is no such
     * information available.
     */
    public Long getLimit()                      { return limit; }
    public void setLimit(Long limit)            { this.limit = limit; }

    /**
     * The remaining rate of allowed calls per time span. {@code null} if there is no such
     * information available.
     */
    public Long getRemaining()                  { return remaining; }
    public void setRemaining(Long remaining)    { this.remaining = remaining; }

}
