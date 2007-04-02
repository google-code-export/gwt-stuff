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
 * A FilteredEventList that presents a view of a subset of elements in another EventList.
 *
 * @author Sandy McArthur
 */
class FilteredEventListImpl extends TransformedEventList implements FilteredEventList {

    private Filter filter = null;

    private static final Filter EVERYTHING = new Filter() {
        public boolean accept(final Object element) {
            return true;
        }
    };

    /**
     * A list of {@link Index}es that map the TransformedEventList's index to the delegate list's
     * indexes.
     */
    private List translations = new ArrayList();

    public FilteredEventListImpl(final EventList delegate, final Filter filter) {
        super(delegate);
        delegate.addListEventListener(new FilteredListEventListener());
        setFilter(filter);
    }

    /**
     * A List of <code>Index</code>s where the translation index is this list's index and the value
     * of the Index is the backing list's index.
     * @return a list of Index objects.
     * @see org.mcarthur.sandy.gwt.event.list.client.TransformedEventList.Index
     */
    private List getTranslations() {
        return translations;
    }

    /**
     * Convenience for <code>(Index)getTranslations().get(index)</code>.
     * @param index the position to look up.
     * @return the Index for a position.
     */
    private Index getTranslationIndex(final int index) {
        return (Index)getTranslations().get(index);
    }

    protected int getSourceIndex(final int mutationIndex) {
        if (mutationIndex < getTranslations().size()) {
            return getTranslationIndex(mutationIndex).getIndex();
        } else if (mutationIndex == getTranslations().size()) {
            return mutationIndex;
        } else {
            throw new IndexOutOfBoundsException("Index: " + mutationIndex + ", Size: " + getTranslations().size());
        }
        /*
        if (mutationIndex < getTranslations().size()) {
            return getTranslationIndex(mutationIndex).getIndex();
        } else if (getTranslations().size() > 0) {
            return getTranslationIndex(getTranslations().size() - 1).getIndex() + 1;
        } else {
            return 0;            
        }
        */
    }

    public int size() {
        return getTranslations().size();
    }

    private class FilteredListEventListener implements ListEventListener {
        public void listChanged(final ListEvent listEvent) {
            if (listEvent.isAdded()) {
                listChangedAdded(listEvent);

            } else if (listEvent.isChanged()) {
                listChangedChanged(listEvent);

            } else if (listEvent.isRemoved()) {
                listChangedRemoved(listEvent);

            } else {
                fireListEvent(listEvent.resource(FilteredEventListImpl.this));
            }
        }

        private void listChangedAdded(final ListEvent listEvent) {
            final List translations = getTranslations();
            final EventList delegate = getDelegate();
            final int delta = listEvent.getIndexEnd() - listEvent.getIndexStart();
            final Iterator iter = translations.iterator();
            int insertAt = 0;
            while (iter.hasNext()) {
                final Index index = (Index)iter.next();
                // Assumes Filter maintains order
                if (index.getIndex() >= listEvent.getIndexStart()) {
                    index.add(delta);
                } else {
                    insertAt++;
                }
            }

            for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                final Object o = delegate.get(i);
                if (filter.accept(o)) {
                    translations.add(insertAt, new Index(i));
                    // XXX: optimize for consecutive objects
                    fireListEvent(new ListEvent(FilteredEventListImpl.this, ListEvent.ADDED, insertAt));
                    insertAt++;
                }
            }
        }

