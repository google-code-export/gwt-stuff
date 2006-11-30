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

import com.google.gwt.core.client.GWT;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * An event driven table that is backed by an {@link EventList}. Each Object in the list reprsents
 * one {@link TableRowGroup row group}.
 *
 * <h3>CSS Style Rules</h3>
 * <ul class="css">
 * <li>.gwtstuff-ObjectListTable { }</li>
 * <li>plus style classes by TableRowGroup, TableRow, TableCell, etc...</li>
 * </ul>

 * @author Sandy McArthur
 */
public class ObjectListTable extends Panel implements SourcesMouseEvents {

    private static ObjectListTableImpl impl;
    static {
        impl = (ObjectListTableImpl)GWT.create(ObjectListTableImpl.class);
        impl.init();
    }

    private TableHeaderGroup thead;
    private TableFooterGroup tfoot;
    private List tbodies = new ArrayList();

    private final WidgetCollection widgets = new WidgetCollection(this);

    private final Renderer model;
    private final EventList objects;
    private final ListEventListener objectsListener = new ListTableListEventListener();

    private MouseListenerCollection mouseListeners = null;

    /**
     * Create a new ObjectListTable backed by an empty object list.
     *
     * @param renderer builds table rows for each object.
     * @see #getObjects()
     */
    public ObjectListTable(final Renderer renderer) {
        this(renderer, EventLists.wrap(new ArrayList()));
    }

    /**
     * Create a new ObjectListTable backed by an EventList.
     *
     * @param renderer converts objects into table rows.
     * @param objects the objects to be displayed by the table.
     */
    public ObjectListTable(final Renderer renderer, final EventList objects) {
        this.model = renderer;
        this.objects = objects;
        setElement(DOM.createTable());
        addStyleName("gwtstuff-ObjectListTable");

        objects.addListEventListener(objectsListener);

        if (objects.size() > 0) {
            // fake a list changed event to initialize the table rows.
            objectsListener.listChanged(new ListEvent(objects, ListEvent.ADDED, 0, objects.size()));
        }
    }

    public EventList getObjects() {
        return objects;
    }

    List getTbodies() {
        return tbodies;
    }

    TableFooterGroup getTfoot() {
        return tfoot;
    }

    TableHeaderGroup getThead() {
        return thead;
    }

    /**
     * Converts objects into table rows.
     *
     * <p>
     * <b>Note:</b> Modifying the EventList backing this table from the render is not allowed and
     * can lead to undefined behavior.
     * </p>
     */
    public interface Renderer {

        /**
         * Create the table rows and cells for an object.
         *
         * @param obj      the object these rows as based on.
         * @param rowGroup the row group to be assoiciated with <code>obj</code>.
         */
        public void render(Object obj, TableBodyGroup rowGroup);

        /**
         * Create the table rows and cells for the table's header.
         * If you do not want to have a table header then simply do nothing to <code>headerGroup</code>.
         *
         * @param headerGroup the table header row group.
         */
        public void renderHeader(TableHeaderGroup headerGroup);

        /**
         * Create the table rows and cells for the table's footer.
         * If you do not want to have a table footer then simply do nothing to the <code>footerGroup</code>.
         *
         * @param footerGroup the table footer row group.
         */
        public void renderFooter(TableFooterGroup footerGroup);
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

    private void add(final ObjectListTableRowGroup rowGroup, final int beforeIndex) {
        // While it shouldn't be important to keep the tfoot element after the tbody elements
        // it seems Opera needs this for generated DOM elements.
        int domIndex = beforeIndex;
        if (beforeIndex < tbodies.size()) {
            final ObjectListTableRowGroup o = (ObjectListTableRowGroup)tbodies.get(beforeIndex);
            if (o != null) {
                domIndex = DOM.getChildIndex(getElement(), o.getElement());
            }
        } else if (tfoot != null) {
            domIndex = DOM.getChildIndex(getElement(), tfoot.getElement());
        } else {
            domIndex = DOM.getChildCount(getElement());
        }
        tbodies.add(beforeIndex, rowGroup);
        DOM.insertChild(getElement(), rowGroup.getElement(), domIndex);

        // TODO? add to widgets before or after impl.add?
        addWidgets(rowGroup);
        
    }

