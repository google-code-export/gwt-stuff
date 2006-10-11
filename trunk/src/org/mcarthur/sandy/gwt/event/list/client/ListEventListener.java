package org.mcarthur.sandy.gwt.event.list.client;

import java.util.EventListener;

/**
 * Observer interface for EventLists.
 *
 * @author Sandy McArthur
 */
public interface ListEventListener extends EventListener {

    /**
     * Invoked when the list is modified.
     *
     * @param listEvent event information.
     */
    public void listChanged(ListEvent listEvent);
}
