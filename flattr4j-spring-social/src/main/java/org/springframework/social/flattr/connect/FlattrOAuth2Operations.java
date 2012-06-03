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

import static org.springframework.social.flattr.connect.FlattrSocialUtils.socialFlattrException;

import java.util.EnumSet;

import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.oauth.AccessToken;
import org.shredzone.flattr4j.oauth.ConsumerKey;
import org.shredzone.flattr4j.oauth.FlattrAuthenticator;
import org.shredzone.flattr4j.oauth.Scope;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.util.MultiValueMap;

/**
 * An {@link OAuth2Operations} implementation that uses {@link FlattrAuthenticator}
 * instead of Spring Social's generic OAuth2 implementation.
 *
 * @author Richard "Shred" Körber
 * @since 2.4
 */
public class FlattrOAuth2Operations implements OAuth2Operations {

    private final FlattrAuthenticator authenticator;

    /**
     * Creates a new {@link FlattrOAuth2Operations}.
     *
     * @param key
     *            {@link ConsumerKey} to be used
     */
    public FlattrOAuth2Operations(ConsumerKey key) {
        authenticator = createFlattrAuthenticator(key);
    }

    @Override
    public String buildAuthenticateUrl(GrantType grantType, OAuth2Parameters parameters) {
        return buildAuthorizeUrl(grantType, parameters);
    }

    @Override
    public String buildAuthorizeUrl(GrantType grantType, OAuth2Parameters parameters) {
        try {
            if (grantType == GrantType.AUTHORIZATION_CODE) {
                authenticator.setResponseType("code");
            } else if (grantType == GrantType.IMPLICIT_GRANT) {
                authenticator.setResponseType("token");
            }

            if (parameters.getRedirectUri() != null) {
                authenticator.setCallbackUrl(parameters.getRedirectUri());
            }

            if (parameters.getScope() != null) {
                authenticator.setScope(parseScope(parameters.getScope()));
            }

            return authenticator.authenticate(parameters.getState());
        } catch (FlattrException ex) {
            throw socialFlattrException(ex);
        }
    }

    @Override
    public AccessGrant exchangeForAccess(String authorizationCode, String redirectUri, MultiValueMap<String, String> additionalParameters) {
        try {
            AccessToken token = authenticator.fetchAccessToken(authorizationCode);
            return new AccessGrant(token.getToken());
        } catch (FlattrException ex) {
            throw socialFlattrException(ex);
        }
    }

    @Override
    public AccessGrant refreshAccess(String refreshToken, String scope, MultiValueMap<String, String> additionalParameters) {
        throw new UnsupportedOperationException("flattr does not support refresh_token");
    }

    /**
     * Creates the {@link FlattrAuthenticator} to be used. May be overridden by
     * subclasses.
     *
     * @param key
     *            {@link ConsumerKey} to be used
     * @return {@link FlattrAuthenticator} that was created
     */
    protected FlattrAuthenticator createFlattrAuthenticator(ConsumerKey key) {
        return new FlattrAuthenticator(key);
    }

    /**
     * Parses a scope string into Flattr scopes. The scopes are separated with white
     * spaces. If a scope is not known, an exception will be thrown.
     *
     * @param scopes
     *            Scopes, case insensitive, separated by spaces
     * @return Set of {@link Scope}
     */
    private EnumSet<Scope> parseScope(String scopes) {
        EnumSet<Scope> result = EnumSet.noneOf(Scope.class);
        for (String scope : scopes.split("\\s+|[,;]")) {
            String trimmed = scope.trim().toUpperCase();
            if (trimmed.length() > 0) {
                result.add(Scope.valueOf(trimmed));
            }
        }
        return result;
    }

}
