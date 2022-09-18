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
package gnu.trove.decorator;

import gnu.trove.TDecorators;
import gnu.trove.map.TIntIntMap;
import gnu.trove.map.TIntLongMap;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TIntLongHashMap;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Test the primitive key/primitive value map decorators
 *
 * @author Eric D. Friedman
 * @author Robert D. Eden
 * @author Jeff Randall
 * @author Yusuf A. Ismail
 */
public class TPrimitivePrimitiveMapDecoratorTest {

    private final int KEY_ONE = 100;
    private final int KEY_TWO = 101;

    @Test
    public void testConstructorWithNull() {
        boolean expectionThrown = false;
        try {
            TDecorators.wrap((TIntLongMap) null);
        } catch (NullPointerException ignored) {
            expectionThrown = true;
        }

        assertTrue(expectionThrown,
                "Wrapping a null value should result in an exception being thrown.");
    }


    @Test
    public void testConstructors() {

        int[] keys = {1138, 42, 86, 99, 101};
        long[] vals = {1138, 42, 86, 99, 101};

        TIntLongMap raw_map = new TIntLongHashMap();
        for (int i = 0; i < keys.length; i++) {
            raw_map.put(keys[i], vals[i]);
        }
        Map<Integer, Long> map = TDecorators.wrap(raw_map);
        assertEquals(keys.length, map.size());

        TIntLongMap raw_capacity = new TIntLongHashMap(20);
        for (int i = 0; i < keys.length; i++) {
            raw_capacity.put(keys[i], vals[i]);
        }
        Map<Integer, Long> capacity = TDecorators.wrap(raw_capacity);
        assertEquals(keys.length, capacity.size());

        TIntLongMap raw_cap_and_factor = new TIntLongHashMap(20, 0.75f);
        for (int i = 0; i < keys.length; i++) {
            raw_cap_and_factor.put(keys[i], vals[i]);
        }
        Map<Integer, Long> cap_and_factor = TDecorators.wrap(raw_cap_and_factor);
        assertEquals(keys.length, cap_and_factor.size());

        TIntLongMap raw_fully_specified =
                new TIntLongHashMap(20, 0.5f, Integer.MIN_VALUE, Long.MIN_VALUE);
        for (int i = 0; i < keys.length; i++) {
            raw_fully_specified.put(keys[i], vals[i]);
        }
        Map<Integer, Long> fully_specified = TDecorators.wrap(raw_fully_specified);
        assertEquals(keys.length, fully_specified.size());

        TIntLongMap raw_copy = new TIntLongHashMap(raw_map);
        Map<Integer, Long> copy = TDecorators.wrap(raw_copy);
        assertEquals(keys.length, fully_specified.size());

        TIntLongMap raw_arrays = new TIntLongHashMap(keys, vals);
        Map<Integer, Long> arrays = TDecorators.wrap(raw_arrays);
        assertEquals(keys.length, arrays.size());


        // Equals in all combinations is paranoid.. but..
        assertEquals(map, map);
        assertEquals(map, capacity);
        assertEquals(map, cap_and_factor);
        assertEquals(map, fully_specified);
        assertEquals(map, copy);
        assertEquals(map, arrays);
        assertEquals(capacity, map);
        assertEquals(capacity, capacity);
        assertEquals(capacity, cap_and_factor);
        assertEquals(capacity, fully_specified);
        assertEquals(capacity, copy);
        assertEquals(capacity, arrays);
        assertEquals(cap_and_factor, map);
        assertEquals(cap_and_factor, capacity);
        assertEquals(cap_and_factor, cap_and_factor);
        assertEquals(cap_and_factor, fully_specified);
        assertEquals(cap_and_factor, copy);
        assertEquals(cap_and_factor, arrays);
        assertEquals(fully_specified, map);
        assertEquals(fully_specified, capacity);
        assertEquals(fully_specified, cap_and_factor);
        assertEquals(fully_specified, fully_specified);
        assertEquals(fully_specified, copy);
        assertEquals(fully_specified, arrays);
        assertEquals(copy, map);
        assertEquals(copy, capacity);
        assertEquals(copy, cap_and_factor);
        assertEquals(copy, fully_specified);
        assertEquals(copy, copy);
        assertEquals(copy, arrays);
        assertEquals(arrays, map);
        assertEquals(arrays, capacity);
        assertEquals(arrays, cap_and_factor);
        assertEquals(arrays, fully_specified);
        assertEquals(arrays, copy);
        assertEquals(arrays, arrays);

        assertSame(raw_map, ((TIntLongMapDecorator) map).getMap());
    }


