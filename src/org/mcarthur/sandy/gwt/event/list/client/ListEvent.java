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
public class ListEvent extends EventObject {

    /**
     * Identifies one or more elements were added.
     * @see #isAdded()
     */
    public static final Type ADDED = new Type("ADDED");

    /**
     * Identifies one or more elements were changed.
     * @see #isChanged()
     */
    public static final Type CHANGED = new Type("CHANGED");

    /**
     * Identifies one or more elements were removed.
     * @see #isRemoved()
     */
    public static final Type REMOVED = new Type("REMOVED");

    private final Type type;

    private final int indexStart;
    private final int indexEnd;

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
     */
    public ListEvent(final EventList source, final Type type, final int index) throws IllegalArgumentException {
        this(source, type, index, index+1);
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
     */
    public ListEvent(final EventList source, final Type type, final int indexStart, final int indexEnd) throws IllegalArgumentException {
        super(source);
        assert indexStart != indexEnd : "indexStart and indexEnd must not be the same value.";
        assert type != null : "type must not be null";
        this.type = type;
        if (indexStart < indexEnd) {
            this.indexStart = indexStart;
            this.indexEnd = indexEnd;
        } else {
            this.indexStart = indexEnd;
            this.indexEnd = indexStart;
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
     * @return one of {@link #ADDED}, {@link #CHANGED}, {@link #REMOVED}.
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

    /**
     * Convience for {@link #getSource()} that casts to {@link EventList}.
     *
     * @return same as {@link #getSource()}.
     */
    public EventList getSourceList() {
        return (EventList)getSource();
    }

    public String toString() {
        return "ListEvent[" + type + " (" + getIndexStart() + "," + getIndexEnd() + ")]";
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
