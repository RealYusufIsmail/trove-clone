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
package gnu.trove;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 *
 */
public class TCollectionsTest {

    @Test
    public void testUnmodifiableList() {
        final TIntArrayList one = new TIntArrayList(new int[] {1, 2, 3, 4});
        final TIntArrayList two = new TIntArrayList(new int[] {1, 2, 3, 4});
        TIntList uOne = TCollections.unmodifiableList(one);
        TIntList uTwo = TCollections.unmodifiableList(two);

        assertEquals(one, two);
        assertEquals(uOne, uTwo);
    }


    @Test
    public void testUnmodifiableSet() {
        final TIntSet one = new TIntHashSet(new int[] {1, 2, 3, 4});
        final TIntSet two = new TIntHashSet(new int[] {1, 2, 3, 4});
        TIntSet uOne = TCollections.unmodifiableSet(one);
        TIntSet uTwo = TCollections.unmodifiableSet(two);

        assertEquals(one, two);
        assertEquals(uOne, uTwo);
    }


    @Test
    public void testUnmodifiableMap() {
        final TIntObjectMap<Integer> one = new TIntObjectHashMap<Integer>();
        one.put(0, Integer.valueOf(0));
        one.put(1, Integer.valueOf(1));
        one.put(2, Integer.valueOf(2));
        one.put(3, Integer.valueOf(3));
        final TIntObjectMap<Integer> two = new TIntObjectHashMap<Integer>(one);
        TIntObjectMap<Integer> uOne = TCollections.unmodifiableMap(one);
        TIntObjectMap<Integer> uTwo = TCollections.unmodifiableMap(two);

        assertEquals(one, two);
        assertEquals(uOne, uTwo);
    }
}