    @Test
    public void testGet() {
        int element_count = 20;
        int[] keys = new int[element_count];
        Long[] vals = new Long[element_count];

        TIntLongMap raw_map = new TIntLongHashMap();
        for (int i = 0; i < element_count; i++) {
            keys[i] = i + 1;
            vals[i] = Long.valueOf(i + 1);
            raw_map.put(keys[i], vals[i]);
        }
        Map<Integer, Long> map = TDecorators.wrap(raw_map);

        assertEquals(vals[10], map.get(Integer.valueOf(keys[10])));
        assertNull(map.get(Integer.valueOf(1138)));

        Integer key = Integer.valueOf(1138);
        map.put(key, null);
        assertTrue(map.containsKey(key));
        assertNull(map.get(key));

        Long long_key = Long.valueOf(1138);
        // noinspection SuspiciousMethodCalls
        assertNull(map.get(long_key));

        Long null_value = Long.valueOf(747);
        map.put(null, null_value);
        assertEquals(null_value, map.get(null));
    }



    @Test /** Be sure that size is large enough to force a resize or two. */
    public void testRehash() {
        int size = 1000;
        int[] keys = new int[size];
        long[] vals = new long[size];
        for (int i = 0; i < size; i++) {
            keys[i] = i + 1;
            vals[i] = keys[i] * 2;
        }

        TIntLongMap raw_map = new TIntLongHashMap();
        for (int i = 0; i < keys.length; i++) {
            raw_map.put(keys[i], vals[i]);
        }
        Map<Integer, Long> map = TDecorators.wrap(raw_map);
        assertEquals(keys.length, map.size());
        for (int i = 0; i < keys.length; i++) {
            Integer key = keys[i];
            Long val = vals[i];
            assertEquals(val, map.get(key), "got incorrect value for index " + i + ", map: " + map);
        }
    }


    @Test
    public void testPutAll() {
        int[] keys = {1138, 42, 86, 99, 101};
        long[] vals = {1138, 42, 86, 99, 101};

        TIntLongMap raw_map = new TIntLongHashMap();
        for (int i = 0; i < keys.length; i++) {
            raw_map.put(keys[i], vals[i] * 2);
        }
        Map<Integer, Long> map = TDecorators.wrap(raw_map);
        assertEquals(keys.length, map.size());

        TIntLongMap target = new TIntLongHashMap();
        target.put(1, 2);
        assertEquals(1, target.size());

        target.putAll(map);
        assertEquals(keys.length + 1, target.size());
        for (int i = 0; i < keys.length; i++) {
            assertEquals(vals[i] * 2, target.get(keys[i]));
        }
        assertEquals(2, target.get(1));


        // java.util.Map source
        Map<Integer, Long> java_map = new HashMap<Integer, Long>();
        for (int i = 0; i < keys.length; i++) {
            java_map.put(keys[i], vals[i] * 2);
        }

        // fresh TIntLongMap
        target = new TIntLongHashMap();
        target.put(1, 2);
        assertEquals(1, target.size());

        target.putAll(java_map);
        assertEquals(keys.length + 1, target.size(), "map size is incorrect: " + keys.length
                + ", source: " + java_map + ", target: " + target);
        for (int i = 0; i < keys.length; i++) {
            assertEquals(vals[i] * 2, target.get(keys[i]));
        }
        assertEquals(2, target.get(1));
    }


    @Test
    public void testClear() {
        int[] keys = {1138, 42, 86, 99, 101};
        long[] vals = {1138, 42, 86, 99, 101};

        TIntLongMap raw_map = new TIntLongHashMap();
        for (int i = 0; i < keys.length; i++) {
            raw_map.put(keys[i], vals[i] * 2);
        }
        Map<Integer, Long> map = TDecorators.wrap(raw_map);
        assertEquals(keys.length, map.size());

        map.clear();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());

