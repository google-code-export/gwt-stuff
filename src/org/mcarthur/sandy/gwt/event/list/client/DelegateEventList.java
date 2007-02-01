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

import java.util.Collection;
import java.util.Iterator;

/**
 * An EventList that provides a view of another EventList.
 * This class is meant to be a base class.
 *
 * @author Sandy McArthur
 */
public abstract class DelegateEventList extends AbstractEventList implements EventList {
    private final EventList delegate;

    protected DelegateEventList(final EventList delegate) {
        this.delegate = delegate;
    }

    /**
     * Get the backing EventList.
     * @return the backing EventList.
     */
    protected EventList getDelegate() {
        return delegate;
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

    public void clear() {
        getDelegate().clear();
    }

    public boolean contains(final Object o) {
        return getDelegate().contains(o);
    }

    public boolean containsAll(final Collection c) {
        return getDelegate().containsAll(c);
    }

    public Object get(final int index) {
        return getDelegate().get(index);
    }

    public boolean isEmpty() {
        return getDelegate().isEmpty();
    }

    public int indexOf(final Object o) {
        return getDelegate().indexOf(o);
    }

    public Iterator iterator() {
        return getDelegate().iterator();
    }

    public int lastIndexOf(final Object o) {
        return getDelegate().lastIndexOf(o);
    }

    public boolean remove(final Object o) {
        return getDelegate().remove(o);
    }

    public Object remove(final int index) {
        return getDelegate().remove(index);
    }

    public boolean removeAll(final Collection c) {
        return getDelegate().removeAll(c);
    }

    public boolean retainAll(final Collection c) {
        return getDelegate().retainAll(c);
    }

    public Object set(final int index, final Object element) {
        return getDelegate().set(index, element);
    }

    public int size() {
        return getDelegate().size();
    }

    public Object[] toArray() {
        return getDelegate().toArray();
    }
}
