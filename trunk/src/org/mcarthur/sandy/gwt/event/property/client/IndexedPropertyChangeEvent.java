package org.mcarthur.sandy.gwt.event.property.client;

/**
 * An "IndexedPropertyChange" event gets delivered whenever a bound indexed property changes.
 *
 * @see java.beans.IndexedPropertyChangeEvent
 */
public class IndexedPropertyChangeEvent extends PropertyChangeEvent {
    private int index;

    /**
     * Constructs a prototypical Event.
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

    public int getIndex() {
        return index;
    }
}
