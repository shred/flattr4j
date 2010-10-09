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
package org.shredzone.flattr4j.oauth;

import java.util.EnumSet;
import org.junit.Assert;
import org.junit.Test;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.oauth.MockFlattrAuthenticator;
import org.shredzone.flattr4j.oauth.RequestToken;

/**
 * Unit test of the {@link FlattrAuthenticator} class.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class FlattrAuthenticatorTest {

    @Test
    public void testFetchRequestToken() throws FlattrException {
        ConsumerKey ck = new ConsumerKey("ck-abc", "ck-123");

        FlattrAuthenticator auth = new MockFlattrAuthenticator(ck);
        auth.setScope(EnumSet.of(Scope.READ, Scope.CLICK));
        RequestToken token = auth.fetchRequestToken();

        Assert.assertEquals("req-abc", token.getToken());
        Assert.assertEquals("req-123", token.getSecret());
        Assert.assertEquals("https://api.flattr.com/oauth/request_token?mock=true&access_scope=read%2Cclick", token.getAuthUrl());
    }

    @Test
    public void testFetchAccessToken() throws FlattrException {
        ConsumerKey ck = new ConsumerKey("ck-abc", "ck-123");

        RequestToken token = new RequestToken();
        token.setToken("req-abc");
        token.setSecret("req-123");

        FlattrAuthenticator auth = new MockFlattrAuthenticator(ck);
        AccessToken accesstoken = auth.fetchAccessToken(token, "654321");

        Assert.assertEquals("acc-abc", accesstoken.getToken());
        Assert.assertEquals("acc-123", accesstoken.getSecret());
    }

    @Test
    public void testBuildScopeString() {
        ConsumerKey ck = new ConsumerKey("ck-abc", "ck-123");
        MockFlattrAuthenticator auth = new MockFlattrAuthenticator(ck);

        Assert.assertEquals("read", auth.buildScopeString());

        auth.getScope().add(Scope.CLICK);
        Assert.assertEquals("read,click", auth.buildScopeString());

        auth.getScope().clear();
        Assert.assertEquals("", auth.buildScopeString());

        auth.setScope(EnumSet.allOf(Scope.class));
        Assert.assertEquals("read,extendedread,publish,click", auth.buildScopeString());

        auth.setScope(EnumSet.noneOf(Scope.class));
        Assert.assertEquals("", auth.buildScopeString());

        auth.setScope(EnumSet.of(Scope.PUBLISH, Scope.EXTENDEDREAD));
        Assert.assertEquals("extendedread,publish", auth.buildScopeString());
    }

}