    private void add(final ObjectListTableRowGroup rowGroup, final ObjectListTableRowGroup beforeGroup) {
        final int beforeIndex;
        if (beforeGroup != null) {
            beforeIndex = tbodies.indexOf(beforeGroup);
        } else {
            beforeIndex = -1;
        }
        add(rowGroup, beforeGroup, beforeIndex);
    }

    private void add(final ObjectListTableRowGroup rowGroup, final ObjectListTableRowGroup beforeGroup, final int beforeIndex) {
        impl.add(this, rowGroup, beforeGroup, beforeIndex);
        // TODO? add to widgets before or after impl.add?
        addWidgets(rowGroup);
    }

    private void addWidgets(final TableRowGroup rowGroup) {
        final Iterator iter = rowGroup.getRows().iterator();
        while (iter.hasNext()) {
            final TableRow tr = (TableRow)iter.next();
            final Iterator cells = tr.iterator();
            while (cells.hasNext()) {
                final Widget cell = (Widget)cells.next();
                if (!widgets.contains(cell)) {
                    widgets.add(cell);
                }
            }
        }
    }

    private void attach(final TableHeaderGroup headerGroup) {
        thead = headerGroup;
        impl.attach(this, headerGroup);
        addWidgets(headerGroup);
    }

    private void attach(final TableFooterGroup footerGroup) {
        tfoot = footerGroup;
        impl.attach(this, footerGroup);
        addWidgets(footerGroup);
    }

    private void detach(final ObjectListTableRowGroup rowGroup) {
        DOM.removeChild(getElement(), rowGroup.getElement());
        tbodies.remove(rowGroup);
    }

    private void remove(final ObjectListTableRowGroup rowGroup) {
        final Iterator rit = rowGroup.getRows().iterator();
        while (rit.hasNext()) {
            final TableRow tr = (TableRow)rit.next();
            final Iterator trit = tr.iterator();
            while (trit.hasNext()) {
                final TableCell tc = (TableCell)trit.next();
                disown(tc);
                widgets.remove(tc);
            }
        }
    }

    private class ListTableListEventListener implements ListEventListener {
        public void listChanged(final ListEvent listEvent) {
            if (listEvent.isAdded()) {
                for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                    final Object obj = objects.get(i);
                    final ObjectListTableRowGroup rowGroup = new ObjectListTableRowGroup(obj);
                    model.render(obj, rowGroup);
                    //add(rowGroup, i);
                    ObjectListTableRowGroup before = null;
                    if (i < tbodies.size()) {
                        before = (ObjectListTableRowGroup)tbodies.get(i);
                    }
                    add(rowGroup, before, i);
                }

            } else if (listEvent.isRemoved()) {
                for (int i = listEvent.getIndexEnd() - 1; i >= listEvent.getIndexStart(); i--) {
                    final ObjectListTableRowGroup rowGroup = (ObjectListTableRowGroup)tbodies.get(i);
                    detach(rowGroup);
                    remove(rowGroup);
                }

            } else if (listEvent.isChanged()) {
                if (true) { // unoptimized
                    for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                        final Object obj = objects.get(i);
                        ObjectListTableRowGroup rowGroup = (ObjectListTableRowGroup)tbodies.get(i);

                        // test if really different
                        if (obj != rowGroup.getObject()) {
                            // XXX: reposition rows instead of just remove/add them

                            // remove old
                            detach(rowGroup);
                            remove(rowGroup);

                            // insert new
                            rowGroup = new ObjectListTableRowGroup(obj);
                            model.render(obj, rowGroup);
                            add(rowGroup, i);
                        }

                    }
                } else { // untested
                    final Map rows = new HashMap(listEvent.getIndexEnd() - listEvent.getIndexStart());
                    int k = listEvent.getIndexStart();
                    for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                        ObjectListTableRowGroup rowGroup = (ObjectListTableRowGroup)tbodies.get(k);
                        rows.put(rowGroup.getObject(), rowGroup);
                        detach(rowGroup);
                    }
                    for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                        final Object obj = objects.get(i);
                        ObjectListTableRowGroup rowGroup = (ObjectListTableRowGroup)rows.remove(obj);
                        if (rowGroup == null) {
                            rowGroup = new ObjectListTableRowGroup(obj);
                            model.render(obj, rowGroup);
                        }
                        ObjectListTableRowGroup before = null;
                        if (i < tbodies.size()) {
                            before = (ObjectListTableRowGroup)tbodies.get(i);
                        }
                        add(rowGroup, before, i);
                    }

