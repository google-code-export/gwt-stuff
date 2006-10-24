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

    public PropertyChangeSupport(final Object source) {
        this.source = source;
    }

    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        if (listener == null) {
            return;
        }
        if (listeners == null) {
            listeners = new ArrayList();
        }
        listeners.add(listener);
    }
    
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
    
    public void firePropertyChange(final String propertyName, final Object oldValue, final Object newValue) {
        if (oldValue == newValue && oldValue != null && oldValue.equals(newValue)) {
            // don't fire if nothing really changed
            return;
        }
        firePropertyChange(new PropertyChangeEvent(source, propertyName, oldValue, newValue));
    }

    public void firePropertyChange(final String propertyName, final int oldValue, final int newValue) {
        if (oldValue != newValue) {
            firePropertyChange(propertyName, new Integer(oldValue), new Integer(newValue));
        }
    }

    public void firePropertyChange(final String propertyName, final boolean oldValue, final boolean newValue) {
        if (oldValue != newValue) {
            firePropertyChange(propertyName, Boolean.valueOf(oldValue), Boolean.valueOf(newValue));
        }
    }

    public void firePropertyChange(final PropertyChangeEvent event) {
        final Object oldValue = event.getOldValue();
        final Object newValue = event.getNewValue();
        if (oldValue == newValue && oldValue != null && oldValue.equals(newValue)) {
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

    public void fireIndexedPropertyChange(final String propertyName, final int index, final Object oldValue, final Object newValue) {
        if (oldValue == newValue && oldValue != null && oldValue.equals(newValue)) {
            // don't fire if nothing really changed
            return;
        }
        firePropertyChange(new IndexedPropertyChangeEvent(source, propertyName, oldValue, newValue, index));
    }

    public void fireIndexedPropertyChange(final String propertyName, final int index, final int oldValue, final int newValue) {
        if (oldValue != newValue) {
            fireIndexedPropertyChange(propertyName, index, new Integer(oldValue), new Integer(newValue));
        }
    }

    public void fireIndexedPropertyChange(final String propertyName, final int index, final boolean oldValue, final boolean newValue) {
        if (oldValue != newValue) {
            fireIndexedPropertyChange(propertyName, index, Boolean.valueOf(oldValue), Boolean.valueOf(newValue));
        }
    }

    public boolean hasListeners(final String propertyName) {
        if (propertyName == null) {
            return listeners != null && listeners.size() > 0;
        } else {
            return propertyListeners.containsKey(propertyName) && ((PropertyChangeSupport)propertyListeners.get(propertyName)).hasListeners(null);
        }
    }
}
