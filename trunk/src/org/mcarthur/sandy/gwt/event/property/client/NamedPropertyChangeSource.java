package org.mcarthur.sandy.gwt.event.property.client;

/**
 * A source for {@link PropertyChangeEvent}s
 * that supports listeners only interested in specific properties.
 * There isn't a parallel in the java.beans package.
 *
 * @author Sandy McArthur
 */
public interface NamedPropertyChangeSource extends PropertyChangeSource {
    /**
     * @see PropertyChangeSupport#addPropertyChangeListener(String,PropertyChangeListener)
     */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

    /**
     * @see PropertyChangeSupport#removePropertyChangeListener(String,PropertyChangeListener)
     */
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);
}
