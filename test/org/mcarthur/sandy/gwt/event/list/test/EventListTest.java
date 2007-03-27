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

import java.util.Collections;
import java.util.List;

/**
 * Tests for {@link org.mcarthur.sandy.gwt.event.list.client.EventList}.
 *
 * @author Sandy McArthur
 */
public abstract class EventListTest extends TestCase {

    protected abstract EventList createEmptyEventLists();

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

    protected void prefill(final List list, final int count) {
        for (int i=0; i < count; i++) {
            list.add(Integer.valueOf(i));
        }
    }
}
