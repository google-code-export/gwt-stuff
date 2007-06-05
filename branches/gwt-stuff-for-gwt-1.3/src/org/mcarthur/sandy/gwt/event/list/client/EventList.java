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
 * An observable {@link List}.
 *
 * EventList implementations do not handle <code>null</code> values very well.
 * Don't expect them to work.
 *
 * @author Sandy McArthur
 */
public interface EventList extends List {
    /**
     * Add another observer to this list.
     *
     * @param listEventListener the observer to add.
     */
    public void addListEventListener(ListEventListener listEventListener);

    /**
     * Remove an observer from this list.
     *
     * @param listEventListener the observer to remove.
     */
    public void removeListEventListener(ListEventListener listEventListener);
}
