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

package org.mcarthur.sandy.gwt.event.list.client;

import java.util.AbstractList;

/**
 * A skeletal implementation of the EventList interface.
 *
 * @author Sandy McArthur
 * @see java.util.AbstractList
 */
public abstract class AbstractEventList extends AbstractList implements EventList {
    // This is an array instead of a List because these aren't volitile and an array carries less overhead.
    private ListEventListener[] listeners = new ListEventListener[0];

    public void addListEventListener(final ListEventListener listEventListener) {
        final ListEventListener[] resizedListeners = new ListEventListener[listeners.length + 1];
        for (int i=0; i < listeners.length; i++) {
            resizedListeners[i] = listeners[i];
        }
        resizedListeners[listeners.length] = listEventListener;
        listeners = resizedListeners;
    }

    public void removeListEventListener(final ListEventListener listEventListener) {
        int indexOfListener = -1;
        for (int i=0; i < listeners.length; i++) {
            if (listeners[i] == listEventListener) {
                indexOfListener = i;
                break;
            }
        }
        if (indexOfListener >= 0) {
            // This needs to be a new list instance because of the way fireListEvent works
            final ListEventListener[] resizedListeners = new ListEventListener[listeners.length - 1];
            for (int i=0; i < indexOfListener; i++) {
                resizedListeners[i] = listeners[i];
            }
            for (int i=indexOfListener+1; i < listeners.length; i++) {
                resizedListeners[i-1] = listeners[i];
            }
            listeners = resizedListeners;
        }
    }

    /**
     * Signals each listener of an event.
     *
     * @param listEvent the event to signal.
     */
    protected void fireListEvent(final ListEvent listEvent) {
        final ListEventListener[] listeners = this.listeners; // capture the current instance
        for (int i=0; i< listeners.length; i++) {
            listeners[i].listChanged(listEvent);
        }
    }

    /**
     * Same as {@link java.util.AbstractList#removeRange(int, int)}.
     * This is only here because GWT's emulation of AbstractList.removeRange(int,int) is
     * broken as of GWT 1.3.3. When GWT has a stable release with a fixed removeRange this
     * method will disapper and use the inherited version instead.
     */
    protected void removeRange(final int start, final int end) {
        // TODO: Remove this method when GWT has a correct AbstractList.removeRange method.
        for (int i = start; i < end; i++) {
            remove(start);
        }
    }
}
