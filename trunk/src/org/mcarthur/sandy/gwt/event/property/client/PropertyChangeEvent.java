package org.mcarthur.sandy.gwt.event.property.client;

import java.util.EventObject;

/**
 * A "PropertyChange" event gets delivered whenever a "bound" or "constrained" property changes.
 *
 * @see java.beans.PropertyChangeEvent
 */
public class PropertyChangeEvent extends EventObject {
    private String propertyName;
    private Object oldValue;
    private Object newValue;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which fired the event.
     * @param propertyName The programmatic name of the property that was changed, or null of the whole object changed.
     * @param oldValue The old value of the property.
     * @param newValue The new value of the property.
     * @throws IllegalArgumentException if source is null.
     */
    public PropertyChangeEvent(final Object source, final String propertyName, final Object oldValue, final Object newValue) {
        super(source);
        this.propertyName = propertyName;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }


    public String getPropertyName() {
        return propertyName;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }
}
