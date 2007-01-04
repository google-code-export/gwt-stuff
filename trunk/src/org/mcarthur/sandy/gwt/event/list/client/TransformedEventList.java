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
import java.util.NoSuchElementException;

/**
 * An EventList that presents a transformed view of another EventList.
 *
 * @author Sandy McArthur
 */
abstract class TransformedEventList extends AbstractEventList implements EventList {
    private final EventList delegate;
    /**
     * A list of {@link Index}es that map the TransformedEventList's index to the delegate list's
     * indexes.
     */
    private List translations = new ArrayList();

    protected TransformedEventList(final EventList delegate) {
        this.delegate = delegate;
    }

    protected EventList getDelegate() {
        return delegate;
    }

    protected List getTranslations() {
        return translations;
    }

    protected Index getTranslationIndex(final int index) {
        return (Index)getTranslations().get(index);
    }

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

    public boolean add(final Object o) {
        return getDelegate().add(o);
    }

    public void add(final int index, final Object element) {
        getDelegate().add(getTranslationIndex(index).getIndex(), element);
    }

    public boolean addAll(final Collection c) {
        return getDelegate().addAll(c);
    }

    public boolean addAll(final int index, final Collection c) {
        return getDelegate().addAll(getTranslationIndex(index).getIndex(), c);
    }

    public Object get(final int index) {
        if (index < size()) {
            final Index idx = getTranslationIndex(index);
            return getDelegate().get(idx.getIndex());
        } else {
            throw new IndexOutOfBoundsException("index: " + index + " must be less than size(): " + size());
        }
    }

    public Object remove(final int index) {
        if (index < size()) {
            final Index idx = getTranslationIndex(index);
            return getDelegate().remove(idx.getIndex());
        } else {
            throw new IndexOutOfBoundsException(index + " out of bounds");
        }
    }

    public boolean removeAll(final Collection c) {
        boolean modified = false;
        final Iterator e = iterator();
        while (e.hasNext()) {
            if (c.contains(e.next())) {
                e.remove();
                modified = true;
            }
        }
        return modified;
    }

    public Object set(final int index, final Object element) {
        if (index < size()) {
            final Index idx = getTranslationIndex(index);
            return getDelegate().set(idx.getIndex(), element);
        } else {
            throw new IndexOutOfBoundsException("index: " + index + " out of bounds");
        }
    }

    public int size() {
        return getTranslations().size();
    }

    public Object[] toArray() {
        return super.toArray();
    }

    public Iterator iterator() {
        // An Iterator that doesn't trip over ConcurrentModificationException
        return new Iterator() {
            private int cursor = 0;
            private int lastRet = -1;

            public boolean hasNext() {
                return cursor != size();
            }

            public Object next() {
                try {
                    final Object next = get(cursor);
                    lastRet = cursor++;
                    return next;
                } catch(IndexOutOfBoundsException e) {
                    throw new NoSuchElementException();
                }
            }

            public void remove() {
                if (lastRet == -1)
                    throw new IllegalStateException();

                TransformedEventList.this.remove(lastRet);
                if (lastRet < cursor)
                    cursor--;
                lastRet = -1;
            }
        };
    }
}