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

import java.util.EventObject;

/**
 * A "PropertyChange" event gets delivered whenever a "bound" or "constrained" property changes.
 *
 * <p>
 * Normally PropertyChangeEvents are accompanied by the name and the old and new value of the changed property.
 * If the new value is a primitive type (such as int or boolean) it must be wrapped as the corresponding java.lang.* Object type (such as Integer or Boolean).
 * </p>
 * <p>
 * <code>Null</code> values may be provided for the old and the new values if their true values are not known.
 * </p>
 * <p>
 * An event source may send a <code>null</code> object as the name to indicate that an arbitrary set of if its properties have changed.
 * In this case the old and new values should also be <code>null</code>.
 * </p>
 *
 * @see java.beans.PropertyChangeEvent
 */
public class PropertyChangeEvent extends EventObject {
    private String propertyName;
    private Object oldValue;
    private Object newValue;

    /**
     * Constructs a new PropertyChangeEvent.
     *
     * @param source The object on which fired the event.
     * @param propertyName The programmatic name of the property that was changed, or <code>null</code> of the whole object changed.
     * @param oldValue The old value of the property if known.
     * @param newValue The new value of the property.
     * @throws IllegalArgumentException if source is <code>null</code>.
     */
    public PropertyChangeEvent(final Object source, final String propertyName, final Object oldValue, final Object newValue) {
        super(source);
        this.propertyName = propertyName;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    /**
     * The programmatic name of the property that was changed.
     *
     * @return the programmatic name of the property that was changed or <code>null</code> if the whole object changed  .
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * The old value for the property, expressed as an Object.
     *
     * @return the old value for the property, expressed as an Object, if known.
     */
    public Object getOldValue() {
        return oldValue;
    }

    /**
     * The new value for the property, expressed as an Object.
     * 
     * @return the new value for the property, expressed as an Object.
     */
    public Object getNewValue() {
        return newValue;
    }
}
