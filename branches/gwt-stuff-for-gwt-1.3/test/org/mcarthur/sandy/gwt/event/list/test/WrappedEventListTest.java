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
import java.util.List;

/**
 * Tests for {@link org.mcarthur.sandy.gwt.event.list.client.WrappedEventList}.
 *
 * @author Sandy McArthur
 */
public class WrappedEventListTest extends EventListTest {
    protected EventList createEmptyEventLists() {
        return EventLists.eventList();
    }

    public void testAddAll() {
        final EventList el = createEmptyEventLists();
        prefillWithIntegers(el, 3);

        final List l = new ArrayList();
        prefillWithIntegers(l, 3);

        ListEventListener lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(ListEvent.createAdded(el, 3, 6), listEvent);
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
        el.addAll(l);
        lel.listChanged(null);
        el.removeListEventListener(lel);

        lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(ListEvent.createAdded(el, 4, 7), listEvent);
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
        el.addAll(4, l);
        lel.listChanged(null);
        el.removeListEventListener(lel);
    }
}
