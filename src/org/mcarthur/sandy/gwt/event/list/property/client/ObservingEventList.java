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

package org.mcarthur.sandy.gwt.event.list.property.client;

import org.mcarthur.sandy.gwt.event.list.client.AbstractEventList;
import org.mcarthur.sandy.gwt.event.list.client.EventList;
import org.mcarthur.sandy.gwt.event.list.client.EventLists;
import org.mcarthur.sandy.gwt.event.list.client.ListEvent;
import org.mcarthur.sandy.gwt.event.list.client.ListEventListener;
import org.mcarthur.sandy.gwt.event.property.client.PropertyChangeSource;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * An <code>EventList</code> that adds itself as a <code>PropertyChangeListener</code> to each
 * element added to the list.
 *
 * Elements added to this list must implement {@link PropertyChangeSource}.
 * When the element fires a {@link java.beans.PropertyChangeEvent} the list is notified and fires
 * a {@link ListEvent#CHANGED} event.
 *
 * <p>
 * This <code>EventList</code> is most useful when combined with a <code>EventList</code> view
 * implementation that can maintain a different ordering or present a subset of the elements this
 * list.
 * </p>
 *
 * @author Sandy McArthur
 * @see PropertyChangeListener
 * @see org.mcarthur.sandy.gwt.event.list.client.FilteredEventList
 * @see org.mcarthur.sandy.gwt.event.list.client.SortedEventList
 */
public final class ObservingEventList extends AbstractEventList implements EventList {

    private final EventList delegate = EventLists.eventList();

    private final PropertyChangeListener pcl = new PropertyChangeEventListListener();

    private final ListEventListener observingListEventListener = new ListEventListener() {
        public void listChanged(final ListEvent listEvent) {
            fireListEvent(listEvent.resource(ObservingEventList.this));
        }
    };

    public ObservingEventList() {
        delegate.addListEventListener(observingListEventListener);
    }

    /**
     * @throws IllegalArgumentException when <code>element</code> does not implement {@link PropertyChangeSource}.
     */
    public boolean add(final Object element) throws IllegalArgumentException {
        checkType(element);
        if (delegate.add(element)) {
            ((PropertyChangeSource)element).addPropertyChangeListener(pcl);
            return true;
        }
        return false;
    }

    /**
     * @throws IllegalArgumentException when <code>element</code> does not implement {@link PropertyChangeSource}.
     */
    public void add(final int index, final Object element) throws IllegalArgumentException {
        checkType(element);
        delegate.add(index, element);
        ((PropertyChangeSource)element).addPropertyChangeListener(pcl);
    }

    /**
     * @throws IllegalArgumentException when any element in <code>c</code> does not implement {@link PropertyChangeSource}.
     */
    public boolean addAll(final Collection c) throws IllegalArgumentException {
        checkAllTypes(c);
        return attachAllListeners(c, delegate.addAll(c));
    }

    public boolean contains(final Object o) {
        return delegate.contains(o);
    }

    public boolean containsAll(final Collection c) {
        return delegate.containsAll(c);
    }

    public Object get(final int index) {
        return delegate.get(index);
    }

    public int indexOf(final Object o) {
        return delegate.indexOf(o);
    }

    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    public int lastIndexOf(final Object o) {
        return delegate.lastIndexOf(o);
    }

    public boolean remove(final Object element) {
        final boolean changed = delegate.remove(element);
        if (changed) {
            ((PropertyChangeSource)element).removePropertyChangeListener(pcl);
        }
        return changed;
    }

    public Object remove(final int index) {
        final Object element = delegate.remove(index);
        if (element != null) {
            ((PropertyChangeSource)element).removePropertyChangeListener(pcl);
        }
        return element;
    }

    public boolean removeAll(final Collection c) {
        final Collection removed = new ArrayList(c);
        removed.retainAll(delegate);
        return detachAllListeners(removed, delegate.removeAll(c));
    }

    public boolean retainAll(final Collection c) {
        final Collection removed = new ArrayList(this);
        removed.retainAll(c);
        return detachAllListeners(removed, delegate.retainAll(c));
    }

    public Object set(final int index, final Object element) {
        final Object removed = delegate.set(index, element);
        ((PropertyChangeSource)removed).removePropertyChangeListener(pcl);
        ((PropertyChangeSource)element).addPropertyChangeListener(pcl);
        return removed;
    }

    public int size() {
        return delegate.size();
    }

    private static void checkType(final Object element) {
        if (!(element instanceof PropertyChangeSource)) {
            throw new IllegalArgumentException("element must implement PropertyChangeSource");
        }
    }

    private static void checkAllTypes(final Collection c) {
        final Iterator iter = c.iterator();
        while (iter.hasNext()) {
            final Object element = iter.next();
            checkType(element);
        }
    }

    private boolean attachAllListeners(final Collection c, final boolean changed) {
        if (changed) {
            final Iterator iter = c.iterator();
            while (iter.hasNext()) {
                final Object element = iter.next();
                if (contains(element)) {
                    ((PropertyChangeSource)element).addPropertyChangeListener(pcl);
                }
            }
        }
        return changed;
    }

    private boolean detachAllListeners(final Collection removed, final boolean changed) {
        if (changed) {
            final Iterator iter = removed.iterator();
            while (iter.hasNext()) {
                final Object element = iter.next();
                if (!contains(element)) {
                    ((PropertyChangeSource)element).removePropertyChangeListener(pcl);
                }
            }
        }
        return changed;
    }

    private class PropertyChangeEventListListener implements PropertyChangeListener {
        public void propertyChange(final PropertyChangeEvent evt) {
            final Object o = evt.getSource();
            final int size = size();
            int i;
            for (i=0; i < size; i++) {
                if (o == get(i)) {
                    fireListEvent(ListEvent.createChanged(ObservingEventList.this, i));
                    break;
                }
            }
        }
    }
}
