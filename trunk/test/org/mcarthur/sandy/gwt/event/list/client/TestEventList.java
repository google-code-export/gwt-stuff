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
import java.util.Comparator;
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

        EventList el = EventLists.eventList();
        //el = EventLists.sortedEventList(EventLists.eventList());
        el = EventLists.sortedEventList(el);
        //el = EventLists.filteredEventList(el);
        el.addListEventListener(new ListEventListener() {
            public void listChanged(final ListEvent listEvent) {
                log(listEvent);
            }
        });

        //log(el);
        el.add(new Person("Sandy", 21));
        //log(el);
        el.add(0, new Person("Keebz", 25));
        //log(el);
        el.add(new Person("Bill", 33));
        //log(el);
        el.add(new Person("Ted", 55));
        log(el);
        //el.remove(1);
        //log(el);
        log("Name");
        ((SortedEventList)el).setComparator(NAME);
        log(el);
        log("Age");
        ((SortedEventList)el).setComparator(AGE);
        log(el);
        log("Name");
        ((SortedEventList)el).setComparator(NAME);
        log(el);
        log("Age");
        ((SortedEventList)el).setComparator(AGE);
        log(el);

        if (false) {
            el.add("1");
            log(el);
            el.add(0, "2");
            log(el);
            //log("one.compareTo(two): " + "one".compareTo("two"));
            el.add("3");
            log(el);
            el.add("4");
            log(el);
            el.remove(1);
            log(el);

            ((SortedEventList)el).sort();
            log(el);

            final List s = new ArrayList();
            s.add("5");
            s.add("6");
            s.add("7");
            s.add("8");
            s.add("9");
            el.addAll(s);
            log(el);

            el.remove("1");
            log(el);
            el.remove("4");
            log(el);

            s.remove("4");
            s.remove("6");
            s.remove("9");

            el.removeAll(s);
            log(el);

            s.clear();
            //s.add("3");
            s.add("6");
            s.add("9");
            el.retainAll(s);
            log(el);
        }
        RootPanel.get("out").add(new Label(el.toString()));
    }

    public void log(final String str) {
        vp.add(new Label(str));
    }

    public void log(final ListEvent listEvent) {
        vp.add(new Label(listEvent.toString()));
    }

    public void log(final List list) {
        vp.add(new Label(list.toString()));
    }

    private static final Comparator NAME = new Comparator() {
        public int compare(final Object o1, final Object o2) {
            final Person p1 = (Person)o1;
            final Person p2 = (Person)o2;
            return p1.getName().compareTo(p2.getName());
        }
    };
    private static final Comparator AGE = new Comparator() {
        public int compare(final Object o1, final Object o2) {
            final Person p1 = (Person)o1;
            final Person p2 = (Person)o2;
            return p1.getAge() - p2.getAge();
        }
    };

    private static class Person implements Comparable {
        private String name;
        private final int age;

        public Person(final String name, final int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public int compareTo(final Object o) {
            final Person person = (Person)o;
            final int ageDiff = person.age - age;
            return ageDiff != 0 ? ageDiff : name.compareTo(person.name);
        }

        public String toString() {
            return name + "{" + age + '}';
        }
    }
}
