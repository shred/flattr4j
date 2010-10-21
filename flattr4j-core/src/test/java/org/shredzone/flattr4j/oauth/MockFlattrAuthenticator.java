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

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;

/**
 * A mock implementation of {@link FlattrAuthenticator} that is used for unit testing.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision$
 */
public class MockFlattrAuthenticator extends FlattrAuthenticator {

    public MockFlattrAuthenticator(ConsumerKey ck) {
        super(ck);
    }

    @Override
    protected OAuthConsumer createConsumer(ConsumerKey ck) {
        return new MockOAuthConsumer(ck);
    }

    @Override
    protected OAuthProvider createProvider() {
        OAuthProvider provider = new MockOAuthProvider(getRequestTokenUrl(), getAccessTokenUrl(), getAuthorizationUrl());
        provider.setOAuth10a(true);
        return provider;
    }

    @Override
    public String buildScopeString() {
        return super.buildScopeString();
    }

}