                    Iterator keyIter = rows.keySet().iterator();
                    while (keyIter.hasNext()) {
                        Object key = keyIter.next();
                        ObjectListTableRowGroup rowGroup = (ObjectListTableRowGroup)rows.get(key);
                        keyIter.remove();
                        remove(rowGroup);
                    }
                }
            }
        }
    }

    class ObjectListTableRowGroup extends TableBodyGroup {
        private final Object obj;

        ObjectListTableRowGroup(final Object obj) {
            this.obj = obj;
            addStyleName("gwtstuff-ObjectListTable-ObjectListTableRowGroup");
        }

        public TableRow newTableRow() {
            return new ObjectListTableRow();
        }

        public void add(final TableRow row) {
            if (row instanceof ObjectListTableRow) {
                super.add(row);
            } else {
                throw new IllegalArgumentException("TableRow instance must be acquired from newTableRow()");
            }
        }

        public Object getObject() {
            return obj;
        }
    }

    private class ObjectListTableHeaderGroup extends TableHeaderGroup {
        ObjectListTableHeaderGroup() {
            addStyleName("gwtstuff-ObjectListTable-ObjectListTableHeaderGroup");
        }

        public TableRow newTableRow() {
            return new ObjectListTableRow();
        }

        public void add(final TableRow row) {
            if (row instanceof ObjectListTableRow) {
                super.add(row);
            } else {
                throw new IllegalArgumentException("TableRow instance must be acquired from newTableRow()");
            }
        }
    }

    private class ObjectListTableFooterGroup extends TableFooterGroup {
        ObjectListTableFooterGroup() {
            addStyleName("gwtstuff-ObjectListTable-ObjectListTableFooterGroup");
        }

        public TableRow newTableRow() {
            return new ObjectListTableRow();
        }

        public void add(final TableRow row) {
            if (row instanceof ObjectListTableRow) {
                super.add(row);
            } else {
                throw new IllegalArgumentException("TableRow instance must be acquired from newTableRow()");
            }
        }
    }

    private class ObjectListTableRow extends TableRow {

        public ObjectListTableRow() {
            addStyleName("gwtstuff-ObjectListTable-ObjectListTableRow");
        }

        public void add(final TableCell cell) {
            if (cell instanceof ObjectListTableDataCell || cell instanceof ObjectListTableHeaderCell) {
                super.add(cell);
            } else {
                throw new IllegalArgumentException("TableCell must be provided by newTableDataCell() or newTableHeaderCell()");
            }
        }

        protected void adopt(final Widget w, final Element container) {
            ObjectListTable.this.adopt(w, container);
        }

        public TableDataCell newTableDataCell() {
            return new ObjectListTableDataCell();
        }

        public TableHeaderCell newTableHeaderCell() {
            return new ObjectListTableHeaderCell();
        }
    }

    private class ObjectListTableDataCell extends TableDataCell {
        public ObjectListTableDataCell() {
            addStyleName("gwtstuff-ObjectListTable-ObjectListTableDataCell");
        }
    }

    private class ObjectListTableHeaderCell extends TableHeaderCell {
        public ObjectListTableHeaderCell() {
            addStyleName("gwtstuff-ObjectListTable-ObjectListTableHeaderCell");
        }
    }


    protected void onAttach() {
        super.onAttach();
    }

    protected void onLoad() {
        super.onLoad();
        if (thead == null) {
            final ObjectListTableHeaderGroup headerGroup = new ObjectListTableHeaderGroup();
            model.renderHeader(headerGroup);
            attach(headerGroup);
        }
        if (tfoot == null) {
            final ObjectListTableFooterGroup footerGroup = new ObjectListTableFooterGroup();
            model.renderFooter(footerGroup);
            attach(footerGroup);
        }
    }

    public void onBrowserEvent(final Event event) {
        super.onBrowserEvent(event);

        Element target = DOM.eventGetTarget(event);
        // this will speed up the isOrHasChild below on large tables
        while (target != null && !DOM.compare(DOM.getParent(target), getElement())) {
            target = DOM.getParent(target);
        }

        final Iterator iter = tbodies.iterator();
        while (iter.hasNext()) {
            final ObjectListTableRowGroup rowGroup = (ObjectListTableRowGroup)iter.next();
            if (DOM.isOrHasChild(rowGroup.getElement(), target)) {
                rowGroup.onBrowserEvent(event);
                break;
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
