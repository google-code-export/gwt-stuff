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
import java.util.Comparator;
import java.util.List;

/**
 * Tests for {@link org.mcarthur.sandy.gwt.event.list.client.FilteredEventList}.
 *
 * @author Sandy McArthur
 */
public class FilteredEventListTest extends TransformedEventListTest {
    private static final FilteredEventList.Filter ODD_FILTER = new FilteredEventList.Filter() {
        public boolean accept(final Object element) {
            final Integer i = (Integer)element;
            return i.intValue() % 2 != 0;
        }
    };
    private static final FilteredEventList.Filter EVEN_FILTER = new FilteredEventList.Filter() {
        public boolean accept(final Object element) {
            final Integer i = (Integer)element;
            return i.intValue() % 2 == 0;
        }
    };

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

    public void testContains() {
        final EventList el = EventLists.eventList();
        prefillWithIntegers(el, 4);
        final FilteredEventList fel = createBackedFilteredEventList(el);

        final List even = new ArrayList();
        even.add(Integer.valueOf(0));
        even.add(Integer.valueOf(2));

        final List odd = new ArrayList();
        odd.add(Integer.valueOf(1));
        odd.add(Integer.valueOf(3));

        fel.setFilter(EVEN_FILTER);

        Collections.shuffle(el);

        assertEquals(EVEN_FILTER, fel.getFilter());

        assertTrue(fel.containsAll(even));
        assertTrue(even.containsAll(fel));

        fel.setFilter(ODD_FILTER);

        assertTrue(fel.containsAll(odd));
        assertTrue(odd.containsAll(fel));
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

        fel.setFilter(EVEN_FILTER);

        assertEquals(2, fel.size());
        assertTrue(even.containsAll(fel));

        fel.setFilter(ODD_FILTER);

        assertEquals(2, fel.size());
        assertTrue(odd.containsAll(fel));

        fel.setFilter(null);

        assertEquals(4, fel.size());
        assertTrue(fel.containsAll(even));
        assertTrue(fel.containsAll(odd));

        fel.setFilter(null);
        assertNull(fel.getFilter());

        final FilteredEventList.Filter noneFilter = new FilteredEventList.Filter() {
            public boolean accept(final Object element) {
                return false;
            }
        };
        fel.setFilter(noneFilter);
        assertEquals(noneFilter, fel.getFilter());

        fel.setFilter(null);
        assertNull(fel.getFilter());
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

        fel.setFilter(EVEN_FILTER);

        fel.add(Integer.valueOf(4));
        try {
            fel.add(Integer.valueOf(5));
            fail("Exepected an IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
            // expected
        }

        fel.setFilter(ODD_FILTER);

        fel.add(Integer.valueOf(5));
        try {
            fel.add(Integer.valueOf(6));
            fail("Exepected an IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
            // expected
        }

        fel.add(0,Integer.valueOf(5));
        try {
            fel.add(0, Integer.valueOf(6));
            fail("Exepected an IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testAddAll() {
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

        fel.setFilter(EVEN_FILTER);

        fel.addAll(even);

        final int beforeSize = fel.size();
        try {
            fel.addAll(odd);
            fail("Exepected an IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
            // expected
        }
        assertEquals(beforeSize, fel.size());
    }

    public void testRemove() {
        super.testRemove();

        final EventList el = EventLists.eventList();
        prefillWithIntegers(el, 6);

        final FilteredEventList fel = createBackedFilteredEventList(el);

        fel.setFilter(EVEN_FILTER);

        final List even = new ArrayList(fel);

        ListEventListener flel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertNull("listEvent: " + listEvent, listEvent);
                        break;
                    default:
                        fail("Unexpected: " + listEvent);
                }
            }
        };
        fel.addListEventListener(flel);
        assertFalse(EVEN_FILTER.accept(el.get(1)));
        assertEquals(3, fel.size());
        Object removed = el.remove(1);
        assertEquals(3, fel.size());
        flel.listChanged(null);
        fel.removeListEventListener(flel);

        el.add(1, removed);

        flel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(ListEvent.createRemoved(fel, 1), listEvent);
                        break;
                    case 1:
                        assertNull("listEvent: " + listEvent, listEvent);
                        break;
                    default:
                        fail("Unexpected: " + listEvent);
                }
            }
        };
        fel.addListEventListener(flel);
        assertTrue(EVEN_FILTER.accept(el.get(2)));
        assertEquals(3, fel.size());
        removed = el.remove(2);
        assertEquals(2, fel.size());
        flel.listChanged(null);
        fel.removeListEventListener(flel);
        assertTrue(even.containsAll(fel));

