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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An EventList that presents a transformed view of another EventList.
 *
 * @author Sandy McArthur
 */
public abstract class TransformedEventList extends AbstractEventList implements EventList {
    private final EventList delegate;

    protected TransformedEventList(final EventList delegate) {
        this.delegate = delegate;
    }

    /**
     * Get the backing EventList.
     * @return the backing EventList.
     */
    protected EventList getDelegate() {
        return delegate;
    }

    protected abstract int getSourceIndex(int mutationIndex);

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

    public void add(final int index, final Object element) {
        getDelegate().add(getSourceIndex(index), element);
    }

    public Object get(final int index) {
        if (index < size()) {
            return getDelegate().get(getSourceIndex(index));
        } else {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
        }
    }

    public Object remove(final int index) {
        if (index < size()) {
            return getDelegate().remove(getSourceIndex(index));
        } else {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
        }
    }

    public Object set(final int index, final Object element) {
        if (index < size()) {
            return getDelegate().set(getSourceIndex(index), element);
        } else {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
        }
    }

    public abstract int size();

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
                    final NoSuchElementException nsee = new NoSuchElementException();
                    nsee.initCause(e);
                    throw nsee;
                }
            }

            public void remove() {
                if (lastRet == -1) {
                    throw new IllegalStateException();
                }

                TransformedEventList.this.remove(lastRet);
                if (lastRet < cursor) {
                    cursor--;
                }
                lastRet = -1;
            }
        };
    }
}
