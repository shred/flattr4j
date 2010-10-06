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

import java.util.Collection;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * An optional tag for the parent button tag.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class TagTag extends BodyTagSupport {
    private static final long serialVersionUID = -2512754082305270317L;
    
    private Collection<String> list;
    private String value;
    
    public void setList(Collection<String> list) {
        this.list = list;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public int doStartTag() throws JspException {
        if (value != null) {
            return SKIP_BODY;
        } else {
            return EVAL_BODY_BUFFERED;
        }
    }

    @Override
    public int doEndTag() throws JspException {
        ButtonTag parent = TagUtils.findAncestorWithType(this, ButtonTag.class);
        if (parent != null) {
            if (value != null) {
                parent.addTag(value);
            } else {
                parent.addTag(getBodyContent().getString());
            }
            if (list != null) {
                parent.addTags(list);
            }
        }
        return EVAL_PAGE;
    }

}
