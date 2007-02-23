/*
 * Copyright 2007 Sandy McArthur, Jr.
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

package org.mcarthur.sandy.gwt.table.client.alpha;

import org.mcarthur.sandy.gwt.event.list.client.AbstractEventList;
import org.mcarthur.sandy.gwt.event.list.client.DetachableEventList;
import org.mcarthur.sandy.gwt.event.list.client.EventList;
import org.mcarthur.sandy.gwt.event.list.client.EventLists;
import org.mcarthur.sandy.gwt.event.list.client.FilteredEventList;
import org.mcarthur.sandy.gwt.event.list.client.ListEvent;
import org.mcarthur.sandy.gwt.event.list.client.ListEventListener;
import org.mcarthur.sandy.gwt.event.list.client.RangedEventList;
import org.mcarthur.sandy.gwt.event.list.client.SortedEventList;

import java.util.Collection;
import java.util.Comparator;

/**
 * An EventList that encapuslates a stack of EventLists and allows that stack to be altered
 * as needed.
 *
 * @author Sandy McArthur
 */
class PropertyTableEventList extends AbstractEventList {

    private final EventList delegate;
    private ListEventListener lel = new PTListEventListener();

    /**
     * The top of this stack of EventLists.
     */
    private EventList el = null;

    private SortedEventList sel = null;
    private Comparator comparator = null;

    private FilteredEventList fel = null;
    private FilteredEventList.Filter filter = null;

    private RangedEventList rel = null;
    private int start = -1;
    private int maxSize = -1;

    private class PTListEventListener implements ListEventListener {
        public void listChanged(final ListEvent listEvent) {
            fireListEvent(listEvent.resource(PropertyTableEventList.this));
        }
    }

    public PropertyTableEventList(final EventList delegate) {
        el = this.delegate = delegate;
        delegate.addListEventListener(lel);
    }

    public void setComparator(final Comparator comparator) {
        if (this.comparator != comparator) {
            this.comparator = comparator;
            rebuildStackComparator();
        }
    }

    private void rebuildStackComparator() {
        //  sel    comp
        //  null  !null: new sel
        // !null   null: remove sel
        // !null  !null: update comp
        //  null   null: nothing
        if (sel == null && comparator != null) {
            // build the stack
            if (size() > 0) {
                fireListEvent(new ListEvent(this, ListEvent.REMOVED, 0, size()));
            }

            delegate.removeListEventListener(lel);

            sel = EventLists.sortedEventList(delegate, comparator);
            el = sel;

            if (fel != null) {
                fel.removeListEventListener(lel);
                ((DetachableEventList)fel).detach();
                fel = EventLists.filteredEventList(el, filter);
                el = fel;
            }

            if (rel != null) {
                rel.removeListEventListener(lel);
                ((DetachableEventList)rel).detach();
                rel = EventLists.steadyRangedEventList(el);
                rel.setStart(start);
                rel.setMaxSize(maxSize);
                el = rel;
            }

            el.addListEventListener(lel);
            if (size() > 0) {
                fireListEvent(new ListEvent(this, ListEvent.ADDED, 0, size()));
            }

        } else if (sel != null && comparator == null) {
            if (size() > 0) {
                fireListEvent(new ListEvent(this, ListEvent.REMOVED, 0, size()));
            }

            delegate.removeListEventListener(lel);
            el = delegate;

            sel.removeListEventListener(lel);
            ((DetachableEventList)sel).detach();

            if (fel != null) {
                fel.removeListEventListener(lel);
                ((DetachableEventList)fel).detach();
                fel = EventLists.filteredEventList(el, filter);
                el = fel;
            }

            if (rel != null) {
                rel.removeListEventListener(lel);
                ((DetachableEventList)rel).detach();
                rel = EventLists.steadyRangedEventList(el);
                rel.setStart(start);
                rel.setMaxSize(maxSize);
                el = rel;
            }

            el.addListEventListener(lel);
            if (size() > 0) {
                fireListEvent(new ListEvent(this, ListEvent.ADDED, 0, size()));
            }

        } else if (sel != null && comparator != null) {
            sel.setComparator(comparator);
        }
    }

    public void setFilter(final FilteredEventList.Filter filter) {
        if (this.filter != filter) {
            this.filter = filter;
            rebuildStackFilter();
        }
    }

    private void rebuildStackFilter() {
        throw new RuntimeException("dunno");
    }

    public Object get(final int index) {
        checkSize(index);
        return el.get(index);
    }

    public boolean add(final Object o) {
        //return el == null ? false : el.add(o);
        return el != null && el.add(o);
    }

    public void add(final int index, final Object element) {
        checkSize(index);
        el.add(index, element);
    }

    public boolean addAll(final Collection c) {
        return el != null && el.addAll(c);
    }

    public boolean addAll(final int index, final Collection c) {
        checkSize(index);
        return el.addAll(index, c);
    }

    public void clear() {
        if (el != null) {
            el.clear();
        }
    }

    public boolean contains(final Object o) {
        return el != null && el.contains(o);
    }

    public boolean containsAll(final Collection c) {
        return el != null && el.containsAll(c);
    }

    public int indexOf(final Object o) {
        return el == null ? -1 : el.indexOf(o);
    }


    public int lastIndexOf(final Object o) {
        return el == null ? -1 : el.lastIndexOf(o);
    }


    public Object remove(final int index) {
        checkSize(index);
        return el.remove(index);
    }

    public boolean remove(final Object o) {
        return el != null && el.remove(o);
    }

    public boolean removeAll(final Collection c) {
        return el != null && el.removeAll(c);
    }

    public boolean retainAll(final Collection c) {
        return el != null && el.retainAll(c);
    }

    public Object set(final int index, final Object element) {
        checkSize(index);
        return el.set(index, element);
    }

    public int size() {
        return el == null ? 0 : el.size();
    }

    public Object[] toArray() {
        return el != null ? el.toArray() : new Object[0];
    }

    private void checkSize(final int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
        }
    }
}
