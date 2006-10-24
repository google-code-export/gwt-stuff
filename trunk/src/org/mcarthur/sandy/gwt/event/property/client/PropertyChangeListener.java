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
 * A "PropertyChange" event gets fired whenever a bean changes a "bound" property.
 * You can register a PropertyChangeListener with a source bean so as to be notified
 * of any bound property updates.
 *
 * @see java.beans.PropertyChangeListener
 */
public interface PropertyChangeListener {
    /**
     * Called when a bound property is changed.
     *
     * @param event An event describing the property change.
     */
    public void propertyChange(PropertyChangeEvent event);
}
