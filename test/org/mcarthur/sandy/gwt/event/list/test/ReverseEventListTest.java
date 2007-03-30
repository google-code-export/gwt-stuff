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
import org.mcarthur.sandy.gwt.event.list.client.ListEvent;
import org.mcarthur.sandy.gwt.event.list.client.ListEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Tests for "ReverseEventList".
 *
 * @author Sandy McArthur
 */
public class ReverseEventListTest extends EventListTest {

    protected EventList createEmptyEventLists() {
        return createBackedEventList(EventLists.eventList());
    }

    protected EventList createBackedEventList(final EventList el) {
        return EventLists.reverseEventList(el);
    }

    public void testIsReversed() {
        final EventList el = EventLists.eventList();
        prefillWithIntegers(el, 3);
        final EventList rel = createBackedEventList(el);

        assertEquals(el.get(0), rel.get(2));
        assertEquals(el.get(1), rel.get(1));
        assertEquals(el.get(2), rel.get(0));

        el.clear();

        final List l = new ArrayList();
        prefillWithIntegers(l, 3);

        rel.addAll(l);

        assertEquals(el.get(0), rel.get(2));
        assertEquals(el.get(1), rel.get(1));
        assertEquals(el.get(2), rel.get(0));
    }

    public void testEventsAreReversed() {
        final EventList el = EventLists.eventList();
        prefillWithIntegers(el, 3);
        final EventList rel = createBackedEventList(el);

        rel.addListEventListener(new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(new ListEvent(rel,ListEvent.ADDED, 0), listEvent);
                        assertEquals("one", listEvent.getSourceList().get(listEvent.getIndexStart()));
                        break;
                    case 1:
                        assertEquals(new ListEvent(rel, ListEvent.ADDED, rel.size()-1), listEvent);
                        assertEquals("two", listEvent.getSourceList().get(listEvent.getIndexStart()));
                        break;
                    case 2:
                        assertEquals(new ListEvent(rel, ListEvent.REMOVED, 0), listEvent);
                        break;
                    case 3:
                        // rel.size has already shrunk by one at this point. 
                        assertEquals(new ListEvent(rel, ListEvent.REMOVED, rel.size()), listEvent);
                        break;
                    default:
                        fail(listEvent.toString() + ", count: " + (count-1));
                        break;
                }
            }
        });

        el.add("one"); // 0
        el.add(0, "two"); // 1

        el.remove("one"); // 2
        el.remove("two"); // 3
    }


    public void testAdd() {
        super.testAdd();

        final EventList el = EventLists.eventList();
        prefillWithIntegers(el, 5);
        final EventList rel = createBackedEventList(el);

        ListEventListener lel = new ListEventListener() {
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
        ListEventListener rlel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(new ListEvent(rel, ListEvent.ADDED, 5), listEvent);
                        break;
                    case 1:
                        assertNull(listEvent);
                        break;
                    default:
                        fail("Unexpected: " + listEvent);
                }
            }
        };
        el.addListEventListener(lel);
        rel.addListEventListener(rlel);
        rel.add("one");
        lel.listChanged(null);
        rlel.listChanged(null);
        el.removeListEventListener(lel);
        rel.removeListEventListener(rlel);


        lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(new ListEvent(rel, ListEvent.ADDED, 5), listEvent);
                        break;
                    case 1:
                        assertNull(listEvent);
                        break;
                    default:
                        fail("Unexpected: " + listEvent);
                }
            }
        };
        rlel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(new ListEvent(rel, ListEvent.ADDED, 1), listEvent);
                        break;
                    case 1:
                        assertNull(listEvent);
                        break;
                    default:
                        fail("Unexpected: " + listEvent);
                }
            }
        };
        el.addListEventListener(lel);
        rel.addListEventListener(rlel);
        rel.add(1, "two");
        lel.listChanged(null);
        rlel.listChanged(null);
        el.removeListEventListener(lel);
        rel.removeListEventListener(rlel);
    }

    public void testAddAll() {
        final EventList el = EventLists.eventList();
        final EventList rel = createBackedEventList(el);

        rel.addListEventListener(new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(new ListEvent(rel,ListEvent.ADDED, 0, 3), listEvent);
                        final List l = new ArrayList();
                        prefillWithIntegers(l, 3);
                        Collections.reverse(l);
                        assertEquals(l, listEvent.getSourceList());
                        break;
                    default:
                        fail(listEvent.toString() + ", count: " + (count-1));
                        break;
                }
            }
        });

        final List l = new ArrayList();
        prefillWithIntegers(l, 3);

        assertEquals(0, el.size());
        el.addAll(l);
    }

    public void testGet() {
        super.testGet();

        final EventList el = EventLists.eventList();
        prefillWithIntegers(el, 3);
        final EventList rel = createBackedEventList(el);


        assertEquals(el.get(0), rel.get(2));
        assertEquals(el.get(1), rel.get(1));
        assertEquals(el.get(2), rel.get(0));

        try {
            rel.get(3);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException iobe) {
            // expected
        }
        try {
            rel.get(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException iobe) {
            // expected
        }
    }

    public void testSet() {
        super.testSet();

        final EventList el = EventLists.eventList();
        prefillWithIntegers(el, 3);
        final EventList rel = createBackedEventList(el);

        assertEquals(3, rel.size());

        rel.set(2, "two");
        assertEquals("two", el.get(0));
        assertFalse(el.contains(Integer.valueOf(0)));

        rel.set(0, "zero");
        assertEquals("zero", el.get(2));
        assertFalse(el.contains(Integer.valueOf(2)));

        assertEquals(3, el.size());
        assertEquals(3, rel.size());

        try {
            rel.set(-1, Integer.valueOf(-1));
            fail("Expected IndexOutOfBoundsException.");
        } catch (IndexOutOfBoundsException iobe) {
            // expected
        }
        assertFalse(el.contains(Integer.valueOf(-1)));

        try {
            rel.set(3, Integer.valueOf(3));
            fail("Expected IndexOutOfBoundsException.");
        } catch (IndexOutOfBoundsException iobe) {
            // expected
        }
        assertFalse(el.contains(Integer.valueOf(3)));
    }

    public void testRemove() {
        final EventList el = EventLists.eventList();
        prefillWithIntegers(el, 3);
        final EventList rel = createBackedEventList(el);

        assertEquals(Integer.valueOf(0), rel.remove(2));
        assertEquals(Integer.valueOf(2), rel.remove(0));

        try {
            rel.remove(-1);
            fail("Expected IndexOutOfBoundsException.");
        } catch (IndexOutOfBoundsException iobe) {
            // expected
        }

        try {
            rel.remove(rel.size());
            fail("Expected IndexOutOfBoundsException.");
        } catch (IndexOutOfBoundsException iobe) {
            // expected
        }
    }
}
