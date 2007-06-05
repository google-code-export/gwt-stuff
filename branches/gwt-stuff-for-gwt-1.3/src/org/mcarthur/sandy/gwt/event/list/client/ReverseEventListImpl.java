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

/**
 * A "ReverseEventList". Elements of the backing list are presented in opposite order.
 *
 * @author Sandy McArthur
 */
class ReverseEventListImpl extends AbstractEventList implements EventList {
    private final EventList delegate;
    private final ReverseListEventListener listEventListener = new ReverseListEventListener();

    private int size = 0;

    public ReverseEventListImpl(final EventList delegate) {
        this.delegate = delegate;
        delegate.addListEventListener(listEventListener);

        size = delegate.size();
    }

    protected int getSourceIndex(final int mutationIndex) {
        return invertIndex(mutationIndex);
    }

    private int invertIndex(final int index) {
        return size() - index;
    }

    public Object get(final int index) {
        try {
            return delegate.get(invertIndex(index)-1);
        } catch (IndexOutOfBoundsException iobe) {
            final IndexOutOfBoundsException e = new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
            e.initCause(iobe);
            throw e;
        }
    }

    public int size() {
        return size;
    }

    public Object set(final int index, final Object element) {
        try {
            return delegate.set(invertIndex(index) - 1, element);
        } catch (IndexOutOfBoundsException iobe) {
            final IndexOutOfBoundsException e = new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
            e.initCause(iobe);
            throw e;
        }
    }

    public void add(final int index, final Object element) {
        try {
            final int invertedIndex = invertIndex(index);
            delegate.add(invertedIndex, element);
        } catch (IndexOutOfBoundsException iobe) {
            final IndexOutOfBoundsException e = new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
            e.initCause(iobe);
            throw e;
        }
    }

    public Object remove(final int index) {
        try {
            return delegate.remove(invertIndex(index)-1);
        } catch (IndexOutOfBoundsException iobe) {
            final IndexOutOfBoundsException e = new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
            e.initCause(iobe);
            throw e;
        }
    }

    private class ReverseListEventListener implements ListEventListener {
        public void listChanged(final ListEvent listEvent) {
            final ListEvent reverse;
            final int sizeDelta = listEvent.getIndexEnd() - listEvent.getIndexStart();
            if (listEvent.isAdded()) {
                size += sizeDelta;
                final int revStart = invertIndex(listEvent.getIndexEnd());
                reverse = ListEvent.createAdded(ReverseEventListImpl.this, revStart, revStart + sizeDelta);

            } else if (listEvent.isChanged()) {
                final int revStart = invertIndex(listEvent.getIndexEnd());
                reverse = ListEvent.createChanged(ReverseEventListImpl.this, revStart, revStart + sizeDelta);

            } else if (listEvent.isRemoved()) {
                final int revStart = invertIndex(listEvent.getIndexEnd());
                reverse = ListEvent.createRemoved(ReverseEventListImpl.this, revStart, revStart + sizeDelta);
                size -= sizeDelta;

            } else {
                reverse = listEvent.resource(ReverseEventListImpl.this);
            }
            fireListEvent(reverse);
        }
    }
}
