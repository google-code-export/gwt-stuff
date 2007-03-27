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
        prefill(el, 3);
        final EventList rel = createBackedEventList(el);

        assertEquals(el.get(0), rel.get(2));
        assertEquals(el.get(1), rel.get(1));
        assertEquals(el.get(2), rel.get(0));

        el.clear();

        final List l = new ArrayList();
        prefill(l, 3);

        rel.addAll(l);

        assertEquals(el.get(0), rel.get(2));
        assertEquals(el.get(1), rel.get(1));
        assertEquals(el.get(2), rel.get(0));
    }

    public void testEventsAreReversed() {
        final EventList el = EventLists.eventList();
        prefill(el, 3);
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
                        prefill(l, 3);
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
        prefill(l, 3);

        assertEquals(0, el.size());
        el.addAll(l);
    }

    public void testGet() {
        fail("Implement me.");
    }

    public void testSet() {
        fail("Implement me.");
    }

    public void testRemove() {
        fail("Implement me.");
    }
}
