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
 * Common interface for iterators that operate via the "advance" method for moving the cursor to the
 * next element.
 */
public interface TAdvancingIterator extends TIterator {
    /**
     * Moves the iterator forward to the next entry.
     *
     * @throws java.util.NoSuchElementException if the iterator is already exhausted
     */
    public void advance();
}
