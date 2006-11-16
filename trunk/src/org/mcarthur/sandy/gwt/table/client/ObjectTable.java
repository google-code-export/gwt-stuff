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

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetCollection;
import org.mcarthur.sandy.gwt.event.list.client.EventList;
import org.mcarthur.sandy.gwt.event.list.client.EventLists;
import org.mcarthur.sandy.gwt.event.list.client.ListEvent;
import org.mcarthur.sandy.gwt.event.list.client.ListEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * TODO: Write JavaDoc
 *
 * @author Sandy McArthur
 */
public class ObjectTable extends Panel implements SourcesMouseEvents {

    private TableHeaderGroup thead;
    private TableFooterGroup tfoot;
    private List tbodies = new ArrayList();

    private final WidgetCollection widgets = new WidgetCollection(this);

    private final TableModel model;
    private final EventList objects;
    private final ListEventListener objectsListener = new ObjectListEventListener();

    private MouseListenerCollection mouseListeners = null;

    public ObjectTable(final TableModel model) {
        this(model, EventLists.wrap(new ArrayList()));
    }

    public ObjectTable(final TableModel model, final EventList objects) {
        this.model = model;
        this.objects = objects;
        setElement(DOM.createTable());

        objects.addListEventListener(objectsListener);
    }

    public EventList getObjects() {
        return objects;
    }

    public interface TableModel {
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

    private void add(final ObjectTableRowGroup rowGroup, int beforeIndex) {
        if (beforeIndex < tbodies.size()) {
            final ObjectTableRowGroup o = (ObjectTableRowGroup)tbodies.get(beforeIndex);
            if (o != null) {
                beforeIndex = DOM.getChildIndex(getElement(), o.getElement());
            }
        }
        tbodies.add(beforeIndex, rowGroup);
        DOM.insertChild(getElement(), rowGroup.getElement(), beforeIndex);
    }

    private void remove(final ObjectTableRowGroup rowGroup) {
        DOM.removeChild(getElement(), rowGroup.getElement());
        tbodies.remove(rowGroup);
        final Iterator rit = rowGroup.getRows().iterator();
        while (rit.hasNext()) {
            final TableRow tr = (TableRow)rit.next();
            final Iterator trit = tr.iterator();
            while (trit.hasNext()) {
                final TableCell tc = (TableCell)trit.next();
                disown(tc);
            }
        }
    }

    private class ObjectListEventListener implements ListEventListener {
        public void listChanged(final ListEvent listEvent) {
            if (listEvent.isAdded()) {
                for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                    final Object obj = objects.get(i);
                    final ObjectTableRowGroup rowGroup = new ObjectTableRowGroup(obj);
                    model.render(obj, rowGroup);
                    add(rowGroup, i);
                }

            } else if (listEvent.isRemoved()) {
                for (int i = listEvent.getIndexEnd() - 1; i >= listEvent.getIndexStart(); i--) {
                    final ObjectTableRowGroup rowGroup = (ObjectTableRowGroup)tbodies.get(i);
                    remove(rowGroup);

                }

            } else if (listEvent.isChanged()) {
                for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                    final Object obj = objects.get(i);
                    ObjectTableRowGroup rowGroup = (ObjectTableRowGroup)tbodies.get(i);

                    // test if really different
                    if (obj != rowGroup.getObject()) {
                        // XXX: reposition rows instead of just remove/add them

                        // remove old
                        remove(rowGroup);

                        // insert new
                        rowGroup = new ObjectTableRowGroup(obj);
                        model.render(obj, rowGroup);
                        add(rowGroup, i);
                    }

                }
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

        public Object getObject() {
            return obj;
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

    public final void onBrowserEvent(final Event event) {
        super.onBrowserEvent(event);

        Element target = DOM.eventGetTarget(event);
        // this will speed up the isOrHasChild below on large tables
        while (target != null && !DOM.compare(DOM.getParent(target), getElement())) {
            target = DOM.getParent(target);
        }

        final Iterator iter = tbodies.iterator();
        while (iter.hasNext()) {
            final ObjectTableRowGroup rowGroup = (ObjectTableRowGroup)iter.next();
            if (DOM.isOrHasChild(rowGroup.getElement(), target)) {
                rowGroup.onBrowserEvent(event);
            }
        }

        if (mouseListeners != null) {
            final int eventType = DOM.eventGetType(event);
            switch (eventType) {
                case Event.ONMOUSEDOWN: {
                    if (mouseListeners != null) {
                        mouseListeners.fireMouseEvent(this, event);
                    }
                    break;
                }

                case Event.ONMOUSEUP: {
                    if (mouseListeners != null) {
                        mouseListeners.fireMouseEvent(this, event);
                    }
                    break;
                }

                case Event.ONMOUSEMOVE: {
                    if (mouseListeners != null) {
                        mouseListeners.fireMouseEvent(this, event);
                    }
                    break;
                }

                case Event.ONMOUSEOVER: {
                    if (mouseListeners != null) {
                        mouseListeners.fireMouseEvent(this, event);
                    }
                    break;
                }

                case Event.ONMOUSEOUT: {
                    if (mouseListeners != null) {
                        mouseListeners.fireMouseEvent(this, event);
                    }
                    break;
                }
            }
        }
    }

    public void addMouseListener(final MouseListener listener) {
        if (mouseListeners == null) {
            mouseListeners = new MouseListenerCollection();
        }
        mouseListeners.add(listener);
    }

    public void removeMouseListener(final MouseListener listener) {
        if (mouseListeners != null) {
            mouseListeners.remove(listener);
        }
    }
}
