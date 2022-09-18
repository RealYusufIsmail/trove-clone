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

/**
 * A {@link gnu.trove.strategy.HashingStrategy} that does identity comparisons (<kbd>==</kbd>) and
 * uses {@link System#identityHashCode(Object)} for hashCode generation.
 */
public class IdentityHashingStrategy<K> implements HashingStrategy<K> {
    static final long serialVersionUID = -5188534454583764904L;


    /**
     * A single instance that can be shared with multiple collections. This instance is thread safe.
     */
    public static final IdentityHashingStrategy<Object> INSTANCE =
            new IdentityHashingStrategy<Object>();


    public int computeHashCode(K object) {
        return System.identityHashCode(object);
    }

    public boolean equals(K o1, K o2) {
        return o1 == o2;
    }
}
