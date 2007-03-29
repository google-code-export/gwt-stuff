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
import org.mcarthur.sandy.gwt.event.list.client.RangedEventList;
import org.mcarthur.sandy.gwt.event.list.client.SortedEventList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Tests for {@link org.mcarthur.sandy.gwt.event.list.client.RangedEventList}.
 *
 * @author Sandy McArthur
 */
public class RangedEventListTest extends TransformedEventListTest {

    protected EventList createEmptyEventLists() {
        return EventLists.rangedEventList();
    }

    protected EventList createBackedEventList(final EventList el) {
        return EventLists.rangedEventList(el);
    }

    protected RangedEventList createBackedRangedEventList(final EventList el) {
        return (RangedEventList)createBackedEventList(el);
    }

    public void testSetStart() {
        final EventList el = EventLists.eventList();
        el.add("one");
        el.add("two");
        final RangedEventList rel = EventLists.rangedEventList(el);

        assertEquals(2, el.size());
        assertEquals(2, rel.size());

        rel.setStart(1);
        assertEquals(1, rel.size());

        rel.setStart(2);
        assertEquals(0, rel.size());

        rel.setStart(99);
        assertEquals(0, rel.size());

        rel.setStart(0);
        assertEquals(2, rel.size());
    }

    public void testAdd() {
        final EventList el = EventLists.eventList();
        el.add("one");
        el.add("two");
        final RangedEventList rel = EventLists.rangedEventList(el);

        rel.setMaxSize(2);

        rel.add("three");
        rel.add("four");

        assertEquals(2, rel.size());
        assertFalse(rel.contains("three"));
        assertFalse(rel.contains("four"));
        assertTrue(el.contains("three"));
        assertTrue(el.contains("four"));
    }

    public void testContains() {
        final EventList el = EventLists.eventList();
        el.add("one");
        el.add("two");
        final RangedEventList rel = EventLists.rangedEventList(el);

        assertTrue(el.contains("one"));
        assertTrue(el.contains("two"));

        rel.setStart(1);
        assertFalse(rel.contains("one"));
        assertTrue(rel.contains("two"));

        rel.setStart(2);
        assertFalse(rel.contains("one"));
        assertFalse(rel.contains("two"));

        rel.setStart(66);
        assertFalse(rel.contains("one"));
        assertFalse(rel.contains("two"));

        rel.setStart(0);
        assertTrue(rel.contains("one"));
        assertTrue(rel.contains("two"));
    }

    public void testContainsAll() {
        final List all = new ArrayList();
        all.add("one");
        all.add("two");

        final EventList el = EventLists.eventList();
        el.addAll(all);
        final RangedEventList rel = EventLists.rangedEventList(el);

        assertTrue(rel.containsAll(all));
        
        rel.setStart(1);
        assertFalse(rel.containsAll(all));

        rel.setStart(2);
        assertFalse(rel.containsAll(all));

        rel.setStart(200);
        assertFalse(rel.containsAll(all));

        rel.setStart(0);
        assertTrue(rel.containsAll(all));
    }

    public void testIndexOf() {
        final List all = new ArrayList();
        all.add("one");
        all.add("two");

        final EventList el = EventLists.eventList();
        el.addAll(all);
        final RangedEventList rel = EventLists.rangedEventList(el);


        assertEquals(0, el.indexOf("one"));
        assertEquals(1, el.indexOf("two"));

        rel.setStart(1);
        assertEquals(-1, rel.indexOf("one"));
        assertEquals(0, rel.indexOf("two"));
    }

    public void testRemoveAll() {
        final List all = new ArrayList();
        all.add("one");
        all.add("two");

        final EventList el = EventLists.eventList();
        el.addAll(all);
        final RangedEventList rel = EventLists.rangedEventList(el);

        rel.setStart(1);
        rel.removeAll(all);

        assertEquals(1, el.size());
        assertEquals(0, rel.size());
    }
    
    public void testRetainAll() {
        final List all = new ArrayList();
        all.add("one");
        all.add("two");

        final EventList el = EventLists.eventList();
        el.addAll(all);
        final RangedEventList rel = EventLists.rangedEventList(el);

        rel.setStart(1);

        final List two = new ArrayList();
        two.add("two");

        rel.retainAll(two);

        assertEquals(1, rel.size());
        assertEquals(all.size(), el.size());
    }

