/*
 * Copyright (c) 2022, Rob Eden, RealYusufIsmail All Rights Reserved.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */ 
package gnu.trove.map.hash;

import gnu.trove.map.TObjectIntMap;
import org.junit.jupiter.api.Test;


import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Test for a number of bugs related to no_entry_value, including 3432402
 */
public class NoEntryValueTest {
    @Test
    public void testAdjustToNoEntry() {
        TObjectIntMap<String> map = new TObjectIntHashMap<>();

        assertEquals(0, map.getNoEntryValue());
        assertEquals(0, map.get("NotInThere"));

        map.put("Value", 1);
        assertEquals(1, map.size());
        assertEquals(1, map.get("Value"));
        assertTrue(map.containsKey("Value"));
        assertTrue(map.containsValue(1));
        assertArrayEquals(new int[] {1}, map.values());

        map.adjustValue("Value", -1);
        assertEquals(1, map.size());
        assertEquals(0, map.get("Value"));
        assertTrue(map.containsKey("Value"));
        assertTrue(map.containsValue(0));
        assertArrayEquals(new int[] {0}, map.values());
    }
}
