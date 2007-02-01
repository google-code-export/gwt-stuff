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

    public void testRemoveAll() {
        fail("Not yet implemented.");
    }
    
    public void testRetainAll() {
        fail("Not yet implemented.");
    }
}
