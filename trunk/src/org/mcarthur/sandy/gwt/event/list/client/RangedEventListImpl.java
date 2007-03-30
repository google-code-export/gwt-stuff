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
 * RangedEventList that presents a view of a range of elements in another EventList.
 *
 * @author Sandy McArthur
 */
class RangedEventListImpl extends TransformedEventList implements RangedEventList {
    private int startOffset = 0;
    private int maxSize = Integer.MAX_VALUE;
    private int size;

    protected RangedEventListImpl(final EventList delegate, final int maxSize) {
        super(delegate);
        size = delegate.size();
        getDelegate().addListEventListener(getListEventListener());
        setStartOffset(0);
        setMaxSize(maxSize);
    }

    protected ListEventListener getListEventListener() {
        return new RangedListEventListener();
    }

    protected class RangedListEventListener implements ListEventListener {
        public void listChanged(final ListEvent listEvent) {
            final int indexStart = listEvent.getIndexStart();
            final int indexEnd = listEvent.getIndexEnd();

            if (listEvent.isAdded()) {
                listChangedAdded(indexStart, indexEnd);

            } else if (listEvent.isChanged()) {
                listChangedChanged(indexStart, indexEnd);

            } else if (listEvent.isRemoved()) {
                listChangedRemoved(indexStart, indexEnd);

            } else {
                fireListEvent(listEvent.resource(RangedEventListImpl.this));
            }
        }

        protected void listChangedAdded(final int indexStart, final int indexEnd) {
            final int start = getStart();
            final int size = size();
            final int maxSize = getMaxSize();
            final int end = sumWithoutOverflow(start, maxSize);

            final int rangeSize = Math.min(indexEnd - indexStart, Math.min(maxSize, end - indexStart));
            final int removeSize = Math.min(size - (maxSize - rangeSize), end - indexStart);
            final int insertStart = indexStart - start;

            if (removeSize > 0) {
                RangedEventListImpl.this.size -= removeSize;
                assert RangedEventListImpl.this.size >= 0 : "size: " + RangedEventListImpl.this.size;
                fireListEvent(new ListEvent(RangedEventListImpl.this, ListEvent.REMOVED, size - removeSize, size));
            }
            if (0 <= insertStart + size && insertStart < maxSize) {
                RangedEventListImpl.this.size += rangeSize;
                assert RangedEventListImpl.this.size >= 0 : "size: " + RangedEventListImpl.this.size;
                final int indexStartOrZero = Math.max(0, insertStart);
                fireListEvent(new ListEvent(RangedEventListImpl.this, ListEvent.ADDED, indexStartOrZero, indexStartOrZero + rangeSize));
            } else {
                // the event was after our range, let them know something changed.
                fireListEvent(new ListEvent(RangedEventListImpl.this));
            }
        }

        protected void listChangedChanged(final int indexStart, final int indexEnd) {
            final int start = getStart();
            final int maxSize = getMaxSize();
            final int end = sumWithoutOverflow(start, maxSize);
            // does the event affect our range?
            if (start < indexEnd && indexStart < end) {
                // clamp to current page range
                final int changedStart = Math.max(0, indexStart - start);
                final int changedEnd = Math.min(maxSize, indexEnd - start);
                fireListEvent(new ListEvent(RangedEventListImpl.this, ListEvent.CHANGED, changedStart, changedEnd));
            } else {
                // Something changed but it didn't directly affect us.
                fireListEvent(new ListEvent(RangedEventListImpl.this));
            }
        }

        protected void listChangedRemoved(final int indexStart, final int indexEnd) {
            final int start = getStart();
            final int size = size();
            final int delegateSize = getDelegate().size();
            final int maxSize = getMaxSize();
            final int end = sumWithoutOverflow(start, maxSize);

            final int rangeSize = Math.min(indexEnd - indexStart, Math.min(maxSize, end - indexStart));
            final int addedSize = Math.min(size - (maxSize - rangeSize), delegateSize - end);
            final int removeStart = Math.max(0, indexStart - start);

            if (removeStart < maxSize) {
                RangedEventListImpl.this.size -= rangeSize;
                assert RangedEventListImpl.this.size >= 0 : "size: " + RangedEventListImpl.this.size;
                fireListEvent(new ListEvent(RangedEventListImpl.this, ListEvent.REMOVED, removeStart, removeStart + rangeSize));

                if (addedSize > 0) {
                    RangedEventListImpl.this.size -= addedSize;
                    assert RangedEventListImpl.this.size >= 0 : "size: " + RangedEventListImpl.this.size;
                    fireListEvent(new ListEvent(RangedEventListImpl.this, ListEvent.ADDED, size - addedSize, size));
                }
            } else {
                // the event was after our range, let them know something changed.
                fireListEvent(new ListEvent(RangedEventListImpl.this));
            }
        }

