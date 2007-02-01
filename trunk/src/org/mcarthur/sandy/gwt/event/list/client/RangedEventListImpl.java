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

import java.util.AbstractList;
import java.util.List;

/**
 * RangedEventList that presents a view of one page of another EventList.
 *
 * @author Sandy McArthur
 */
class RangedEventListImpl extends TransformedEventList implements RangedEventList {
    private int start;
    private int maxSize;
    private List translationList = new TranslationList();

    protected RangedEventListImpl(final EventList delegate) {
        this(delegate, Integer.MAX_VALUE);
    }

    protected RangedEventListImpl(final EventList delegate, final int maxSize) {
        super(delegate);
        getDelegate().addListEventListener(new PaginatedListEventListener());
        setStart(0);
        setMaxSize(maxSize);
    }

    private class PaginatedListEventListener implements ListEventListener {
        public void listChanged(final ListEvent listEvent) {
            // XXX: lots of room for event fireng optimizations here
            final int indexStart = listEvent.getIndexStart();
            final int indexEnd = listEvent.getIndexEnd();
            final int maxEnd = start + maxSize;

            if (listEvent.isAdded()) {
                if (indexStart < maxEnd) {
                    final int addedSize = indexEnd - indexStart;
                    final int oldTotal = getTotal() - addedSize;
                    // if the old size was less than the max size
                    if (oldTotal < maxEnd) {
                        // we need to add to increase size() to maxSize and update for the rest
                        final int visableAddedSize = Math.min(maxEnd - indexStart, addedSize);
                        final int addStart = Math.max(0, indexStart - start);
                        final int addEnd = addStart + visableAddedSize;
                        if (size() > 0) {
                            fireListEvent(new ListEvent(RangedEventListImpl.this, ListEvent.ADDED, addStart, addEnd));
                            if (addEnd != size()) {
                                fireListEvent(new ListEvent(RangedEventListImpl.this, ListEvent.CHANGED, addEnd, size()));
                            }
                        }
                    } else {
                        fireListEvent(new ListEvent(RangedEventListImpl.this, ListEvent.CHANGED, 0, maxSize));
                    }
                } else {
                    fireListEvent(new ListEvent(RangedEventListImpl.this));
                }

            } else if (listEvent.isChanged()) {
                // does the event affect our range?
                if (start < indexEnd && indexStart < maxEnd) {
                    // clamp to current page range
                    final int changedStart = Math.max(0, indexStart - start);
                    final int changedEnd = Math.min(maxSize, indexEnd - start);
                    fireListEvent(new ListEvent(RangedEventListImpl.this, ListEvent.CHANGED, changedStart, changedEnd));
                } else {
                    fireListEvent(new ListEvent(RangedEventListImpl.this));
                }

            } else if (listEvent.isRemoved()) {
                if (indexStart < maxEnd) {
                    final int removedSize = indexEnd - indexStart;
                    final int oldTotal = getTotal() + removedSize;
                    // if the new size shrinks the visible size
                    if (size() < maxSize) {
                        final int removedStart = Math.max(0, indexStart - start);
                        final int removedEnd = removedStart + removedSize;
                        fireListEvent(new ListEvent(RangedEventListImpl.this, ListEvent.REMOVED, removedStart, removedEnd));
                    } else {
                        fireListEvent(new ListEvent(RangedEventListImpl.this, ListEvent.CHANGED, 0, maxSize));
                    }
                } else {
                    fireListEvent(new ListEvent(RangedEventListImpl.this));
                }

            } else {
                fireListEvent(listEvent.resource(RangedEventListImpl.this));
            }
        }
    }

    protected List getTranslations() {
        return translationList;
    }

    public int getStart() {
        return start;
    }

    public void setStart(final int start) {
        if (start < 0) {
            throw new IllegalArgumentException("Start must be positive. was: " + start);
        }
        if (this.start != start) {
            final int oldStart = this.start;
            final int oldSize = size();
            this.start = start;
            final int newSize = size();
            if (oldSize < newSize) {
                // bigger, start decreased
                final int sizeChange = newSize - oldSize;
                fireListEvent(new ListEvent(RangedEventListImpl.this, ListEvent.ADDED, 0, sizeChange));
                if (newSize > sizeChange) {
                    fireListEvent(new ListEvent(RangedEventListImpl.this, ListEvent.CHANGED, sizeChange, newSize));
                }
            } else if (oldSize > newSize) {
                // smaller, start increased
                final int startChange = start - oldStart;
                final int sizeChange = oldSize - newSize;
                fireListEvent(new ListEvent(RangedEventListImpl.this, ListEvent.REMOVED, 0, sizeChange));
                if (newSize > 0) {
                    fireListEvent(new ListEvent(RangedEventListImpl.this, ListEvent.CHANGED, 0, newSize));
                }
            } else if (newSize > 0) {
                fireListEvent(new ListEvent(RangedEventListImpl.this, ListEvent.CHANGED, 0, newSize));
            }
        }
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(final int maxSize) {
        if (maxSize < 0) {
            throw new IllegalArgumentException("MaxSize must be positive. was: " + maxSize);
        }
        final int oldMaxSize = this.maxSize;
        final int oldSize = size();
        this.maxSize = maxSize;
        final int size = size();
        if (oldMaxSize < maxSize) {
            // grew, Add
            final int actualChange = size - oldSize;
            if (actualChange > 0) {
                fireListEvent(new ListEvent(RangedEventListImpl.this, ListEvent.ADDED, size - actualChange, size));
            }
        } else if (oldMaxSize > maxSize) {
            // shrunk, remove
            final int actualChange = oldSize - size;
            if (actualChange > 0) {
                fireListEvent(new ListEvent(RangedEventListImpl.this, ListEvent.REMOVED, size, size + actualChange));
            }
        }
    }

    public int getTotal() {
        return getDelegate().size();
    }

    /**
     * A List that creates
     * {@link org.mcarthur.sandy.gwt.event.list.client.TransformedEventList.Index}s as needed based
     * on {@link RangedEventListImpl#start} and {@link RangedEventListImpl#maxSize}.
     */
    private class TranslationList extends AbstractList {
        public Object get(final int index) {
            if (index >= size()) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
            }
            return new Index(start + index);
        }

        public int size() {
            return Math.max(0, Math.min(maxSize, getTotal() - start));
        }
    }
}
