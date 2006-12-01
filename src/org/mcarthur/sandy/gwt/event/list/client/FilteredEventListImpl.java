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
import java.util.NoSuchElementException;

/**
 * TODO: Write Javadoc
 *
 * @author Sandy McArthur
 */
class FilteredEventListImpl extends AbstractEventList implements FilteredEventList {
    private final EventList delegate;
    /**
     * A translation array where translate[unfiltered] is the index of the element in delegate.
     */
    private int[] translate;
    private int translateSize = 0;

    private Filter filter = EVERYTHING;

    private static final Filter EVERYTHING = new Filter() {
        public boolean accept(final Object element) {
            return true;
        }
    };

    public FilteredEventListImpl(final EventList delegate) {
        this(delegate, null);
    }

    public FilteredEventListImpl(final EventList delegate, final Filter filter) {
        this.delegate = delegate;
        translate = new int[delegate.size()];
        delegate.addListEventListener(new FilteredListEventListener());
        filter();
        setFilter(filter);
    }

    private class FilteredListEventListener implements ListEventListener {
        public void listChanged(final ListEvent listEvent) {
            final EventList delegate = listEvent.getSourceList();
            if (listEvent.isAdded()) {
                final int delegateSize = delegate.size();
                if (delegateSize > translate.length) {
                    final int[] t = new int[delegateSize];
                    for (int i = 0; i < translate.length; i++) {
                        t[i] = translate[i];
                    }
                    for (int i = translate.length; i < t.length; i++) {
                        t[i] = Integer.MIN_VALUE;
                    }
                    translate = t;
                }

                // If not at the end, then shuffle
                if (listEvent.getIndexEnd() < translate.length) {
                    final int delta = listEvent.getIndexEnd() - listEvent.getIndexStart();
                    for (int i = translate.length; i >= listEvent.getIndexEnd(); i--) {
                        translate[i] = translate[i-delta] + delta;
                    }
                    for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                        translate[i] = Integer.MIN_VALUE;
                    }
                }

                for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                    final Object o = delegate.get(i);
                    if (filter.accept(o)) {
                        translate[i] = i;
                        fireListEvent(new ListEvent(FilteredEventListImpl.this, ListEvent.ADDED, indexOf(o)));
                    }
                }

            } else if (listEvent.isChanged()) {

            } else if (listEvent.isRemoved()) {
                // TODO: trim translate
            }
        }
    }

    public Filter getFilter() {
        return filter != EVERYTHING ? filter : null;
    }

    public void setFilter(Filter filter) {
        filter = filter != null ? filter : EVERYTHING;
        if (this.filter != filter) {
            this.filter = filter;
            filter();
        }
    }

    public void filter() {
        // nothing to do in this implementation
    }


    // TODO: Everything below here is crap!

    public boolean add(final Object o) {
        if (delegate.add(o)) {
            return filter.accept(o);
        } else {
            return false;
        }
    }

    public void add(final int index, final Object element) {
        final Object o = get(index);
        final int p = delegate.indexOf(o);
        delegate.add(p, element);
    }

    public boolean addAll(final Collection c) {
        return super.addAll(c);
    }

    public boolean addAll(final int index, final Collection c) {
        return super.addAll(index, c);
    }

    public void clear() {
        final Iterator iter = iterator();
        while (iter.hasNext()) {
            iter.next();
            iter.remove();
        }
    }

    public boolean contains(final Object o) {
        return super.contains(o);
    }

    public boolean containsAll(final Collection c) {
        return super.containsAll(c);
    }

    public Object get(final int index) {
        final Iterator iter = iterator();
        int i = -1;
        while (iter.hasNext()) {
            final Object o = iter.next();
            i++;
            if (index == i) {
                return o;
            }
        }
        return null;
    }

    public int indexOf(final Object o) {
        final Iterator iter = iterator();
        int i = -1;
        while (iter.hasNext()) {
            final Object obj = iter.next();
            i++;
            if (o == obj) {
                return i;
            }
        }
        return -1;
    }

    public boolean isEmpty() {
        return super.isEmpty();
    }

    public int lastIndexOf(final Object o) {
        int found = -1;
        final Iterator iter = iterator();
        int i = -1;
        while (iter.hasNext()) {
            final Object obj = iter.next();
            i++;
            if (o.equals(obj)) {
                found = i;
            }
        }
        return found;
    }

    public Object remove(final int index) {
        final Iterator iter = iterator();
        int i = -1;
        while (iter.hasNext()) {
            Object o = iter.next();
            i++;
            if (index == i) {
                iter.remove();
                return o;
            }
        }
        throw new IndexOutOfBoundsException(index + " out of bounds");
    }

    public boolean remove(final Object o) {
        return super.remove(o);
    }

    public boolean removeAll(final Collection c) {
        return super.removeAll(c);
    }

    public boolean retainAll(final Collection c) {
        return super.retainAll(c);
    }

    public Object set(final int index, final Object element) {
        final Object o = get(index);
        final int p = delegate.indexOf(o);
        return super.set(p, element);
    }

    public int size() {
        int i = 0;
        final Iterator iter = iterator();
        while (iter.hasNext()) {
            iter.next();
            i++;
        }
        return i;
    }

    public Object[] toArray() {
        return super.toArray();
    }

    public Iterator iterator() {
        return new Iterator() {
            int cursor = -1;
            int lastRet = -1;

            public boolean hasNext() {
                for (int i = cursor+1; i < delegate.size(); i++) {
                    if (filter.accept(delegate.get(i))) {
                        return true;
                    }
                }
                return false;
            }

            public Object next() {
                if (cursor == -1) {
                    advance();
                }
                if (cursor < delegate.size()) {
                    final Object next = delegate.get(cursor);
                    lastRet = cursor;
                    advance();
                    return next;
                } else {
                    throw new NoSuchElementException();
                }
            }

            private void advance() {
                for (cursor+=1; cursor < delegate.size(); cursor++) {
                    if (filter.accept(delegate.get(cursor))) {
                        return;
                    }
                }
            }

            public void remove() {
                if (lastRet == -1) {
                    throw new IllegalStateException();
                }
                delegate.remove(lastRet);
                if (lastRet < cursor) {
                    cursor--;
                }
                lastRet = -1;
            }
        };
    }
}
