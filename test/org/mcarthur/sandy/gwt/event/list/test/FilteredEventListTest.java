/*
 * Copyright 2007 Sandy McArthur, Jr.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.mcarthur.sandy.gwt.event.list.test;

import org.mcarthur.sandy.gwt.event.list.client.EventList;
import org.mcarthur.sandy.gwt.event.list.client.EventLists;
import org.mcarthur.sandy.gwt.event.list.client.FilteredEventList;
import org.mcarthur.sandy.gwt.event.list.client.SortedEventList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Tests for {@link org.mcarthur.sandy.gwt.event.list.client.FilteredEventList}.
 *
 * @author Sandy McArthur
 */
public class FilteredEventListTest extends TransformedEventListTest {

    public void testToArrayRespectsFilters() {
        final EventList el = EventLists.eventList();
        final FilteredEventList fel = EventLists.filteredEventList(el);
        fel.setFilter(new FilteredEventList.Filter() {
            public boolean accept(final Object element) {
                return ((Number)element).intValue() % 2 == 0;
            }
        });

        for (int i=0; i < 10; i++) {
            el.add(new Integer(i));
        }

        assertEquals(10, el.size());
        assertEquals(5, fel.size());
        assertEquals(5, fel.toArray().length);
    }

    public void testFoo() {
        final EventList el = EventLists.eventList();
        final SortedEventList sel = EventLists.sortedEventList(el);
        final FilteredEventList fel = EventLists.filteredEventList(sel);

        el.add(new Integer(25));
        el.add(new Integer(33));
        el.add(new Integer(55));
        el.add(new Integer(49));
        el.add(new Integer(32));
        el.add(new Integer(57));

        final Comparator natural = new Comparator() {
            public int compare(final Object o1, final Object o2) {
                return ((Comparable)o1).compareTo(o2);
            }
        };
        final Comparator reverse = new Comparator() {
            public int compare(final Object o1, final Object o2) {
                return natural.compare(o2, o1);
            }
        };

        sel.setComparator(natural);

        final FilteredEventList.Filter filter = new FilteredEventList.Filter() {
            public boolean accept(final Object element) {
                final int i = ((Number)element).intValue();
                return  20 < i && i < 50;
            }
        };

        fel.setFilter(filter);

        final List n1 = new ArrayList(fel);

        sel.setComparator(reverse);

        final List r1 = new ArrayList(fel);

        assertEquals(n1.size(), r1.size());
        assertTrue(n1.containsAll(r1));
        assertTrue(r1.containsAll(n1));
    }
}
