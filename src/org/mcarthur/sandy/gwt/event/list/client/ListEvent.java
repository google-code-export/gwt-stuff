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

import java.util.EventObject;

/**
 * Event encapsulates changes to an EventList.
 *
 * @author Sandy McArthur
 */
public final class ListEvent extends EventObject {

    /**
     * Identifies one or more elements were added.
     *
     * @see #isAdded()
     * @see #createAdded(EventList, int)
     * @see #createAdded(EventList, int, int)
     */
    public static final Type ADDED = new Type("ADDED");

    /**
     * Identifies one or more elements were changed.
     *
     * @see #isChanged()
     * @see #createChanged(EventList, int)
     * @see #createChanged(EventList, int, int)
     */
    public static final Type CHANGED = new Type("CHANGED");

    /**
     * Identifies one or more elements were removed.
     *
     * @see #isRemoved()
     * @see #createRemoved(EventList, int)
     * @see #createRemoved(EventList, int, int)
     */
    public static final Type REMOVED = new Type("REMOVED");

    /**
     * Identifies that the current EventList is about to perform a series of related changes.
     * If an entire list change can be expressed with one {@link ListEvent} then batch events should not be used.
     * For each <code>BATCH_START</code> event fired there <b>must</b> be one {@link #BATCH_END} event fired.
     * Pairs of batch events can be nested in other pairs of batch events.
     *
     * @see #isBatchStart()
     * @see #BATCH_END
     * @see #createBatchStart(EventList)
     * @see #createBatchStart(EventList, ListEvent)
     */
    public static final Type BATCH_START = new Type("BATCH_START");

    /**
     * Identifies that the current EventList has finished a series of related changes.
     * If an entire list change can be expressed with one {@link ListEvent} then batch events should not be used.
     * For each <code>BATCH_END</code> event fired there <b>must</b> have been one {@link #BATCH_START} event fired.
     * Pairs of batch events can be nested in other pairs of batch events.
     *
     * @see #isBatchEnd()
     * @see #BATCH_START
     * @see #createBatchEnd(EventList)
     * @see #createBatchEnd(EventList, ListEvent)
     */
    public static final Type BATCH_END = new Type("BATCH_END");

    /**
     * Identifies zero elements changed but the list changed in other ways.
     * @see #createOther(EventList)
     */
    public static final Type OTHER = new Type("OTHER");

    public static ListEvent createAdded(final EventList source, final int index) throws IllegalArgumentException {
        return createAdded(source, index, null);
    }

    public static ListEvent createAdded(final EventList source, final int index, final ListEvent cause) throws IllegalArgumentException {
        return new ListEvent(source, ADDED, index);
    }

    public static ListEvent createAdded(final EventList source, final int indexStart, final int indexEnd) throws IllegalArgumentException {
        return createAdded(source, indexStart, indexEnd, null);
    }

    public static ListEvent createAdded(final EventList source, final int indexStart, final int indexEnd, final ListEvent cause) throws IllegalArgumentException {
        return new ListEvent(source, ADDED, indexStart, indexEnd, cause);
    }

    public static ListEvent createChanged(final EventList source, final int index) throws IllegalArgumentException {
        return createChanged(source, index, null);
    }

    public static ListEvent createChanged(final EventList source, final int index, final ListEvent cause) throws IllegalArgumentException {
        return new ListEvent(source, CHANGED, index);
    }

    public static ListEvent createChanged(final EventList source, final int indexStart, final int indexEnd) throws IllegalArgumentException {
        return createChanged(source, indexStart, indexEnd, null);
    }

    public static ListEvent createChanged(final EventList source, final int indexStart, final int indexEnd, final ListEvent cause) throws IllegalArgumentException {
        return new ListEvent(source, CHANGED, indexStart, indexEnd, cause);
    }

    public static ListEvent createRemoved(final EventList source, final int index) throws IllegalArgumentException {
        return createRemoved(source, index, null);
    }

