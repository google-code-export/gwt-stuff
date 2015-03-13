# Introduction #

This module provides interfaces that mark objects as sources for
java.beans.PropertyChangeEvent. This is needed because GWT does not support reflection
and there is no standard way to indicate that an object is the source of property change
events.

# Dependencies #

Since GWT does not yet support the java.beans.PropertyChangeSupport and related classes
you will need to include the [GWTx](http://code.google.com/p/gwtx/) module in your project
also to enable you to use these classes in your GWT Project.

# Inherits #



&lt;inherits name="org.mcarthur.sandy.gwt.event.property.PropertyChange"/&gt;

 :
> This inherits the Property Change module for use in your project.