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
package org.shredzone.flattr4j.impl.xml;

import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Submission;

/**
 * Builds an XML document from a {@link Submission}.
 *
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public final class SubmissionXmlWriter {

    private SubmissionXmlWriter() {
        // Private constructor
    }

    /**
     * Converts a {@link Submission} to its XML representation.
     * 
     * @param submission
     *            {@link Submission} to be converted
     * @return XML document representing that {@link Submission}
     */
    public static String write(Submission submission) throws FlattrException {
        StringBuilder sb = new StringBuilder();
        sb.append("<thing>");
        sb.append("<url>").append(escape(submission.getUrl())).append("</url>");
        sb.append("<title>").append(cdata(submission.getTitle())).append("</title>");
        sb.append("<category>").append(escape(submission.getCategory().getCategoryId())).append("</category>");
        sb.append("<description>").append(cdata(submission.getDescription())).append("</description>");
        sb.append("<language>").append(escape(submission.getLanguage().getLanguageId())).append("</language>");
        sb.append("<hidden>").append(submission.isHidden() ? 1 : 0).append("</hidden>");
        sb.append("<tags>");
        for (String tag : submission.getTags()) {
            sb.append("<tag>").append(escape(tag)).append("</tag>");
        }
        sb.append("</tags>");
        sb.append("</thing>");
        return sb.toString();
    }
    
    /**
     * Simple XML escape for a character sequence.
     * 
     * @param str
     *            String to be escaped
     * @return Escaped string
     */
    private static String escape(String str) {
        return str.replace("&", "&amp;").replace("<", "&lt;").replace("\"", "&quot;");
    }
    
    /**
     * CDATA escape for a character sequence.
     * 
     * @param str
     *            String to be escaped
     * @return Escaped string
     */
    private static String cdata(String str) {
        return "<![CDATA[" + str.replace("]]>", "]]]]><![CDATA[>") + "]]>";
    }

}
