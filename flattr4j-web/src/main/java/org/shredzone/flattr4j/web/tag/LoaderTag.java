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

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.shredzone.flattr4j.model.Category;
import org.shredzone.flattr4j.model.CategoryId;
import org.shredzone.flattr4j.model.Language;
import org.shredzone.flattr4j.model.LanguageId;
import org.shredzone.flattr4j.model.User;
import org.shredzone.flattr4j.model.UserId;
import org.shredzone.flattr4j.web.ButtonType;
import org.shredzone.flattr4j.web.builder.LoaderBuilder;

/**
 * A tag that creates the JavaScript loader tag.
 *
 * @author Richard "Shred" Körber
 */
public class LoaderTag extends BodyTagSupport {
    private static final long serialVersionUID = -5892160942069969373L;

    private LoaderBuilder builder;
    private String var;
    private String scope;

    public void setBare(boolean bare) {
        setupBuilder();
        if (bare) builder.bare();
    }

    public void setManual(boolean manual) {
        setupBuilder();
        if (manual) builder.manual();
    }

    public void setHttps(boolean https) {
        setupBuilder();
        if (https) builder.https();
    }

    public void setPopout(boolean popout) {
        setupBuilder();
        builder.popout(popout);
    }

    public void setUser(Object user) {
        setupBuilder();
        if (user instanceof UserId) {
            builder.user((UserId) user);
        } else {
            builder.user(User.withId(user.toString()));
        }
    }

    public void setButton(Object type) {
        setupBuilder();
        if (type instanceof ButtonType) {
            builder.button((ButtonType) type);
        } else {
            builder.button(ButtonType.valueOf(type.toString().toUpperCase()));
        }
    }

    public void setLanguage(Object language) {
        setupBuilder();
        if (language instanceof LanguageId) {
            builder.language((LanguageId) language);
        } else {
            builder.language(Language.withId(language.toString()));
        }
    }

    public void setCategory(Object category) {
        setupBuilder();
        if (category instanceof CategoryId) {
            builder.category((CategoryId) category);
        } else {
            builder.category(Category.withId(category.toString()));
        }
    }

    public void setPrefix(String prefix) {
        setupBuilder();
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
            builder = new LoaderBuilder();
        }
    }

    /**
     * Disposes the builder instance.
     */
    protected void disposeBuilder() {
        builder = null;
    }

}
