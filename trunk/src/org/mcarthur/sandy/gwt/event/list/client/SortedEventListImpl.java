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
 * BROKEN!
 * A SortedEventList that presents a sorted view of another EventList.
 *
 * @author Sandy McArthur
 */
class SortedEventListImpl extends TransformedEventList implements SortedEventList {    
    private Comparator comparator;

    private static final Comparator NATURAL = new Comparator() {
        public int compare(final Object o1, final Object o2) {
            return ((Comparable)o1).compareTo(o2);
        }
    };

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

    private class SortedListEventListener implements ListEventListener {
        public void listChanged(final ListEvent listEvent) {
            final List delegate = getDelegate();
            final List translations = getTranslations();

            if (listEvent.isAdded()) {
                for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                    final Object o = delegate.get(i);
                    int pos;
                    for (pos = 0; pos < translations.size(); pos++) {
                        final Object posO = delegate.get(((Index)translations.get(pos)).getIndex());
                        if (comparator.compare(o, posO) > 0) {
                            break;
                        }
                    }

                    final Index newIdx = new Index(i);
                    final Index revIdx = new Index(pos);

                    // insert
                    final Iterator iter = translations.iterator();
                    while (iter.hasNext()) {
                        final Index idx = (Index)iter.next();
                        if (idx.getIndex() >= i) {
                            idx.add(1);
                        }
                    }
                    translations.add(pos, newIdx);
                    reverse.add(i, revIdx);
                    fireListEvent(new ListEvent(SortedEventListImpl.this, ListEvent.ADDED, i));
                }
            } else if (listEvent.isChanged()) {
                // XXX: fuck it! just resort the whole thing.
                sort();

            } else if (listEvent.isRemoved()) {
                for (int i = listEvent.getIndexEnd() - 1; i >= listEvent.getIndexStart(); i--) {
                    // XXX: Should be able to remove them all in one loop
                    final Index revIdx = (Index)reverse.get(i);
                    final Index tnsIdx = (Index)translations.get(revIdx.getIndex());
                    final Iterator tnsIter = translations.iterator();
                    while (tnsIter.hasNext()) {
                        final Index idx = (Index)tnsIter.next();
                        if (idx == tnsIdx) {
                            tnsIter.remove();
                        } else if (idx.getIndex() > tnsIdx.getIndex()) {
                            idx.sub(1);
                        }
                    }

                    final Iterator revIter = reverse.iterator();
                    while(revIter.hasNext()) {
                        final Index idx = (Index)revIter.next();
                        if (idx == revIdx) {
                            revIter.remove();
                        } else if (idx.getIndex() > revIdx.getIndex()) {
                            idx.sub(1);
                        }
                    }
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
        final List sorted = new ArrayList();
        sorted.addAll(getDelegate());
        Collections.sort(sorted, comparator);
        for (int i=0; i < sorted.size(); i++) {
            final Object o = sorted.get(i);
            final Object t = get(i);
            if (o != t) {
                final Index idx = (Index)getTranslations().get(i);
                idx.setIndex(i);
                final Iterator iter = getDelegate().iterator();
                for (int j=0; iter.hasNext(); j++) {
                    final Object d = getDelegate().get(j);
                    if (d == o) {
                        ((Index)reverse.get(j)).setIndex(i);
                    }
                }
                fireListEvent(new ListEvent(this, ListEvent.CHANGED, i));
            }
        }
    }


    public Object remove(final int index) {
        return super.remove(index);
    }
}
