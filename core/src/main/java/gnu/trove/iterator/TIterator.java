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
 * Common interface for all iterators used in Trove.
 */
public interface TIterator {
    /**
     * Returns true if the iterator can be advanced past its current location.
     *
     * @return a <code>boolean</code> value
     */
    public boolean hasNext();

    /**
     * Removes the last entry returned by the iterator. The result of invoking this method more than
     * once for a single entry is undefined and can leave the underlying data structure in a
     * confused state.
     */
    public void remove();
}
