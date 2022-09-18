/*
 * Copyright (C) 2022 RealYusufIsmail
 *
 * This library is free software; you can redistribute it and/or
 *
 * modify it under the terms of the GNU Lesser General Public
 *
 * License as published by the Free Software Foundation; either
 *
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 */ 
package gnu.trove.decorator;

import gnu.trove.TDecorators;
import gnu.trove.list.TIntList;
import junit.framework.TestCase;

/**
 * @author Jim Davies
 */
public class TPimativeListDecoratorTest extends TestCase {
    public void testConstructorWithNull() {
        boolean expectionThrown = false;
        try {
            TDecorators.wrap((TIntList) null);
        } catch (NullPointerException ignored) {
            expectionThrown = true;
        }

        assertTrue("Wrapping a null value should result in an expection being thrown.",
                expectionThrown);
    }
}
