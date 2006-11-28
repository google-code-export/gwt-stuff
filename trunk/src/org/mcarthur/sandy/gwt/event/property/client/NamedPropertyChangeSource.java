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

import java.beans.PropertyChangeListener;

/**
 * A source for {@link java.beans.PropertyChangeEvent}s
 * that supports listeners only interested in specific properties.
 * There isn't a parallel interface in the java.beans package.
 *
 * @author Sandy McArthur
 */
public interface NamedPropertyChangeSource extends PropertyChangeSource {
    /**
     * Add a PropertyChangeListener for a specific property.
     * The listener will be invoked only when a property change names that specific property.
     * 
     * @param propertyName The name of the property to listen on.
     * @param listener The PropertyChangeListener to be added.
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(String,PropertyChangeListener)
     */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

    /**
     * Remove a PropertyChangeListener for a specific property.
     *
     * @param propertyName The name of the property that was listened on.
     * @param listener The PropertyChangeListener to be removed.
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(String,PropertyChangeListener)
     */
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);
}
