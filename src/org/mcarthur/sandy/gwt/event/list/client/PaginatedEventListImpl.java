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

/**
 * TODO: Write Javadoc
 *
 * @author Sandy McArthur
 */
class PaginatedEventListImpl extends TransformedEventList implements PaginatedEventList {
    private int start;
    private int maxSize;
    private List translationList = new TranslationList();

    protected PaginatedEventListImpl(final EventList delegate) {
        this(delegate, Integer.MAX_VALUE);
    }

    protected PaginatedEventListImpl(final EventList delegate, final int maxSize) {
        super(delegate);
        getDelegate().addListEventListener(new PaginatedListEventListener());
        setStart(0);
        setMaxSize(maxSize);
    }

    private class PaginatedListEventListener implements ListEventListener {
        public void listChanged(final ListEvent listEvent) {
            // does the event affect our range?
            if (start < listEvent.getIndexEnd() && listEvent.getIndexStart() < start + maxSize) {
                // find translated start
                int tStart = Math.max(0, listEvent.getIndexStart() - start);
                // find translated end
                int tEnd = Math.min(maxSize, listEvent.getIndexEnd() - start);
                // fire new event
                fireListEvent(new ListEvent(PaginatedEventListImpl.this, listEvent.getType(), tStart, tEnd));
            }
        }
    }

    protected List getTranslations() {
        return translationList;
    }

    // TODO: event handling
    // TODO: paginate code
    
    public int getStart() {
        return start;
    }

    public void setStart(final int start) {
        // TODO: fire event for list change.
        this.start = start;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(final int maxSize) {
        // TODO: fire event for list change.
        this.maxSize = maxSize;
    }

    public int getTotal() {
        return getDelegate().size();
    }

    /**
     * A List that creates
     * {@link org.mcarthur.sandy.gwt.event.list.client.TransformedEventList.Index}s as needed based
     * on {@link PaginatedEventListImpl#start} and {@link PaginatedEventListImpl#maxSize}.
     */
    private class TranslationList extends AbstractList {
        public Object get(final int index) {
            // FIXME: bug here.
            if (index >= size()) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
            }
            return new Index(start + index);
        }

        public int size() {
            return Math.min(maxSize, getDelegate().size() - start);
        }
    }
}
