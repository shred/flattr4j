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
package org.shredzone.flattr4j.connector;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.shredzone.flattr4j.exception.MarshalException;

/**
 * Represents the raw Flattr data.
 * <p>
 * Basically, this is a wrapper around {@link JSONObject}, which takes care for the
 * {@link JSONException} and also for serialization of JSON structures.
 *
 * @author Richard "Shred" Körber
 * @version $Revision:$
 */
public class FlattrObject implements Serializable, Externalizable {
    private static final long serialVersionUID = -6640392574244365803L;
    
    private JSONObject data;

    /**
     * Creates a new, empty {@link FlattrObject}.
     */
    public FlattrObject() {
        data = new JSONObject();
    }
    
    /**
     * Creates a {@link FlattrObject} from the given {@link JSONObject}.
     * 
     * @param data
     *          {@link JSONObject} to use. It is not cloned. It's contents may be changed
     *          by this {@link FlattrObject}.
     */
    public FlattrObject(JSONObject data) {
        this.data = data;
    }
    
    /**
     * Creates a {@link FlattrObject} from the given JSON string.
     * 
     * @param json
     *          JSON string to initialize the {@link FlattrObject} with
     */
    public FlattrObject(String json) {
        try {
            this.data = (JSONObject) new JSONTokener(json).nextValue();
        } catch (JSONException ex) {
            throw new MarshalException(ex);
        }
    }

    /**
     * Checks if there is a key.
     * 
     * @param key
     *          Key to check for
     * @return {@code true} if there is such a key (value may still be {@code null}).
     */
    public boolean has(String key) {
        return data.has(key);
    }
    
    /**
     * Gets an Object from the given key.
     * 
     * @param key
     *          Key to read from
     * @return Object that was read
     * @throws MarshalException if there was no such key
     */
    public Object getObject(String key) {
        try {
            return data.get(key);
        } catch (JSONException ex) {
            throw new MarshalException(key, ex);
        }
    }

    /**
     * Gets a String from the given key.
     * 
     * @param key
     *          Key to read from
     * @return String that was read
     * @throws MarshalException if there was no such key
     */
    public String get(String key) {
        try {
            return data.getString(key);
        } catch (JSONException ex) {
            throw new MarshalException(key, ex);
        }
    }
    
    /**
     * Gets a String from the given subKey which is a property of the given key.
     * 
     * @param key
     *          Key of the parent object
     * @param subKey
     *          Key to read from
     * @return String that was read
     * @throws MarshalException if there was no such key or subKey
     */
    public String getSubString(String key, String subKey) {
        try {
            JSONObject obj = data.getJSONObject(key);
            return obj.getString(subKey);
        } catch (JSONException ex) {
            throw new MarshalException(key, ex);
        }
    }
    
    /**
     * Gets a {@link FlattrObject} from the given key.
     * 
     * @param key
     *          Key to read from
     * @return {@link FlattrObject} that was read
     * @throws MarshalException if there was no such key
     */
    public FlattrObject getFlattrObject(String key) {
        try {
            return new FlattrObject(data.getJSONObject(key));
        } catch (JSONException ex) {
            throw new MarshalException(key, ex);
        }
    }

    /**
     * Gets an integer from the given key.
     * 
     * @param key
     *          Key to read from
     * @return integer that was read
     * @throws MarshalException if there was no such key, or if it did not contain the expected type
     */
    public int getInt(String key) {
        try {
            return data.getInt(key);
        } catch (JSONException ex) {
            throw new MarshalException(key, ex);
        }
    }
    
    /**
     * Gets a boolean from the given key.
     * 
     * @param key
     *          Key to read from
     * @return boolean that was read
     * @throws MarshalException if there was no such key, or if it did not contain the expected type
     */
    public boolean getBoolean(String key) {
        try {
            return data.getBoolean(key);
        } catch (JSONException ex) {
            throw new MarshalException(key, ex);
        }
    }

    /**
     * Gets a {@link Date} from the given key.
     * 
     * @param key
     *          Key to read from
     * @return {@link Date} that was read, or {@code null} if no date was set
     * @throws MarshalException if there was no such key, or if it did not contain the expected type
     */
    public Date getDate(String key) {
        try {
            if (data.isNull(key)) {
                return null;
            }
            
            long ts = data.getLong(key);
            return (ts != 0 ? new Date(ts * 1000L) : null);
        } catch (JSONException ex) {
            throw new MarshalException(key, ex);
        }
    }

    /**
     * Gets a collection of String from the given key.
     * 
     * @param key
     *          Key to read from
     * @return Collection of Strings
     * @throws MarshalException if there was no such key, or if it did not contain the expected type
     */
    public List<String> getStrings(String key) {
        try {
            JSONArray array = data.getJSONArray(key);
            List<String> result = new ArrayList<String>(array.length());
            for (int ix = 0; ix < array.length(); ix++) {
                result.add(array.getString(ix));
            }
            return result;
        } catch (JSONException ex) {
            throw new MarshalException(key, ex);
        }
    }

    /**
     * Changes the key and sets it to the given value.
     * 
     * @param key Key to write to
     * @param value Value to be written
     * @throws MarshalException if the key could not be changed
     */
    public void put(String key, Object value) {
        try {
            data.put(key, value);
        } catch (JSONException ex) {
            throw new MarshalException(key, ex);
        }
    }
    
    /**
     * Puts a collection of strings as array object to the given key.
     * 
     * @param key Key to write to
     * @param value Collection of Strings to write
     * @throws MarshalException if the key could not be changed
     */
    public void putStrings(String key, Collection<String> value) {
        try {
            JSONArray array = new JSONArray();
            if (value != null) {
                for (String tag : value) {
                    array.put(tag);
                }
            }
            data.put(key, array);
        } catch (JSONException ex) {
            throw new MarshalException(key, ex);
        }
    }

    /**
     * Returns the current state of the {@link FlattrObject} as JSON string.
     * 
     * @return JSON representation of the current state
     */
    @Override
    public String toString() {
        return data.toString();
    }
    
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(data.toString());
    }
    
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        try {
            data = new JSONObject(in.readUTF());
        } catch (JSONException ex) {
            throw new IOException("JSON deserialization failed: " + ex.getMessage());
        }
    }

}
