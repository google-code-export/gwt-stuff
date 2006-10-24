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

package org.mcarthur.sandy.gwt.event.list.client.test;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.mcarthur.sandy.gwt.event.list.client.EventList;
import org.mcarthur.sandy.gwt.event.list.client.EventLists;
import org.mcarthur.sandy.gwt.event.list.client.ListEvent;
import org.mcarthur.sandy.gwt.event.list.client.ListEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * TODO: Write Class JavaDoc
 *
 * @author Sandy McArthur
 */
public class TestEventList implements EntryPoint {
    VerticalPanel vp = new VerticalPanel();

    public void onModuleLoad() {
        RootPanel.get("events").add(vp);

        final List l = new ArrayList();
        final EventList el = EventLists.wrap(l);
        el.addListEventListener(new ListEventListener() {
            public void listChanged(final ListEvent listEvent) {
                log(listEvent);
            }
        });

        el.add("one");
        el.add("two");
        el.add("three");

        final Set s = new HashSet(4);
        s.add("four");
        s.add("five");
        s.add("six");
        s.add("seven");
        el.addAll(s);

        el.remove("two");
        el.remove("four");

        s.remove("four");
        s.remove("six");

        el.removeAll(s);

        RootPanel.get("out").add(new Label(el.toString()));
    }

    public void log(final ListEvent listEvent) {
        vp.add(new Label(listEvent.toString()));
    }
}
