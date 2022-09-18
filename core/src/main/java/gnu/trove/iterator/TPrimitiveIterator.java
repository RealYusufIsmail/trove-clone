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
package gnu.trove.iterator;

/**
 * <p>
 * Implements all iterator functions for the hashed object set. Subclasses may override
 * objectAtIndex to vary the object returned by calls to next() (e.g. for values, and Map.Entry
 * objects).
 * </p>
 * <p>
 * Note that iteration is fastest if you forego the calls to <kbd>hasNext</kbd> in favor of checking
 * the size of the structure yourself and then call next() that many times:
 * </p>
 * 
 * <pre>
 * Iterator i = collection.iterator();
 * for (int size = collection.size(); size-- &gt; 0;) {
 *     Object o = i.next();
 * }
 * </pre>
 * <p>
 * You may, of course, use the hasNext(), next() idiom too if you aren't in a performance critical
 * spot.
 * </p>
 */
public interface TPrimitiveIterator extends TIterator {
    /**
     * Returns true if the iterator can be advanced past its current location.
     *
     * @return a <code>boolean</code> value
     */
    public boolean hasNext();


    /**
     * Removes the last entry returned by the iterator. Invoking this method more than once for a
     * single entry will leave the underlying data structure in a confused state.
     */
    public void remove();

} // TPrimitiveIterator