    public void testClearOfDeeperList() {
        final EventList el = EventLists.eventList();
        final SortedEventList sel = EventLists.sortedEventList(el);
        // keep a reverse sort
        sel.setComparator(new Comparator() {
            public int compare(final Object o1, final Object o2) {
                final Comparable c1 = (Comparable)o1;
                final Comparable c2 = (Comparable)o2;
                return c2.compareTo(c1);
            }
        });
        final FilteredEventList fel = EventLists.filteredEventList(sel);
        final RangedEventList rel = createBackedRangedEventList(fel);
        rel.setMaxSize(4);

        // don't change the order
        el.add(new Integer(25));
        el.add(new Integer(33));
        el.add(new Integer(55));
        el.add(new Integer(7));
        el.add(new Integer(93));

        rel.addListEventListener(new ListEventListener() {
            public void listChanged(final ListEvent listEvent) {
                if (listEvent.isChanged()) {
                    for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd();i ++) {
                        listEvent.getSourceList().get(i);
                    }
                }
            }
        });

        el.clear();
    }

    public void testSizePlusMaxSizeDoesNotOverflow() {
        final EventList el = EventLists.eventList();

        for (int i=0; i < 10; i++) {
            el.add(new Integer(i));
        }

        final RangedEventList rel = createBackedRangedEventList(el); // maxSize set to Integer.MAX_VALUE

        rel.setStart(3);

        final ListEventListener addedListener = new ListEventListener() {
            public void listChanged(final ListEvent listEvent) {
                if (!listEvent.isAdded()) {
                    fail("Expecting an ADDED event. got: " + listEvent);
                }
            }
        };
        rel.addListEventListener(addedListener);

        el.add("one");

        rel.removeListEventListener(addedListener);

        final ListEventListener changedListener = new ListEventListener() {
            public void listChanged(final ListEvent listEvent) {
                if (!listEvent.isChanged()) {
                    fail("Expecting an CHANGED event. got: " + listEvent);
                }
            }
        };

        rel.addListEventListener(changedListener);

        el.set(el.indexOf("one"), "two");

        rel.removeListEventListener(changedListener);

        final ListEventListener removedListener = new ListEventListener() {
            public void listChanged(final ListEvent listEvent) {
                if (!listEvent.isRemoved()) {
                    fail("Expecting an REMOVED event. got: " + listEvent);
                }
            }
        };

        rel.addListEventListener(removedListener);

        el.remove("two");

        rel.removeListEventListener(removedListener);
    }

    public void testAddBeforeRangeStart() {
        // all elements are shifted down one
        final EventList el = EventLists.eventList();
        prefill(el, 100);

        final RangedEventList rel = createBackedRangedEventList(el);
        rel.setStart(10);
        rel.setMaxSize(10);

        // if the max size is in effect, the last element(s) is removed and the first is added
        ListEventListener lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(new ListEvent(rel, ListEvent.REMOVED, 9), listEvent);
                        break;
                    case 1:
                        assertEquals(new ListEvent(rel, ListEvent.ADDED, 0), listEvent);
                        break;
                    case 2:
                        assertNull(listEvent);
                        break;
                    default:
                        fail("Unexpected: " + listEvent);
                }
            }
        };
        rel.addListEventListener(lel);
        el.add(0, "one");
        lel.listChanged(null);
        rel.removeListEventListener(lel);

        // if max size doesn't have an effect then element(s) are added to the start
        rel.setMaxSize(Integer.MAX_VALUE);
        lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(new ListEvent(rel, ListEvent.ADDED, 0), listEvent);
                        break;
                    case 1:
                        assertNull(listEvent);
                        break;
                    default:
                        fail("Unexpected: " + listEvent);
                }
            }
        };
        rel.addListEventListener(lel);
        el.add(0, "two");
        lel.listChanged(null);
        rel.removeListEventListener(lel);
    }

    public void testSetBeforeRangeStart() {
        final EventList el = EventLists.eventList();
        prefill(el, 100);

        final RangedEventList rel = createBackedRangedEventList(el);
        rel.setStart(10);
        rel.setMaxSize(10);

        ListEventListener lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(new ListEvent(rel), listEvent);
                        break;
                    case 1:
                        assertNull(listEvent);
                        break;
                    default:
                        fail("Unexpected: " + listEvent);
                }
            }
        };
        rel.addListEventListener(lel);
        el.set(0, "one");
        lel.listChanged(null);
        rel.removeListEventListener(lel);
    }

    public void testRemoveBeforeRangeStart() {
        // all elements are shifted up one
        final EventList el = EventLists.eventList();
        prefill(el, 100);

        final RangedEventList rel = createBackedRangedEventList(el);
        rel.setStart(10);
        rel.setMaxSize(10);

        // if the max size is in effect, the last element(s) is removed and the first is added
        ListEventListener lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(new ListEvent(rel, ListEvent.REMOVED, 0), listEvent);
                        break;
                    case 1:
                        assertEquals(new ListEvent(rel, ListEvent.ADDED, 9), listEvent);
                        break;
                    case 2:
                        assertNull(listEvent);
                        break;
                    default:
                        fail("Unexpected: " + listEvent);
                }
            }
        };
        rel.addListEventListener(lel);
        el.remove(0);
        lel.listChanged(null);
        rel.removeListEventListener(lel);

        // if max size doesn't have an effect then element(s) are added to the start
        rel.setMaxSize(Integer.MAX_VALUE);
        lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(new ListEvent(rel, ListEvent.REMOVED, 0), listEvent);
                        break;
                    case 1:
                        assertNull(listEvent);
                        break;
                    default:
                        fail("Unexpected: " + listEvent);
                }
            }
        };
        rel.addListEventListener(lel);
        el.remove(0);
        lel.listChanged(null);
        rel.removeListEventListener(lel);
    }

    public void testAddAtRangeStartViaDeeperList() {
        final EventList el = EventLists.eventList();
        prefill(el, 100);

        final RangedEventList rel = createBackedRangedEventList(el);
        rel.setStart(10);
        rel.setMaxSize(10);

        ListEventListener lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(new ListEvent(rel, ListEvent.REMOVED, 9), listEvent);
                        break;
                    case 1:
                        assertEquals(new ListEvent(rel, ListEvent.ADDED, 0), listEvent);
                        break;
                    case 2:
                        assertNull(listEvent);
                        break;
                    default:
                        fail("Unexpected: " + listEvent);
                }
            }
        };
        rel.addListEventListener(lel);
        el.add(10, "one"); // should have same effect as testAddBeforeRangeStart()
        lel.listChanged(null);
        rel.removeListEventListener(lel);

        rel.setMaxSize(Integer.MAX_VALUE);

        lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(new ListEvent(rel, ListEvent.ADDED, 0), listEvent);
                        break;
                    case 1:
                        assertNull(listEvent);
                        break;
                    default:
                        fail("Unexpected: " + listEvent);
                }
            }
        };
        rel.addListEventListener(lel);
        el.add(10, "two"); // should have same effect as testAddBeforeRangeStart()
        lel.listChanged(null);
        rel.removeListEventListener(lel);
    }

    public void testSetAtRangeStartViaDeeperList() {
        fail("implement test.");
    }

    public void testRemoveAtRangeStartViaDeeperList() {
        fail("implement test.");
    }

    public void testAddAtRangeStart() {
        final EventList el = EventLists.eventList();
        prefill(el, 100);

        final RangedEventList rel = createBackedRangedEventList(el);
        rel.setStart(10);
        rel.setMaxSize(10);

        ListEventListener lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(new ListEvent(rel, ListEvent.REMOVED, 9), listEvent);
                        break;
                    case 1:
                        assertEquals(new ListEvent(rel, ListEvent.ADDED, 0), listEvent);
                        break;
                    case 2:
                        assertNull(listEvent);
                        break;
                    default:
                        fail("Unexpected: " + listEvent);
                }
            }
        };
        rel.addListEventListener(lel);
        rel.add(0, "one");
        lel.listChanged(null);
        rel.removeListEventListener(lel);

        rel.setMaxSize(Integer.MAX_VALUE);

        lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(new ListEvent(rel, ListEvent.ADDED, 0), listEvent);
                        break;
                    case 1:
                        assertNull(listEvent);
                        break;
                    default:
                        fail("Unexpected: " + listEvent);
                }
            }
        };
        rel.addListEventListener(lel);
        rel.add(0, "two");
        lel.listChanged(null);
        rel.removeListEventListener(lel);
    }

    public void testSetAtRangeStart() {
        fail("implement test.");
    }

    public void testRemoveAtRangeStart() {
        fail("implement test.");
    }

    public void testAddAcrossRangeStart() {
        final EventList el = EventLists.eventList();
        prefill(el, 100);

        final RangedEventList rel = createBackedRangedEventList(el);
        rel.setStart(10);
        rel.setMaxSize(10);

        final List few = new ArrayList();
        prefill(few, 3);

        ListEventListener lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(new ListEvent(rel, ListEvent.REMOVED, 7, 10), listEvent);
                        break;
                    case 1:
                        assertEquals(new ListEvent(rel, ListEvent.ADDED, 0,3), listEvent);
                        break;
                    case 2:
                        assertNull(listEvent);
                        break;
                    default:
                        fail("Unexpected: " + listEvent);
                }
            }
        };
        rel.addListEventListener(lel);
        assertEquals(3, few.size());
        el.addAll(9, few);
        lel.listChanged(null);
        rel.removeListEventListener(lel);

        rel.setMaxSize(Integer.MAX_VALUE);

        lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(new ListEvent(rel, ListEvent.ADDED, 0, 3), listEvent);
                        break;
                    case 1:
                        assertNull(listEvent);
                        break;
                    default:
                        fail("Unexpected: " + listEvent);
                }
            }
        };
        rel.addListEventListener(lel);
        el.addAll(8, few);
        lel.listChanged(null);
        rel.removeListEventListener(lel);

    }

    public void testRemoveAcrossRangeStart() {
        fail("implement test.");
    }

    public void testAddInRange() {
        final EventList el = EventLists.eventList();
        prefill(el, 100);

        final RangedEventList rel = createBackedRangedEventList(el);
        rel.setStart(10);
        rel.setMaxSize(10);

        final List few = new ArrayList();
        prefill(few, 3);

        ListEventListener lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(new ListEvent(rel, ListEvent.REMOVED, 7, 10), listEvent);
                        break;
                    case 1:
                        assertEquals(new ListEvent(rel, ListEvent.ADDED, 2, 5), listEvent);
                        break;
                    case 2:
                        assertNull(listEvent);
                        break;
                    default:
                        fail("Unexpected: " + listEvent);
                }
            }
        };
        rel.addListEventListener(lel);
        assertEquals(3, few.size());
        el.addAll(12, few);
        lel.listChanged(null);
        rel.removeListEventListener(lel);

        rel.setMaxSize(Integer.MAX_VALUE);

        lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(new ListEvent(rel, ListEvent.ADDED, 5, 8), listEvent);
                        break;
                    case 1:
                        assertNull(listEvent);
                        break;
                    default:
                        fail("Unexpected: " + listEvent);
                }
            }
        };
        rel.addListEventListener(lel);
        assertEquals(3, few.size());
        el.addAll(15, few);
        lel.listChanged(null);
        rel.removeListEventListener(lel);
    }

    public void testSetInRange() {
        fail("implement test.");
    }

    public void testRemoveInRange() {
        fail("implement test.");
    }

    public void testAddAcrossMaxRange() {
        final EventList el = EventLists.eventList();
        prefill(el, 100);

        final RangedEventList rel = createBackedRangedEventList(el);
        rel.setStart(10);
        rel.setMaxSize(10);

        final List few = new ArrayList();
        prefill(few, 3);

        ListEventListener lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(new ListEvent(rel, ListEvent.REMOVED, 9), listEvent);
                        break;
                    case 1:
                        assertEquals(new ListEvent(rel, ListEvent.ADDED, 9), listEvent);
                        break;
                    case 2:
                        assertNull(listEvent);
                        break;
                    default:
                        fail("Unexpected: " + listEvent);
                }
            }
        };
        rel.addListEventListener(lel);
        assertEquals(3, few.size());
        el.addAll(19, few);
        lel.listChanged(null);
        rel.removeListEventListener(lel);
    }

    public void testRemoveAcrossMaxRange() {
        fail("implement test.");
    }

    public void testAddAfterMaxRange() {
        final EventList el = EventLists.eventList();
        prefill(el, 100);

        final RangedEventList rel = createBackedRangedEventList(el);
        rel.setStart(10);
        rel.setMaxSize(10);

        final List few = new ArrayList();
        prefill(few, 3);

        ListEventListener lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(new ListEvent(rel), listEvent);
                        break;
                    case 1:
                        assertNull("Expected null, not: " + listEvent, listEvent);
                        break;
                    default:
                        fail("Unexpected: " + listEvent);
                }
            }
        };
        rel.addListEventListener(lel);
        assertEquals(3, few.size());
        el.addAll(29, few);
        lel.listChanged(null);
        rel.removeListEventListener(lel);
    }

    public void testSetAfterMaxRange() {
        fail("implement test.");
    }

    public void testRemoveAfterMaxRange() {
        fail("implement test.");
    }
}
