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
package org.shredzone.flattr4j.web.builder;

import org.shredzone.flattr4j.model.Category;
import org.shredzone.flattr4j.model.Language;
import org.shredzone.flattr4j.model.User;
import org.shredzone.flattr4j.web.ButtonType;

/**
 * Builds the JavaScript loader for the Flattr API.
 * <p>
 * The builder uses reasonable default settings that can be overridden with its
 * methods. All methods return a reference to the builder, so methods can be daisy-chained.
 * <p>
 * Example: <code>String loader = new LoaderBuilder().uid("123456").toString();</code>
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class LoaderBuilder {
    private String baseUrl = "http://api.flattr.com";
    private String version = "0.6";
    private boolean bare = false;
    private boolean automatic = true;
    private boolean https = false;
    private String uid = null;
    private ButtonType type = null;
    private String language = null;
    private String category = null;
    private String prefix = null;
    
    /**
     * Sets the base URL of the Flattr API. Defaults to "http://api.flattr.com". Do not
     * change unless you know what you are doing.
     * 
     * @param baseUrl
     *            New Flattr API url to be used
     */
    public LoaderBuilder baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    /**
     * Sets the API version to be used. Usually there is no need to change the default
     * value, so do not change unless you know what you are doing.
     * 
     * @param version
     *            API version to be used (e.g. "0.6")
     */
    public LoaderBuilder version(String version) {
        this.version = version;
        return this;
    }
    
    /**
     * The builder will return a bare javascript, without an enclosing &lt;script&gt; tag.
     */
    public LoaderBuilder bare() {
        bare = true;
        return this;
    }

    /**
     * The Flattr API will not be initialized automatically, but requires a manual
     * invocation of <code>FlattrLoader.setup()</code>. By default, the API is initialized
     * automatically on onload.
     */
    public LoaderBuilder manual() {
        automatic = false;
        return this;
    }

    /**
     * Use https for loading Flattr resources (javascripts, images etc). By default http
     * is used.
     */
    public LoaderBuilder https() {
        https = true;
        return this;
    }

    /**
     * Sets the default user ID.
     * 
     * @param uid
     *            User ID
     */
    public LoaderBuilder uid(String uid) {
        this.uid = uid;
        return this;
    }

    /**
     * Sets the default {@link User}.
     * 
     * @param user
     *            {@link User}
     */
    public LoaderBuilder user(User user) {
        return uid(user.getId());
    }

    /**
     * Sets the default button type to be used.
     * 
     * @param type
     *            {@link ButtonType} to be used
     */
    public LoaderBuilder button(ButtonType type) {
        this.type = type;
        return this;
    }

    /**
     * Sets the default language. Must be one of the supported Flattr languages.
     * 
     * @param language
     *            Default language (e.g. "en_US")
     */
    public LoaderBuilder language(String language) {
        this.language = language;
        return this;
    }

    /**
     * Sets the default {@link Language}.
     * 
     * @param language
     *            Default {@link Language}
     */
    public LoaderBuilder language(Language language) {
        return language(language.getId());
    }

    /**
     * Sets the default category. Must be one of the supported Flattr categories.
     * 
     * @param category
     *            Default category (e.g. "text")
     */
    public LoaderBuilder category(String category) {
        this.category = category;
        return this;
    }
    
    /**
     * Sets the default {@link Category}.
     * 
     * @param category
     *            Default {@link Category}
     */
    public LoaderBuilder category(Category category) {
        return category(category.getId());
    }
    
    /**
     * Sets a HTML5 key prefix. By default "data-flattr" is used.
     * 
     * @param prefix
     *            HTML5 key prefix. The string must start with "data-"
     */
    public LoaderBuilder prefix(String prefix) {
        if (!prefix.startsWith("data-")) {
            throw new IllegalArgumentException("prefix must start with \"data-\"");
        }
        this.prefix = prefix;
        return this;
    }
    
    /**
     * Builds a loader script of the current setup.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        char separator = '?';
        
        if (!bare) {
            sb.append("<script type=\"text/javascript\">/* <![CDATA[ */\n");
        }
        
        String url = baseUrl;
        if (https) {
            url = url.replaceFirst("^http://", "https://");
        }

        sb.append("(function() {");
        sb.append("var s = document.createElement('script'),");
        sb.append("t = document.getElementsByTagName('script')[0];");
        sb.append("s.type = 'text/javascript';");
        sb.append("s.async = true;");
        sb.append("s.src = '").append(url).append("/js/").append(version).append("/load.js");
        
        if (automatic) {
            sb.append(separator).append("mode=auto");
            separator = '&';
        }
        
        if (uid != null) {
            sb.append(separator).append("uid=").append(uid);
            separator = '&';
        }
        
        if (type != null) {
            sb.append(separator).append("button=");
            switch (type) {
                case COMPACT: sb.append("compact"); break;
                case DEFAULT: sb.append("default"); break;
            }
            separator = '&';
        }
        
        if (language != null) {
            sb.append(separator).append("language=").append(language);
            separator = '&';
        }
        
        if (category != null) {
            sb.append(separator).append("category=").append(category);
            separator = '&';
        }
        
        if (prefix != null) {
            sb.append(separator).append("html5-key-prefix").append(prefix);
            separator = '&';
        }
        
        sb.append("';");
        sb.append("t.parentNode.insertBefore(s, t);");
        sb.append("})();\n");

        if (!bare) {
            sb.append("/* ]]> */</script>");
        }
        
        return sb.toString();
    }
    
}
