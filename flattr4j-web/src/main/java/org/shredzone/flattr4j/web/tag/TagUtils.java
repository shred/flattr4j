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
package org.shredzone.flattr4j.web.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

/**
 * Utility methods for tag libraries.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public final class TagUtils {

    private TagUtils() {
        // Private constructor
    }

    /**
     * Set an attribute with the given scope.
     *
     * @param pageContext
     *            {@link PageContext} of the invoking tag
     * @param attribute
     *            Attribute name
     * @param value
     *            Value of that attribute
     * @param scope
     *            Scope to be used. {@code null} means page scope.
     * @throws JspException
     *             if the scope was unknown
     */
    public static void setScopedAttribute(PageContext pageContext, String attribute, Object value, String scope)
    throws JspException {
        int scopeId = PageContext.PAGE_SCOPE;
        if (scope != null) {
            if ("page".equals(scope)) {
                scopeId = PageContext.PAGE_SCOPE;
            } else if ("request".equals(scope)) {
                scopeId = PageContext.REQUEST_SCOPE;
            } else if ("session".equals(scope)) {
                scopeId = PageContext.SESSION_SCOPE;
            } else if ("application".equals(scope)) {
                scopeId = PageContext.APPLICATION_SCOPE;
            } else {
                throw new JspException("Unknown scope: " + scope);
            }
        }
        pageContext.setAttribute(attribute, value, scopeId);
    }

    /**
     * Finds an ancestor tag of the given type. Starting from the given Tag, it traverses
     * up the parent tags until a tag of the given type is found.
     *
     * @param <T>
     *            Type to find and return
     * @param from
     *            Tag to start from
     * @param type
     *            Type to find
     * @return Ancestor of that type, or {@code null} if none was found.
     */
    @SuppressWarnings("unchecked")
    public static <T> T findAncestorWithType(Tag from, Class<T> type) {
        Tag parent = from.getParent();
        while (parent != null) {
            if (type.isAssignableFrom(parent.getClass())) {
                return (T) parent;
            }
            parent = parent.getParent();
        }
        return null;
    }

}