        el.add(2, removed);

        ListEventListener lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(ListEvent.createRemoved(el, 2), listEvent);
                        break;
                    case 1:
                        assertNull("listEvent: " + listEvent, listEvent);
                        break;
                    default:
                        fail("Unexpected: " + listEvent);
                }
            }
        };
        flel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(ListEvent.createRemoved(fel, 1), listEvent);
                        break;
                    case 1:
                        assertNull("listEvent: " + listEvent, listEvent);
                        break;
                    default:
                        fail("Unexpected: " + listEvent);
                }
            }
        };
        el.addListEventListener(lel);
        fel.addListEventListener(flel);
        assertTrue(EVEN_FILTER.accept(el.get(2)));
        assertEquals(3, fel.size());
        fel.remove(1);
        assertEquals(2, fel.size());
        lel.listChanged(null);
        flel.listChanged(null);
        fel.removeListEventListener(flel);
    }

    public void testSet() {
        super.testSet(); // TODO: uncomment when FilteredEventListImpl is optimized

        final FilteredEventList fel = (FilteredEventList)createEmptyEventLists();

        prefillWithIntegers(fel, 6);

        fel.setFilter(ODD_FILTER);

        fel.set(0, Integer.valueOf(3));
        try {
            fel.set(0, Integer.valueOf(4));
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testSetViaDeeperList() {
        final EventList el = EventLists.eventList();
        prefillWithIntegers(el, 6);

        final FilteredEventList fel = createBackedFilteredEventList(el);

        fel.setFilter(EVEN_FILTER);

        ListEventListener lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertNull("listEvent: " + listEvent, listEvent);
                        break;
                    default:
                        fail("Unexpected: " + listEvent);
                }
            }
        };
        fel.addListEventListener(lel);
        assertFalse(EVEN_FILTER.accept(Integer.valueOf(-1)));
        el.set(1, Integer.valueOf(-1));
        lel.listChanged(null);
        assertEquals(3, fel.size());
        fel.removeListEventListener(lel);

        lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(ListEvent.createChanged(fel, 1), listEvent);
                        break;
                    case 1:
                        assertNull("listEvent: " + listEvent, listEvent);
                        break;
                    default:
                        fail("Unexpected: " + listEvent);
                }
            }
        };
        fel.addListEventListener(lel);
        assertTrue(EVEN_FILTER.accept(Integer.valueOf(-2)));
        el.set(2, Integer.valueOf(-2));
        lel.listChanged(null);
        assertEquals(3, fel.size());
        fel.removeListEventListener(lel);

        lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(ListEvent.createAdded(fel, 2), listEvent);
                        break;
                    case 1:
                        assertNull("listEvent: " + listEvent, listEvent);
                        break;
                    default:
                        fail("Unexpected: " + listEvent);
                }
            }
        };
        fel.addListEventListener(lel);
        assertTrue(EVEN_FILTER.accept(Integer.valueOf(30)));
        el.set(3, Integer.valueOf(30));
        lel.listChanged(null);
        assertEquals(4, fel.size());
        fel.removeListEventListener(lel);

        lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(ListEvent.createRemoved(fel, 2), listEvent);
                        break;
                    case 1:
                        assertNull("listEvent: " + listEvent, listEvent);
                        break;
                    default:
                        fail("Unexpected: " + listEvent);
                }
            }
        };
        fel.addListEventListener(lel);
        assertFalse(EVEN_FILTER.accept(Integer.valueOf(3)));
        el.set(3, Integer.valueOf(3));
        lel.listChanged(null);
        assertEquals(3, fel.size());
        fel.removeListEventListener(lel);
    }
}
