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

    private ButtonBuilder builder = new ButtonBuilder();
    
    private boolean descripted = false;
    private String var = null;
    private String scope = null;
    
    public void setUrl(String url) {
        builder.url(url);
    }
    
    public void setUser(String uid) {
        builder.user(uid);
    }
    
    public void setUser(User user) {
        builder.user(user);
    }
    
    public void setTitle(String title) {
        builder.title(title);
    }
    
    public void setDescription(String description) {
        builder.description(description);
        descripted = true;
    }
    
    public void setCategory(String category) {
        builder.category(category);
    }
    
    public void setCategory(Category category) {
        builder.category(category);
    }
    
    public void setLanguage(String language) {
        builder.language(language);
    }
    
    public void setLanguage(Language language) {
        builder.language(language);
    }
    
    public void setButton(String type) {
        builder.button(ButtonType.valueOf(type));
    }
    
    public void setButton(ButtonType type) {
        builder.button(type);
    }
    
    public void setHidden(boolean hidden) {
        if (hidden) builder.hidden();
    }
    
    public void setHtml5(boolean html5) {
        if (html5) builder.html5();
    }
    
    public void setPrefix(String prefix) {
        builder.prefix(prefix);
    }
    
    public void setStyle(String style) {
        builder.style(style);
    }
    
    public void setStyleClass(String styleClass) {
        builder.styleClass(styleClass);
    }
    
    public void setVar(String var) {
        this.var = var;
    }
    
    public void setScope(String scope) {
        this.scope = scope;
    }

    public void addTag(String tag) {
        builder.tag(tag);
    }
    
    public void addTags(Collection<String> tags) {
        builder.tags(tags);
    }
    
    @Override
    public void setAttribute(String name, String value) {
        builder.attribute(name, value);
    }

    @Override
    public int doStartTag() throws JspException {
        if (descripted) {
            return SKIP_BODY;
        } else {
            return EVAL_BODY_BUFFERED;
        }
    }

    @Override
    public int doEndTag() throws JspException {
        if (!descripted) {
            String body = getBodyContent().getString().trim();
            if (!body.isEmpty()) {
                builder.descriptionTruncate(body);
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

        return EVAL_PAGE;
    }

}
