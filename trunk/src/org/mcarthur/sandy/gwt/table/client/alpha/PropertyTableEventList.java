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

package org.mcarthur.sandy.gwt.table.client.alpha;

import org.mcarthur.sandy.gwt.event.list.client.AbstractEventList;
import org.mcarthur.sandy.gwt.event.list.client.EventList;
import org.mcarthur.sandy.gwt.event.list.client.FilteredEventList;
import org.mcarthur.sandy.gwt.event.list.client.RangedEventList;
import org.mcarthur.sandy.gwt.event.list.client.SortedEventList;

import java.util.Collection;

/**
 * An EventList that encapuslates a stack of EventLists and allows that stack to be altered
 * as needed.
 *
 * @author Sandy McArthur
 */
class PropertyTableEventList extends AbstractEventList {

    private EventList delegate = null;

    private EventList el = null;
    private SortedEventList sel = null;
    private FilteredEventList fel = null;
    private RangedEventList rel = null;

    public PropertyTableEventList() {
        throw new RuntimeException("Impl not yet finished.");
    }

    public Object get(final int index) {
        checkSize(index);
        return delegate.get(index);
    }

    public boolean add(final Object o) {
        //return delegate == null ? false : delegate.add(o);
        return delegate != null && delegate.add(o);
    }

    public void add(final int index, final Object element) {
        checkSize(index);
        delegate.add(index, element);
    }

    public boolean addAll(final int index, final Collection c) {
        checkSize(index);
        return delegate.addAll(index, c);
    }

    public void clear() {
        if (delegate != null) {
            delegate.clear();
        }
    }

    public int indexOf(final Object o) {
        return delegate == null ? -1 : delegate.indexOf(o);
    }


    public int lastIndexOf(final Object o) {
        return delegate == null ? -1 : delegate.lastIndexOf(o);
    }

    public int size() {
        return delegate == null ? 0 : delegate.size();
    }

    private void checkSize(final int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
        }
    }
}
