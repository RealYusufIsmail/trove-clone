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
package gnu.trove.decorator;

import gnu.trove.TDecorators;
import gnu.trove.list.TIntList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Jim Davies
 */
public class TPimativeListDecoratorTest {

    @Test
    public void testConstructorWithNull() {
        boolean expectionThrown = false;
        try {
            TDecorators.wrap((TIntList) null);
        } catch (NullPointerException ignored) {
            expectionThrown = true;
        }

        assertTrue(expectionThrown,
                "Wrapping a null value should result in an exception being thrown.");
    }
}
