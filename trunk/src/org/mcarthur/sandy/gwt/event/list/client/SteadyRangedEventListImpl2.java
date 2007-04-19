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

import com.google.gwt.core.client.GWT;

/**
 * RangedEventList that presents a view of a range of elements in another EventList.
 *
 * @author Sandy McArthur
 */
class SteadyRangedEventListImpl2 extends RangedEventListImpl2 implements RangedEventList {

    protected SteadyRangedEventListImpl2(final EventList delegate, final int maxSize) {
        super(delegate, maxSize);
    }

    protected ListEventListener getListEventListener() {
        // TODO: finish this
        if (false) {
            // doesn't work yet
            return new SteadyRangedListEventListener();
        } else {
            // not the correct behavior but it works
            GWT.log("SteadyRangedEventList currently behaves like RangedEventList until some bugs in the SteadyRangedEventList logic can be fixed.", null);            
            return super.getListEventListener();
        }
    }

    private class SteadyRangedListEventListener extends RangedListEventListener {

        protected void listChangedAdded(final ListEvent listEvent) {
            final int indexStart = listEvent.getIndexStart();
            final int indexEnd = listEvent.getIndexEnd();
            if (indexStart <= getStart()) {
                // the add was before the start offset
                super.listChangedAdded(listEvent);
            } else {
                super.listChangedAdded(listEvent);
            }
        }

        protected void listChangedRemoved(final ListEvent listEvent) {
            final int indexStart = listEvent.getIndexStart();
            final int indexEnd = listEvent.getIndexEnd();
            if (indexEnd <= getStart()) {
                // if the removal didn't affect any visible elements
                final int removedSize = indexEnd - indexStart;
                setStartOffset(getStart() - removedSize);
                fireListEvent(ListEvent.createOther(SteadyRangedEventListImpl2.this));

            } else if (indexStart < getStart()) {
                // TODO: start offset needs adjusting and some visible elements were removed
                // split the listEvent into two events:
                // 1: for the visable range
                // 2: for before the start offset
                super.listChangedRemoved(listEvent);
            } else {
                super.listChangedRemoved(listEvent);
            }
        }
    }
}
