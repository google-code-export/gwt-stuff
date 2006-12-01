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

import java.util.Collection;
import java.util.Iterator;

/**
 * An EventList that presents a subset of the elements actually in the list.
 * This implementation of List does not maintain all of the List contracts.
 *
 * <p>
 * Elements in this list will be in one of two groups, filtered and unfiltered.
 * Filtered elements are not visable via this list and many methods do not act on them.
 * Unfiltered elements are visable via this list.
 *
 * If the filter is changed the
 * </p>
 *
 * @author Sandy McArthur
 */
public interface FilteredEventList extends EventList {

    public Filter getFilter();
    public void setFilter(Filter filter);

    /**
     * Force the list to be (re)filtered.
     * This should be invoked if the elements in the list are mutable and one or more of them have changed.
     */
    public void filter();


    /**
     * Appends the specified element to the end of this list.
     *
     * If the element is not acepted by the filter it will still be added to the list, though not viewable.
     */
    public boolean add(Object o);

    /**
     * Inserts the specified element at the specified position in this list.
     *
     * If the element is not acepted by the filter it will still be added to the list, though not viewable.
     */
    public void add(int index, Object element);

    /**
     * Appends all of the elements in the specified collection to the end of this list.
     *
     * If elements are not acepted by the filter it will still be added to the list, though not viewable.
     */
    public boolean addAll(Collection c);

    /**
     * Inserts all of the elements in the specified collection into this list at the specified position.
     *
     * If elements are not acepted by the filter it will still be added to the list, though not viewable.
     */
    public boolean addAll(int index, Collection c);

    /**
     * Removes all of the unfiltered elements from this list.
     */
    public void clear();

    /**
     * Returns <code>true</code> if this list contains the specified unfiltered element.
     */
    public boolean contains(Object o);

    /**
     * Returns <code>true</code> if all of the elements of the specified collection are also unfiltered
     * elements in this list.
     */
    public boolean containsAll(Collection c);

    /**
     * Returns the unfiltered element at the specified position in this list.
     */
    public Object get(int index);

    /**
     * Returns the unfiltered index in this list of the first occurrence of the specified
     * element, or -1 if this list does not contain this element or it is filtered.
     * The result of this methos is no longer valid after the filter has been changed.
     */
    public int indexOf(Object o);

    /**
     * Returns <code>true</code> if this list contains no unfiltered elements.
     *
     * @return <tt>true</tt> if this list contains no unfiltered elements.
     */
    public boolean isEmpty();

    /**
     * Returns an iterator over the unfiltered elements in this list.
     *
     * @return an iterator over the unfiltered elements in this list.
     */
    public Iterator iterator();

    /**
     * Returns the index in this list of the last unfiltered occurrence of the specified
     * element, or -1 if this list does not contain this element or it is filtered.
     * The result of this methos is no longer valid after the filter has been changed.
     */
    public int lastIndexOf(Object o);

    /**
     * Removes the unfiltered element at the specified position in this list.
     */
    public Object remove(int index);

    /**
     * Removes the first unfiltered occurrence in this list of the specified element.
     */
    public boolean remove(Object o);

    /**
     * Removes all the elements that are contained in the
     * specified collection from this list that are also unfiltered.
     */
    public boolean removeAll(Collection c);

    /**
     * Retains only the unfiltered elements in this list that are contained in the
     * specified collection.
     */
    public boolean retainAll(Collection c);

    /**
     * Replaces the unfiltered element at the specified position in this list with the
     * specified element.
     */
    public Object set(int index, Object element);

    /**
     * Returns the number of unfiltered elements in this list.
     */
    public int size();

    /**
     * Returns an array containing all of the unfiltered elements in this list.
     */
    public Object[] toArray();

    /**
     * A filter for elements in a FilteredEventList.
     */
    public static interface Filter {

        /**
         * Determines whether or not a given element should be presented by the FilteredEventList.
         *
         * @param element the element to test.
         * @return <code>true</code> if this element should be presented by the list (unflitered), else <code>false</code> (filtered).
         */
        public boolean accept(Object element);
    }
}
