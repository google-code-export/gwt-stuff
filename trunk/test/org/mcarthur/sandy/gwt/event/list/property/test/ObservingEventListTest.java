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

package org.mcarthur.sandy.gwt.event.list.property.test;

import junit.framework.TestCase;
import org.mcarthur.sandy.gwt.event.list.client.EventList;
import org.mcarthur.sandy.gwt.event.list.client.ListEvent;
import org.mcarthur.sandy.gwt.event.list.client.ListEventListener;
import org.mcarthur.sandy.gwt.event.list.property.client.ObservingEventList;
import org.mcarthur.sandy.gwt.event.property.client.PropertyChangeSource;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests for {@link org.mcarthur.sandy.gwt.event.list.property.client.ObservingEventList}.
 *
 * @author Sandy McArthur
 */
public class ObservingEventListTest extends TestCase {
    protected EventList createEmptyEventLists() {
        return new ObservingEventList();
    }

    public void testAdd() {
        final EventList el = createEmptyEventLists();
        prefillWithMutableNumbers(el, 10);

        final ListEventListener lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(ListEvent.createChanged(el, 0), listEvent);
                        break;
                    case 1:
                        assertNull(listEvent);
                        break;
                    case 2:
                        assertEquals(ListEvent.createChanged(el, 4), listEvent);
                        break;
                    case 3:
                        assertNull(listEvent);
                        break;
                    case 4:
                        assertEquals(ListEvent.createChanged(el, 9), listEvent);
                        break;
                    case 5:
                        assertNull(listEvent);
                        break;
                    default:
                        fail("Unexpected: " + listEvent);
                }
            }
        };
        el.addListEventListener(lel);

        MutableNumber mn = (MutableNumber)el.get(0);
        mn.setValue(mn.getValue() + 1000);
        lel.listChanged(null);

        mn = (MutableNumber)el.get(4);
        mn.setValue(mn.getValue() + 1000);
        lel.listChanged(null);

        mn = (MutableNumber)el.get(9);
        mn.setValue(mn.getValue() + 1000);
        lel.listChanged(null);
    }

    public void testAddInt() {
        final EventList el = createEmptyEventLists();
        prefillWithMutableNumbers(el, 10);

        final ListEventListener lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(ListEvent.createAdded(el, 5), listEvent);
                        break;
                    case 1:
                        assertNull(listEvent);
                        break;
                    case 2:
                        assertEquals(ListEvent.createChanged(el, 5), listEvent);
                        break;
                    case 3:
                        assertNull(listEvent);
                        break;
                    default:
                        fail("Unexpected: " + listEvent);
                }
            }
        };
        el.addListEventListener(lel);

        final MutableNumber mn = new MutableNumber(-1);
        el.add(5, mn);
        lel.listChanged(null);

        mn.setValue(mn.getValue() + 1000);
        lel.listChanged(null);
    }

    public void testAddAll() {
        final List l = new ArrayList();
        prefillWithMutableNumbers(l, 10);

        final EventList el = createEmptyEventLists();
        el.addAll(l);

        final ListEventListener lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(ListEvent.createChanged(el, 0), listEvent);
                        break;
                    case 1:
                        assertNull(listEvent);
                        break;
                    case 2:
                        assertEquals(ListEvent.createChanged(el, 4), listEvent);
                        break;
                    case 3:
                        assertNull(listEvent);
                        break;
                    case 4:
                        assertEquals(ListEvent.createChanged(el, 9), listEvent);
                        break;
                    case 5:
                        assertNull(listEvent);
                        break;
                    default:
                        fail("Unexpected: " + listEvent);
                }
            }
        };
        el.addListEventListener(lel);

        MutableNumber mn = (MutableNumber)el.get(0);
        mn.setValue(mn.getValue() + 1000);
        lel.listChanged(null);

        mn = (MutableNumber)el.get(4);
        mn.setValue(mn.getValue() + 1000);
        lel.listChanged(null);

        mn = (MutableNumber)el.get(9);
        mn.setValue(mn.getValue() + 1000);
        lel.listChanged(null);
    }

    public void testRemove() {
        final EventList el = createEmptyEventLists();
        prefillWithMutableNumbers(el, 10);

        MutableNumber mn = (MutableNumber)el.get(0);

        assertEquals(1, mn.pcs.getPropertyChangeListeners().length);

        el.remove(mn);
        assertEquals(0, mn.pcs.getPropertyChangeListeners().length);


        mn = (MutableNumber)el.remove(0);
        assertEquals(0, mn.pcs.getPropertyChangeListeners().length);
    }

    public void testSet() {
        final EventList el = createEmptyEventLists();
        prefillWithMutableNumbers(el, 10);

        final MutableNumber mn = new MutableNumber(1234);

        assertEquals(0, mn.pcs.getPropertyChangeListeners().length);

        final MutableNumber mn2 = (MutableNumber)el.set(4, mn);
        assertEquals(1, mn.pcs.getPropertyChangeListeners().length);
        assertEquals(0, mn2.pcs.getPropertyChangeListeners().length);
    }

    private void prefillWithMutableNumbers(final List l, final int count) {
        for (int i=0; i < count; i++) {
            l.add(new MutableNumber(i));
        }
    }

    public static class MutableNumber extends Number implements Comparable, PropertyChangeSource {
        public final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

        private volatile int value;

        public MutableNumber(int value) {
            setValue(value);
        }

        public int getValue() {
            return value;
        }

        public void setValue(final int value) {
            final int old = getValue();
            this.value = value;
            pcs.firePropertyChange("value", old, value);
        }

        public int intValue() {
            return value;
        }

        public long longValue() {
            return value;
        }

        public float floatValue() {
            return value;
        }

        public double doubleValue() {
            return value;
        }

        public int compareTo(final Object o) {
            final Number n = (Number)o;
            return intValue() - n.intValue();
        }

        public void addPropertyChangeListener(final PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(listener);
        }

        public void removePropertyChangeListener(final PropertyChangeListener listener) {
            pcs.removePropertyChangeListener(listener);
        }
    }
}
