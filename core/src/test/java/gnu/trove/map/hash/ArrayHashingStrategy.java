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
package gnu.trove.map.hash;

import gnu.trove.strategy.HashingStrategy;

import java.io.Serializable;
import java.util.Arrays;


/**
*
*/
public class ArrayHashingStrategy implements HashingStrategy<char[]>, Serializable {

    public int computeHashCode(char[] o) {
        return Arrays.hashCode(o);
    }

    public boolean equals(char[] o1, char[] o2) {
        return Arrays.equals(o1, o2);
    }
}
