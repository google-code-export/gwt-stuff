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

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

/**
 * This is a utility class that can be used by beans that support bound properties.
 * You can use an instance of this class as a member field of your bean and delegate various work to it.
 * 
 * @see java.beans.PropertyChangeSupport
 */
public class PropertyChangeSupport {
    private Object source;

    private List listeners;
    private Map propertyListeners = new HashMap();

    /**
     * Constructs a PropertyChangeSupport object.
     * 
     * @param source the object to be given as the source for any events.
     */
    public PropertyChangeSupport(final Object source) {
        this.source = source;
    }

    /**
     * Add a PropertyChangeListener to the listener list.
     * The listener is registered for all properties.
     *
     * @param listener the PropertyChangeListener to be added.
     */
    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        if (listener == null) {
            return;
        }
        if (listeners == null) {
            listeners = new ArrayList();
        }
        listeners.add(listener);
    }

    /**
     * Remove a PropertyChangeListener from the listener list.
     * This removes a PropertyChangeListener that was registered for all properties.
     *
     * @param listener the PropertyChangeListener to be removed.
     */
    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        if (listener == null || listeners == null) {
            return;
        }
        listeners.remove(listener);
    }

    public PropertyChangeListener[] getPropertyChangeListeners() {
        if (listeners == null) {
            return new PropertyChangeListener[0];
        }
        final PropertyChangeListener[] pcl = new PropertyChangeListener[listeners.size()];
        final Iterator iter = listeners.iterator();
        int i = 0;
        while (iter.hasNext()) {
            pcl[i++] = (PropertyChangeListener)iter.next();
        }
        return pcl;
    }

    /**
     * Add a PropertyChangeListener for a specific property.
     * The listener will be invoked only when a call on firePropertyChange names that specific property.
     * 
     * @param propertyName the name of the property to listen on.
     * @param listener the PropertyChangeListener to be added.
     */
    public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
        if (propertyName == null || listener == null) {
            return;
        }
        if (propertyListeners == null) {
            propertyListeners = new HashMap();
        }

        PropertyChangeSupport propertyListener = (PropertyChangeSupport)propertyListeners.get(propertyName);
        if (propertyListener == null) {
            propertyListener = new PropertyChangeSupport(source);
            propertyListeners.put(propertyName, propertyListener);
        }
        propertyListener.addPropertyChangeListener(listener);
    }

    /**
     * Remove a PropertyChangeListener for a specific property.
     *
     * @param propertyName the name of the property that was listened on.
     * @param listener the PropertyChangeListener to be removed.
     */
    public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
        if (propertyListeners == null || propertyName == null || listener == null) {
            return;
        }
        final PropertyChangeSupport propertyListener = (PropertyChangeSupport)propertyListeners.get(propertyName);
        if (propertyListener != null) {
            propertyListener.removePropertyChangeListener(listener);
            // Remove key/value if no longer used.
            if (propertyListener.listeners != null && propertyListener.listeners.size() == 0) {
                propertyListeners.remove(propertyName);
            }
        }
    }

    public PropertyChangeListener[] getPropertyChangeListeners(final String propertyName) {
        if (propertyListeners == null || propertyName == null) {
            return new PropertyChangeListener[0];
        }

        final PropertyChangeSupport propertyListener = (PropertyChangeSupport)propertyListeners.get(propertyName);
        if (propertyListener == null) {
            return new PropertyChangeListener[0];
        }
        return propertyListener.getPropertyChangeListeners();
    }

    /**
     * Report a bound property update to any registered listeners.
     * No event is fired if old and new are equal and non-<code>null</code>.
     *
     * @param propertyName the programmatic name of the property that was changed.
     * @param oldValue the old value of the property.
     * @param newValue the new value of the property.
     */
    public void firePropertyChange(final String propertyName, final Object oldValue, final Object newValue) {
        if (oldValue != null && newValue != null && oldValue.equals(newValue)) {
            // don't fire if nothing really changed
            return;
        }
        firePropertyChange(new PropertyChangeEvent(source, propertyName, oldValue, newValue));
    }

    /**
     * Wrap oldValue and newValue in {@link Integer} objects and call {@link #firePropertyChange(String, Object, Object)}.
     */
    public void firePropertyChange(final String propertyName, final int oldValue, final int newValue) {
        if (oldValue != newValue) {
            firePropertyChange(propertyName, new Integer(oldValue), new Integer(newValue));
        }
    }

    /**
     * Wrap oldValue and newValue in {@link Boolean} objects and call {@link #firePropertyChange(String, Object, Object)}.
     */
    public void firePropertyChange(final String propertyName, final boolean oldValue, final boolean newValue) {
        if (oldValue != newValue) {
            firePropertyChange(propertyName, Boolean.valueOf(oldValue), Boolean.valueOf(newValue));
        }
    }

    /**
     * Fire an existing PropertyChangeEvent to any registered listeners.
     * No event is fired if the given event's old and new values are equal and non-<code>null</code>.
     *
     * @param event the PropertyChangeEvent object.
     */
    public void firePropertyChange(final PropertyChangeEvent event) {
        final Object oldValue = event.getOldValue();
        final Object newValue = event.getNewValue();
        if (oldValue != null && newValue != null && oldValue.equals(newValue)) {
            // don't fire if nothing really changed
            return;
        }

        if (listeners != null) {
            final Iterator iter = listeners.iterator();
            while (iter.hasNext()) {
                final PropertyChangeListener listener = (PropertyChangeListener)iter.next();
                listener.propertyChange(event);
            }
        }

        if (propertyListeners != null) {
            final String propertyName = event.getPropertyName();
            if (propertyName != null) {
                final PropertyChangeSupport propertyListener = (PropertyChangeSupport)propertyListeners.get(propertyName);
                if (propertyListener != null) {
                    propertyListener.firePropertyChange(event);
                }
            }
        }
    }

    /**
     * Report a bound indexed property update to any registered listeners.
     * No event is fired if old and new values are equal and non-<code>null</code>.
     *
     * @param propertyName programmatic name of the property that was changed.
     * @param index index of the property element that was changed.
     * @param oldValue old value of the property.
     * @param newValue new value of the property.
     */
    public void fireIndexedPropertyChange(final String propertyName, final int index, final Object oldValue, final Object newValue) {
        if (oldValue != null && newValue != null && oldValue.equals(newValue)) {
            // don't fire if nothing really changed
            return;
        }
        firePropertyChange(new IndexedPropertyChangeEvent(source, propertyName, oldValue, newValue, index));
    }

    /**
     * Wrap oldValue and newValue in {@link Integer} objects and call {@link #fireIndexedPropertyChange(String, int, Object, Object)}.
     */
    public void fireIndexedPropertyChange(final String propertyName, final int index, final int oldValue, final int newValue) {
        if (oldValue != newValue) {
            fireIndexedPropertyChange(propertyName, index, new Integer(oldValue), new Integer(newValue));
        }
    }

    /**
     * Wrap oldValue and newValue in {@link Boolean} objects and call {@link #fireIndexedPropertyChange(String, int, Object, Object)}.
     */
    public void fireIndexedPropertyChange(final String propertyName, final int index, final boolean oldValue, final boolean newValue) {
        if (oldValue != newValue) {
            fireIndexedPropertyChange(propertyName, index, Boolean.valueOf(oldValue), Boolean.valueOf(newValue));
        }
    }

    /**
     * Check if there are any listeners for a specific property, including those registered on all properties.
     * If propertyName is <code>null</code>, only check for listeners registered on all properties.
     *
     * @param propertyName the property name.
     * @return <code>true</code> if there are one or more listeners for the given property.
     */
    public boolean hasListeners(final String propertyName) {
        if (listeners != null && listeners.size() > 0) {
            return true;
        } else if (propertyName != null) {
            return propertyListeners.containsKey(propertyName)
                    && ((PropertyChangeSupport)propertyListeners.get(propertyName)).hasListeners(null);
        } else {
            return false;
        }
    }
}
