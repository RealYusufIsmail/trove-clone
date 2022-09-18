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

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Static functions for unit tests
 */
public class HashTestKit {
    /**
     * Confirm that the internal FREE counter matches the values in the slots.
     */
    public static void checkFreeSlotCount(THash hash, Object[] slot_keys, Object free_marker) {

        int free_counter = hash._free;

        int count = 0;
        for (Object slot_key : slot_keys) {
            if (slot_key == free_marker)
                count++;
        }

        assertEquals(free_counter, count);
    }
}
