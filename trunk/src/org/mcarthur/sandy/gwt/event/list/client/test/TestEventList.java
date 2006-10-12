package org.mcarthur.sandy.gwt.event.list.client.test;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.mcarthur.sandy.gwt.event.list.client.EventList;
import org.mcarthur.sandy.gwt.event.list.client.EventLists;
import org.mcarthur.sandy.gwt.event.list.client.ListEventListener;
import org.mcarthur.sandy.gwt.event.list.client.ListEvent;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

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