        private void listChangedChanged(final ListEvent listEvent) {
            final List delegate = getDelegate();
            int tStart = 0;

            for (tStart = 0; tStart < translations.size(); tStart++) {
                final Index index = (Index)translations.get(tStart);
                if (index.getIndex() >= listEvent.getIndexStart()) {
                    break;
                }
            }
            for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                final Index index = tStart < translations.size() ? (Index)translations.get(tStart) : null;
                final Object obj = delegate.get(i);

                if (index == null) {
                    // end of translations list, just append
                    if (filter.accept(obj)) {
                        translations.add(new Index(i));
                        // XXX: optimize for consecutive objects
                        fireListEvent(new ListEvent(FilteredEventListImpl.this, ListEvent.ADDED, tStart));
                        tStart++;
                    }

                } else if (index.getIndex() == i) {
                    // was accepted
                    if (filter.accept(obj)) {
                        // changed some how
                        // XXX: optimize for consecutive objects
                        fireListEvent(new ListEvent(FilteredEventListImpl.this, ListEvent.CHANGED, tStart));
                        tStart++;
                    } else {
                        // no longer accepted
                        translations.remove(tStart);
                        // XXX: optimize for consecutive objects
                        fireListEvent(new ListEvent(FilteredEventListImpl.this, ListEvent.REMOVED, tStart));
                        // no need to incr tStart because the remove shifted all down one
                    }

                } else if (index.getIndex() > i) {
                    // was not accepted
                    if (filter.accept(obj)) {
                        // now it's accepted
                        translations.add(tStart, new Index(i));
                        // XXX: optimize for consecutive objects
                        fireListEvent(new ListEvent(FilteredEventListImpl.this, ListEvent.ADDED, tStart));
                        tStart++;
                    } else {
                        // still not accepted
                    }
                }
                assert index == null || !(index.getIndex() < i) : "Index: " + index + " is less than i: " + i;
            }
        }

        private void listChangedRemoved(final ListEvent listEvent) {
            final List translations = getTranslations();
            final EventList delegate = getDelegate();
            final int delta = listEvent.getIndexEnd() - listEvent.getIndexStart();
            final Iterator iter = translations.iterator();
            int pos = 0;
            int lower = translations.size(); // delegate.size();
            int upper = -1;
            while (iter.hasNext()) {
                final Index index = (Index)iter.next();
                final int i = index.getIndex();
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
        final List delegate = getDelegate();
        final List translations = getTranslations();
        for (int i=0; i < delegate.size(); i++) {
            final Object o = delegate.get(i);
            final boolean accepted = filter.accept(o);
            if (accepted) {
                if (pos < translations.size()) {
                    final Index index = getTranslationIndex(pos);
                    if (index.getIndex() != i) {
                        translations.add(pos, new Index(i));
                        fireListEvent(new ListEvent(this, ListEvent.ADDED, pos));
                    } // else already there, no change
                } else {
                    translations.add(new Index(i));
                    fireListEvent(new ListEvent(this, ListEvent.ADDED, translations.size()-1));
                }
                pos++;
            } else {
                if (pos < translations.size()) {
                    final Index index = getTranslationIndex(pos);
                    if (index.getIndex() == i) {
                        translations.remove(pos);
                        fireListEvent(new ListEvent(this, ListEvent.REMOVED, pos));
                    }
                }
            }
        }
        assert pos == translations.size() : "pos: " + pos + " size: " + translations.size();
    }

    public boolean add(final Object o) {
        if (filter.accept(o)) {
            return super.add(o);
        } else {
            throw new IllegalArgumentException("Rejected by Filter: " + o);
        }
    }

    public void add(final int index, final Object element) {
        if (filter.accept(element)) {
            super.add(index, element);
        } else {
            throw new IllegalArgumentException("Rejected by Filter: " + element);
        }
    }

    private void checkAll(final Collection c) {
        final Iterator iter = c.iterator();
        while (iter.hasNext()) {
            final Object o = iter.next();
            if (!filter.accept(o)) {
                throw new IllegalArgumentException("Rejected by Filter: " + o);
            }
        }
    }

    public boolean addAll(final Collection c) throws IllegalArgumentException {
        checkAll(c);
        return super.addAll(c);
    }

    public boolean addAll(final int index, final Collection c) throws IllegalArgumentException {
        checkAll(c);
        return super.addAll(index, c);
    }

    public Object set(final int index, final Object element) {
        if (filter.accept(element)) {
            return super.set(index, element);
        } else {
            throw new IllegalArgumentException("Rejected by Filter: " + element);
        }
    }
}
