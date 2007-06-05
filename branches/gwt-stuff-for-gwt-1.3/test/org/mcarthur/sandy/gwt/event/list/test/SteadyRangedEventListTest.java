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
import org.mcarthur.sandy.gwt.event.list.client.RangedEventList;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for the steady {@link org.mcarthur.sandy.gwt.event.list.client.RangedEventList}.
 *
 * @author Sandy McArthur
 */
public class SteadyRangedEventListTest extends RangedEventListTest {

    protected EventList createEmptyEventLists() {
        return createBackedEventList(EventLists.eventList());
    }

    protected EventList createBackedEventList(final EventList el) {
        return EventLists.steadyRangedEventList(el);
    }


    public void testAddBeforeRangeStart() {
        // Insert before start doesn't affect visible elements
        final EventList el = EventLists.eventList();
        prefillWithIntegers(el, 100);

        final RangedEventList rel = createBackedRangedEventList(el);
        rel.setStart(10);
        rel.setMaxSize(10);

        final ListEventListener lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(ListEvent.createOther(rel), listEvent);
                        break;
                    default:
                        fail("Unexpected: " + listEvent);
                }
            }
        };
        rel.addListEventListener(lel);
        el.add(0, "one");
        rel.removeListEventListener(lel);
    }

    public void testRemoveBeforeStartOfRange() {
        final EventList el = EventLists.eventList();
        final RangedEventList rel = createBackedRangedEventList(el);

        prefillWithIntegers(el, 10);

        rel.setStart(5);
        rel.setMaxSize(100);

        el.remove(0);

        assertEquals(4, rel.getStart());
        assertEquals(5, rel.size());
    }

    public void testRemoveBeforeRangeStart() {
        // all elements are shifted up one
        final EventList el = EventLists.eventList();
        prefillWithIntegers(el, 100);

        final RangedEventList rel = createBackedRangedEventList(el);
        rel.setStart(10);
        rel.setMaxSize(10);
        assertEquals(10, rel.size());

        // if the max size is in effect, the last element(s) is removed and the first is added
        ListEventListener lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(ListEvent.createOther(rel), listEvent);
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
        assertEquals(10, rel.size());
        lel.listChanged(null);
        rel.removeListEventListener(lel);

        // if max size doesn't have an effect then element(s) are removed before the start
        rel.setMaxSize(Integer.MAX_VALUE);
        lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(ListEvent.createOther(rel), listEvent);
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

    public void testRemoveRangeSpanningStartOfRange() {
        final EventList el = EventLists.eventList();
        final RangedEventList rel = createBackedRangedEventList(el);

        for (int i=0; i < 10; i++) {
            el.add(new Integer(i));
        }

        rel.setStart(5);

        final List middle = new ArrayList();
        for (int i=3; i < 7; i++) {
            middle.add(el.get(i));
        }

        el.removeAll(middle);

        assertEquals(3, rel.getStart());
        assertEquals(3, rel.size());
    }

    public void testRemoveAfterStartOfRange() {
        final EventList el = EventLists.eventList();
        final RangedEventList rel = createBackedRangedEventList(el);

        for (int i=0; i < 10; i++) {
            el.add(new Integer(i));
        }

        rel.setStart(5);

        el.remove(8);

        assertEquals(5, rel.getStart());
        assertEquals(4, rel.size());
    }
}
