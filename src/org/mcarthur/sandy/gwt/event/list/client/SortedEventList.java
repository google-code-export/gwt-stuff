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
import java.util.Comparator;

/**
 * An EventList that maintains a sorted order of objects add to the list.
 * This implementation of List does not maintain all of the List contracts.
 * Mainly, for any method that adds or sets to a specific index the index is ignored to
 * maintain the elements in sorted order.
 *
 * @author Sandy McArthur
 */
public interface SortedEventList extends EventList {

    /**
     * A Comparator that implements
     * {@link Comparable natural ordering}.
     */
    public static final Comparator NATURAL = new Comparator() {
        public int compare(final Object o1, final Object o2) {
            return ((Comparable)o1).compareTo(o2);
        }
    };

    /**
     * Return the Comparator used to sort this list.
     * If this returns <code>null</code> then this list uses
     * <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/Comparable.html">natural ordering</a>.
     *
     * @return the Comparator used to sort the list, else <code>null</code> if natural ordering is used.
     */
    public Comparator getComparator();

    /**
     * Set the Comparator used to sort this list.
     * If <code>comparator</code> is <code>null</code> then
     * <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/Comparable.html">natural ordering</a>
     * is used.
     * Setting this to a new value causes the list to be resorted.
     *
     * @param comparator the Comparator used to sort the list, if <code>null</code> natural ordering is used.
     */
    public void setComparator(Comparator comparator);

    /**
     * Force the list to be resorted.
     */
    public void resort();

    /**
     * The has the same behavior as {@link #add(Object)}.
     * @param index <em>ignored</em>.
     * @see #add(Object)
     */
    public void add(int index, Object element);

    /**
     * This has the same behavior as {@link #addAll(Collection)}.
     * @param index <em>ignored</em>.
     * @see #addAll(Collection)
     */
    public boolean addAll(int index, Collection c);

    /**
     * Behaves as if remove(index) and then add(Object) were called.
     * @see #remove(int)
     * @see #add(Object)
     */
    public Object set(int index, Object element);
}
