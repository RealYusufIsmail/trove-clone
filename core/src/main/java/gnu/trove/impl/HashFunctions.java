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
package gnu.trove.impl;

/**
 * Provides various hash functions.
 *
 * @author wolfgang.hoschek@cern.ch
 * @version 1.0, 09/24/99
 */
public final class HashFunctions {
    /**
     * Returns a hashcode for the specified value.
     *
     * @return a hash code value for the specified value.
     */
    public static int hash(double value) {
        assert !Double.isNaN(value) : "Values of NaN are not supported.";

        long bits = Double.doubleToLongBits(value);
        return (int) (bits ^ (bits >>> 32));
        // return (int) Double.doubleToLongBits(value*663608941.737);
        // this avoids excessive hashCollisions in the case values are
        // of the form (1.0, 2.0, 3.0, ...)
    }

    /**
     * Returns a hashcode for the specified value.
     *
     * @return a hash code value for the specified value.
     */
    public static int hash(float value) {
        assert !Float.isNaN(value) : "Values of NaN are not supported.";

        return Float.floatToIntBits(value * 663608941.737f);
        // this avoids excessive hashCollisions in the case values are
        // of the form (1.0, 2.0, 3.0, ...)
    }

    /**
     * Returns a hashcode for the specified value.
     *
     * @return a hash code value for the specified value.
     */
    public static int hash(int value) {
        return value;
    }

    /**
     * Returns a hashcode for the specified value.
     *
     * @return a hash code value for the specified value.
     */
    public static int hash(long value) {
        return ((int) (value ^ (value >>> 32)));
    }

    /**
     * Returns a hashcode for the specified object.
     *
     * @return a hash code value for the specified object.
     */
    public static int hash(Object object) {
        return object == null ? 0 : object.hashCode();
    }

}
