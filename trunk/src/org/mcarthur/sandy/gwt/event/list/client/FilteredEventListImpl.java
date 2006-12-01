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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * TODO: Write Javadoc
 *
 * @author Sandy McArthur
 */
class FilteredEventListImpl extends AbstractEventList implements FilteredEventList {
    private final EventList delegate;
    private Filter filter;

    private static final Filter EVERYTHING = new Filter() {
        public boolean accept(final Object element) {
            return true;
        }
    };

    public FilteredEventListImpl(final EventList delegate) {
        this(delegate, null);
    }

    public FilteredEventListImpl(final EventList delegate, final Filter filter) {
        this.delegate = delegate;
        setFilter(filter);
    }

    public Filter getFilter() {
        return filter != EVERYTHING ? filter : null;
    }

    public void setFilter(Filter filter) {
        filter = filter != null ? filter : EVERYTHING;
        if (this.filter != filter) {
            this.filter = filter;
            filter();
        }
    }

    public void filter() {
        // TODO: implement filter
    }

    public boolean add(final Object o) {
        delegate.add(o);
        return filter.accept(o);
    }

    public Object get(final int index) {
        final Iterator iter = iterator();
        int i = -1;
        while (iter.hasNext()) {
            final Object o = iter.next();
            i++;
            if (index == i) {
                return o;
            }
        }
        return null;
    }

    public int size() {
        int i = 0;
        final Iterator iter = iterator();
        while (iter.hasNext()) {
            iter.next();
            i++;
        }
        return i;
    }

    public Iterator iterator() {
        return new Iterator() {
            int cursor = -1;
            int lastRet = -1;

            public boolean hasNext() {
                for (int i = cursor+1; i < delegate.size(); i++) {
                    if (filter.accept(delegate.get(i))) {
                        return true;
                    }
                }
                return false;
            }

            public Object next() {
                if (cursor == -1) {
                    advance();
                }
                if (cursor < delegate.size()) {
                    final Object next = delegate.get(cursor);
                    lastRet = cursor;
                    advance();
                    return next;
                } else {
                    throw new NoSuchElementException();
                }
            }

            private void advance() {
                for (cursor+=1; cursor < delegate.size(); cursor++) {
                    if (filter.accept(delegate.get(cursor))) {
                        return;
                    }
                }
            }

            public void remove() {
                if (lastRet == -1) {
                    throw new IllegalStateException();
                }
                delegate.remove(lastRet);
                if (lastRet < cursor) {
                    cursor--;
                }
                lastRet = -1;
            }
        };
    }
}
