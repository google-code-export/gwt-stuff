package org.mcarthur.sandy.gwt.event.list.client;

import java.util.List;

/**
 * An observable {@link List}.
 *
 * @author Sandy McArthur
 */
public interface EventList extends List {
    /**
     * Add another observer to this list.
     *
     * @param listEventListener the observer to add.
     */
    public void addListEventListener(ListEventListener listEventListener);

    /**
     * Remove an observer from this list.
     *
     * @param listEventListener the observer to remove.
     */
    public void removeListEventListener(ListEventListener listEventListener);
}
