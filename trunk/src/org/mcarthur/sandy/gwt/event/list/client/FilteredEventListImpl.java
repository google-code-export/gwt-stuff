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

import com.google.gwt.core.client.GWT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
    private List translate = new ArrayList();

    private static class Index extends Number {
        private int index = -1;

        public Index() {
        }

        public Index(final int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(final int index) {
            this.index = index;
        }

        public void add(final int amt) {
            setIndex(getIndex() + amt);
        }

        public void sub(final int amt) {
            setIndex(getIndex() - amt);
        }

        public byte byteValue() {
            return (byte)index;
        }

        public short shortValue() {
            return (short)index;
        }

        public int intValue() {
            return index;
        }

        public long longValue() {
            return index;
        }

        public float floatValue() {
            return index;
        }

        public double doubleValue() {
            return index;
        }

        public String toString() {
            return Integer.toString(index);
        }
    }

    private Filter filter = null;

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
        delegate.addListEventListener(new FilteredListEventListener());
        setFilter(filter);
    }

    private class FilteredListEventListener implements ListEventListener {
        public void listChanged(final ListEvent listEvent) {
            final EventList delegate = listEvent.getSourceList();
            if (listEvent.isAdded()) {
                final int delta = listEvent.getIndexEnd() - listEvent.getIndexStart();
                final Iterator iter = translate.iterator();
                int insertAt = 0;
                while (iter.hasNext()) {
                    final Index index = (Index)iter.next();
                    // Assumes Filter maintains order
                    if (index.intValue() >= listEvent.getIndexStart()) {
                        index.add(delta);
                    } else {
                        insertAt++;
                    }
                }

                for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                    final Object o = delegate.get(i);
                    if (filter.accept(o)) {
                        translate.add(insertAt, new Index(i));
                        // XXX: optimize for consecutive objects
                        fireListEvent(new ListEvent(FilteredEventListImpl.this, ListEvent.ADDED, insertAt));
                        insertAt++;
                    }
                }

            } else if (listEvent.isChanged()) {
                for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                    final boolean accepted = filter.accept(delegate.get(i));
                    for (int k=0; k < translate.size(); k++) {
                        final Index index = (Index)translate.get(k);
                        if (index.intValue() == i) {
                            if (accepted) {
                                fireListEvent(new ListEvent(FilteredEventListImpl.this, ListEvent.CHANGED, k));
                            } else {
                                translate.remove(k);
                                fireListEvent(new ListEvent(FilteredEventListImpl.this, ListEvent.REMOVED, k));
                                k--;
                            }
                            break;
                        } else if (i < index.intValue()) {
                            if (accepted) {
                                translate.add(k, new Index(i));
                                fireListEvent(new ListEvent(FilteredEventListImpl.this, ListEvent.ADDED, k));
                            }
                            break;
                        }
                    }
                }

            } else if (listEvent.isRemoved()) {
                final int delta = listEvent.getIndexEnd() - listEvent.getIndexStart();
                final Iterator iter = translate.iterator();
                int pos = 0;
                int lower = delegate.size();
                int upper = -1;
                while (iter.hasNext()) {
                    final Index index = (Index)iter.next();
                    final int i = index.intValue();
                    // assumes filter maintains order
                    if (listEvent.getIndexStart() <= i && i < listEvent.getIndexEnd()) {
                        iter.remove();
                        lower = Math.min(lower, pos);
                        upper = Math.max(pos, upper);
                    } else if (listEvent.getIndexEnd() <= i) {
                        index.sub(delta);
                    }
                    pos++;
                }
                if (lower <= upper) {
                    fireListEvent(new ListEvent(FilteredEventListImpl.this, ListEvent.REMOVED, lower, upper+1));
                }
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
        int pos = 0;
        for (int i=0; i < delegate.size(); i++) {
            final Object o = delegate.get(i);
            final boolean accepted = filter.accept(o);
            if (accepted) {
                if (pos < translate.size()) {
                    final Index index = (Index)translate.get(pos);
                    if (index.intValue() != i) {
                        translate.add(pos, new Index(i));
                        fireListEvent(new ListEvent(this, ListEvent.ADDED, pos));
                    } // else already there, no change
                } else {
                    translate.add(new Index(i));
                    fireListEvent(new ListEvent(this, ListEvent.ADDED, translate.size()-1));
                }
                pos++;
            } else {
                if (pos < translate.size()) {
                    final Index index = (Index)translate.get(pos);
                    if (index.intValue() == i) {
                        translate.remove(pos);
                        fireListEvent(new ListEvent(this, ListEvent.REMOVED, pos));
                    }
                }
            }
        }
        if (pos != translate.size()) {
            throw new IllegalStateException("pos: " + pos + " size: " + translate.size());
        }
    }

    public boolean add(final Object o) {
        return delegate.add(o) && filter.accept(o);
    }

    public void add(final int index, final Object element) {
        final Object o = get(index);
        final int p = delegate.indexOf(o);
        delegate.add(p, element);
    }

    public boolean addAll(final Collection c) {
        return delegate.addAll(c);
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
        if (index < size()) {
            final Index idx = (Index)translate.get(index);
            return delegate.get(idx.intValue());
        } else {
            throw new IndexOutOfBoundsException("index: " + index + " must be less than size: " + size());
        }
    }

    public int indexOf(final Object o) {
        return super.indexOf(o);
    }

    public boolean isEmpty() {
        return super.isEmpty();
    }

    public int lastIndexOf(final Object o) {
        return super.lastIndexOf(o);
    }

    public Object remove(final int index) {
        if (index < size()) {
            final Index idx = (Index)translate.get(index);
            return delegate.remove(idx.intValue());
        } else {
            throw new IndexOutOfBoundsException(index + " out of bounds");
        }
    }

    public boolean remove(final Object o) {
        return super.remove(o);
    }

    public boolean removeAll(final Collection c) {
        //return super.removeAll(c);
        boolean modified = false;
        boolean again;
        do {
            again = false;
            final Iterator e = iterator();
            try {
                while (e.hasNext()) {
                    if (c.contains(e.next())) {
                        e.remove();
                        modified = true;
                    }
                }
            } catch (RuntimeException re) {
                if (GWT.getTypeName(re).equals("java.util.ConcurrentModificationException")) {
                    again = true;
                } else {
                    throw re;
                }

            }
        } while (again);
        return modified;

    }

    public boolean retainAll(final Collection c) {
        return super.retainAll(c);
    }

    public Object set(final int index, final Object element) {
        if (index < size()) {
            final Index idx = (Index)translate.get(index);
            return delegate.set(idx.intValue(), element);
        } else {
            throw new IndexOutOfBoundsException(index + " out of bounds");
        }
    }

    public int size() {
        return translate.size();
    }

    public Object[] toArray() {
        return super.toArray();
    }

    public Iterator iterator() {
        return new Iterator() {
            private Iterator iter = translate.iterator();
            private Index idx = null;

            public boolean hasNext() {
                return iter.hasNext();
            }

            public Object next() {
                idx = (Index)iter.next();
                return delegate.get(idx.intValue());
            }

            public void remove() {
                if (idx != null) {
                    delegate.remove(idx.intValue());
                    idx = null;
                } else {
                    throw new IllegalStateException("call next() first");
                }
            }
        };
    }
}
