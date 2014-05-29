/*
 * flattr4j - A Java library for Flattr
 *
 * Copyright (C) 2012 Richard "Shred" Körber
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
package org.springframework.social.flattr.connect;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.oauth.AccessToken;
import org.shredzone.flattr4j.oauth.ConsumerKey;
import org.shredzone.flattr4j.oauth.FlattrAuthenticator;
import org.shredzone.flattr4j.oauth.Scope;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Parameters;

/**
 * Unit tests for {@link FlattrOAuth2Operations}.
 *
 * @author Richard "Shred" Körber
 */
public class FlattrOAuth2OperationsTest {
    public static final String CONSUMER_KEY = "consumerKey123";
    public static final String CONSUMER_SECRET = "consumerSecret321";
    public static final String STATE = "state456";
    public static final String ACCESSCODE = "code546";
    public static final String TOKEN = "token654";

    @Test
    public void testBuildAuthenticateUrl() {
        FlattrOAuth2Operations op = createInstance();

        OAuth2Parameters param = new OAuth2Parameters();
        param.setRedirectUri("http://example.com/redirect");
        param.setScope(Scope.FLATTR.name() + " " + Scope.THING.name());
        param.setState(STATE);
        String url = op.buildAuthenticateUrl(GrantType.AUTHORIZATION_CODE, param);

        assertThat(url, is("https://flattr.com/oauth/authorize?response_type=code" +
                        "&client_id=" + CONSUMER_KEY +
                        "&redirect_uri=http%3A%2F%2Fexample.com%2Fredirect" +
                        "&scope=flattr+thing" +
                        "&state=" + STATE));
    }

    @Test
    public void testBuildAuthorizeUrl() {
        FlattrOAuth2Operations op = createInstance();

        OAuth2Parameters param = new OAuth2Parameters();
        param.setRedirectUri("http://example.com/redirect");
        param.setScope("   " + Scope.EMAIL.name() + " ,  " + Scope.THING.name() + " ");
        param.setState(STATE);
        String url = op.buildAuthenticateUrl(GrantType.IMPLICIT_GRANT, param);

        assertThat(url, is("https://flattr.com/oauth/authorize?response_type=token" +
                        "&client_id=" + CONSUMER_KEY +
                        "&redirect_uri=http%3A%2F%2Fexample.com%2Fredirect" +
                        "&scope=thing+email" +
                        "&state=" + STATE));
    }

    @Test
    public void testExchangeForAccess() {
        FlattrOAuth2Operations op = createInstance();

        AccessGrant grant = op.exchangeForAccess(ACCESSCODE, null, null);
        assertThat(grant, allOf(
            hasProperty("accessToken", is(TOKEN)),
            hasProperty("refreshToken", nullValue()),
            hasProperty("expireTime", nullValue())
        ));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRefreshAccess() {
        FlattrOAuth2Operations op = createInstance();
        op.refreshAccess(TOKEN, null, null);
    }

    /**
     * Creates an {@link FlattrOAuth2Operations} instance that uses
     * {@link TestFlattrAuthenticator}.
     */
    public FlattrOAuth2Operations createInstance() {
        ConsumerKey key = new ConsumerKey(CONSUMER_KEY, CONSUMER_SECRET);
        return new FlattrOAuth2Operations(key) {
            @Override
            protected FlattrAuthenticator createFlattrAuthenticator(ConsumerKey key) {
                return new TestFlattrAuthenticator(key);
            }
        };
    }

    /**
     * A {@link FlattrAuthenticator} that does not really connect to Flattr.
     */
    public static class TestFlattrAuthenticator extends FlattrAuthenticator {
        public TestFlattrAuthenticator(ConsumerKey consumerKey) {
            super(consumerKey);
            assertThat(consumerKey.getKey(), is(CONSUMER_KEY));
            assertThat(consumerKey.getSecret(), is(CONSUMER_SECRET));
        }

        @Override
        public AccessToken fetchAccessToken(String code) throws FlattrException {
            assertThat(code, is(ACCESSCODE));
            return new AccessToken(TOKEN);
        }
    }

}
