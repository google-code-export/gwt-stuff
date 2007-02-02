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
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * A SortedEventList that presents a sorted view of another EventList.
 *
 * @author Sandy McArthur
 * @see EventLists#sortedEventList()
 */
class SortedEventListImpl extends TransformedEventList implements SortedEventList {    
    private Comparator comparator;

    private static final Comparator NATURAL = new Comparator() {
        public int compare(final Object o1, final Object o2) {
            return ((Comparable)o1).compareTo(o2);
        }
    };

    /**
     * A list of {@link Index}es that map the TransformedEventList's index to the delegate list's
     * indexes.
     */
    private List translations = new ArrayList();

    private final List reverse = new ArrayList();

    protected SortedEventListImpl(final EventList delegate, final Comparator comparator) {
        super(delegate);
        final int size = delegate.size();
        for (int i=0; i < size; i++) {
            getTranslations().add(new Index(i));
            reverse.add(new Index(i));
        }
        delegate.addListEventListener(new SortedListEventListener());
        setComparator(comparator);
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
        // TODO: deal with mutationIndex == size()
        if (mutationIndex < size()) {
            return getTranslationIndex(mutationIndex).getIndex();
        } else {
            return size();
        }
    }

    public int size() {
        return getTranslations().size();
    }

    private class SortedListEventListener implements ListEventListener {
        public void listChanged(final ListEvent listEvent) {
            final List delegate = getDelegate();
            final List translations = getTranslations();
            final List reverse = SortedEventListImpl.this.reverse;

            if (listEvent.isAdded()) {
                for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                    final Object o = delegate.get(i);
                    int pos;
                    for (pos = 0; pos < translations.size(); pos++) {
                        final Object posO = delegate.get(((Index)translations.get(pos)).getIndex());
                        if (comparator.compare(posO, o) > 0) {
                            break;
                        }
                    }

                    final Index newIdx = new Index(i);
                    final Index revIdx = new Index(pos);

                    // insert
                    shiftUp(i, translations.iterator());
                    shiftUp(pos, reverse.iterator());
                    translations.add(pos, newIdx);
                    reverse.add(i, revIdx);
                    fireListEvent(new ListEvent(SortedEventListImpl.this, ListEvent.ADDED, pos));
                }
            } else if (listEvent.isChanged()) {
                // TODO: Optimize
                // XXX: fuck it! just resort the whole thing.
                sort();

            } else if (listEvent.isRemoved()) {
                for (int i = listEvent.getIndexEnd() - 1; i >= listEvent.getIndexStart(); i--) {
                    // XXX: Should be able to remove them all in one loop
                    final Index revIdx = (Index)reverse.get(i);
                    final Index tranIdx = (Index)translations.get(revIdx.getIndex());
                    removeAndShift(tranIdx, translations.iterator());
                    removeAndShift(revIdx, reverse.iterator());
                    fireListEvent(new ListEvent(SortedEventListImpl.this, ListEvent.REMOVED, revIdx.getIndex()));                    
                }
                
            } else {
                fireListEvent(listEvent.resource(SortedEventListImpl.this));
            }
        }

        private void shiftUp(final int index, final Iterator iter) {
            while (iter.hasNext()) {
                final Index idx = (Index)iter.next();
                if (idx.getIndex() >= index) {
                    idx.add(1);
                }
            }
        }

        private void removeAndShift(final Index index, final Iterator iter) {
            while (iter.hasNext()) {
                final Index idx = (Index)iter.next();
                if (idx == index) {
                    iter.remove();
                } else if (idx.getIndex() > index.getIndex()) {
                    idx.sub(1);
                }
            }
        }
    }

    public Comparator getComparator() {
        return comparator != NATURAL ? comparator : null;
    }

    public void setComparator(Comparator comparator) {
        comparator = comparator != null ? comparator : NATURAL;
        if (this.comparator != comparator) {
            this.comparator = comparator;
            sort();
        }
    }

    public void sort() {
        if (size() == 0) {
            return;
        }
        // TODO: optimize
        final List delegate = getDelegate();
        final List sorted = new ArrayList();
        sorted.addAll(delegate);
        Collections.sort(sorted, comparator);
        
        for (int i=0; i < sorted.size(); i++) {
            final Object o = sorted.get(i);
            int j;
            for (j=0; j < delegate.size(); j++) {
                if (o == delegate.get(j)) {
                    break;
                }
            }
            getTranslationIndex(i).setIndex(j);
            ((Index)reverse.get(j)).setIndex(i);
        }
        final ListEvent event = new ListEvent(this, ListEvent.CHANGED, 0, size());
        fireListEvent(event);
    }
}
