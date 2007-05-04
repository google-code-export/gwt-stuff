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

package org.mcarthur.sandy.gwt.event.list.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A FilteredEventList that presents a view of a subset of elements in another EventList.
 *
 * @author Sandy McArthur
 */
class FilteredEventListImpl2 extends AbstractEventList implements FilteredEventList {
    // TODO: The EVERYTHING filter should have almost no overhead
    // TODO: add event batching

    private static final Filter EVERYTHING = new Filter() {
        public boolean accept(final Object element) {
            return true;
        }
    };

    /**
     * This is needed during a batch change from a deeper list.
     */
    private final List elements = new ArrayList();
    private final EventList delegate;

    /**
     * A list of {@link Index}es that map the TransformedEventList's index to the delegate list's
     * indexes.
     */
    private final List translations = new ArrayList();

    private Filter filter = null;

    public FilteredEventListImpl2(final EventList delegate, final Filter filter) {
        this.delegate = delegate;
        elements.addAll(delegate);
        delegate.addListEventListener(new FilteredListEventListener());
        setFilter(filter);
    }

    protected int getSourceIndex(final int mutationIndex) {
        if (mutationIndex < getTranslations().size()) {
            return getTranslationIndex(mutationIndex).getIndex();
        } else if (mutationIndex == getTranslations().size()) {
            return mutationIndex;
        } else {
            throw new IndexOutOfBoundsException("Index: " + mutationIndex + ", Size: " + getTranslations().size());
        }
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

    private Index getTranslationIndex(final int index) {
        return (Index)getTranslations().get(index);
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
        final List elements = this.elements;
        final List translations = getTranslations();
        fireListEvent(ListEvent.createBatchStart(this));
        for (int i=0; i < elements.size(); i++) {
            final Object o = elements.get(i);
            final boolean accepted = filter.accept(o);
            if (accepted) {
                if (pos < translations.size()) {
                    final Index index = getTranslationIndex(pos);
                    if (index.getIndex() != i) {
                        translations.add(pos, new Index(i));
                        fireListEvent(ListEvent.createAdded(this, pos));
                    } // else already there, no change
                } else {
                    translations.add(new Index(i));
                    fireListEvent(ListEvent.createAdded(this, translations.size()-1));
                }
                pos++;
            } else {
                if (pos < translations.size()) {
                    final Index index = getTranslationIndex(pos);
                    if (index.getIndex() == i) {
                        translations.remove(pos);
                        fireListEvent(ListEvent.createRemoved(this, pos));
                    }
                }
            }
        }
        fireListEvent(ListEvent.createBatchEnd(this));
        assert pos == translations.size() : "pos: " + pos + " size: " + translations.size();
    }

    public void add(final int index, final Object element) {
        if (filter.accept(element)) {
            delegate.add(getSourceIndex(index), element);
        } else {
            throw new IllegalArgumentException("Rejected by Filter: " + element);
        }
    }

    public Object get(final int index) {
        return elements.get(getSourceIndex(index));
    }

    public Object remove(final int index) {
        if (index < size()) {
            return delegate.remove(getSourceIndex(index));
        } else {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
        }
    }

    public Object set(final int index, final Object element) {
        if (filter.accept(element)) {
            if (index < size()) {
                return delegate.set(getSourceIndex(index), element);
            } else {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
            }
        } else {
            throw new IllegalArgumentException("Rejected by Filter: " + element);
        }
    }

    public int size() {
        return getTranslations().size();
    }

    private class FilteredListEventListener implements ListEventListener {
        public void listChanged(final ListEvent listEvent) {
            if (listEvent.isAdded()) {
                for (int i=listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                    elements.add(i, listEvent.getSourceList().get(i));
                }
                listChangedAdded(listEvent);

            } else if (listEvent.isChanged()) {
                for (int i=listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                    elements.set(i, listEvent.getSourceList().get(i));
                }
                listChangedChanged(listEvent);

            } else if (listEvent.isRemoved()) {
                for (int i=listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                    elements.remove(listEvent.getIndexStart());
                }
                listChangedRemoved(listEvent);

            } else {
                fireListEvent(listEvent.resource(FilteredEventListImpl2.this));
            }
        }

        private void listChangedAdded(final ListEvent listEvent) {
            final List translations = getTranslations();
            final List elements = FilteredEventListImpl2.this.elements;
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

            if (delta > 1) fireListEvent(ListEvent.createBatchStart(FilteredEventListImpl2.this, listEvent));
            for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                final Object o = elements.get(i);
                if (filter.accept(o)) {
                    translations.add(insertAt, new Index(i));
                    // XXX: optimize for consecutive objects
                    fireListEvent(ListEvent.createAdded(FilteredEventListImpl2.this, insertAt));
                    insertAt++;
                }
            }
            if (delta > 1) fireListEvent(ListEvent.createBatchEnd(FilteredEventListImpl2.this, listEvent));
        }

        private void listChangedChanged(final ListEvent listEvent) {
            final List elements = FilteredEventListImpl2.this.elements;
            int tStart = 0;

            for (tStart = 0; tStart < translations.size(); tStart++) {
                final Index index = (Index)translations.get(tStart);
                if (index.getIndex() >= listEvent.getIndexStart()) {
                    break;
                }
            }
            if (listEvent.getIndexEnd() - listEvent.getIndexStart() > 1) {
                fireListEvent(ListEvent.createBatchStart(delegate, listEvent));
            }
            for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                final Index index = tStart < translations.size() ? (Index)translations.get(tStart) : null;
                final Object obj = elements.get(i);

                if (index == null) {
                    // end of translations list, just append
                    if (filter.accept(obj)) {
                        translations.add(new Index(i));
                        // XXX: optimize for consecutive objects
                        fireListEvent(ListEvent.createAdded(FilteredEventListImpl2.this, tStart));
                        tStart++;
                    }

                } else if (index.getIndex() == i) {
                    // was accepted
                    if (filter.accept(obj)) {
                        // changed some how
                        // XXX: optimize for consecutive objects
                        fireListEvent(ListEvent.createChanged(FilteredEventListImpl2.this, tStart));
                        tStart++;
                    } else {
                        // no longer accepted
                        translations.remove(tStart);
                        // XXX: optimize for consecutive objects
                        fireListEvent(ListEvent.createRemoved(FilteredEventListImpl2.this, tStart));
                        // no need to incr tStart because the remove shifted all down one
                    }

                } else if (index.getIndex() > i) {
                    // was not accepted
                    if (filter.accept(obj)) {
                        // now it's accepted
                        translations.add(tStart, new Index(i));
                        // XXX: optimize for consecutive objects
                        fireListEvent(ListEvent.createAdded(FilteredEventListImpl2.this, tStart));
                        tStart++;
                    } else {
                        // still not accepted
                    }
                }
                assert index == null || !(index.getIndex() < i) : "Index: " + index + " is less than i: " + i;
            }
            if (listEvent.getIndexEnd() - listEvent.getIndexStart() > 1) {
                fireListEvent(ListEvent.createBatchEnd(delegate, listEvent));
            }
        }

        private void listChangedRemoved(final ListEvent listEvent) {
            final List translations = getTranslations();
            final List elements = FilteredEventListImpl2.this.elements;
            final int delta = listEvent.getIndexEnd() - listEvent.getIndexStart();
            final Iterator iter = translations.iterator();
            int pos = 0;
            int lower = translations.size(); // elements.size();
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
                fireListEvent(ListEvent.createRemoved(FilteredEventListImpl2.this, lower, upper + 1));
            }
        }
    }

    /**
     * A mutable number.
     */
    protected static class Index {
        private int index;

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

        public String toString() {
            return Integer.toString(index);
        }
    }
}