        TIntLongMap raw_empty = new TIntLongHashMap();
        Map<Integer, Long> empty = TDecorators.wrap(raw_empty);
        assertEquals(empty, map);
    }


    @Test
    public void testRemove() {
        int[] keys = {1138, 42, 86, 99, 101, 727, 117};
        Long[] vals = new Long[keys.length];

        TIntLongMap raw_map = new TIntLongHashMap();
        for (int i = 0; i < keys.length; i++) {
            vals[i] = (long) (keys[i] * 2);
            raw_map.put(keys[i], vals[i]);
        }
        Map<Integer, Long> map = TDecorators.wrap(raw_map);

        assertEquals(keys.length, map.size());
        for (int i = 0; i < keys.length; i++) {
            assertEquals(vals[i], map.get(keys[i]));
        }
        assertEquals(vals[0], map.remove(keys[0]));
        assertEquals(vals[3], map.remove(keys[3]));
        assertNull(map.remove(keys[0]));
        assertEquals(vals[5], map.remove(keys[5]));
        assertNull(map.remove(11010110));

        assertNull(map.get(1138));
        assertNull(map.get(1138));
        assertNull(map.get(null));

        Long null_value = 2112L;
        map.put(null, null_value);
        assertEquals(null_value.longValue(), raw_map.get(raw_map.getNoEntryKey()));
        assertTrue(map.containsKey(null));
        Long value = map.get(null);
        assertEquals(null_value, value, "value: " + value);
        assertEquals(null_value, map.remove(null));
        assertFalse(map.containsKey(null));

        // noinspection SuspiciousMethodCalls
        assertNull(map.remove(1138L));
    }


    @Test
    public void testKeySetMisc() {
        int[] keys = {1138, 42, 86, 99, 101, 727, 117};
        long[] vals = new long[keys.length];

        TIntLongMap raw_map = new TIntLongHashMap();
        for (int i = 0; i < keys.length; i++) {
            vals[i] = keys[i] * 2;
            raw_map.put(keys[i], vals[i]);
        }
        Map<Integer, Long> map = TDecorators.wrap(raw_map);

        Set<Integer> set = map.keySet();
        Integer[] sorted_keys = new Integer[keys.length];
        for (int i = 0; i < keys.length; i++) {
            sorted_keys[i] = keys[i];
        }
        Arrays.sort(sorted_keys);
        Integer[] setarray = set.toArray(new Integer[set.size()]);
        Arrays.sort(setarray);
        assertArrayEquals(sorted_keys, setarray, "expected: " + Arrays.toString(sorted_keys)
                + ", was: " + Arrays.toString(setarray));

        // noinspection ToArrayCallWithZeroLengthArrayArgument
        setarray = set.toArray(new Integer[0]);
        Arrays.sort(setarray);
        assertArrayEquals(sorted_keys, setarray, "expected: " + Arrays.toString(sorted_keys)
                + ", was: " + Arrays.toString(setarray));

        assertFalse(set.remove(1), "remove of element not in set succeeded: " + set);
        assertEquals(keys.length, set.size());
        assertEquals(keys.length, map.size());

        assertTrue(set.remove(42), "remove of element in set failed: " + set);
        assertEquals(keys.length - 1, set.size());
        assertEquals(keys.length - 1, map.size());

        try {
            set.add(42);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // Expected
        }
    }


    @Test
    public void testKeySetContainsAll() {
        int[] keys = {1138, 42, 86, 99, 101, 727, 117};
        long[] vals = new long[keys.length];

        TIntLongMap raw_map = new TIntLongHashMap();
        for (int i = 0; i < keys.length; i++) {
            vals[i] = keys[i] * 2;
            raw_map.put(keys[i], vals[i]);
        }
        Map<Integer, Long> map = TDecorators.wrap(raw_map);

        Set<Integer> set = map.keySet();
        assertEquals(map.size(), set.size());
        assertFalse(set.isEmpty());

        // test with a java.util.Map
        Set<Number> java_set = new HashSet<>();
        for (int key : keys) {
            java_set.add(key);
        }
        assertTrue(set.containsAll(java_set));
        java_set.add(12);
        assertFalse(set.containsAll(java_set));
        java_set.remove(12);
        assertTrue(set.containsAll(java_set));
        java_set.add(12L);
        assertFalse(set.containsAll(java_set));
    }


    @Test
    public void testKeySetAddAll() {
        int[] keys = {1138, 42, 86, 99, 101, 727, 117};
        long[] vals = new long[keys.length];

        TIntLongMap raw_map = new TIntLongHashMap();
        for (int i = 0; i < keys.length; i++) {
            vals[i] = keys[i] * 2;
            raw_map.put(keys[i], vals[i]);
        }
        Map<Integer, Long> map = TDecorators.wrap(raw_map);

        Set<Integer> set = map.keySet();
        assertEquals(map.size(), set.size());
        assertFalse(set.isEmpty());

        // test with a java.util.Map
        Set<Integer> java_set = new HashSet<Integer>();
        for (int key : keys) {
            java_set.add(key);
        }

        try {
            set.addAll(java_set);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // Expected
        }

        try {
            set.addAll(set);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // Expected
        }
    }



    @Test
    public void testKeySetRetainAllCollection() {
        int[] keys = {1138, 42, 86, 99, 101, 727, 117};
        long[] vals = new long[keys.length];

        TIntLongMap raw_map = new TIntLongHashMap();
        for (int i = 0; i < keys.length; i++) {
            vals[i] = keys[i] * 2;
            raw_map.put(keys[i], vals[i]);
        }
        Map<Integer, Long> map = TDecorators.wrap(raw_map);

        Set<Integer> set = map.keySet();
        assertEquals(map.size(), set.size());
        assertFalse(set.isEmpty());

        // test with a java.util.Map
        Set<Number> java_set = new HashSet<Number>();
        for (int key : keys) {
            java_set.add(key);
        }
        assertFalse(set.retainAll(java_set));
        assertEquals(keys.length, set.size());
        assertEquals(keys.length, map.size());
        for (int key : keys) {
            assertTrue(set.contains(key));
            assertTrue(map.containsKey(key));
        }
        java_set.remove(42);
        assertTrue(set.retainAll(java_set),
                "set should have been modified: " + set + ", java: " + java_set);
        assertEquals(keys.length - 1, set.size());
        assertEquals(keys.length - 1, map.size());

        // noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] != 42) {
                assertTrue(set.contains(keys[i]));
                assertTrue(map.containsKey(keys[i]));
            } else {
                assertFalse(set.contains(keys[i]));
                assertFalse(map.containsKey(keys[i]));
            }
        }
    }


    @Test
    public void testKeySetRemoveAllCollection() {
        int[] keys = {1138, 42, 86, 99, 101, 727, 117};
        long[] vals = new long[keys.length];

        TIntLongMap raw_map = new TIntLongHashMap();
        for (int i = 0; i < keys.length; i++) {
            vals[i] = keys[i] * 2;
            raw_map.put(keys[i], vals[i]);
        }
        Map<Integer, Long> map = TDecorators.wrap(raw_map);

        Set<Integer> set = map.keySet();
        assertEquals(map.size(), set.size());
        assertFalse(set.isEmpty());

        // test with a java.util.Map
        Set<Number> java_set = new HashSet<Number>();
        assertFalse(set.removeAll(java_set));
        assertEquals(keys.length, set.size());
        assertEquals(keys.length, map.size());
        for (int key : keys) {
            assertTrue(set.contains(key));
            assertTrue(map.containsKey(key));
        }

        for (int key : keys) {
            java_set.add(key);
        }
        java_set.remove(42);
        assertTrue(set.removeAll(java_set),
                "set should have been modified: " + set + ", java: " + java_set);
        assertEquals(1, set.size(), "set: " + set);
        assertEquals(1, map.size(), "set: " + set);

        // noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] == 42) {
                assertTrue(set.contains(keys[i]));
                assertTrue(map.containsKey(keys[i]));
            } else {
                assertFalse(set.contains(keys[i]));
                assertFalse(map.containsKey(keys[i]));
            }
        }
    }


    @Test
    public void testKeySetEquals() {
        int[] keys = {1138, 42, 86, 99, 101, 727, 117};
        Integer[] integer_keys = new Integer[keys.length];
        long[] vals = new long[keys.length];

        TIntLongMap raw_map = new TIntLongHashMap();
        for (int i = 0; i < keys.length; i++) {
            integer_keys[i] = keys[i];
            vals[i] = keys[i] * 2;
            raw_map.put(keys[i], vals[i]);
        }
        Map<Integer, Long> map = TDecorators.wrap(raw_map);

        Set<Integer> set = map.keySet();
        assertEquals(map.size(), set.size());
        assertFalse(set.isEmpty());

        Set<Integer> other = new HashSet<>(Arrays.asList(integer_keys));

        assertEquals(set, other, "sets incorrectly not equal: " + set + ", " + other);

        Integer[] mismatched = {72, 49, 53, 1024, 999};
        Set<Integer> unequal = new HashSet<Integer>(Arrays.asList(mismatched));

        assertNotEquals(set, unequal, "sets incorrectly equal: " + set + ", " + unequal);

        // Change length, different code branch
        unequal.add(1);
        assertFalse(set.equals(unequal), "sets incorrectly equal: " + set + ", " + unequal);

        assertFalse(set.equals(new Object()), "set incorrectly equals a random object");
    }


    @Test
    public void testKeySetHashCode() {
        int[] keys = {1138, 42, 86, 99, 101, 727, 117};
        Integer[] integer_keys = new Integer[keys.length];
        long[] vals = new long[keys.length];

        TIntLongMap raw_map = new TIntLongHashMap();
        for (int i = 0; i < keys.length; i++) {
            integer_keys[i] = keys[i];
            vals[i] = keys[i] * 2;
            raw_map.put(keys[i], vals[i]);
        }
        Map<Integer, Long> map = TDecorators.wrap(raw_map);

        Set<Integer> set = map.keySet();
        assertEquals(map.size(), set.size());
        assertFalse(set.isEmpty());


        Set<Integer> other = new HashSet<Integer>(Arrays.asList(integer_keys));

        assertEquals(set.hashCode(), other.hashCode(),
                "hashcodes incorrectly not equal: " + set + ", " + other);
    }


    @Test
    public void testKeySetIterator() {
        int[] keys = {1138, 42, 86, 99, 101, 727, 117};
        Integer[] integer_keys = new Integer[keys.length];
        long[] vals = new long[keys.length];

        TIntLongMap raw_map = new TIntLongHashMap();
        for (int i = 0; i < keys.length; i++) {
            integer_keys[i] = keys[i];
            vals[i] = keys[i] * 2;
            raw_map.put(keys[i], vals[i]);
        }
        Map<Integer, Long> map = TDecorators.wrap(raw_map);

        List<Integer> list = Arrays.asList(integer_keys);
        Set<Integer> set = map.keySet();

        // test basic iterator function.
        Iterator<Integer> iter = set.iterator();
        while (iter.hasNext()) {
            int key = iter.next();
            assertTrue(list.contains(key),
                    "key set should only contain keys: " + key + ", set; " + set);
        }

        assertFalse(iter.hasNext());
        try {
            iter.next();
            fail("Expect NoSuchElementException");
        } catch (NoSuchElementException ex) {
            // Expected.
        }

        // Start over with new iterator -- test iter.remove()
        iter = set.iterator();
        while (iter.hasNext()) {
            int key = iter.next();
            assertTrue(list.contains(key),
                    "key set should only contain keys: " + key + ", set; " + set);
            if (key == keys[3]) {
                iter.remove();
                assertFalse(set.contains(key),
                        "set contains removed element: " + key + ", set: " + set);
            }
        }
        assertEquals(map.size(), set.size());
        assertEquals(keys.length - 1, map.size());
        assertNull(map.get(keys[3]));
    }



    @Test
    @SuppressWarnings({"ToArrayCallWithZeroLengthArrayArgument"})
    public void testKeys() {
        TIntLongMap raw_map = new TIntLongHashMap();
        Map<Integer, Long> map = TDecorators.wrap(raw_map);

        map.put(KEY_ONE, 10L);
        map.put(KEY_TWO, 20L);

        assertEquals(2, map.size());

        Integer[] keys = map.keySet().toArray(new Integer[map.size()]);
        assertEquals(2, keys.length);
        List<Integer> keys_list = Arrays.asList(keys);

        assertTrue(keys_list.contains(KEY_ONE));
        assertTrue(keys_list.contains(KEY_TWO));

        // noinspection ToArrayCallWithZeroLengthArrayArgument
        Integer[] keys2 = map.keySet().toArray(new Integer[0]);
        assertEquals(2, keys2.length);
        List<Integer> keys_list2 = Arrays.asList(keys2);

        assertTrue(keys_list2.contains(KEY_ONE));
        assertTrue(keys_list2.contains(KEY_TWO));

        int element_count = 20;
        raw_map = new TIntLongHashMap(element_count, 0.5f, Integer.MIN_VALUE, Long.MIN_VALUE);
        map = TDecorators.wrap(raw_map);
        for (int i = 0; i < element_count; i++) {
            map.put(i, (long) (i * i));
        }
        assertEquals(element_count, map.size());
        keys = map.keySet().toArray(new Integer[0]);
        Arrays.sort(keys);
        assertEquals(element_count, keys.length);
        for (int i = 0; i < element_count; i++) {
            assertEquals(Integer.valueOf(i), keys[i], "expected: " + i + " got: " + keys[i]
                    + ", i: " + i + ", keys: " + Arrays.toString(keys));
            assertEquals(Long.valueOf(i * i), map.get(keys[i]));
        }
    }


    @Test
    public void testValueCollectionMisc() {
        int[] keys = {1138, 42, 86, 99, 101, 727, 117};
        Long[] vals = new Long[keys.length];

        TIntLongMap raw_map = new TIntLongHashMap();
        for (int i = 0; i < keys.length; i++) {
            vals[i] = Long.valueOf(keys[i] * 2);
            raw_map.put(keys[i], vals[i]);
        }
        Map<Integer, Long> map = TDecorators.wrap(raw_map);

        Collection<Long> values = map.values();
        Long[] sorted_values = new Long[keys.length];
        System.arraycopy(vals, 0, sorted_values, 0, keys.length);
        Arrays.sort(sorted_values);
        Long[] setarray = values.toArray(new Long[values.size()]);
        Arrays.sort(setarray);
        assertArrayEquals(sorted_values, setarray, "expected: " + Arrays.toString(sorted_values)
                + ", was: " + Arrays.toString(setarray));

        setarray = values.toArray(new Long[values.size()]);
        Arrays.sort(setarray);
        assertArrayEquals(sorted_values, setarray, "expected: " + Arrays.toString(sorted_values)
                + ", was: " + Arrays.toString(setarray));

        assertFalse(values.remove(Long.valueOf(1)),
                "remove of element not in collection succeeded: " + values);
        assertEquals(keys.length, values.size());
        assertEquals(keys.length, map.size());

        assertTrue(values.remove(Long.valueOf(42 * 2)),
                "remove of element in collection failed: " + values);
        assertEquals(keys.length - 1, values.size());
        assertEquals(keys.length - 1, map.size());

        try {
            values.add(Long.valueOf(42));
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // Expected
        }
    }

    @Test
    public void testValueCollectionContainsAll() {
        int[] keys = {1138, 42, 86, 99, 101, 727, 117};
        long[] vals = new long[keys.length];

        TIntLongMap raw_map = new TIntLongHashMap();
        for (int i = 0; i < keys.length; i++) {
            vals[i] = keys[i] * 2;
            raw_map.put(keys[i], vals[i]);
        }
        Map<Integer, Long> map = TDecorators.wrap(raw_map);

        Collection<Long> values = map.values();
        assertEquals(map.size(), values.size());
        assertFalse(values.isEmpty());

        // test with a java.util.Map
        Set<Number> java_set = new HashSet<Number>();
        for (long val : vals) {
            java_set.add(Long.valueOf(val));
        }
        assertTrue(values.containsAll(java_set));
        java_set.add(12);
        assertFalse(values.containsAll(java_set));
        java_set.remove(12);
        assertTrue(values.containsAll(java_set));
        java_set.add(12L);
        assertFalse(values.containsAll(java_set));
    }


    @Test
    public void testValueCollectionAddAll() {
        int[] keys = {1138, 42, 86, 99, 101, 727, 117};
        long[] vals = new long[keys.length];

        TIntLongMap raw_map = new TIntLongHashMap();
        for (int i = 0; i < keys.length; i++) {
            vals[i] = keys[i] * 2;
            raw_map.put(keys[i], vals[i]);
        }
        Map<Integer, Long> map = TDecorators.wrap(raw_map);

        Collection<Long> values = map.values();
        assertEquals(map.size(), values.size());
        assertFalse(values.isEmpty());

        // test with a java.util.Map
        Set<Long> java_set = new HashSet<Long>();
        for (long val : vals) {
            java_set.add(Long.valueOf(val));
        }

        try {
            values.addAll(java_set);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // Expected
        }

        try {
            values.addAll(values);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // Expected
        }
    }

    @Test
    public void testValueCollectionRetainAllCollection() {
        int[] keys = {1138, 42, 86, 99, 101, 727, 117};
        long[] vals = new long[keys.length];

        TIntLongMap raw_map = new TIntLongHashMap();
        for (int i = 0; i < keys.length; i++) {
            vals[i] = keys[i] * 2;
            raw_map.put(keys[i], vals[i]);
        }
        Map<Integer, Long> map = TDecorators.wrap(raw_map);

        Collection<Long> values = map.values();
        assertEquals(map.size(), values.size());
        assertFalse(values.isEmpty());

        // test with a java.util.Map
        Set<Number> java_set = new HashSet<Number>();
        for (long val : vals) {
            java_set.add(val);
        }
        assertFalse(values.retainAll(java_set));
        assertEquals(keys.length, values.size());
        assertEquals(keys.length, map.size());
        for (int i = 0; i < keys.length; i++) {
            assertTrue(values.contains(vals[i]));
            assertTrue(map.containsValue(vals[i]));
        }
        java_set.remove((long) (42 * 2));
        assertTrue(values.retainAll(java_set),
                "collection should have been modified: " + values + "\njava: " + java_set);
        assertEquals(keys.length - 1, values.size());
        assertEquals(keys.length - 1, map.size());
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] != 42) {
                assertTrue(values.contains(vals[i]));
                assertTrue(map.containsValue(vals[i]));
            } else {
                assertFalse(values.contains(vals[i]));
                assertFalse(map.containsValue(vals[i]));
            }
        }
    }


    @Test
    public void testValueCollectionRemoveAllCollection() {
        int[] keys = {1138, 42, 86, 99, 101, 727, 117};
        long[] vals = new long[keys.length];

        TIntLongMap raw_map = new TIntLongHashMap();
        for (int i = 0; i < keys.length; i++) {
            vals[i] = keys[i] * 2;
            raw_map.put(keys[i], vals[i]);
        }
        Map<Integer, Long> map = TDecorators.wrap(raw_map);

        Collection<Long> values = map.values();
        assertEquals(map.size(), values.size());
        assertFalse(values.isEmpty());

        // test with a java.util.Map
        Set<Number> java_set = new HashSet<Number>();
        assertFalse(values.removeAll(java_set));
        assertEquals(vals.length, values.size());
        assertEquals(vals.length, map.size());
        for (int i = 0; i < keys.length; i++) {
            assertTrue(values.contains(vals[i]));
            assertTrue(map.containsValue(vals[i]));
        }

        for (long val : vals) {
            java_set.add(val);
        }
        java_set.remove((long) (42 * 2));
        assertTrue(values.removeAll(java_set),
                "values should have been modified: " + values + ", java: " + java_set);
        assertEquals(1, values.size(), "set: " + values);
        assertEquals(1, map.size(), "set: " + values);

        // noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < vals.length; i++) {
            if (vals[i] == 42 * 2) {
                assertTrue(values.contains(vals[i]));
                assertTrue(map.containsValue(vals[i]));
            } else {
                assertFalse(values.contains(vals[i]));
                assertFalse(map.containsValue(vals[i]));
            }
        }
    }


    @Test
    public void testValueCollectionIterator() {
        int[] keys = {1138, 42, 86, 99, 101, 727, 117};
        Long[] vals = new Long[keys.length];

        TIntLongMap raw_map = new TIntLongHashMap();
        for (int i = 0; i < keys.length; i++) {
            vals[i] = (long) (keys[i] * 2);
            raw_map.put(keys[i], vals[i]);
        }
        Map<Integer, Long> map = TDecorators.wrap(raw_map);

        List<Long> list = Arrays.asList(vals);
        Collection<Long> set = map.values();

        // test basic iterator function.
        Iterator<Long> iter = set.iterator();
        while (iter.hasNext()) {
            long val = iter.next();
            assertTrue(list.contains(val),
                    "value collection should only contain values: " + val + ", set; " + set);
        }

        assertFalse(iter.hasNext());
        try {
            iter.next();
            fail("Expect NoSuchElementException");
        } catch (NoSuchElementException ex) {
            // Expected.
        }

        // Start over with new iterator -- test iter.remove()
        iter = set.iterator();
        while (iter.hasNext()) {
            long val = iter.next();
            assertTrue(list.contains(val),
                    "value collection should only contain values: " + val + ", set; " + set);
            if (val == vals[3]) {
                iter.remove();
                assertFalse(set.contains(val),
                        "set contains removed element: " + val + ", set: " + set);
            }
        }
        assertEquals(map.size(), set.size());
        assertEquals(keys.length - 1, map.size());
        assertNull(map.get(keys[3]));
    }


    @Test
    public void testValues() {
        TIntLongMap raw_map = new TIntLongHashMap();
        Map<Integer, Long> map = TDecorators.wrap(raw_map);

        map.put(KEY_ONE, 1L);
        map.put(KEY_TWO, 2L);

        assertEquals(2, map.size());

        Long[] values = map.values().toArray(new Long[map.size()]);
        assertEquals(2, values.length);
        List<Long> values_list = Arrays.asList(values);

        assertTrue(values_list.contains(1L));
        assertTrue(values_list.contains(2L));

        Long[] values2 = map.values().toArray(new Long[map.size()]);
        assertEquals(2, values2.length);
        List<Long> keys_list2 = Arrays.asList(values2);

        assertTrue(keys_list2.contains(1L));
        assertTrue(keys_list2.contains(2L));

        int element_count = 20;
        raw_map = new TIntLongHashMap(20, 0.5f, Integer.MIN_VALUE, Long.MIN_VALUE);
        map = TDecorators.wrap(raw_map);
        for (int i = 0; i < element_count; i++) {
            map.put(i, (long) (i * i));
        }
        assertEquals(element_count, map.size());
        Long[] vals = map.values().toArray(new Long[map.size()]);
        Arrays.sort(vals);
        assertEquals(element_count, vals.length);
        for (int i = 0; i < element_count; i++) {
            assertEquals(Long.valueOf(i * i), vals[i], "expected: " + i * i + " got: " + vals[i]
                    + ", i: " + i + ", vals: " + Arrays.toString(vals));
            assertEquals(Long.valueOf(i * i), map.get(i));
        }
    }


    @Test
    public void testEntrySet() {
        int element_count = 20;
        Integer[] keys = new Integer[element_count];
        Long[] vals = new Long[element_count];

        TIntLongMap raw_map =
                new TIntLongHashMap(element_count, 0.5f, Integer.MIN_VALUE, Long.MIN_VALUE);
        Map<Integer, Long> map = TDecorators.wrap(raw_map);

        for (int i = 0; i < element_count; i++) {
            keys[i] = i + 1;
            vals[i] = (long) (i + 1);
            map.put(keys[i], vals[i]);
        }
        assertEquals(element_count, map.size());

        Set<Map.Entry<Integer, Long>> entries = map.entrySet();
        assertEquals(element_count, entries.size());
        assertFalse(entries.isEmpty());
        // noinspection unchecked
        Map.Entry<Integer, Long>[] array = entries.toArray(new Map.Entry[entries.size()]);
        for (Map.Entry<Integer, Long> entry : array) {
            assertTrue(entries.contains(entry));
        }
        assertFalse(entries.contains(null));

        assertEquals(array[0].hashCode(), array[0].hashCode());
        assertTrue(array[0].hashCode() != array[1].hashCode());

        assertEquals(array[0], array[0]);
        assertNotEquals(array[0], array[1]);
        Integer key = array[0].getKey();
        Long old_value = Long.valueOf(array[0].getValue());
        assertEquals(Long.valueOf(old_value), array[0].setValue(old_value * 2));
        assertEquals(Long.valueOf(old_value * 2), map.get(key));
        assertEquals(Long.valueOf(old_value * 2), array[0].getValue());

        // Adds are not allowed
        Map.Entry<Integer, Long> invalid_entry = new Map.Entry<Integer, Long>() {
            public Integer getKey() {
                return null;
            }

            public Long getValue() {
                return null;
            }

            public Long setValue(Long value) {
                return null;
            }
        };
        List<Map.Entry<Integer, Long>> invalid_entry_list =
                new ArrayList<Map.Entry<Integer, Long>>();
        invalid_entry_list.add(invalid_entry);

        try {
            entries.add(invalid_entry);
            fail("Expected OperationUnsupportedException");
        } catch (UnsupportedOperationException ex) {
            // Expected
        }

        try {
            entries.addAll(invalid_entry_list);
            fail("Expected OperationUnsupportedException");
        } catch (UnsupportedOperationException ex) {
            // Expected
        }

        assertFalse(entries.containsAll(invalid_entry_list));
        assertFalse(entries.removeAll(invalid_entry_list));

        List<Map.Entry<Integer, Long>> partial_list = new ArrayList<Map.Entry<Integer, Long>>();
        partial_list.add(array[3]);
        partial_list.add(array[4]);
        assertTrue(entries.removeAll(partial_list));
        assertEquals(element_count - 2, entries.size());
        assertEquals(element_count - 2, map.size());

        entries.clear();
        assertTrue(entries.isEmpty());
        assertTrue(map.isEmpty());
    }


    @Test
    public void testEquals() {
        int[] keys = {1138, 42, 86, 99, 101, 727, 117};
        long[] vals = new long[keys.length];

        TIntLongMap raw_map = new TIntLongHashMap();
        for (int i = 0; i < keys.length; i++) {
            vals[i] = keys[i] * 2;
            raw_map.put(keys[i], vals[i]);
        }
        Map<Integer, Long> map = TDecorators.wrap(raw_map);
        assertEquals(map, map);

        TIntIntMap raw_int_map = new TIntIntHashMap();
        for (int i = 0; i < keys.length; i++) {
            raw_int_map.put(keys[i], (int) vals[i]);
        }
        Map<Integer, Integer> int_map = TDecorators.wrap(raw_int_map);
        assertNotEquals(map, int_map);

        // Change a value..
        TIntLongMap raw_unequal = new TIntLongHashMap(raw_map);
        Map<Integer, Long> unequal = TDecorators.wrap(raw_unequal);
        map.put(keys[3], vals[3] + 1);
        assertNotEquals(map, unequal);

        // Change length
        raw_unequal = new TIntLongHashMap(raw_map);
        unequal = TDecorators.wrap(raw_unequal);
        map.put(13, Long.valueOf(26));
        assertNotEquals(map, unequal);
    }


    @Test
    public void testHashCode() {
        int[] keys = {1138, 42, 86, 99, 101, 727, 117};
        long[] vals = new long[keys.length];

        TIntLongMap raw_map = new TIntLongHashMap();
        for (int i = 0; i < keys.length; i++) {
            vals[i] = keys[i] * 2;
            raw_map.put(keys[i], vals[i]);
        }
        Map<Integer, Long> map = TDecorators.wrap(raw_map);

        TIntLongMap raw_other = new TIntLongHashMap();
        Map<Integer, Long> other = TDecorators.wrap(raw_other);
        other.putAll(map);
        assertEquals(map.hashCode(), other.hashCode(),
                "hashcodes incorrectly not equal: " + map + ", " + other);

        TIntLongMap raw_unequal = new TIntLongHashMap();
        for (int key : keys) {
            raw_unequal.put(key, key);
        }
        Map<Integer, Long> unequal = TDecorators.wrap(raw_unequal);
        assertNotEquals(map.hashCode(), unequal.hashCode(),
                "hashcodes unlikely equal: " + map + ", " + unequal);

        int[] raw_mismatched = {72, 49, 53, 1024, 999};
        TIntLongMap raw_mismatched_map = new TIntLongHashMap();
        for (int aRaw_mismatched : raw_mismatched) {
            raw_mismatched_map.put(aRaw_mismatched, Long.valueOf(aRaw_mismatched * 37));
        }
        Map<Integer, Long> mismatched = TDecorators.wrap(raw_mismatched_map);
        assertFalse(map.hashCode() == mismatched.hashCode(),
                "hashcodes unlikely equal: " + map + ", " + mismatched);
    }



    @Test
    public void testToString() {
        TIntLongMap raw_map = new TIntLongHashMap();
        Map<Integer, Long> map = TDecorators.wrap(raw_map);
        map.put(11, Long.valueOf(1));
        map.put(22, Long.valueOf(2));

        String to_string = map.toString();
        assertTrue(to_string.equals("{11=1, 22=2}") || to_string.equals("{22=2, 11=1}"), to_string);
    }


    @Test
    public void testSerialize() throws Exception {
        int[] keys = {1138, 42, 86, 99, 101, 727, 117};
        long[] vals = new long[keys.length];

        TIntLongMap raw_map = new TIntLongHashMap();
        for (int i = 0; i < keys.length; i++) {
            vals[i] = keys[i] * 2;
            raw_map.put(keys[i], vals[i]);
        }
        Map<Integer, Long> map = TDecorators.wrap(raw_map);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(map);

        ByteArrayInputStream bias = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bias);

        // noinspection unchecked
        Map<Integer, Long> deserialized = (Map<Integer, Long>) ois.readObject();

        assertEquals(map, deserialized);
    }

    @Test
    public void testBug3432212A() throws Exception {
        Map<Integer, Long> trove = new TIntLongMapDecorator(new TIntLongHashMap());
        trove.put(null, 1L);
        assertFalse(trove.isEmpty());
        assertEquals(1, trove.size());
        assertEquals(1, trove.entrySet().size());
        assertEquals(1, trove.keySet().size());
        assertNull(trove.keySet().iterator().next());
    }

    @Test
    public void testBug3432212B() throws Exception {
        Map<Integer, Long> trove = new TIntLongMapDecorator(new TIntLongHashMap());
        trove.put(1, null);
        assertFalse(trove.isEmpty());
        assertEquals(1, trove.size());
        assertEquals(1, trove.entrySet().size());
        assertEquals(1, trove.keySet().size());
        assertNull(trove.get(1));
        assertNull(trove.values().iterator().next());
    }
}
