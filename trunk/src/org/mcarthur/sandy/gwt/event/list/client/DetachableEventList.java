/*
 * Copyright 2007 Sandy McArthur, Jr.
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

/**
 * An EventList that has a backing EventList that it can be disconnected from.
 * Because JavaScript does not support weak references or a finalize method you need to call
 * {@link #detach()} when you are done with this EventList to free any associated resources. 
 *
 * @author Sandy McArthur
 */
public interface DetachableEventList extends EventList {
    /**
     * Disassociates this EventList with any backing EventList.
     * Calling any methods after calling this method will result in an exception.
     *
     * @return the EventList that was backing this EventList.
     */
    public EventList detach();
}
