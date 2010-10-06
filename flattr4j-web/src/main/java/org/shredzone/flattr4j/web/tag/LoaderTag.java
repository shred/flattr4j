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

import org.shredzone.flattr4j.model.Category;
import org.shredzone.flattr4j.model.Language;
import org.shredzone.flattr4j.model.User;
import org.shredzone.flattr4j.web.ButtonType;
import org.shredzone.flattr4j.web.builder.LoaderBuilder;

/**
 * A tag that creates the JavaScript loader tag.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class LoaderTag extends BodyTagSupport {
    private static final long serialVersionUID = -5892160942069969373L;
    
    private LoaderBuilder builder = new LoaderBuilder();
    private String var;
    private String scope;

    public void setBare(boolean bare) {
        if (bare) builder.bare();
    }

    public void setManual(boolean manual) {
        if (manual) builder.manual();
    }

    public void setHttps(boolean https) {
        if (https) builder.https();
    }

    public void setUser(String uid) {
        builder.user(uid);
    }

    public void setUser(User user) {
        builder.user(user);
    }

    public void setButton(String button) {
        setButton(ButtonType.valueOf(button));
    }

    public void setButton(ButtonType type) {
        builder.button(type);
    }

    public void setLanguage(String language) {
        builder.language(language);
    }

    public void setLanguage(Language language) {
        builder.language(language);
    }

    public void setCategory(String category) {
        builder.category(category);
    }

    public void setCategory(Category category) {
        builder.category(category);
    }

    public void setPrefix(String prefix) {
        builder.prefix(prefix);
    }

    public void setVar(String var) {
        this.var = var;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public int doStartTag() throws JspException {
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

        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

}
