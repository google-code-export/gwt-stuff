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
import java.util.Iterator;
import java.util.List;

/**
 * Wrapper of a {@link List} that fires events when changes are made.
 *
 * @author Sandy McArthur
 */
class WrappedEventList extends AbstractEventList implements EventList {
    private final List delegate;

    public WrappedEventList(final List delegate) {
        this.delegate = delegate;
    }

    public boolean add(final Object element) {
        final int index = delegate.size();
        final boolean b = delegate.add(element);
        fireListEvent(new ListEvent(this, ListEvent.ADDED, index));
        return b;
    }

    public void add(final int index, final Object element) {
        delegate.add(index, element);
        fireListEvent(new ListEvent(this, ListEvent.ADDED, index));
    }

    public boolean addAll(final Collection c) {
        final int indexStart = delegate.size();
        final boolean b = delegate.addAll(c);
        fireListEvent(new ListEvent(this, ListEvent.ADDED, indexStart, delegate.size()));
        return b;
    }

    public boolean addAll(final int index, final Collection c) {
        final boolean b = delegate.addAll(index, c);
        fireListEvent(new ListEvent(this, ListEvent.ADDED, index, c.size()));
        return b;
    }

    public void clear() {
        final int indexEnd = delegate.size();
        delegate.clear();
        fireListEvent(new ListEvent(this, ListEvent.REMOVED, 0, indexEnd));
    }

    public boolean contains(final Object element) {
        return delegate.contains(element);
    }

    public boolean containsAll(final Collection c) {
        return delegate.containsAll(c);
    }

    public boolean equals(final Object o) {
        return delegate.equals(o);
    }

    public Object get(final int index) {
        return delegate.get(index);
    }

    public int hashCode() {
        return delegate.hashCode();
    }

    public int indexOf(final Object element) {
        return delegate.indexOf(element);
    }

    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    public Iterator iterator() {
        return new WrappedEventListIterator(this, delegate.iterator());
    }

    public int lastIndexOf(final Object element) {
        return delegate.lastIndexOf(element);
    }

    public Object remove(final int index) {
        final Object element = delegate.remove(index);
        fireListEvent(new ListEvent(this, ListEvent.REMOVED, index));
        return element;
    }

    public boolean remove(final Object element) {
        final int index = delegate.indexOf(element);
        final boolean wasRemoved = delegate.remove(element);
        // only fire an event when the list changed
        if (wasRemoved) {
            fireListEvent(new ListEvent(this, ListEvent.REMOVED, index));
        }
        return wasRemoved;
    }

    public boolean removeAll(final Collection c) {
        // Figure out which objects will be removed
        // and their order in delegate
        final List toBeRemoved = new ArrayList();
        Iterator iter = delegate.iterator();
        while (iter.hasNext()) {
            final Object o = iter.next();
            if (c.contains(o)) {
                toBeRemoved.add(o);
            }
        }

        iter = toBeRemoved.iterator();
        int start = -1;
        int run = 1;
        while (iter.hasNext() || start != -1) { // loop for each item to be removed and then once more
            final Object o = iter.hasNext() ? iter.next() : null;
            if (start == -1) { // first element
                start = delegate.indexOf(o);
                run = 1;

            } else if (o != null && delegate.indexOf(o) == start) { // consecutive and not end
                run++;

            } else { // not consecutive or end
                fireListEvent(new ListEvent(this, ListEvent.REMOVED, start, start + run));
                if (o != null) { // not end
                    start = delegate.indexOf(o);
                } else { // end
                    start = -1;
                }
                run = 1;
            }

            if (start != -1) { // not end
                delegate.remove(start);
            }
        }

        // were any removed?
        return toBeRemoved.size() > 0;
    }

    public boolean retainAll(final Collection c) {
        boolean changed = false;
        final Iterator iter = iterator();
        while (iter.hasNext()) {
            // XXX optimze event firing
            final Object element = iter.next();
            if (!c.contains(element)) {
                iter.remove();
                changed = true;
            }
        }
        return changed;
    }

    public Object set(final int index, final Object element) {
        final Object oldElement = delegate.set(index, element);
        fireListEvent(new ListEvent(this, ListEvent.CHANGED, index));
        return oldElement;
    }

    public int size() {
        return delegate.size();
    }

    public Object[] toArray() {
        return delegate.toArray();
    }

    private class WrappedEventListIterator implements Iterator {
        private final EventList eventList;
        private final Iterator delegate;
        private Object element;

        public WrappedEventListIterator(final EventList eventList, final Iterator delegate) {
            this.eventList = eventList;
            this.delegate = delegate;
        }

        public boolean hasNext() {
            return delegate.hasNext();
        }

        public Object next() {
            element = delegate.next();
            return element;
        }

        public void remove() {
            final int index = eventList.indexOf(element);
            delegate.remove();
            fireListEvent(new ListEvent(eventList, ListEvent.REMOVED, index));
        }
    }
}
