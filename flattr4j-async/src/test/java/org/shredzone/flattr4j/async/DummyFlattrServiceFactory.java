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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.connector.RateLimit;

/**
 * A factory class for creating dummy {@link FlattrService} implementations that do not
 * support any calls except {@link FlattrService#getLastRateLimit()} and
 * {@link FlattrService#setFullMode(boolean)}.
 *
 * @author Richard "Shred" Körber
 */
public class DummyFlattrServiceFactory {

    /**
     * Creates a new dummy {@link FlattrService} with an empty {@link RateLimit} instance.
     *
     * @return dummy {@link FlattrService}
     */
    public static FlattrService create() {
        return create(new RateLimit());
    }

    /**
     * Creates a new dummy {@link FlattrService}.
     *
     * @param rateLimit
     *            {@link RateLimit} to be returned by
     *            {@link FlattrService#getLastRateLimit()}.
     * @return dummy {@link FlattrService}
     */
    public static FlattrService create(final RateLimit rateLimit) {
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if("getLastRateLimit".equals(method.getName()) && args == null) {
                    // return RateLimit object
                    return rateLimit;

                } else if ("setFullMode".equals(method.getName()) && args.length == 1) {
                    // just ignore this call
                    return null;

                } else {
                    // all other calls are unsupported
                    throw new UnsupportedOperationException();
                }
            }
        };

        return (FlattrService) Proxy.newProxyInstance(
                        DummyFlattrServiceFactory.class.getClassLoader(),
                        new Class[] { FlattrService.class },
                        handler);
    }

}
