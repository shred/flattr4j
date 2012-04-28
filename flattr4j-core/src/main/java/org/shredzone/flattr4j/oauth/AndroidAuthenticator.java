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
package org.shredzone.flattr4j.oauth;

import org.shredzone.flattr4j.exception.FlattrException;

import android.content.Intent;
import android.net.Uri;

/**
 * An extension of {@link FlattrAuthenticator} that helps through the authentication
 * process on Android devices.
 *
 * @see <a href="http://www.shredzone.org/projects/flattr4j/wiki/OAuth_Android">How to
 *      authenticate with flattr4j on Android</a>
 * @author Richard "Shred" Körber
 * @since 2.3
 */
public class AndroidAuthenticator extends FlattrAuthenticator {

    /**
     * Constructs a new instance with the given {@link ConsumerKey}.
     *
     * @param host
     *            Host name that is configured in the manifest
     * @param consumerKey
     *            {@link ConsumerKey}
     */
    public AndroidAuthenticator(String host, ConsumerKey consumerKey) {
        super(consumerKey);
        setCallbackUrl("flattr4j://" + host + "/authenticate");
    }

    /**
     * Constructs a new instance with the given consumer key and secret.
     *
     * @param host
     *            Host name that is configured in the manifest
     * @param key
     *            Consumer key
     * @param secret
     *            Consumer secret
     */
    public AndroidAuthenticator(String host, String key, String secret) {
        this(host, new ConsumerKey(key, secret));
    }

    /**
     * Creates an {@link Intent} for forwarding the user to the Flattr web page for
     * authentication.
     * <p>
     * When the returned activity is started, a browser is opened. It shows a Flattr web
     * page asking the user to authenticate and grant the requested scopes for your
     * application.
     * <p>
     * When the authentication completes successfully, your activity is resumed with your
     * callback URL passed in.
     *
     * @return Created {@link Intent}
     */
    public Intent createAuthenticateIntent() throws FlattrException {
        return createAuthenticateIntent(null);
    }

    /**
     * Creates an {@link Intent} for forwarding the user to the Flattr web page for
     * authentication.
     * <p>
     * When the returned activity is started, a browser is opened. It shows a Flattr web
     * page asking the user to authenticate and grant the requested scopes for your
     * application.
     * <p>
     * When the authentication completes successfully, your activity is resumed with your
     * callback URL passed in.
     *
     * @param state
     *            A value that is passed to the callback URL, to maintain state and
     *            reidentify the user between request and callback. Optional, may be
     *            {@code null}.
     * @return Created {@link Intent}
     */
    public Intent createAuthenticateIntent(String state) throws FlattrException {
        String url = super.authenticate(state);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        return intent;
    }

    /**
     * When the authentication was completed, your activity is resumed. The callback URL
     * carrying the authentication code is passed in with the {@link Intent}.
     *
     * @param uri
     *            {@link Uri} that was passed in with the the {@link Intent}, containing
     *            the callback URL from Flattr. It is safe to pass {@code null} here.
     * @return {@link AccessToken}, or {@code null} if the {@link Uri} did not provide a
     *         valid code.
     */
    public AccessToken fetchAccessToken(Uri uri) throws FlattrException {
        if (uri != null) {
            String code = uri.getQueryParameter("code");
            if (code != null) {
                return fetchAccessToken(code);
            }
        }

        return null;
    }

    /**
     * Returns the state passed in with the {@link Intent}. The state was set at
     * {@link #createAuthenticateIntent(String)}.
     *
     * @param uri
     *            {@link Uri} that was passed in with the the {@link Intent}, containing
     *            the callback URL from Flattr. It is safe to pass {@code null} here.
     * @return state, or {@code null} if no state was found
     */
    public String getState(Uri uri) {
        return (uri != null ? uri.getQueryParameter("state") : null);
    }

}
