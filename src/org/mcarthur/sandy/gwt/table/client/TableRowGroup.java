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

package org.mcarthur.sandy.gwt.table.client;

import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.DOM;

import java.util.List;
import java.util.ArrayList;

import org.mcarthur.sandy.gwt.event.list.client.EventList;
import org.mcarthur.sandy.gwt.event.list.client.EventLists;
import org.mcarthur.sandy.gwt.event.list.client.ListEventListener;
import org.mcarthur.sandy.gwt.event.list.client.ListEvent;

/**
 * Base class for a HTML Row Group.
 *
 * @author Sandy McArthur
 * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#h-11.2.3">HTML Row Group</a>
 */
abstract class TableRowGroup extends UIObject {
    private final EventList rows = EventLists.wrap(new ArrayList());

    protected TableRowGroup(final Element element) {
        setElement(element);
        rows.addListEventListener(new TableRowGroupListEventListener());
    }

    /**
     * Add another {@link TableRow} to this row group.
     * This has the same effect as <code>getRows().add(row)</code>.
     * @param row the table row to be added.
     */
    public void add(final TableRow row) {
        rows.add(row);
    }

    /**
     * Get the List of {@link TableRow}s.
     * @return a List of {@link TableRow}s.
     */
    public List getRows() {
        return rows;
    }

    public abstract TableRow newTableRow();

    private class TableRowGroupListEventListener implements ListEventListener {
        public void listChanged(final ListEvent listEvent) {
            final Element rowGroupElement = getElement();
            if (listEvent.isAdded()) {
                for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                    final TableRow row = (TableRow)rows.get(i);
                    // XXX: Do we need to call stuff on the TableRow?
                    DOM.appendChild(rowGroupElement, row.getElement());
                }
            } else if (listEvent.isRemoved()) {
                for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                    final Element e = DOM.getChild(rowGroupElement, i);
                    // XXX: Do we need to call stuff on the TableRow?
                    DOM.removeChild(rowGroupElement, e);
                }
            } else if (listEvent.isChanged()) {
                for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                    final TableRow row = (TableRow)rows.get(i);
                    final Element e = DOM.getChild(rowGroupElement, i);
                    if (row.getElement() != e) {
                        // XXX: Do we need to call stuff on the TableRow?
                        DOM.removeChild(rowGroupElement, e);
                        DOM.insertChild(rowGroupElement, row.getElement(), i);
                    }
                }

            } else {
                throw new IllegalStateException("Unexpected ListEvent.type: " + listEvent.getType());
            }
        }
    }
}
