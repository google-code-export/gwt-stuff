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


    public void testInsertBeforeRangeStart() {
        // Insert before start doesn't affect visible elements
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

        for (int i=0; i < 10; i++) {
            el.add(new Integer(i));
        }

        rel.setStart(5);
        rel.setMaxSize(100);

        el.remove(0);

        assertEquals(4, rel.getStart());
        assertEquals(5, rel.size());
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
