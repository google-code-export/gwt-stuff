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
import org.mcarthur.sandy.gwt.event.list.client.RangedEventList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sandymac
 * Date: Feb 1, 2007
 * Time: 12:33:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class RangedEventListTest extends TransformedEventListTest {

    public void testSetStart() {
        EventList el = EventLists.eventList();
        el.add("one");
        el.add("two");
        RangedEventList rel = EventLists.rangedEventList(el);

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
        EventList el = EventLists.eventList();
        el.add("one");
        el.add("two");
        RangedEventList rel = EventLists.rangedEventList(el);

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
        EventList el = EventLists.eventList();
        el.add("one");
        el.add("two");
        RangedEventList rel = EventLists.rangedEventList(el);

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
        List all = new ArrayList();
        all.add("one");
        all.add("two");

        EventList el = EventLists.eventList();
        el.addAll(all);
        RangedEventList rel = EventLists.rangedEventList(el);

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

        EventList el = EventLists.eventList();
        el.addAll(all);
        RangedEventList rel = EventLists.rangedEventList(el);


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

        EventList el = EventLists.eventList();
        el.addAll(all);
        RangedEventList rel = EventLists.rangedEventList(el);

        rel.setStart(1);
        rel.removeAll(all);

        assertEquals(1, el.size());
        assertEquals(0, rel.size());
    }
    
    public void testRetainAll() {
        final List all = new ArrayList();
        all.add("one");
        all.add("two");

        EventList el = EventLists.eventList();
        el.addAll(all);
        RangedEventList rel = EventLists.rangedEventList(el);

        rel.setStart(1);

        List two = new ArrayList();
        two.add("two");

        rel.retainAll(two);

        assertEquals(1, rel.size());
        assertEquals(all.size(), el.size());
    }
}
