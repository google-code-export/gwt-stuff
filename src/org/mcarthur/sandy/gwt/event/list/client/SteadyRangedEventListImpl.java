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
class SteadyRangedEventListImpl extends RangedEventListImpl implements RangedEventList {

    protected SteadyRangedEventListImpl(final EventList delegate, final int maxSize) {
        super(delegate, maxSize);
    }

    protected ListEventListener getListEventListener() {
        return new SteadyPaginatedListEventListener();
        //return new SteadyRangedListEventListener();
    }

    private class SteadyRangedListEventListener extends RangedListEventListener {

        protected void listChangedRemoved(final int indexStart, final int indexEnd) {
            if (indexEnd <= getStart()) {
                // if the removal didn't affect any visible elements
                final int removedSize = indexEnd - indexStart;
                setStartOffset(getStart() - removedSize);
                fireListEvent(new ListEvent(SteadyRangedEventListImpl.this));

            } else {
                super.listChangedRemoved(indexStart, indexEnd);
            }
        }
    }

    private class SteadyPaginatedListEventListener implements ListEventListener {
        public void listChanged(final ListEvent listEvent) {
            // XXX: lots of room for event fireng optimizations here
            final int indexStart = listEvent.getIndexStart();
            final int indexEnd = listEvent.getIndexEnd();
            final int maxEnd = sumWithoutOverflow(getStart(), getMaxSize());

            if (listEvent.isAdded()) {
                final int addedSize = indexEnd - indexStart;
                if (indexStart < getStart()) {
                    // if the insert causes the start offset to change
                    setStartOffset(getStart() + addedSize);
                    fireListEvent(new ListEvent(SteadyRangedEventListImpl.this));

                } else if (indexStart < maxEnd) {
                    final int oldTotal = getTotal() - addedSize;
                    // if the old size was less than the max size
                    if (oldTotal < maxEnd) {
                        // we need to add to increase size() to maxSize and update for the rest
                        final int visableAddedSize = Math.min(maxEnd - indexStart, addedSize);
                        final int addStart = Math.max(0, indexStart - getStart());
                        final int addEnd = addStart + visableAddedSize;
                        if (size() > 0) {
                            fireListEvent(new ListEvent(SteadyRangedEventListImpl.this, ListEvent.ADDED, addStart, addEnd));
                            if (addEnd != size()) {
                                fireListEvent(new ListEvent(SteadyRangedEventListImpl.this, ListEvent.CHANGED, addEnd, size()));
                            }
                        }
                    } else {
                        fireListEvent(new ListEvent(SteadyRangedEventListImpl.this, ListEvent.CHANGED, 0, getMaxSize()));
                    }
                } else {
                    fireListEvent(new ListEvent(SteadyRangedEventListImpl.this));
                }

            } else if (listEvent.isChanged()) {
                // does the event affect our range?
                if (getStart() < indexEnd && indexStart < maxEnd) {
                    // clamp to current page range
                    final int changedStart = Math.max(0, indexStart - getStart());
                    final int changedEnd = Math.min(getMaxSize(), indexEnd - getStart());
                    fireListEvent(new ListEvent(SteadyRangedEventListImpl.this, ListEvent.CHANGED, changedStart, changedEnd));
                } else {
                    fireListEvent(new ListEvent(SteadyRangedEventListImpl.this));
                }

            } else if (listEvent.isRemoved()) {
                final int removedSize = indexEnd - indexStart;
                if (indexEnd <= getStart()) {
                    // if the removal didn't affect any visible elements
                    setStartOffset(getStart() - removedSize);
                    fireListEvent(new ListEvent(SteadyRangedEventListImpl.this));

                } else if (indexStart < maxEnd) {
                    // if the new size shrinks the visible size
                    if (size() < getMaxSize()) {
                        final int removedStart = Math.max(0, indexStart - getStart());
                        final int removedEnd = removedStart + removedSize;
                        fireListEvent(new ListEvent(SteadyRangedEventListImpl.this, ListEvent.REMOVED, removedStart, removedEnd));
                    } else {
                        fireListEvent(new ListEvent(SteadyRangedEventListImpl.this, ListEvent.CHANGED, 0, getMaxSize()));
                    }
                } else {
                    fireListEvent(new ListEvent(SteadyRangedEventListImpl.this));
                }

            } else {
                fireListEvent(listEvent.resource(SteadyRangedEventListImpl.this));
            }
        }

        private int sumWithoutOverflow(final int start, final int maxSize) {
            return (Integer.MAX_VALUE - maxSize > start) ? start + maxSize : Integer.MAX_VALUE;
        }
    }

}
