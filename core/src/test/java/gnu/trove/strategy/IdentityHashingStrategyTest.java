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
package gnu.trove.strategy;

import gnu.trove.map.hash.TCustomHashMap;
import junit.framework.TestCase;

import java.util.Map;


/**
 *
 */
public class IdentityHashingStrategyTest extends TestCase {
    public void testInMap() {
        Map<Integer, String> map =
                new TCustomHashMap<Integer, String>(new IdentityHashingStrategy<Integer>());

        Integer first = new Integer(0);
        Integer second = new Integer(0);

        map.put(first, "first");

        assertEquals(1, map.size());
        assertTrue(map.containsKey(first));
        assertFalse(map.containsKey(second));
        assertEquals("first", map.get(first));

        map.put(second, "second");

        assertEquals(2, map.size());
        assertEquals("first", map.get(first));
        assertEquals("second", map.get(second));

        map.remove(first);

        assertEquals(1, map.size());
        assertFalse(map.containsKey(first));
        assertTrue(map.containsKey(second));
    }
}
