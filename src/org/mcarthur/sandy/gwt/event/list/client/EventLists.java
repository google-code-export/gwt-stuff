/*
 * Copyright 2006 Sandy McArthur, Jr.
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

package org.mcarthur.sandy.gwt.event.list.client;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Static methods that operate on or return {@link EventList}s.
 *
 * @author Sandy McArthur
 */
public class EventLists {
    private EventLists() {
    }

    /**
     * Create a new EventList.
     *
     * @return a new EventList.
     */
    public static EventList eventList() {
        return wrap(new ArrayList());
    }

    /**
     * Creates a FilteredEventList.
     *
     * @return An EventList that can be filtered.
     */
    public static FilteredEventList filteredEventList() {
        return filteredEventList(eventList());
    }

    /**
     * Creates a filterable view of another EventList.
     * Changes to <code>eventList</code> will be automatically reflected in the view and
     * modifications to the FilteredEventList's elements will be propagated to <code>eventList</code>.
     *
     * @param eventList the event list to create a filterable view of.
     * @return A view over eventList that can be filtered.
     */
    public static FilteredEventList filteredEventList(final EventList eventList) {
        return filteredEventList(eventList, null);
    }

    /**
     * Creates a FilteredEventList with an initial filter.
     *
     * @param filter the filter to initially select the elements presented by the view.
     * @return A view over eventList that is filtered with <code>filter</code>.
     */
    public static FilteredEventList filteredEventList(final FilteredEventList.Filter filter) {
        return new FilteredEventListImpl(eventList(), filter);
    }

    /**
     * Creates a filterable view of another EventList with an initial filter.
     * Changes to <code>eventList</code> will be automatically reflected in the view and
     * modifications to the FilteredEventList's elements will be propagated to <code>eventList</code>.
     *
     * @param eventList the event list to create a filterable view of.
     * @param filter the filter to initially select the elements presented by the view.
     * @return A view over eventList that is filtered with <code>filter</code>.
     */
    public static FilteredEventList filteredEventList(final EventList eventList, final FilteredEventList.Filter filter) {
        return new FilteredEventListImpl(eventList, filter);
    }

    public static PaginatedEventList paginatedEventList() {
        return paginatedEventList(eventList());
    }

    public static PaginatedEventList paginatedEventList(final EventList eventList) {
        return paginatedEventList(eventList, Integer.MAX_VALUE);
    }

    public static PaginatedEventList paginatedEventList(final EventList eventList, final int maxSize) {
        return new PaginatedEventListImpl(eventList, maxSize);
    }

    /**
     * Create a <code>SortedEventList</code> that defaults to natural ordering.
     *
     * @return a <code>SortedEventList</code>.
     * @see Comparable
     * @see SortedEventList#setComparator(Comparator)
     * @see #sortedEventList(EventList)
     * @see #sortedEventList(java.util.Comparator)
     * @see #sortedEventList(EventList, java.util.Comparator)
     */
    public static SortedEventList sortedEventList() {
        return sortedEventList(eventList());
    }

    /**
     * Create a sorted view of another EventList.
     * Changes to <code>eventList</code> will be automatically reflected in the view and
     * modifications to the SortedEventList's elements will be propagated to <code>eventList</code>.
     *
     * @param eventList the event list to create a sorted view of.
     * @return a view over eventList that can be sorted.
     * @see Comparable
     * @see SortedEventList#setComparator(Comparator)
     * @see #sortedEventList()
     * @see #sortedEventList(java.util.Comparator)
     * @see #sortedEventList(EventList, java.util.Comparator)
     */
    public static SortedEventList sortedEventList(final EventList eventList) {
        return sortedEventList(eventList, null);
    }

    /**
     * Create a sorted event list with an initial sort order.
     *
     * @param comparator the Comparator used to sort the list, if <code>null</code> natural ordering is used.
     * @return an empty sorted event list with an intial sort order.
     * @see SortedEventList#setComparator(Comparator)
     * @see #sortedEventList()
     * @see #sortedEventList(EventList)
     * @see #sortedEventList(EventList, java.util.Comparator)
     */
    public static SortedEventList sortedEventList(final Comparator comparator) {
        return sortedEventList(eventList(), comparator);
    }

    /**
     * Create a sorted view of another EventList with an initial sort order.
     * Changes to <code>eventList</code> will be automatically reflected in the view and
     * modifications to the SortedEventList's elements will be propagated to <code>eventList</code>.
     *
     * @param eventList the event list to create a sorted view of.
     * @param comparator the Comparator used to sort the list, if <code>null</code> natural ordering is used.
     * @return a sorted view of <code>eventList</code> with an intial sort order.
     * @see SortedEventList#setComparator(Comparator)
     * @see #sortedEventList()
     * @see #sortedEventList(EventList)
     * @see #sortedEventList(java.util.Comparator)
     */
    public static SortedEventList sortedEventList(final EventList eventList, final Comparator comparator) {
        return new SortedEventListImpl(eventList, comparator);
    }

    /**
     * Wrap a <code>List</code> so it can be monitored for changes.
     * <p>
     * The list to be wrapped <strong>must not</strong> be modified except by the returned EventList
     * else events will be missed and the internal states will get corrupted.
     * </p>
     * <p>
     * If <code>list</code> is already an instace of <code>EventList</code> it will not be wrapped
     * again.
     * </p>
     * <p>
     * If it is possible to use {@link #eventList()} to create a new EventList and populate it with
     * {@link EventList#addAll(java.util.Collection)} then that is prefered over using the
     * <code>wrap</code> method to obtain a EventList of existing data. 
     * </p>
     *
     * @param list the list to wrap in an <code>EventList</code>.
     * @return an EventList wrapping list.
     * @see #eventList()
     */
    public static EventList wrap(final List list) {
        if (list instanceof EventList) {
            return (EventList)list;
        } else if (list != null) {
            return new WrappedEventList(list);
        } else {
            throw new IllegalArgumentException("list must not be null.");
        }
    }
}
