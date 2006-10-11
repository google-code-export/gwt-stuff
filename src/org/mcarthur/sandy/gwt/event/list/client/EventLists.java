package org.mcarthur.sandy.gwt.event.list.client;

import java.util.List;

/**
 * Static methods that operate on or return {@link EventList}s.
 *
 * @author Sandy McArthur
 */
public class EventLists {
    /**
     * Wrap a <code>List</code> so it can be monitored for changes. The list to be wrapped must not
     * be modified except by methods of the returned EventList else events will be missed.
     * If <code>list</code> is already an instace of <code>EventList</code> it will not be wrapped
     * again.
     *
     * @param list the list to wrap in an <code>EventList</code>.
     * @return an EventList wrapping list.
     */
    public static EventList wrap(final List list) {
        if (list instanceof EventList) {
            return (EventList)list;
        } else {
            return new WrappedEventList(list);
        }
    }
}
