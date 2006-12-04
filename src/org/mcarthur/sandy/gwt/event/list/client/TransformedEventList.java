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
        getDelegate().add(index, element);
    }

    public boolean addAll(final Collection c) {
        return getDelegate().addAll(c);
    }

    public boolean addAll(final int index, final Collection c) {
        return getDelegate().addAll(index, c);
    }

    public Object get(final int index) {
        if (index < size()) {
            final Index idx = (Index)getTranslations().get(index);
            return getDelegate().get(idx.getIndex());
        } else {
            throw new IndexOutOfBoundsException("index: " + index + " must be less than size(): " + size());
        }
    }

    public Object remove(final int index) {
        if (index < size()) {
            final Index idx = (Index)getTranslations().get(index);
            return getDelegate().remove(idx.getIndex());
        } else {
            throw new IndexOutOfBoundsException(index + " out of bounds");
        }
    }

    public boolean removeAll(final Collection c) {
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

    public Object set(final int index, final Object element) {
        if (index < size()) {
            final Index idx = (Index)getTranslations().get(index);
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
        return new Iterator() {
            private Iterator iter = getTranslations().iterator();
            private Index idx = null;

            public boolean hasNext() {
                return iter.hasNext();
            }

            public Object next() {
                idx = (Index)iter.next();
                return getDelegate().get(idx.getIndex());
            }

            public void remove() {
                if (idx != null) {
                    getDelegate().remove(idx.getIndex());
                    idx = null;
                } else {
                    throw new IllegalStateException("call next() first");
                }
            }
        };
    }
}
