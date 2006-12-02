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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * ALPHA, do not use yet.
 * An EventList that maintains a sorted order of objects add to the list.
 * This List implementation doesn't maintain all of the List contracts.
 * Mainly, any method that adds or sets to a specific index ignores the index parameter so as to
 * maintain sorted order.
 *
 * @author Sandy McArthur
 */
class SortedEventListImpl extends WrappedEventList implements SortedEventList {
    private Comparator comparator;

    private static final Comparator NATURAL = new Comparator() {
        public int compare(final Object o1, final Object o2) {
            return ((Comparable)o1).compareTo(o2);
        }
    };


    public SortedEventListImpl() {
        this(null);
    }

    public SortedEventListImpl(final Comparator comparator) {
        super(new ArrayList());
        setComparator(comparator);
    }

    public boolean add(final Object element) {
        int lower = 0;
        int upper = super.size() - 1;

        while (lower <= upper) {
            final int middle = (lower + upper) >> 1;
            final Object o = super.get(middle);

            final int c = comparator.compare(o, element);

            if (c < 0) {
                lower = middle + 1;
            } else if (c > 0) {
                upper = middle - 1;
            } else {
                super.add(middle, element);
                return true;
            }

        }
        super.add(lower, element);
        return true;
    }

    /**
     * Same as {@link #add(Object)}.
     *
     * @param index <em>ignored</em>
     * @see #add(Object)
     */
    public void add(final int index, final Object element) {
        add(element);
    }

    public boolean addAll(final Collection c) {
        final Iterator iter = c.iterator();
        while (iter.hasNext()) {
            add(iter.next()); // XXX Optimze
        }
        return c.size() > 0;
    }

    /**
     * Same as {@link #addAll(java.util.Collection)}.
     *
     * @param index <em>ignored</em>
     * @see #addAll(java.util.Collection)
     */
    public boolean addAll(final int index, final Collection c) {
        return addAll(c);
    }

    /**
     * Same as remove(index) and add(Object).
     *
     * @param index position of removed element.
     * @param element object to be added.
     * @return the removed element.
     * @see #remove(int) 
     * @see #add(Object) 
     */
    public Object set(final int index, final Object element) {
        final Object removed = super.remove(index);
        add(element);
        return removed;
    }

    public Comparator getComparator() {
        return comparator != NATURAL ? comparator : null;
    }

    /**
     * Set the sort order for this list.
     *
     * @param comparator sort order for this list, null for natural ordering.
     */
    public void setComparator(Comparator comparator) {
        comparator = comparator != null ? comparator : NATURAL;
        if (this.comparator != comparator) {
            this.comparator = comparator;
            sort();
        }
    }

    public void sort() {
        if (size() == 0) {
            // nothing to sort
            return;
        }
        final List all = new ArrayList();
        all.addAll(this);
        Collections.sort(all, comparator);

        // XXX: Optimize me
        if (true) {
            clear();

        } else {
            // try to keep runs
            final Iterator thisIter = this.iterator();
            final Iterator sortIter = all.iterator();
            while (sortIter.hasNext()) {
                final Object thisObj = thisIter.next();
                final Object sortObj = sortIter.next();

                if (thisObj != sortObj) {
                    thisIter.remove();
                } else {
                    sortIter.remove();
                }
            }
        }
        addAll(all);
    }
}
