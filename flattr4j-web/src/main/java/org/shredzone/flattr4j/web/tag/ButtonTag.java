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
import java.util.Collection;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.shredzone.flattr4j.model.Category;
import org.shredzone.flattr4j.model.Language;
import org.shredzone.flattr4j.model.User;
import org.shredzone.flattr4j.web.ButtonType;
import org.shredzone.flattr4j.web.builder.ButtonBuilder;

/**
 * A button to a Flattr thing.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class ButtonTag extends BodyTagSupport implements Attributed {
    private static final long serialVersionUID = -2011193251581466746L;

    private ButtonBuilder builder;
    
    private boolean descripted = false;
    private String var = null;
    private String scope = null;
    
    public void setUrl(String url) {
        setupBuilder();
        builder.url(url);
    }
    
    public void setUser(Object user) {
        setupBuilder();
        if (user instanceof User) {
            builder.user((User) user);
        } else {
            builder.user(user.toString());
        }
    }
    
    public void setTitle(String title) {
        setupBuilder();
        builder.title(title);
    }
    
    public void setDescription(String description) {
        setupBuilder();
        builder.description(description);
        descripted = true;
    }
    
    public void setCategory(Object category) {
        setupBuilder();
        if (category instanceof Category) {
            builder.category((Category) category);
        } else {
            builder.category(category.toString());
        }
    }
    
    public void setLanguage(Object language) {
        setupBuilder();
        if (language instanceof Language) {
            builder.language((Language) language);
        } else {
            builder.language(language.toString());
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
    
    public void setHidden(boolean hidden) {
        setupBuilder();
        if (hidden) builder.hidden();
    }
    
    public void setHtml5(boolean html5) {
        setupBuilder();
        if (html5) builder.html5();
    }
    
    public void setPrefix(String prefix) {
        setupBuilder();
        builder.prefix(prefix);
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

    public void addTag(String tag) {
        setupBuilder();
        builder.tag(tag);
    }
    
    public void addTags(Collection<String> tags) {
        setupBuilder();
        builder.tags(tags);
    }
    
    @Override
    public void setAttribute(String name, String value) {
        setupBuilder();
        builder.attribute(name, value);
    }

    @Override
    public int doStartTag() throws JspException {
        setupBuilder();

        if (descripted) {
            return SKIP_BODY;
        } else {
            return EVAL_BODY_BUFFERED;
        }
    }

    @Override
    public int doEndTag() throws JspException {
        if (!descripted) {
            BodyContent bc = getBodyContent();
            if (bc != null) {
                String description = bc.getString().trim();
                if (!description.isEmpty()) {
                    builder.descriptionTruncate(description);
                }
            }
        }
        
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
            builder = new ButtonBuilder();
        }
    }

    /**
     * Disposes the builder instance.
     */
    protected void disposeBuilder() {
        builder = null;
    }

}
