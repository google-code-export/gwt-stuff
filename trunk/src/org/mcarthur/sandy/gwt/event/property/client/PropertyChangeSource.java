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
 * A source for {@link PropertyChangeEvent}s.
 * There isn't a parallel in the java.beans package.
 *
 * @author Sandy McArthur
 */
public interface PropertyChangeSource {
    /**
     * Add a PropertyChangeListener for all properties.
     *
     * @param listener the PropertyChangeListener to be added.
     * @see PropertyChangeSupport#addPropertyChangeListener(PropertyChangeListener)
     */
    public void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Remove a PropertyChangeListener for all properties.
     * This removes a listener that was registered with {@link #addPropertyChangeListener(PropertyChangeListener)}.
     *
     * @param listener the PropertyChangeListener to be removed.
     * @see PropertyChangeSupport#removePropertyChangeListener(PropertyChangeListener)
     */
    public void removePropertyChangeListener(PropertyChangeListener listener);
}
