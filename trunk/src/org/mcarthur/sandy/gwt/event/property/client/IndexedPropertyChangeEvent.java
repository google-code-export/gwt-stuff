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

package org.mcarthur.sandy.gwt.event.property.client;

/**
 * An "IndexedPropertyChange" event gets delivered whenever a bound indexed property changes.
 *
 * @see java.beans.IndexedPropertyChangeEvent
 */
public class IndexedPropertyChangeEvent extends PropertyChangeEvent {
    private int index;

    /**
     * Constructs a IndexedPropertyChangeEvent.
     *
     * @param source The object on which fired the event.
     * @param propertyName The programmatic name of the property that was changed, or null of the whole object changed.
     * @param oldValue The old value of the property.
     * @param newValue The new value of the property.
     * @param index The index of the property element that was changed.
     * @throws IllegalArgumentException if source is null.
     */
    public IndexedPropertyChangeEvent(final Object source, final String propertyName, final Object oldValue, final Object newValue, final int index) {
        super(source, propertyName, oldValue, newValue);
        this.index = index;
    }

    /**
     * The index of the property that was changed.
     *
     * @return The index specifying the property element that was changed.
     */
    public int getIndex() {
        return index;
    }
}