    public static ListEvent createRemoved(final EventList source, final int index, final ListEvent cause) throws IllegalArgumentException {
        return new ListEvent(source, REMOVED, index);
    }

    public static ListEvent createRemoved(final EventList source, final int indexStart, final int indexEnd) throws IllegalArgumentException {
        return createRemoved(source, indexStart, indexEnd, null);
    }

    public static ListEvent createRemoved(final EventList source, final int indexStart, final int indexEnd, final ListEvent cause) throws IllegalArgumentException {
        return new ListEvent(source, REMOVED, indexStart, indexEnd, cause);
    }

    public static ListEvent createBatchStart(final EventList source) throws IllegalArgumentException {
        return createBatchStart(source, null);
    }

    public static ListEvent createBatchStart(final EventList source, final ListEvent cause) throws IllegalArgumentException {
        return new ListEvent(source, BATCH_START);
    }

    public static ListEvent createBatchEnd(final EventList source) throws IllegalArgumentException {
        return createBatchEnd(source, null);
    }

    public static ListEvent createBatchEnd(final EventList source, final ListEvent cause) throws IllegalArgumentException {
        return new ListEvent(source, BATCH_END);
    }

    public static ListEvent createOther(final EventList source) throws IllegalArgumentException {
        return createOther(source, null);
    }

    public static ListEvent createOther(final EventList source, final ListEvent cause) throws IllegalArgumentException {
        return new ListEvent(source, OTHER);
    }

    private final Type type;

    private final int indexStart;
    private final int indexEnd;

    private final ListEvent cause;

    /**
     * Construct a ListEvent when none of the elements changed but the list did in some other manner.
     * @param source The EventList on which the ListEvent initially occurred.
     * @see #OTHER
     * @deprecated {@link #createOther(EventList)}
     */
    public ListEvent(final EventList source) {
        super(source);
        type = OTHER;
        indexStart = -1;
        indexEnd = -1;
        cause = null;
    }

    private ListEvent(final EventList source, final Type type) throws IllegalArgumentException {
        super(source);
        this.type = type;
        indexStart = -1;
        indexEnd = -1;
        cause = null;
    }

    /**
     * Constructs a ListEvent for one element.
     * This is the same as calling
     * {@link #ListEvent(EventList, org.mcarthur.sandy.gwt.event.list.client.ListEvent.Type, int, int)}
     *  with sequential indexes.
     *
     * @param source The EventList on which the ListEvent initially occurred.
     * @param type one of {@link #ADDED}, {@link #CHANGED}, or {@link #REMOVED}.
     * @param index affected element.
     * @throws IllegalArgumentException if source is <code>null</code>.
     * @deprecated {@link #createAdded(EventList, int)} {@link #createChanged(EventList, int)} {@link #createRemoved(EventList, int)} 
     */
    public ListEvent(final EventList source, final Type type, final int index) throws IllegalArgumentException {
        this(source, type, index, index+1, null);
    }

    /**
     * Constructs a ListEvent for a range of elements.
     * If needed <code>indexStart</code> and <code>indexEnd</code> will be transposed to keep them
     * in numerical order.
     *
     * @param source The EventList on which the ListEvent initially occurred.
     * @param type one of {@link #ADDED}, {@link #CHANGED}, or {@link #REMOVED}.
     * @param indexStart one end of the interval.
     * @param indexEnd one end of the interval.
     * @throws IllegalArgumentException if source is <code>null</code>.
     * @deprecated {@link #createAdded(EventList, int, int)} {@link #createChanged(EventList, int, int)} {@link #createRemoved(EventList, int, int)}
     */
    public ListEvent(final EventList source, final Type type, final int indexStart, final int indexEnd) throws IllegalArgumentException {
        this(source, type, indexStart, indexEnd, null);
    }

