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
 */
abstract class SortedEventListImpl extends TransformedEventList implements SortedEventList {    
    private Comparator comparator;

    private static final Comparator NATURAL = new Comparator() {
        public int compare(final Object o1, final Object o2) {
            return ((Comparable)o1).compareTo(o2);
        }
    };

    private final List sorted = new ArrayList();

    protected SortedEventListImpl(final EventList delegate, final Comparator comparator) {
        super(delegate);
        final int size = delegate.size();
        for (int i=0; i < size; i++) {
            getTranslations().add(new Index(i));
        }
        sorted.addAll(delegate);
        Collections.sort(sorted, comparator != null ? comparator : NATURAL);
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
                        if (getComparator().compare(o, posO) > 0) {
                            break;
                        }
                    }

                    final Index newIdx = new Index(i);
                    if (pos == translations.size()) {
                        // append
                        translations.add(newIdx);
                        sorted.add(o);
                        fireListEvent(new ListEvent(SortedEventListImpl.this, ListEvent.ADDED, i));

                    } else {
                        // insert
                        final Iterator iter = translations.iterator();
                        while (iter.hasNext()) {
                            final Index idx = (Index)iter.next();
                            if (idx.getIndex() >= i) {
                                idx.add(1);
                            }
                        }
                        translations.add(pos, newIdx);
                        sorted.add(pos, o);
                    }
                }
            } else if (listEvent.isChanged()) {
                for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                    final Object o = delegate.get(i);
                    // TODO: need to implement a list that tracks the order of elements before the event.
                }

            } else if (listEvent.isRemoved()) {
                for (int i = listEvent.getIndexEnd() - 1; i >= listEvent.getIndexStart(); i--) {
                    for (int pos = 0; pos < translations.size(); pos++) {
                        final Index idx = (Index)translations.get(pos);
                        if (idx.getIndex() == i) {
                            translations.remove(pos);
                            sorted.remove(pos);
                            pos--;
                        } else if (idx.getIndex() >= i) {
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
        Collections.sort(getTranslations(), new Comparator() {
            public int compare(final Object i1, final Object i2) {
                final Object o1 = getTranslations().get(((Index)i1).getIndex());
                final Object o2 = getTranslations().get(((Index)i2).getIndex());
                return getComparator().compare(o1, o2);
            }
        });
        fireListEvent(new ListEvent(this, ListEvent.CHANGED, 0, size()));
    }
}
