package org.mcarthur.sandy.gwt.event.property.client;

/**
 * A source for {@link PropertyChangeEvent}s.
 * There isn't a parallel in the java.beans package.
 *
 * @author Sandy McArthur
 */
public interface PropertyChangeSource {
    /**
     * @see PropertyChangeSupport#addPropertyChangeListener(PropertyChangeListener)
     */
    public void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * @see PropertyChangeSupport#removePropertyChangeListener(PropertyChangeListener)
     */
    public void removePropertyChangeListener(PropertyChangeListener listener);
}
