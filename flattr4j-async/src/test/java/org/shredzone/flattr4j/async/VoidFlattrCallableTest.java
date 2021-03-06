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
package org.shredzone.flattr4j.async;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.shredzone.flattr4j.FlattrService;

/**
 * Unit tests for {@link VoidFlattrCallable}.
 *
 * @author Richard "Shred" Körber
 */
public class VoidFlattrCallableTest {

    private boolean executed;

    @Test
    public void testExecute() throws Exception {
        final FlattrService mockService = mock(FlattrService.class);

        VoidFlattrCallable callable = new VoidFlattrCallable() {
            @Override
            public void execute(FlattrService service) throws Exception {
                assertThat(service, sameInstance(mockService));
                executed = true;
            }
        };

        executed = false;
        Void result = callable.call(mockService);
        assertThat(executed, is(true));
        assertThat(result, nullValue());
    }

}
