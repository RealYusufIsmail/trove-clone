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
package gnu.trove.list;

/**
 * Simple adapter class implementing {@link TLinkable}, so you don't have to. Example:
 * 
 * <pre>
 * private class MyObject extends TLinkableAdapter&lt;MyObject&gt; {
 *     private final String value;
 * 
 *     MyObject(String value) {
 *         this.value = value;
 *     }
 * 
 *     public String getValue() {
 *         return value;
 *     }
 * }
 * </pre>
 */
public abstract class TLinkableAdapter<T extends TLinkable> implements TLinkable<T> {
    private volatile T next;
    private volatile T prev;

    @Override
    public T getNext() {
        return next;
    }

    @Override
    public void setNext(T next) {
        this.next = next;
    }

    @Override
    public T getPrevious() {
        return prev;
    }

    @Override
    public void setPrevious(T prev) {
        this.prev = prev;
    }
}
