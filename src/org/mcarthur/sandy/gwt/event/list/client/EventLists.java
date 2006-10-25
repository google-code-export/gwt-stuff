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

import java.util.List;

/**
 * Static methods that operate on or return {@link EventList}s.
 *
 * @author Sandy McArthur
 */
public class EventLists {
    private EventLists() {
    }
    
    /**
     * Wrap a <code>List</code> so it can be monitored for changes. The list to be wrapped must not
     * be modified except by methods of the returned EventList else events will be missed.
     * If <code>list</code> is already an instace of <code>EventList</code> it will not be wrapped
     * again.
     *
     * @param list the list to wrap in an <code>EventList</code>.
     * @return an EventList wrapping list.
     */
    public static EventList wrap(final List list) {
        if (list instanceof EventList) {
            return (EventList)list;
        } else {
            return new WrappedEventList(list);
        }
    }
}
