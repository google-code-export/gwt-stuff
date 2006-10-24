package org.mcarthur.sandy.gwt.event.property.client;

/**
 * A "PropertyChange" event gets fired whenever a bean changes a "bound" property.
 * You can register a PropertyChangeListener with a source bean so as to be notified
 * of any bound property updates.
 *
 * @see java.beans.PropertyChangeListener
 */
public interface PropertyChangeListener {
    public void propertyChange(PropertyChangeEvent event);
}
