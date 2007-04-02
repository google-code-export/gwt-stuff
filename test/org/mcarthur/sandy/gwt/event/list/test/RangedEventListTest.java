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

        ListEventListener lel = new ListEventListener() {
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
        rel.setStart(1);
        lel.listChanged(null);
        assertEquals(1, rel.size());
        rel.removeListEventListener(lel);

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
        rel.setStart(2);
        lel.listChanged(null);
        assertEquals(0, rel.size());
        rel.removeListEventListener(lel);

        lel = new ListEventListener() {
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
        rel.setStart(99);
        lel.listChanged(null);
        assertEquals(0, rel.size());
        rel.removeListEventListener(lel);

        lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(new ListEvent(rel, ListEvent.ADDED, 0, 2), listEvent);
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
        rel.setStart(0);
        lel.listChanged(null);
        assertEquals(2, rel.size());
        assertEquals(el, rel);
        rel.removeListEventListener(lel);


        el.add("three");
        rel.setStart(1);
        rel.setMaxSize(2);

        lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(new ListEvent(rel, ListEvent.REMOVED, 1), listEvent);
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
        rel.setStart(0);
        lel.listChanged(null);
        rel.removeListEventListener(lel);

        try {
            rel.setStart(-1);
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testSetMaxSize() {
        final EventList el = EventLists.eventList();
        prefillWithIntegers(el, 10);
        final RangedEventList rel = EventLists.rangedEventList(el);

        assertEquals(10, el.size());
        assertEquals(el.size(), rel.size());

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
        rel.setMaxSize(1000);
        lel.listChanged(null);
        assertEquals(el.size(), rel.size());
        rel.removeListEventListener(lel);

        lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(new ListEvent(rel, ListEvent.REMOVED, 5, 10), listEvent);
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
        rel.setMaxSize(5);
        lel.listChanged(null);
        assertEquals(5, rel.size());
        rel.removeListEventListener(lel);

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
        rel.setMaxSize(8);
        lel.listChanged(null);
        assertEquals(8, rel.size());
        rel.removeListEventListener(lel);

        lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(new ListEvent(rel, ListEvent.ADDED, 8, 10), listEvent);
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
        rel.setMaxSize(Integer.MAX_VALUE);
        lel.listChanged(null);
        assertEquals(el.size(), rel.size());
        rel.removeListEventListener(lel);

        try {
            rel.setMaxSize(-1);
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testGetTotal() {
        final EventList el = EventLists.eventList();
        prefillWithIntegers(el, 50);
        final RangedEventList rel = EventLists.rangedEventList(el);

        assertEquals(el.size(), rel.getTotal());

        rel.remove(4);
        assertEquals(el.size(), rel.getTotal());

        el.remove(40);
        assertEquals(el.size(), rel.getTotal());

        rel.setMaxSize(15);
        assertEquals(el.size(), rel.getTotal());

        rel.setStart(25);
        assertEquals(el.size(), rel.getTotal());
    }

    public void testAdd() {
        super.testAdd();

        final EventList el = EventLists.eventList();
        el.add("one");
        el.add("two");
        final RangedEventList rel = createBackedRangedEventList(el);

        rel.setMaxSize(2);

        rel.add("three");
        rel.add("four");

        assertEquals(2, rel.size());
        assertFalse(rel.contains("three"));
        assertFalse(rel.contains("four"));
        assertTrue(el.contains("three"));
        assertTrue(el.contains("four"));
    }

    public void testAddWhenStartOffsetAfterDeeperSize() {
        final EventList el = EventLists.eventList();
        final RangedEventList rel = createBackedRangedEventList(el);
        rel.setStart(2);

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
        el.add("zero");
        lel.listChanged(null);
        rel.removeListEventListener(lel);

        lel = new ListEventListener() {
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
        el.add("one");
        lel.listChanged(null);
        rel.removeListEventListener(lel);

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
        el.add("two");
        lel.listChanged(null);
        rel.removeListEventListener(lel);
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
        EventList e = null;
        final EventList el = EventLists.eventList(); e = el;
        final List elReplay = new EventListReplayList(el);

        final SortedEventList sel;
        if (true) {
            sel = EventLists.sortedEventList(e);
            // keep a reverse sort
            sel.setComparator(new Comparator() {
                public int compare(final Object o1, final Object o2) {
                    final Comparable c1 = (Comparable)o1;
                    final Comparable c2 = (Comparable)o2;
                    return c2.compareTo(c1);
                }
            });
            e = sel;
        } else {
            sel = null;
        }
        final List selReplay = sel != null ? new EventListReplayList(sel) : null;

        final FilteredEventList fel;
        if (!true) {
            fel = EventLists.filteredEventList(e);
            e = fel;
        } else {
            fel = null;
        }
        final List felReplay = fel != null ? new EventListReplayList(fel) : null;

        final RangedEventList rel;
        if (true) {
            rel = createBackedRangedEventList(e);
            rel.setMaxSize(4);
            e = rel;
        } else {
            rel = null;
        }
        final List relReplay = rel != null ? new EventListReplayList(rel) : null;

        // don't change the order
        el.add(new Integer(25));
        el.add(new Integer(33));
        el.add(new Integer(55));
        el.add(new Integer(7));
        el.add(new Integer(93));

        assertEquals(elReplay, el);
        assertEquals(selReplay, sel);
        assertEquals(felReplay, fel);
        assertEquals(relReplay, rel);

        if (rel != null) {
            rel.addListEventListener(new ListEventListener() {
                public void listChanged(final ListEvent listEvent) {
                    if (listEvent.isChanged()) {
                        for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd();i ++) {
                            listEvent.getSourceList().get(i);
                        }
                    }
                }
            });
        }

        el.clear();

        assertEquals(elReplay, el);
        assertEquals(selReplay, sel);
        assertEquals(felReplay, fel);
        assertEquals(relReplay, rel);
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

    public void testAddFromDeeperListPastMaxSize() {
        final EventList el = EventLists.eventList();
        final RangedEventList rel = createBackedRangedEventList(el);
        //rel.setStart(10);
        rel.setMaxSize(2);

        ListEventListener lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                    case 1:
                        assertEquals(new ListEvent(rel, ListEvent.ADDED, count - 1), listEvent);
                        break;
                    case 2:
                        assertEquals(new ListEvent(rel), listEvent);
                        break;
                    case 3:
                        assertNull(listEvent);
                        break;
                    default:
                        fail("Unexpected: " + listEvent);
                }
            }
        };
        rel.addListEventListener(lel);
        el.add("zero");
        el.add("one"); // FIXME: currently fails
        el.add("two");
        lel.listChanged(null);
        rel.removeListEventListener(lel);

    }

    public void testAddBeforeRangeStart() {
        // all elements are shifted down one
        final EventList el = EventLists.eventList();
        prefillWithIntegers(el, 100);

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
        prefillWithIntegers(el, 100);

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
        prefillWithIntegers(el, 100);

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
        prefillWithIntegers(el, 100);

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
        final EventList el = EventLists.eventList();
        prefillWithIntegers(el, 100);

        final RangedEventList rel = createBackedRangedEventList(el);
        rel.setStart(10);
        rel.setMaxSize(10);

        ListEventListener lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(new ListEvent(rel, ListEvent.CHANGED, 0), listEvent);
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
        el.set(10, "one");
        lel.listChanged(null);
        rel.removeListEventListener(lel);
    }

    public void testRemoveAtRangeStartViaDeeperList() {
        final EventList el = EventLists.eventList();
        prefillWithIntegers(el, 100);

        final RangedEventList rel = createBackedRangedEventList(el);
        rel.setStart(10);
        rel.setMaxSize(10);

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
        el.remove(10);
        lel.listChanged(null);
        rel.removeListEventListener(lel);

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
        el.remove(10);
        lel.listChanged(null);
        rel.removeListEventListener(lel);
    }

    public void testAddAtRangeStart() {
        final EventList el = EventLists.eventList();
        prefillWithIntegers(el, 100);

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
        final EventList el = EventLists.eventList();
        prefillWithIntegers(el, 100);

        final RangedEventList rel = createBackedRangedEventList(el);
        rel.setStart(10);
        rel.setMaxSize(10);

        ListEventListener lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(new ListEvent(rel, ListEvent.CHANGED, 0), listEvent);
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
        rel.set(0, "one");
        lel.listChanged(null);
        rel.removeListEventListener(lel);
    }

    public void testRemoveAtRangeStart() {
        final EventList el = EventLists.eventList();
        prefillWithIntegers(el, 100);

        final RangedEventList rel = createBackedRangedEventList(el);
        rel.setStart(10);
        rel.setMaxSize(10);

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
        rel.remove(0);
        lel.listChanged(null);
        rel.removeListEventListener(lel);

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
        rel.remove(0);
        lel.listChanged(null);
        rel.removeListEventListener(lel);
    }

    public void testAddAcrossRangeStart() {
        final EventList el = EventLists.eventList();
        prefillWithIntegers(el, 100);

        final RangedEventList rel = createBackedRangedEventList(el);
        rel.setStart(10);
        rel.setMaxSize(10);

        final List few = new ArrayList();
        prefillWithIntegers(few, 3);

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
        final EventList el = EventLists.eventList();
        prefillWithIntegers(el, 100);

        final RangedEventList rel = createBackedRangedEventList(el);
        rel.setStart(10);
        rel.setMaxSize(10);

        final List few = new ArrayList();
        few.addAll(el.subList(9,12));
        assertEquals(3, few.size());

        ListEventListener lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(new ListEvent(rel, ListEvent.REMOVED, 0, 3), listEvent);
                        break;
                    case 1:
                        assertEquals(new ListEvent(rel, ListEvent.ADDED, 7, 10), listEvent);
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
        el.removeAll(few);
        lel.listChanged(null);
        rel.removeListEventListener(lel);

        rel.setMaxSize(Integer.MAX_VALUE);
        few.clear();
        few.addAll(el.subList(9,12));
        assertEquals(3, few.size());

        lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(new ListEvent(rel, ListEvent.REMOVED, 0, 3), listEvent);
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
        el.removeAll(few);
        lel.listChanged(null);
        rel.removeListEventListener(lel);
    }

    public void testAddInRange() {
        final EventList el = EventLists.eventList();
        prefillWithIntegers(el, 100);

        final RangedEventList rel = createBackedRangedEventList(el);
        rel.setStart(10);
        rel.setMaxSize(10);

        final List few = new ArrayList();
        prefillWithIntegers(few, 3);

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
        final EventList el = EventLists.eventList();
        prefillWithIntegers(el, 100);

        final RangedEventList rel = createBackedRangedEventList(el);
        rel.setStart(10);
        rel.setMaxSize(10);

        ListEventListener lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(new ListEvent(rel, ListEvent.CHANGED, 5), listEvent);
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
        rel.set(5, "one");
        lel.listChanged(null);
        rel.removeListEventListener(lel);
    }

    public void testRemoveInRange() {
        final EventList el = EventLists.eventList();
        prefillWithIntegers(el, 100);

        final RangedEventList rel = createBackedRangedEventList(el);
        rel.setStart(10);
        rel.setMaxSize(10);

        ListEventListener lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(new ListEvent(rel, ListEvent.REMOVED, 4), listEvent);
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
        rel.remove(4);
        lel.listChanged(null);
        rel.removeListEventListener(lel);

        rel.setMaxSize(Integer.MAX_VALUE);

        lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(new ListEvent(rel, ListEvent.REMOVED, 6), listEvent);
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
        rel.remove(6);
        lel.listChanged(null);
        rel.removeListEventListener(lel);
    }

    public void testRemoveWhenSizeIsMaxSize() {
        final EventList el = EventLists.eventList();
        prefillWithIntegers(el, 10);

        final RangedEventList rel = createBackedRangedEventList(el);
        //rel.setStart(10);
        rel.setMaxSize(10);

        ListEventListener lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(new ListEvent(rel, ListEvent.REMOVED, 4), listEvent);
                        break;
                    case 1:
                        assertNull("was: " + listEvent, listEvent);
                        break;
                    default:
                        fail("Unexpected: " + listEvent);
                }
            }
        };
        rel.addListEventListener(lel);
        rel.remove(4);
        lel.listChanged(null);
        rel.removeListEventListener(lel);
    }

    public void testAddAcrossMaxRange() {
        final EventList el = EventLists.eventList();
        prefillWithIntegers(el, 100);

        final RangedEventList rel = createBackedRangedEventList(el);
        rel.setStart(10);
        rel.setMaxSize(10);

        final List few = new ArrayList();
        prefillWithIntegers(few, 3);

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
        final EventList el = EventLists.eventList();
        prefillWithIntegers(el, 100);

        final RangedEventList rel = createBackedRangedEventList(el);
        rel.setStart(10);
        rel.setMaxSize(10);

        final List few = new ArrayList();
        few.addAll(el.subList(19, 22));
        assertEquals(3, few.size());

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
        el.addAll(19, few);
        lel.listChanged(null);
        rel.removeListEventListener(lel);
    }

    public void testRemoveAcrossMaxRange2() {
        final EventList el = EventLists.eventList();
        final List elReplay = new EventListReplayList(el);
        prefillWithIntegers(el, 4);

        final RangedEventList rel = createBackedRangedEventList(el);
        final List relReplay = new EventListReplayList(rel);
        rel.setMaxSize(2);

        assertEquals(elReplay, el);
        assertEquals(relReplay, rel);

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
        //rel.addListEventListener(lel);
        int end = el.size();
        int[] sizes = new int[] {2, 2, 2, 1};
        for (int i=0; i < sizes.length; i++) {
            el.remove(0);
            assertEquals("i: " + i, sizes[i], rel.size());
        }
        //lel.listChanged(null);
        //rel.removeListEventListener(lel);

        assertEquals(elReplay, el);
        assertEquals(relReplay, rel);
    }

    public void testAddAfterMaxRange() {
        final EventList el = EventLists.eventList();
        prefillWithIntegers(el, 100);

        final RangedEventList rel = createBackedRangedEventList(el);
        rel.setStart(10);
        rel.setMaxSize(10);

        final List few = new ArrayList();
        prefillWithIntegers(few, 3);

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
        final EventList el = EventLists.eventList();
        prefillWithIntegers(el, 100);

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
        el.set(50, "one");
        lel.listChanged(null);
        rel.removeListEventListener(lel);
    }

    public void testRemoveAfterMaxRange() {
        final EventList el = EventLists.eventList();
        prefillWithIntegers(el, 100);

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
        el.remove(50);
        lel.listChanged(null);
        rel.removeListEventListener(lel);
    }
}
