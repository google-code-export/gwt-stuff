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

/**
 * An EventList that provides a view of range of another EventList.
 *
 * <p>
 * When the backing EventList changes in a way that doesn't affect the list of elements
 * a {@link org.mcarthur.sandy.gwt.event.list.client.ListEvent} of type
 * {@link org.mcarthur.sandy.gwt.event.list.client.ListEvent#OTHER} is fired.
 * </p>
 *
 * @author Sandy McArthur
 */
public interface RangedEventList extends EventList {

    /**
     * Get the index of the backing list element that is the first element in this list.
     * This value may change as the underlying list changes.
     *
     * @return the index of the backing list element that is the first element in this list.
     */
    public int getStart();

    /**
     * Set the index of the backing list element that is the first element in this list.
     *
     * @param start the index of the backing list element that is the first element in this list.
     * @throws IllegalArgumentException when start is negative.
     * @throws UnsupportedOperationException if the start offset cannot be changed.
     */
    public void setStart(int start) throws IllegalArgumentException, UnsupportedOperationException;

    /**
     * Get the max size of this list.
     * This is essentially the number of elements in a page.
     *
     * @return the max size of this list.
     * @see #size()
     */
    public int getMaxSize();

    /**
     * Set the max size of this list.
     * This is essentially the number of elements in a page.
     *
     * @param maxSize the max size of this list.
     * @throws IllegalArgumentException when maxSize is negative.
     * @throws UnsupportedOperationException if the maxSize cannot be changed.
     */
    public void setMaxSize(int maxSize) throws IllegalArgumentException, UnsupportedOperationException;

    /**
     * Returns the number of elements in this list.
     * Will be less than or equal to {@link #getMaxSize()}.
     *
     * @return the number of elements in this list.
     * @see #getMaxSize()
     */
    int size();

    /**
     * Get the size of the backing list.
     *
     * @return the size of the backing list.
     */
    public int getTotal();
}
