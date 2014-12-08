/*
 * flattr4j - A Java library for Flattr
 *
 * Copyright (C) 2014 Richard "Shred" Körber
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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.nio.charset.Charset;

import org.junit.Test;
import org.shredzone.flattr4j.connector.RequestType;

/**
 * Unit tests for {@link FlattrConnection}.
 *
 * @author Richard "Shred" Körber
 */
public class FlattrConnectionTest {

    @Test
    public void base64Test() {
        FlattrConnection conn = new FlattrConnection(RequestType.GET);
        assertThat(conn.base64(""), is(""));
        assertThat(conn.base64("1"), is("MQ=="));
        assertThat(conn.base64("12"), is("MTI="));
        assertThat(conn.base64("123"), is("MTIz"));
        assertThat(conn.base64("foo:bar"), is("Zm9vOmJhcg=="));
        assertThat(conn.base64("barFooX2"), is("YmFyRm9vWDI="));
        assertThat(conn.base64("TeStFoüX4"), is("VGVTdEZvw7xYNA=="));
        assertThat(conn.base64("AlmostDone"), is("QWxtb3N0RG9uZQ=="));
        assertThat(conn.base64("Just4Test#i"), is("SnVzdDRUZXN0I2k="));
        assertThat(conn.base64("FinAlTeSt567"), is("RmluQWxUZVN0NTY3"));
    }

    @Test
    public void charsetTest() {
        Charset utf8 = Charset.forName("utf-8");
        Charset iso_8859_15 = Charset.forName("iso-8859-15");

        FlattrConnection conn = new FlattrConnection(RequestType.GET);
        assertThat(conn.getCharset(null), is(utf8));
        assertThat(conn.getCharset(""), is(utf8));
        assertThat(conn.getCharset("text/html"), is(utf8));
        assertThat(conn.getCharset("text/html; charset=utf-8"), is(utf8));
        assertThat(conn.getCharset("text/html; charset=iso-8859-15"), is(iso_8859_15));
        assertThat(conn.getCharset("text/html; charset=iso-8859-15; foo=yes"), is(iso_8859_15));
        assertThat(conn.getCharset("TEXT/HTML; CHARSET=ISO-8859-15"), is(iso_8859_15));
        assertThat(conn.getCharset("text/html; charset=\"iso-8859-15\""), is(iso_8859_15));
        assertThat(conn.getCharset("text/html ; charset=\"iso-8859-15\" ; foo=yes"), is(iso_8859_15));
        assertThat(conn.getCharset("text/html; charset=UnKnOwN"), is(utf8));
    }

}
