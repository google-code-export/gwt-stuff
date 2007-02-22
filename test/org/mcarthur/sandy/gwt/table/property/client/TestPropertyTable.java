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

package org.mcarthur.sandy.gwt.table.property.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import org.mcarthur.sandy.gwt.event.list.client.EventList;
import org.mcarthur.sandy.gwt.event.list.client.EventLists;
import org.mcarthur.sandy.gwt.table.client.alpha.PropertyDescriptor;
import org.mcarthur.sandy.gwt.table.client.alpha.PropertyTable;
import org.mcarthur.sandy.gwt.table.client.alpha.PropertyTableModel;
import org.mcarthur.sandy.gwt.table.client.alpha.SortablePropertyDescriptor;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * TODO: Write Javadoc
 *
 * @author Sandy McArthur
 */
public class TestPropertyTable implements EntryPoint {
    private PropertyTable pt;

    private EventList el = EventLists.eventList();

    public void onModuleLoad() {

        for (int i=0; i < 100; i++) {
            el.add(new Contact());
        }

        pt = new PropertyTable(new PTM());
        RootPanel.get().add(pt);
    }

    private class PTM implements PropertyTableModel {

        public EventList getElements() {
            return el;
        }

        public Collection/*<PropertyDescriptor>*/ getPropertyDescriptors() {
            final Set pd = new HashSet();
            pd.add(new FirstPD());
            pd.add(new LastPD());
            return pd;
        }

        private class FirstPD implements SortablePropertyDescriptor {

            public String getName() {
                return "first";
            }

            public String getDisplayName() {
                return "First Name";
            }

            public String getShortDescription() {
                return "Contact's first name.";
            }

            public Widget createWidget(final Object element) {
                final Contact c = (Contact)element;
                return new Label(c.getFirst());
            }

            public Comparator getComparator() {
                return new Comparator() {
                    public int compare(final Object o1, final Object o2) {
                        final Contact c1 = (Contact)o1;
                        final Contact c2 = (Contact)o2;
                        return c1.getFirst().compareTo(c2.getFirst());
                    }
                };
            }
        }

        private class LastPD implements PropertyDescriptor {

            public String getName() {
                return "last";
            }

            public String getDisplayName() {
                return "Last Name";
            }

            public String getShortDescription() {
                return "Contact's last name.";
            }

            public Widget createWidget(final Object element) {
                final Contact c = (Contact)element;
                return new Label(c.getLast());
            }
        }

        public String getRowGroupSpec() {
            return "first last";
        }
    }


    private static List firstNames;
    private static List lastNames;
    static {
        String[] f = {"Bob", "Joe", "Tom", "Jane", "Jill", "Sally", "Laura", "Sandy", "Alex", "Mary", "Mark", "Zack", " Beth", "Greg"};
        firstNames = Arrays.asList(f);
        //Collections.shuffle(firstNames);

        String[] l = {"Hacker", "Neal", "Nott", "Palmer", "Pell", "Eddy", "Eton", "Taylor", "Smith", "Dawson", "Lee", "Hung"};
        lastNames = Arrays.asList(l);
        //Collections.shuffle(lastNames);
    }

    static class Contact implements Comparable {

        private static String nextFirst() {
            return (String)firstNames.get((int)(Math.random() * firstNames.size()));
        }

        private static String nextLast() {
            return (String)lastNames.get((int)(Math.random() * lastNames.size()));
        }

        private String first = nextFirst();
        private String last = nextLast();
        private int age = (int)(Math.random() * 80 + 18);
        //private String email = first + "." + last + "@example.com";

        public String getFirst() {
            return first;
        }

        public String getLast() {
            return last;
        }

        public int getAge() {
            return age;
        }

        public String getEmail() {
            return first + "." + last + "@example.com";
        }

        public int compareTo(final Object o) {
            final Contact c = (Contact)o;
            int i = last.compareTo(c.last);
            if (i == 0) {
                i = first.compareTo(c.first);
            }
            return i;
        }
    }
}
