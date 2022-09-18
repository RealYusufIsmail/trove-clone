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
package gnu.trove.list.linked;

import gnu.trove.list.TLinkableAdapter;
import junit.framework.TestCase;


/**
 *
 */
public class TLinkableAdapterTest extends TestCase {
    public void testOverride() {
        TLinkedList<MyObject> list = new TLinkedList<MyObject>();

        list.add(new MyObject("1"));
        list.add(new MyObject("2"));
        list.add(new MyObject("3"));

        int i = 1;
        for (MyObject obj : list) {
            assertEquals(String.valueOf(i), obj.getValue());
            i++;
        }
    }


    private class MyObject extends TLinkableAdapter<MyObject> {
        private final String value;

        MyObject(String value) {
            this.value = value;
        }


        public String getValue() {
            return value;
        }
    }
}
