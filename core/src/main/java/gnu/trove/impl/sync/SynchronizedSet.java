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
package gnu.trove.impl.sync;

import java.util.Set;


/** Local implementation of SynchronizedSet so we can set the mutex explicitly. */
class SynchronizedSet<E> extends SynchronizedCollection<E> implements Set<E> {
    private static final long serialVersionUID = 487447009682186044L;

    SynchronizedSet(Set<E> s, Object mutex) {
        super(s, mutex);
    }

    public boolean equals(Object o) {
        synchronized (mutex) {
            return c.equals(o);
        }
    }

    public int hashCode() {
        synchronized (mutex) {
            return c.hashCode();
        }
    }
}