        private int sumWithoutOverflow(final int start, final int maxSize) {
            return (Integer.MAX_VALUE - maxSize > start) ? start + maxSize : Integer.MAX_VALUE;
        }
    }

    protected int getSourceIndex(final int mutationIndex) {
        return getStart() + mutationIndex;
    }

    public int size() {
        return size;
    }

    public int getStart() {
        return getStartOffset();
    }

    public void setStart(final int newStart) {
        if (newStart < 0) {
            throw new IllegalArgumentException("Start must be positive. was: " + newStart);
        }
        if (getStartOffset() != newStart) {
            final int oldStart = getStartOffset();
            final int startDelta = newStart - oldStart;
            assert startDelta != 0;
            final int oldSize = size();
            final int newSize = Math.min(getMaxSize(), Math.max(getDelegate().size() - newStart, 0));

            if (oldSize == 0 && newSize == 0) {
                // offset changed but contents didn't.
                setStartOffset(newStart);
                fireListEvent(new ListEvent(this));
                return;
            }

            final int sizeDelta = newSize - oldSize;

            if (startDelta > 0) {
                // start increased
                final int delta = Math.min(startDelta, getMaxSize());
                assert delta > 0 : "delta: " + delta;
                size -= delta;
                assert size >= 0 : "size: " + size;

                setStartOffset(newStart);
                fireListEvent(new ListEvent(this, ListEvent.REMOVED, 0, delta));

                final int toAdd = delta + sizeDelta;
                assert toAdd >= 0 : "Size changed in a unexpected way.";

                if (toAdd > 0) {
                    size += toAdd;
                    fireListEvent(new ListEvent(this, ListEvent.ADDED, oldSize - delta, newSize));
                }
            } else {
                // start decreased
                // startDelta is negative
                final int delta = Math.min(-startDelta, oldSize);
                assert delta >= 0 : "delta: " + delta;
                size -= delta;
                assert size >= 0 : "size: " + size;

                setStartOffset(newStart);
                if (delta > 0) {
                    fireListEvent(new ListEvent(this, ListEvent.REMOVED, oldSize - delta, oldSize));                    
                }

                final int toAdd = delta + sizeDelta;
                assert toAdd >= 0 : "Size changed in a unexpected way.";

                if (toAdd > 0) {
                    size += toAdd;
                    fireListEvent(new ListEvent(this, ListEvent.ADDED, 0, toAdd));
                }
            }
        }
    }

    protected int getStartOffset() {
        return startOffset;
    }

    protected void setStartOffset(final int startOffset) {
        assert startOffset >= 0 : "startOffset must be greater than zero. was: " + startOffset;
        this.startOffset = startOffset;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(final int newMaxSize) {
        if (newMaxSize < 0) {
            throw new IllegalArgumentException("maxSize must be positive. was: " + newMaxSize);
        } else if (maxSize == newMaxSize) {
            // no change
            return;
        }
        
        final int oldMaxSize = maxSize;
        final int oldSize = size();
        final int newSize = Math.min(newMaxSize, Math.max(getDelegate().size() - getStart(), 0));

        if (oldSize == newSize) {
            // offset changed but contents didn't.
            maxSize = newMaxSize;
            fireListEvent(new ListEvent(this));
            return;
        }

        final int maxSizeDelta = newMaxSize - oldMaxSize;
        assert maxSizeDelta != 0 : "maxSizeDelta: " + maxSizeDelta;

        if (maxSizeDelta > 0) {
            // maxSize increased
            final int delta = Math.min(maxSizeDelta, newSize - oldSize);
            assert delta >= 0 : "delta: " + delta;
            size += delta;
            assert size >= 0 : "size: " + size;

            maxSize = newMaxSize;

            if (delta > 0) {
                fireListEvent(new ListEvent(this, ListEvent.ADDED, oldSize, newSize));
            }

        } else {
            // maxSize decreased
            // maxSizeDelta is negative
            final int delta = Math.min(-maxSizeDelta, oldSize - newSize);
            assert delta >= 0 : "delta: " + delta;
            size -= delta;
            assert size >= 0 : "size: " + size;

            maxSize = newMaxSize;

            if (delta > 0) {
                fireListEvent(new ListEvent(this, ListEvent.REMOVED, newSize, oldSize));
            }
        }
    }

    public int getTotal() {
        return getDelegate().size();
    }
}
