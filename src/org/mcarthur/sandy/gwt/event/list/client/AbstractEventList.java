package org.mcarthur.sandy.gwt.event.list.client;

import java.util.AbstractList;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A skeletal implementation of the EventList interface.
 *
 * @author Sandy McArthur
 * @see java.util.AbstractList
 */
public abstract class AbstractEventList extends AbstractList implements EventList {
    private final List listeners = new ArrayList();

    public void addListEventListener(final ListEventListener listEventListener) {
        listeners.add(listEventListener);
    }

    public void removeListEventListener(final ListEventListener listEventListener) {
        listeners.remove(listEventListener);
    }

    /**
     * Signals each listener of an event.
     *
     * @param listEvent the event to signal.
     */
    protected void fireListEvent(final ListEvent listEvent) {
        final Iterator iter = listeners.iterator();
        while (iter.hasNext()) {
            final ListEventListener listEventListener = (ListEventListener)iter.next();
            listEventListener.listChanged(listEvent);
        }
    }
}
