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

/**
 * Tests for {@link org.mcarthur.sandy.gwt.event.list.client.FilteredEventList}.
 *
 * @author Sandy McArthur
 */
public class FilteredEventListTest extends TransformedEventListTest {

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
}
