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
package org.shredzone.flattr4j.connector.impl;

import java.text.MessageFormat;
import java.util.logging.Level;

import android.util.Log;

/**
 * A simple logger class that delegates log output either to {@link android.util.Log} (if
 * available) or to {@link java.util.logging.Logger}.
 *
 * @author Richard "Shred" Körber
 * @since 2.3
 */
public class Logger {
    private final java.util.logging.Logger logger;
    private final String tag;

    /**
     * Creates a new {@link Logger}.
     *
     * @param tag
     *            Tag that is used for Android logging
     * @param className
     *            Class name that is used for Java logging
     */
    public Logger(String tag, String className) {
        this.tag = tag;

        java.util.logging.Logger log = null;
        try {
            // Test if Android logging is available...
            Log.isLoggable(tag, Log.INFO);
        } catch (Throwable t) {
            log = java.util.logging.Logger.getLogger(className);
        }
        this.logger = log;
    }

    /**
     * Logs on a verbose level.
     *
     * @param msg
     *            Message to be logged
     * @param args
     *            Arguments for formatting the message
     */
    public void verbose(String msg, Object... args) {
        if (logger != null) {
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, msg, args);
            }
        } else {
            if (Log.isLoggable(tag, Log.VERBOSE)) {
                Log.v(tag, MessageFormat.format(msg, args));
            }
        }
    }

    /**
     * Logs on an info level.
     *
     * @param msg
     *            Message to be logged
     * @param args
     *            Arguments for formatting the message
     */
    public void info(String msg, Object... args) {
        if (logger != null) {
            if (logger.isLoggable(Level.INFO)) {
                logger.log(Level.INFO, msg, args);
            }
        } else {
            if (Log.isLoggable(tag, Log.INFO)) {
                Log.i(tag, MessageFormat.format(msg, args));
            }
        }
    }

    /**
     * Logs on an error level.
     *
     * @param msg
     *            Message to be logged
     * @param args
     *            Arguments for formatting the message
     */
    public void error(String msg, Object... args) {
        if (logger != null) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, msg, args);
            }
        } else {
            if (Log.isLoggable(tag, Log.WARN)) {
                Log.w(tag, MessageFormat.format(msg, args));
            }
        }
    }

}
