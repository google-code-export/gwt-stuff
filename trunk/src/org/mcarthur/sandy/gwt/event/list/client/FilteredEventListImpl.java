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

    public FilteredEventListImpl(final EventList delegate) {
        this(delegate, null);
    }

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
        } else if (getTranslations().size() > 0) {
            return getTranslationIndex(getTranslations().size() - 1).getIndex() + 1;
        } else {
            return 0;            
        }
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

        private void XlistChangedChanged(final ListEvent listEvent) {
            // FIXME: this is buggy
            final EventList delegate = getDelegate();
            final List translations = getTranslations();
            for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                final Object delegateElement = delegate.get(i);
                final boolean accepted = filter.accept(delegateElement);
                for (int k=0; k < translations.size(); k++) {
                    final Index index = getTranslationIndex(k);
                    if (index.getIndex() == i) {
                        if (accepted) {
                            fireListEvent(new ListEvent(FilteredEventListImpl.this, ListEvent.CHANGED, k));
                        } else {
                            translations.remove(k);
                            fireListEvent(new ListEvent(FilteredEventListImpl.this, ListEvent.REMOVED, k));
                        }
                        break;
                    } else if (i < index.getIndex()) {
                        if (accepted) {
                            translations.add(k, new Index(i));
                            fireListEvent(new ListEvent(FilteredEventListImpl.this, ListEvent.ADDED, k));
                        }
                        break;
                    }
                }
            }
        }

        private void listChangedChanged(final ListEvent listEvent) {
            // TODO: optimize me
            final List delegate = getDelegate();
            translations.clear();
            for (int i=0; i < delegate.size(); i++) {
                final Object o = delegate.get(i);
                final boolean accepted = filter.accept(o);
                if (accepted) {
                    translations.add(new Index(i));
                }                 
            }
            fireListEvent(new ListEvent(FilteredEventListImpl.this, ListEvent.REMOVED, 0, size()));
            fireListEvent(new ListEvent(FilteredEventListImpl.this, ListEvent.ADDED, 0, size()));
        }

        private void listChangedRemoved(final ListEvent listEvent) {
            final List translations = getTranslations();
            final EventList delegate = getDelegate();
            final int delta = listEvent.getIndexEnd() - listEvent.getIndexStart();
            final Iterator iter = translations.iterator();
            int pos = 0;
            int lower = delegate.size();
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
        if (pos != translations.size()) {
            throw new IllegalStateException("pos: " + pos + " size: " + translations.size());
        }
    }

    public boolean add(final Object o) {
        return filter.accept(o) && super.add(o);
    }

    public void add(final int index, final Object element) {
        if (filter.accept(element)) {
            if (index == size()) {
                super.add(size(), element);
            } else {
                final Object o = get(index);
                final int p = getDelegate().indexOf(o);
                super.add(p, element);
            }
        } else {
            throw new IllegalArgumentException("Filter rejected element.");
        }
    }

    private List onlyAccepted(final Collection c) {
        final List toBeAdded = new ArrayList();
        final Iterator iter = c.iterator();
        while (iter.hasNext()) {
            final Object o = iter.next();
            if (filter.accept(o)) {
                toBeAdded.add(o);
            }
        }
        return toBeAdded;
    }

    public boolean addAll(final Collection c) {
        return super.addAll(onlyAccepted(c));
    }

    public boolean addAll(final int index, final Collection c) {
        return super.addAll(index, onlyAccepted(c));
    }
}
