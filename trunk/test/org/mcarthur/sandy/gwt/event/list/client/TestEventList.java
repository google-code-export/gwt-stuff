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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.ArrayList;
import java.util.List;

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

        log(el);
        el.add("one");
        log(el);
        el.add("two");
        log(el);
        el.add("three");
        log(el);

        final List s = new ArrayList();
        s.add("four");
        s.add("five");
        s.add("six");
        s.add("seven");
        s.add("eight");
        s.add("nine");
        el.addAll(s);
        log(el);

        el.remove("two");
        log(el);
        el.remove("four");
        log(el);

        s.remove("four");
        s.remove("six");
        s.remove("nine");

        el.removeAll(s);
        log(el);

        s.clear();
        //s.add("three");
        s.add("six");
        s.add("nine");
        el.retainAll(s);
        log(el);

        RootPanel.get("out").add(new Label(el.toString()));
    }

    public void log(final ListEvent listEvent) {
        vp.add(new Label(listEvent.toString()));
    }

    public void log(final List list) {
        vp.add(new Label(list.toString()));
    }
}
