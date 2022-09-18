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
package gnu.trove.function;

/**
 * Interface for functions that accept and return one Object reference. <br>
 * Created: Mon Nov 5 22:19:36 2001
 *
 * @author Eric D. Friedman
 * @version $Id: TObjectFunction.java,v 1.1.2.1 2009/09/06 17:02:19 upholderoftruth Exp $
 */

public interface TObjectFunction<T, R> {

    /**
     * Execute this function with <kbd>value</kbd>
     *
     * @param value an <code>Object</code> input
     * @return an <code>Object</code> result
     */
    public R execute(T value);
}// TObjectFunction
