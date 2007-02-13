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
import org.mcarthur.sandy.gwt.event.list.client.ListEvent;
import org.mcarthur.sandy.gwt.event.list.client.ListEventListener;
import org.mcarthur.sandy.gwt.event.list.client.SortedEventList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Tests for {@link org.mcarthur.sandy.gwt.event.list.client.SortedEventList}.
 *
 * @author Sandy McArthur
 */
public class SortedEventListTest extends TransformedEventListTest {
    private final Integer I0 = new Integer(0);
    private final Integer I5 = new Integer(5);
    private final Integer I10 = new Integer(10);
    private final Integer I15 = new Integer(15);
    private final Integer I20 = new Integer(20);

    public void testAdd() {
        final EventList el = EventLists.eventList();
        el.add(I0);
        el.add(I20);

        final SortedEventList sel = EventLists.sortedEventList(el);

        assertEquals(sel.get(0), I0);
        assertEquals(sel.get(1), I20);

        el.add(I10);
        assertEquals(sel.get(1), I10);
        assertEquals(sel.get(2), I20);

        sel.add(I15);
        assertEquals(sel.get(1), I10);
        assertEquals(sel.get(3), I20);

        sel.add(0, I5);
        assertEquals(sel.get(0), I0);
        assertEquals(sel.get(1), I5);
        assertEquals(sel.get(2), I10);
    }

    public void testAddAll() {
        final List l = new ArrayList();
        l.add(I0);
        l.add(I20);
        l.add(I10);
        l.add(I5);

        final SortedEventList sel = EventLists.sortedEventList();
        sel.addAll(l);

        assertEquals(sel.get(0), I0);
        assertEquals(sel.get(1), I5);
        assertEquals(sel.get(2), I10);
        assertEquals(sel.get(3), I20);

        sel.clear();
        l.remove(I20);
        sel.add(I20);
        sel.addAll(1, l);

        assertEquals(sel.get(0), I0);
        assertEquals(sel.get(1), I5);
        assertEquals(sel.get(2), I10);
        assertEquals(sel.get(3), I20);
    }

    public void testIndexOf() {
        final SortedEventList sel = EventLists.sortedEventList();

        sel.add(I20);
        sel.add(I5);
        sel.add(I10);

        assertEquals(0, sel.indexOf(I5));
        assertEquals(1, sel.indexOf(I10));
        assertEquals(2, sel.indexOf(I20));
        assertEquals(-1, sel.indexOf(I0));
    }

    public void testRemove() {
        final EventList el = EventLists.eventList();
        el.add(I10);
        el.add(I5);
        el.add(I20);
        el.add(I0);

        final SortedEventList sel = EventLists.sortedEventList(el);

        sel.remove(2);
        assertFalse(el.contains(I10));

        el.remove(I20);
        assertFalse(el.contains(I20));
    }

    public void testConsistentStateForRemovedEvents() throws Exception {

        final EventList deepest = EventLists.eventList();
        deepest.add("hello");
        deepest.add("world");

        final SortedEventList sel = EventLists.sortedEventList(deepest);

        sel.addListEventListener(new ListEventListener() {

            public void listChanged(final ListEvent listEvent) {
                //System.out.println("list changed: " + listEvent + " size = " + sel.size());
                // assertEquals(0, sel.size());
                for (Iterator iter = sel.iterator(); iter.hasNext();) {
                    final Object element = iter.next();
                }

            }

        });

        deepest.clear();
    }

    public void testSortWithAFilteredList() {
        final EventList el = EventLists.eventList();
        for (int i=0; i < 10; i++) {
            el.add(new Integer(i));
        }
        Collections.shuffle(el);

        final FilteredEventList fel = EventLists.filteredEventList(el);
        fel.setFilter(new FilteredEventList.Filter() {
            public boolean accept(final Object element) {
                return ((Number)element).intValue() % 2 == 0;
            }
        });

        final SortedEventList sel = EventLists.sortedEventList(fel);

        sel.sort();

        assertEquals(10, el.size());
        assertEquals(5, fel.size());
        assertEquals(5, sel.size());
        assertTrue(el.containsAll(fel));
        assertTrue(el.containsAll(sel));
        assertTrue(fel.containsAll(sel));
        assertTrue(sel.containsAll(fel));

        final List sorted = new ArrayList(sel);
        Collections.sort(sorted);
        assertEquals(sorted, sel);

        //System.err.println(" el: " + el);
        //System.err.println("fel: " + fel);
        //System.err.println("sel: " + sel);
        //System.err.println("far: " + Arrays.asList(fel.toArray()));
        //System.err.println("sar: " + Arrays.asList(sel.toArray()));
    }

}
