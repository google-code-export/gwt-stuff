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

import com.google.gwt.user.client.ui.WidgetCollection;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.DOM;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import org.mcarthur.sandy.gwt.event.list.client.EventList;
import org.mcarthur.sandy.gwt.event.list.client.EventLists;
import org.mcarthur.sandy.gwt.event.list.client.ListEventListener;
import org.mcarthur.sandy.gwt.event.list.client.ListEvent;

/**
 * TODO: Write JavaDoc
 *
 * @author Sandy McArthur
 */
public class ObjectTable extends Panel {

    private TableHeaderGroup thead;
    private TableFooterGroup tfoot;
    private List tbodies = new ArrayList();

    private final WidgetCollection widgets = new WidgetCollection(this);

    private final ObjectTableModel model;
    private final EventList objects;
    private final ListEventListener objectsListener = new ObjectListEventListener();

    public ObjectTable(final ObjectTableModel model) {
        this(model, EventLists.wrap(new ArrayList()));
    }

    public ObjectTable(final ObjectTableModel model, final EventList objects) {
        this.model = model;
        this.objects = objects;
        setElement(DOM.createTable());

        objects.addListEventListener(objectsListener);
    }


    public EventList getObjects() {
        return objects;
    }

    public interface ObjectTableModel {
        public void render(Object obj, TableRowGroup rowGroup);
    }


    /**
     * Gets an iterator for the contained widgets. This iterator is required to
     * implement {@link java.util.Iterator#remove()}.
     */
    public Iterator iterator() {
        return widgets.iterator();
    }

    /**
     * Removes a child widget.
     *
     * @param w the widget to be removed
     * @return <code>true</code> if the widget was present
     */
    public boolean remove(final Widget w) {
        final boolean removed = widgets.contains(w);
        if (removed) {
            widgets.remove(w);
        }
        return removed;
    }

    private void add(ObjectTableRowGroup rowGroup, int beforeIndex) {
        tbodies.add(beforeIndex, rowGroup);
        DOM.insertChild(getElement(), rowGroup.getElement(), beforeIndex);
    }

    private class ObjectListEventListener implements ListEventListener {
        public void listChanged(ListEvent listEvent) {
            if (listEvent.isAdded()) {
                for (int i=listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                    Object obj = objects.get(i);
                    ObjectTableRowGroup rowGroup = new ObjectTableRowGroup(obj);
                    model.render(obj, rowGroup);
                    add(rowGroup, i);
                }
            } else if (listEvent.isRemoved()) {
                for (int i=listEvent.getIndexEnd()-1; i >= listEvent.getIndexStart(); i--) {
                    ObjectTableRowGroup rowGroup = (ObjectTableRowGroup)tbodies.get(i);
                    // TODO disown
                }

            } else if (listEvent.isChanged()) {
                // TODO test if different
                // TODO remove old
                // TODO insert new
            }
        }
    }

    private class ObjectTableRowGroup extends TableBodyGroup {
        private final Object obj;

        public ObjectTableRowGroup(final Object obj) {
            this.obj = obj;
        }

        public TableRow newTableRow() {
            return new ObjectTableRow();
        }


        public void add(final TableRow row) {
            if (row instanceof ObjectTableRow) {
                super.add(row);
            } else {
                throw new IllegalArgumentException("TableRow instance must be acquired from newTableRow()");
            }
        }
    }

    private class ObjectTableRow extends TableRow {

        protected void adopt(final Widget w, final Element container) {
            ObjectTable.this.adopt(w, container);
        }

        public TableDataCell newTableDataCell() {
            return new ObjectTableDataCell();
        }

        public TableHeaderCell newTableHeaderCell() {
            return new ObjectTableHeaderCell();
        }
    }

    private class ObjectTableDataCell extends TableDataCell {
    }

    private class ObjectTableHeaderCell extends TableHeaderCell {
    }
}
