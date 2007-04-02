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

package org.mcarthur.sandy.gwt.event.list.test;

import org.mcarthur.sandy.gwt.event.list.client.EventList;
import org.mcarthur.sandy.gwt.event.list.client.ListEvent;
import org.mcarthur.sandy.gwt.event.list.client.ListEventListener;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * A {@link java.util.List} that listens to events from an
 * {@link org.mcarthur.sandy.gwt.event.list.client.EventList} and recreates their changes internally.
 * This list should always be {@link java.util.List#equals(Object)} to the
 * {@link org.mcarthur.sandy.gwt.event.list.client.EventList} it is replaying events from.
 *
 * @author Sandy McArthur
 */
class EventListReplayList extends AbstractList {
    private final List elements = new ArrayList();
    private final EventList replay;

    public EventListReplayList(final EventList replay) {
        this.replay = replay;
        elements.addAll(replay);

        replay.addListEventListener(new ReplayListEventListener());
    }

    public Object get(final int index) {
        return elements.get(index);
    }

    public int size() {
        return elements.size();
    }

    private class ReplayListEventListener implements ListEventListener {
        public void listChanged(final ListEvent listEvent) {
            if (listEvent.isAdded()) {
                for (int i=listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                    elements.add(i, listEvent.getSourceList().get(i));
                }
            } else if (listEvent.isChanged()) {
                for (int i=listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                    elements.set(i, listEvent.getSourceList().get(i));
                }
            } else if (listEvent.isRemoved()) {
                for (int i=listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                    elements.remove(listEvent.getIndexStart());
                }
            }
        }
    }
}
