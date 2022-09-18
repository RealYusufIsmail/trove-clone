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
package gnu.trove.impl.hash;

import gnu.trove.map.hash.TObjectLongHashMap;
import org.junit.jupiter.api.Test;

/**
 *
 */
public class TObjectHashTest {
    // Test case bug bug ID 3067307
    @Test
    public void testBug3067307() {
        TObjectLongHashMap<String> testHash = new TObjectLongHashMap<String>();
        final int c = 1000;
        for (long i = 1; i < c; i++) {
            final String data = "test-" + i;
            testHash.put(data, i);
            testHash.remove(data);
        }
    }

    // Test case bug bug ID 3067307
    @Test
    public void testBug3067307_noAutoCompact() {
        TObjectLongHashMap<String> testHash = new TObjectLongHashMap<String>();
        testHash.setAutoCompactionFactor(0);
        final int c = 1000;
        for (long i = 1; i < c; i++) {
            final String data = "test-" + i;
            testHash.put(data, i);
            testHash.remove(data);
        }
    }
}
