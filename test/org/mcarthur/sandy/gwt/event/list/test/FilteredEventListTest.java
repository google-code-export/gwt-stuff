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

    protected EventList createEmptyEventLists() {
        return EventLists.filteredEventList();
    }

    protected EventList createBackedEventList(final EventList el) {
        return EventLists.filteredEventList(el);
    }

    protected FilteredEventList createBackedFilteredEventList(final EventList el) {
        return (FilteredEventList)createBackedEventList(el);
    }

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

    public void testSetFilter() {
        final EventList el = EventLists.eventList();
        prefillWithIntegers(el, 4);
        final FilteredEventList fel = createBackedFilteredEventList(el);

        final List even = new ArrayList();
        even.add(Integer.valueOf(0));
        even.add(Integer.valueOf(2));

        final List odd = new ArrayList();
        odd.add(Integer.valueOf(1));
        odd.add(Integer.valueOf(3));

        final FilteredEventList.Filter evenFilter = new FilteredEventList.Filter() {
            public boolean accept(final Object element) {
                final Integer i = (Integer)element;
                return i.intValue() % 2 == 0;
            }
        };
        fel.setFilter(evenFilter);

        assertEquals(2, fel.size());
        assertTrue(even.containsAll(fel));

        final FilteredEventList.Filter oddFilter = new FilteredEventList.Filter() {
            public boolean accept(final Object element) {
                final Integer i = (Integer)element;
                return i.intValue() % 2 == 1;
            }
        };
        fel.setFilter(oddFilter);

        assertEquals(2, fel.size());
        assertTrue(odd.containsAll(fel));

        fel.setFilter(null);

        assertEquals(4, fel.size());
        assertTrue(fel.containsAll(even));
        assertTrue(fel.containsAll(odd));
    }

    public void testAdd() {
        super.testAdd();
        
        final EventList el = EventLists.eventList();
        prefillWithIntegers(el, 4);
        final FilteredEventList fel = createBackedFilteredEventList(el);

        final List even = new ArrayList();
        even.add(Integer.valueOf(0));
        even.add(Integer.valueOf(2));

        final List odd = new ArrayList();
        odd.add(Integer.valueOf(1));
        odd.add(Integer.valueOf(3));

        final FilteredEventList.Filter evenFilter = new FilteredEventList.Filter() {
            public boolean accept(final Object element) {
                final Integer i = (Integer)element;
                return i.intValue() % 2 == 0;
            }
        };
        fel.setFilter(evenFilter);

        fel.add(Integer.valueOf(4));
        try {
            fel.add(Integer.valueOf(5));
            fail("Exepected an IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
            // expected
        }

        final FilteredEventList.Filter oddFilter = new FilteredEventList.Filter() {
            public boolean accept(final Object element) {
                final Integer i = (Integer)element;
                return i.intValue() % 2 == 1;
            }
        };
        fel.setFilter(oddFilter);

        fel.add(Integer.valueOf(5));
        try {
            fel.add(Integer.valueOf(6));
            fail("Exepected an IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testSet() {
        //super.testSet(); // TODO: uncomment when FilteredEventListImpl is optimized
    }
}
