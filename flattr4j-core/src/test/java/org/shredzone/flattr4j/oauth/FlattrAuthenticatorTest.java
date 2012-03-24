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
package org.shredzone.flattr4j.oauth;

import java.util.EnumSet;

import org.junit.Assert;
import org.junit.Test;
import org.shredzone.flattr4j.exception.FlattrException;

/**
 * Unit test of the {@link FlattrAuthenticator} class.
 *
 * @author Richard "Shred" Körber
 */
public class FlattrAuthenticatorTest {

    @Test
    public void testAuthenticate() throws FlattrException {
        ConsumerKey ck = new ConsumerKey("ck-abc", "ck-123");
        FlattrAuthenticator auth = new FlattrAuthenticator(ck);
        auth.getScope().add(Scope.FLATTR);
        auth.getScope().add(Scope.THING);

        String url = auth.authenticate("myState");
        Assert.assertEquals("https://flattr.com/oauth/authorize?response_type=code&client_id=ck-abc&scope=flattr+thing&state=myState", url);
    }

    @Test
    public void testBuildScopeString() {
        ConsumerKey ck = new ConsumerKey("ck-abc", "ck-123");
        FlattrAuthenticator auth = new FlattrAuthenticator(ck);

        auth.getScope().add(Scope.FLATTR);
        Assert.assertEquals("flattr", auth.buildScopeString());

        auth.getScope().add(Scope.THING);
        Assert.assertEquals("flattr thing", auth.buildScopeString());

        auth.getScope().clear();
        Assert.assertEquals("", auth.buildScopeString());

        auth.setScope(EnumSet.allOf(Scope.class));
        Assert.assertEquals("flattr thing email extendedread", auth.buildScopeString());

        auth.setScope(EnumSet.noneOf(Scope.class));
        Assert.assertEquals("", auth.buildScopeString());
    }

}
