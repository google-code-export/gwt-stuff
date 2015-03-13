# Introduction #

Those familiar with [Glazed Lists](http://publicobject.com/glazedlists/) will be familiar
with the ideas behind GWT-Stuff's EventList module. EventList provides a
[java.util.List](http://java.sun.com/j2se/1.5.0/docs/api/java/util/List.html)
implementation that fires events when the list is modified. This is very useful for
keeping your model and view separate and allowing the view to automatically update when
the model changes.

## Property Change Support ##

If your objects are mutable it may be desirable for you objects to notify the EventList when their state changes so that a SortedEventList, a FilteredEventList, or a RangedEventList can update themselves. The PropertyChangeEventList module combines the EventList module and PropertyChange module from GWT-Stuff to provide an EventList implementation that monitors the elements in itself.

# Inherits #



&lt;inherits name="org.mcarthur.sandy.gwt.event.list.EventList"/&gt;

 :
> This inherits the EventList module for use in your project.



&lt;inherits name="org.mcarthur.sandy.gwt.event.list.property.PropertyChangeEventList"/&gt;

 :
> This inherts the PropertyChangeEventList for use in your project.
> This module depends on GwtStuffPropertyChange.