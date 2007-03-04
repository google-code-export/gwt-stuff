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

package org.mcarthur.sandy.gwt.table.test.client;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.RootPanel;
import org.mcarthur.sandy.gwt.event.list.client.EventList;
import org.mcarthur.sandy.gwt.event.list.client.EventLists;
import org.mcarthur.sandy.gwt.table.client.ObjectListTable;
import org.mcarthur.sandy.gwt.table.client.TableBodyGroup;
import org.mcarthur.sandy.gwt.table.client.TableFooterGroup;
import org.mcarthur.sandy.gwt.table.client.TableHeaderGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for {@link org.mcarthur.sandy.gwt.table.client.ObjectListTable.AttachRenderer}.
 *
 * @author Sandy McArthur
 */
public class ObjectListTableAttachRendererTest extends GWTTestCase {
    public String getModuleName() {
        return "org.mcarthur.sandy.gwt.table.test.TestTable";
    }

    private EventList people = EventLists.eventList();
    private List events = new ArrayList();

    protected void setUp() throws Exception {
        super.setUp();
        events = new ArrayList();
        people.add(new Person("One"));
    }

    protected void tearDown() throws Exception {
        people.clear();
        events.clear(); events = null;
        super.tearDown();
    }

    public void testCallbacksCalledWhenAppropriate() {
        final List expected = new ArrayList();
        final ObjectListTable olt;

        // Initial items are rendered
        olt = new ObjectListTable(new AttachPersonRenderer(events), people);
        expected.add("render");
        assertEquals(expected, events);

        //atcching to browser's document causes the headers/footers to be rendered and rows to be attached
        RootPanel.get().add(olt);
        expected.add("renderHeader");
        expected.add("renderFooter");
        expected.add("onAttachHeader");
        expected.add("onAttachFooter");
        expected.add("onAttach");
        assertEquals(expected, events);

        // adding a element will rendered and attached if the table is attached
        people.add(new Person("Bob"));
        expected.add("render");
        expected.add("onAttach");
        assertEquals(expected, events);

        // removing a person should cause a detch
        people.remove(0);
        expected.add("onDetach");
        


        // detaching the table causes headers, rows and footer to be detached.
        RootPanel.get().remove(olt);
        expected.add("onDetachHeader");
        expected.add("onDetachFooter");
        expected.add("onDetach");
        assertEquals(expected, events);

        // should just render
        people.add(new Person("Joe"));
        expected.add("render");
        assertEquals(expected, events);

        // removing while detached shouldn't affect anything
        people.remove(0);
        assertEquals(expected, events);
    }

    static class AttachPersonRenderer extends ObjectListTableRendererTest.PersonRenderer implements ObjectListTable.AttachRenderer {
        public AttachPersonRenderer(final List events) {
            super(events);
        }

        public void onAttach(final Object obj, final TableBodyGroup rowGroup) {
            events.add("onAttach");
        }

        public void onAttach(final TableHeaderGroup rowGroup) {
            events.add("onAttachHeader");
        }

        public void onAttach(final TableFooterGroup rowGroup) {
            events.add("onAttachFooter");
        }

        public void onDetach(final Object obj, final TableBodyGroup rowGroup) {
            events.add("onDetach");
        }

        public void onDetach(final TableHeaderGroup rowGroup) {
            events.add("onDetachHeader");
        }

        public void onDetach(final TableFooterGroup rowGroup) {
            events.add("onDetachFooter");
        }
    }
}
