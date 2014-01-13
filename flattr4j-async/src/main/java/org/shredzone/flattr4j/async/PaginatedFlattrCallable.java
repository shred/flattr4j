/*
 * flattr4j - A Java library for Flattr
 *
 * Copyright (C) 2014 Richard "Shred" Körber
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
package org.shredzone.flattr4j.async;

import org.shredzone.flattr4j.FlattrService;

/**
 * A {@link FlattrCallable} that allows pagination.
 *
 * @author Iulius Gutberlet
 * @author Richard "Shred" Körber
 */
public abstract class PaginatedFlattrCallable<R> extends AbstractFlattrCallable<R> {
    private static final long serialVersionUID = -4773347390673116071L;

    private Integer count;
    private Integer page;

    /**
     * The number of entries per page. {@code null} defaults to 30 entries.
     */
    public void setCount(Integer count)         { this.count = count; }
    public Integer getCount()                   { return count; }

    /**
     * Page number (counted from 1), or {@code null} to turn off paging.
     */
    public void setPage(Integer page)           { this.page = page; }
    public Integer getPage()                    { return page; }

    /**
     * Calls the appropriate method at the service and returns the result.
     *
     * @param service
     *            Preconfigured {@link FlattrService} to be invoked
     * @param count
     *            Number of elements per page, or {@code null}
     * @param page
     *            Page number, or {@code null}
     * @return Result returned from the Flattr method.
     */
    public abstract R call(FlattrService service, Integer count, Integer page) throws Exception;

    @Override
    public R call(FlattrService service) throws Exception {
        return call(service, count, page);
    }

}
