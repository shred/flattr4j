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

import java.io.Serializable;

/**
 * A request token.
 *
 * @author Richard "Shred" Körber
 * @version $Revision: 605 $
 * @deprecated Not used in OAuth2. Do not use in new code!
 */
@Deprecated
public class RequestToken implements Serializable {
    private static final long serialVersionUID = 4364281809783036307L;
    
    private String token;
    private String secret;
    private String authUrl;
    
    public String getToken()            { return token; }
    public void setToken(String token)  { this.token = token; }
    
    public String getSecret()           { return secret; }
    public void setSecret(String secret) { this.secret = secret; }

    /**
     * User needs to visit this Flattr url in order to complete authentication.
     */
    public String getAuthUrl()          { return authUrl; }
    public void setAuthUrl(String authUrl) { this.authUrl = authUrl; }

}