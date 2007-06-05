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

import junit.framework.TestCase;
import org.mcarthur.sandy.gwt.event.list.client.EventList;
import org.mcarthur.sandy.gwt.event.list.client.ListEvent;
import org.mcarthur.sandy.gwt.event.list.client.ListEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Tests for {@link org.mcarthur.sandy.gwt.event.list.client.EventList}.
 *
 * @author Sandy McArthur
 */
public abstract class EventListTest extends TestCase {

    protected abstract EventList createEmptyEventLists();

    protected final void prefillWithIntegers(final List list, final int count) {
        for (int i=0; i < count; i++) {
            list.add(Integer.valueOf(i));
        }
    }

    public void testClearOnEmptyList() {
        final List l = createEmptyEventLists();
        l.clear();
    }

    public void testAddAllWithEmptyList() {
        final EventList el = createEmptyEventLists();
        el.addAll(Collections.EMPTY_LIST);
        el.add(new Object());
        el.addAll(0, Collections.EMPTY_LIST);
    }

    public void testAdd() {
        final EventList el = createEmptyEventLists();

        ListEventListener lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        // this should be the only elelemt, thus index == 0
                        assertEquals(ListEvent.createAdded(el, 0), listEvent);
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
        el.add("one");
        lel.listChanged(null);
        el.removeListEventListener(lel);

        lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        // Because of EventLists that change the order we cannot check the indexes
                        assertTrue("Expected ADDED: " + listEvent, listEvent.isAdded());
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
        el.add(0, "two");
        lel.listChanged(null);
        el.removeListEventListener(lel);

        try {
            el.add(el.size()+1, "three");
            fail("Expected IndexOutOfBoundsException.");
        } catch (IndexOutOfBoundsException iobe) {
            // expected
        }
    }

    public void testGet() {
        final EventList el = createEmptyEventLists();
        el.add("one");
        assertEquals("one", el.get(0));
        // anything else?
    }

    public void testRemove() {
        final EventList el = createEmptyEventLists();
        el.add("one");

        final ListEventListener lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        // this should be the only element, thus index == 0
                        assertEquals(ListEvent.createRemoved(el, 0), listEvent);
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
        el.remove(0);
        lel.listChanged(null);
        el.removeListEventListener(lel);

        assertEquals(0, el.size());

        try {
            el.remove(0);
            fail("EventList.remove(int) should have thrown an IndexOutOfBoundsException.");
        } catch (IndexOutOfBoundsException iobe) {
            // expected
        }
    }

    public void testSet() {
        final EventList el = createEmptyEventLists();
        try {
            el.set(0, "zero");
            fail("EventList.set(int,Object) should have thrown an IndexOutOfBoundsException.");
        } catch (IndexOutOfBoundsException iobe) {
            // expected
        }

        el.add("one");
        assertTrue(el.contains("one"));

        final ListEventListener lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        // this should be the only element, thus index == 0
                        assertEquals(ListEvent.createChanged(el, 0), listEvent);
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
        el.set(0, "two");
        lel.listChanged(null);
        el.removeListEventListener(lel);

        assertFalse(el.contains("one"));
        assertTrue(el.contains("two"));
    }

    public void testSize() {
        final EventList el = createEmptyEventLists();
        for (int i=0; i < 10; i++) {
            assertEquals(i, el.size());
            el.add(Integer.valueOf(i));
        }
        assertEquals(10, el.size());
    }

    public void testRemoveAll() {
        final List l = new ArrayList();
        final EventList el = createEmptyEventLists();
        final List replay = new EventListReplayList(el);

        prefillWithIntegers(l, 150);

        Collections.shuffle(l, new Random(8482));

        el.addAll(l);

        Collections.shuffle(l, new Random(4372));

        assertTrue(l.containsAll(el));
        assertTrue(el.containsAll(l));

        final List sub = new ArrayList(l.subList(50, 100));

        l.removeAll(sub);
        el.removeAll(sub);

        final List a = new ArrayList(l);
        final List b = new ArrayList(el);
        Collections.sort(a);
        Collections.sort(b);
        assertEquals(a, b);

        assertTrue(l.containsAll(el));
        assertTrue(el.containsAll(l));
        assertEquals(el, replay);
        assertTrue(l.containsAll(replay));
        assertTrue(replay.containsAll(l));
    }

    public void testRetainAll() {
        final List l = new ArrayList();
        final EventList el = createEmptyEventLists();
        final List replay = new EventListReplayList(el);

        prefillWithIntegers(l, 150);

        Collections.shuffle(l, new Random(1283));

        el.addAll(l);

        Collections.shuffle(l, new Random(5623));

        assertTrue(l.containsAll(el));
        assertTrue(el.containsAll(l));

        final List sub = new ArrayList(l.subList(50, 100));

        l.retainAll(sub);
        el.retainAll(sub);

        final List a = new ArrayList(l);
        final List b = new ArrayList(el);
        Collections.sort(a);
        Collections.sort(b);
        assertEquals(a, b);

        assertTrue(l.containsAll(el));
        assertTrue(el.containsAll(l));
        assertEquals(el, replay);
        assertTrue(l.containsAll(replay));
        assertTrue(replay.containsAll(l));
    }
}
