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
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A skeletal implementation of the EventList interface.
 *
 * @author Sandy McArthur
 * @see java.util.AbstractList
 */
public abstract class AbstractEventList extends AbstractList implements EventList {
    private final List listeners = new ArrayList();

    public void addListEventListener(final ListEventListener listEventListener) {
        listeners.add(listEventListener);
    }

    public void removeListEventListener(final ListEventListener listEventListener) {
        listeners.remove(listEventListener);
    }

    /**
     * Signals each listener of an event.
     *
     * @param listEvent the event to signal.
     */
    protected void fireListEvent(final ListEvent listEvent) {
        final Iterator iter = listeners.iterator();
        while (iter.hasNext()) {
            final ListEventListener listEventListener = (ListEventListener)iter.next();
            listEventListener.listChanged(listEvent);
        }
    }
}
