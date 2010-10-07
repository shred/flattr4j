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
package org.shredzone.flattr4j.web.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.shredzone.flattr4j.model.RegisteredThing;
import org.shredzone.flattr4j.web.ButtonType;
import org.shredzone.flattr4j.web.builder.StaticButtonBuilder;

/**
 * A static button to a Flattr thing.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class StaticTag extends BodyTagSupport implements Attributed {
    private static final long serialVersionUID = -7356980489242218537L;

    private StaticButtonBuilder builder;
    
    private String var = null;
    private String scope = null;
    
    public void setThing(Object thing) {
        setupBuilder();
        if (thing instanceof RegisteredThing) {
            builder.thing((RegisteredThing) thing);
        } else {
            builder.thing(thing.toString());
        }
    }
    
    public void setButtonUrl(String url) {
        setupBuilder();
        builder.buttonUrl(url);
    }
    
    public void setButton(Object type) {
        setupBuilder();
        if (type instanceof ButtonType) {
            builder.button((ButtonType) type);
        } else {
            builder.button(ButtonType.valueOf(type.toString().toUpperCase()));
        }
    }
    
    public void setStyle(String style) {
        setupBuilder();
        builder.style(style);
    }
    
    public void setStyleClass(String styleClass) {
        setupBuilder();
        builder.styleClass(styleClass);
    }
    
    public void setVar(String var) {
        this.var = var;
    }
    
    public void setScope(String scope) {
        this.scope = scope;
    }
    
    @Override
    public void setAttribute(String name, String value) {
        setupBuilder();
        builder.attribute(name, value);
    }

    @Override
    public int doStartTag() throws JspException {
        setupBuilder();
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        String tag = builder.toString();

        if (var != null) {
            TagUtils.setScopedAttribute(pageContext, var, tag, scope);

        } else {
            try {
                pageContext.getOut().print(tag);
            } catch (IOException ex) {
                throw new JspException(ex);
            }
        }

        disposeBuilder();
        return EVAL_PAGE;
    }

    /**
     * Creates a new builder instance, if not already done.
     */
    protected void setupBuilder() {
        if (builder == null) {
            builder = new StaticButtonBuilder();
        }
    }

    /**
     * Disposes the builder instance.
     */
    protected void disposeBuilder() {
        builder = null;
    }

}