    private ListEvent(final EventList source, final Type type, final int indexStart, final int indexEnd, final ListEvent cause) throws IllegalArgumentException {
        super(source);
        assert OTHER.equals(type) || indexStart != indexEnd : "indexStart and indexEnd must not be the same value.";
        assert type != null : "type must not be null";
        this.type = type;
        if (indexStart < indexEnd) {
            this.indexStart = indexStart;
            this.indexEnd = indexEnd;
        } else {
            this.indexStart = indexEnd;
            this.indexEnd = indexStart;
        }
        this.cause = cause;
    }

    /**
     * Creates a copy of this ListEvent using a new EventList source.
     *
     * @param newSource the new source EventList for the ListEvent.
     * @return a ListEvent copy but with newSource as the event's source.
     */
    public ListEvent resource(final EventList newSource) {
        if (OTHER.equals(getType())) {
            return ListEvent.createOther(newSource, this);
        } else if (BATCH_START.equals(getType())) {
            return ListEvent.createBatchStart(newSource, this);
        } else if (BATCH_END.equals(getType())) {
            return ListEvent.createBatchEnd(newSource, this);
        } else {
            return new ListEvent(newSource, getType(), getIndexStart(), getIndexEnd(), this);
        }
    }

    /**
     * First index in the range, inclusive.
     *
     * @return first index in the range, inclusive.
     */
    public int getIndexStart() {
        return indexStart;
    }

    /**
     * Last index in the range, exclusive.
     *
     * @return last index in the range, exclusive.
     */
    public int getIndexEnd() {
        return indexEnd;
    }

    /**
     * The type of event.
     *
     * @return one of {@link #ADDED}, {@link #CHANGED}, {@link #REMOVED}, {@link #OTHER}.
     */
    public Type getType() {
        return type;
    }

    /**
     * True when this event's type is {@link #ADDED}.
     * @return true when this event's type is {@link #ADDED}.
     */
    public final boolean isAdded() {
        return ADDED.equals(getType());
    }

    /**
     * True when this event's type is {@link #CHANGED}.
     * @return true when this event's type is {@link #CHANGED}.
     */
    public final boolean isChanged() {
        return CHANGED.equals(getType());
    }

    /**
     * True when this event's type is {@link #REMOVED}.
     * @return true when this event's type is {@link #REMOVED}.
     */
    public final boolean isRemoved() {
        return REMOVED.equals(getType());
    }

    public final boolean isBatchStart() {
        return BATCH_START.equals(getType());
    }

    public final boolean isBatchEnd() {
        return BATCH_END.equals(getType());
    }

    public final boolean isOther() {
        return OTHER.equals(getType());
    }

    /**
     * Convience for {@link #getSource()} that casts to {@link EventList}.
     *
     * @return same as {@link #getSource()}.
     */
    public EventList getSourceList() {
        return (EventList)getSource();
    }

    public String toString() {
        if (OTHER.equals(getType()) || BATCH_START.equals(getType()) || BATCH_END.equals(getType())) {
            return "ListEvent[" + getType() + "]";
        } else {
            return "ListEvent[" + getType() + " (" + getIndexStart() + "," + getIndexEnd() + ")]";
        }
    }

    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof ListEvent)) return false;
        //if (o == null || getClass() != o.getClass()) return false;

        final ListEvent event = (ListEvent)o;

        return indexEnd == event.indexEnd && indexStart == event.indexStart && type.equals(event.type) && getSourceList().equals(event.getSourceList());
    }

    public int hashCode() {
        int result;
        result = type.hashCode();
        result = 31 * result + indexStart;
        result = 31 * result + indexEnd;
        return result;
    }

    /**
     * Enum of the possible types of ListEvents.
     */
    public static final class Type {
        private final String name;

        private Type(final String name) {
            this.name = name;
        }

        public boolean equals(final Object o) {
            if (this == o) return true;
            if (!(o instanceof Type)) return false;
            final Type that = (Type)o;
            return name.equals(that.name);
        }

        public int hashCode() {
            return name.hashCode();
        }

        public String toString() {
            return name;
        }
    }
}
