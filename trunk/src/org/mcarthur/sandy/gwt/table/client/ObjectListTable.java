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
 * An event driven table that is backed by an {@link EventList}. Each Object in the list is
 * reprsented by one {@link TableRowGroup row group}.
 *
 * <h3>CSS Style Rules</h3>
 * <ul class="css">
 * <li>.gwtstuff-ObjectListTable { /&#042; table element (table) &#042;/ }</li>
 * <li>.gwtstuff-ObjectListTable-ObjectListTableHeaderGroup { /&#042; table header row group element (thead) &#042;/ }</li>
 * <li>.gwtstuff-ObjectListTable-ObjectListTableFooterGroup { /&#042; table footer row group element (tfoot) &#042;/ }</li>
 * <li>.gwtstuff-ObjectListTable-ObjectListTableBodyGroup { /&#042; table body row group element (tbody) &#042;/ }</li>
 * <li>.gwtstuff-ObjectListTable-ObjectListTableRow { /&#042; table row element (tr) &#042;/ }</li>
 * <li>.gwtstuff-ObjectListTable-ObjectListTableHeaderCell { /&#042; table header cell element (th) &#042;/ }</li>
 * <li>.gwtstuff-ObjectListTable-ObjectListTableDataCell { /&#042; table data cell element (td) &#042;/ }</li>
 * <li>plus style classes inherited by {@link TableRowGroup}, {@link TableRow}, {@link TableCell}, etc...</li>
 * </ul>

 * @author Sandy McArthur
 */
public class ObjectListTable extends Panel implements SourcesMouseEvents {

    private static final String CLASS_GWTSTUFF_OBJECTLISTTABLE = Constants.GWTSTUFF + "-ObjectListTable";

    private static ObjectListTableImpl impl;
    static {
        impl = (ObjectListTableImpl)GWT.create(ObjectListTableImpl.class);
        impl.init();
    }

    private final EventList/*<TableColSpec>*/ colSpec;
    private TableHeaderGroup thead;
    private TableFooterGroup tfoot;
    private List/*<TableBodyGroup>*/ tbodies = new ArrayList();

    private final WidgetCollection widgets = new WidgetCollection(this);

    private final Renderer renderer;
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
        this(renderer, EventLists.eventList());
    }

    /**
     * Create a new ObjectListTable backed by an EventList.
     *
     * @param renderer converts objects into table rows.
     * @param objects the objects to be displayed by the table.
     */
    public ObjectListTable(final Renderer renderer, final EventList objects) {
        this.renderer = renderer;
        this.objects = objects;
        setElement(DOM.createTable());
        addStyleName(CLASS_GWTSTUFF_OBJECTLISTTABLE);

        objects.addListEventListener(objectsListener);

        if (objects.size() > 0) {
            // fake a list changed event to initialize the table rows.
            objectsListener.listChanged(new ListEvent(objects, ListEvent.ADDED, 0, objects.size()));
        }

        if (renderer instanceof ColSpecRenderer) {
            final ColSpecRenderer colSpecRenderer = (ColSpecRenderer)renderer;
            colSpec = EventLists.eventList();
            colSpec.addListEventListener(new ColSpecListEventListener());
            colSpec.addAll(colSpecRenderer.getColSpec());
        } else {
            colSpec = null;
        }
    }

    /**
     * The EventList backing this ObjectListTable.
     *
     * @return the EventList backing this ObjectListTable.
     */
    public EventList getObjects() {
        return objects;
    }

    List/*<TableBodyGroup>*/ getTbodies() {
        return tbodies;
    }

    TableFooterGroup getTfoot() {
        //createTfoot();
        return tfoot;
    }

    TableHeaderGroup getThead() {
        //createThead();
        return thead;
    }

    private void createTfoot() {
        if (tfoot == null) {
            final ObjectListTableFooterGroup footerGroup = new ObjectListTableFooterGroup();
            renderer.renderFooter(footerGroup);
            attach(footerGroup); // TODO: Does this do the right thing?
        }
    }

    private void createThead() {
        if (thead == null) {
            final ObjectListTableHeaderGroup headerGroup = new ObjectListTableHeaderGroup();
            renderer.renderHeader(headerGroup);
            attach(headerGroup); // TODO: Does this do the right thing?
        }
    }

    final Renderer getRenderer() {
        return renderer;
    }

    /**
     * Converts objects into table rows.
     *
     * <p>
     * <b>Note:</b> Modifying the EventList backing this table from the renderer is not allowed and
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
     * A renderer implementing this interface receives notification when TableRowGroups are
     * attached and detached to the browser's document. This provides a means to register and
     * unregister event listeners that affect the TableRowGroups state.
     *
     * <p>
     * <b>Note:</b> Adding or removing table rows or table cells from an attach or detach event
     * can lead to undefined behavior.
     * </p>
     */
    public interface AttachRenderer extends Renderer {

        /**
         * Invoked when a TableBodyGroup has been attached to the browser's document.
         *
         * @param obj the object this row group represents.
         * @param rowGroup the row group representing the object.
         * @see #onDetach(Object, TableBodyGroup)
         */
        public void onAttach(Object obj, TableBodyGroup rowGroup);

        /**
         * Invoked when a TableHeaderGroup has been attached to the browser's document.
         *
         * @param rowGroup the table's header row group.
         */
        public void onAttach(TableHeaderGroup rowGroup);

        /**
         * Invoked when a TableFooterGroup has been attached to the browser's document.
         * 
         * @param rowGroup the table's footer row group.
         */
        public void onAttach(TableFooterGroup rowGroup);

        /**
         * Invoked when a TableBodyGroup is detached from the browser's document.
         * 
         * @param obj the object this row group represents.
         * @param rowGroup the row group representing the object.
         * @see #onAttach(Object, TableBodyGroup)
         */
        public void onDetach(Object obj, TableBodyGroup rowGroup);

        /**
         * Invoked when a TableHeaderGroup is detached from the browser's document.
         *
         * @param rowGroup the table's header row group.
         */
        public void onDetach(TableHeaderGroup rowGroup);

        /**
         * Invoked when a TableFooterGroup is detached from the browser's document.
         *
         * @param rowGroup the table's footer row group.
         */
        public void onDetach(TableFooterGroup rowGroup);
    }

    /**
     * ALPHA: A renderer implementing this interface can provide column and column group information
     * to help control how the browser renders the table.
     *
     * <p>
     * <b>Note:</b> This class is part of an alpha API and is likely to change in incompatible ways
     * between releases.
     * </p>
     *
     * @see org.mcarthur.sandy.gwt.table.client.TableColGroup
     * @see org.mcarthur.sandy.gwt.table.client.TableCol
     */
    public interface ColSpecRenderer extends Renderer {

        /**
         * Return the current list of <code>TableColSpec</code> elements for this table.
         *
         * <p>
         * If you do not wish to set column groups for this table <b>do not</b> return <code>null</code>.
         * Either return an empty list or, preferably, do not implement this interface.
         * </p>
         *
         * @return a List of {@link org.mcarthur.sandy.gwt.table.client.TableColSpec}s.
         */
        public List/*<TableColSpec>*/ getColSpec();
    }

    final List/*<TableColSpec>*/ getColSpec() {
        return colSpec;
    }

    /**
     * Required by the HasWidgets interface, do not use this in your own code.
     */
    public Iterator iterator() {
        return widgets.iterator();
    }

    /**
     * Required by the HasWidgets interface, do not use this in your own code.
     * You should remove elements from the EventList to cause widgets to be removed from the table.
     * This method is only public because the HasWidgets interface requires it.
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

    private void add(final ObjectListTableBodyGroup rowGroup, final ObjectListTableBodyGroup beforeGroup) {
        final int beforeIndex;
        if (beforeGroup != null) {
            beforeIndex = tbodies.indexOf(beforeGroup);
        } else {
            beforeIndex = -1;
        }
        add(rowGroup, beforeGroup, beforeIndex);
    }

    private void add(final ObjectListTableBodyGroup rowGroup, final ObjectListTableBodyGroup beforeGroup, final int beforeIndex) {
        impl.add(this, rowGroup, beforeGroup, beforeIndex);
        addWidgets(rowGroup);
        // 2007-02-26: GWTCompiler can optimize this out if the instanceof is first
        if (renderer instanceof AttachRenderer && isAttached()) {
            final AttachRenderer attachRenderer = (AttachRenderer)renderer;
            attachRenderer.onAttach(rowGroup.getObject(), rowGroup);
        }
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

    private void detach(final ObjectListTableBodyGroup rowGroup) {
        // 2007-02-26: GWTCompiler can optimize this out if the instanceof is first
        if (renderer instanceof AttachRenderer && isAttached()) {
            final AttachRenderer attachRenderer = (AttachRenderer)renderer;
            attachRenderer.onDetach(rowGroup.getObject(), rowGroup);
        }
        DOM.removeChild(getElement(), rowGroup.getElement());
        tbodies.remove(rowGroup);
    }

    private void remove(final ObjectListTableBodyGroup rowGroup) {
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
                    final ObjectListTableBodyGroup rowGroup = new ObjectListTableBodyGroup(obj);
                    renderer.render(obj, rowGroup);
                    //add(rowGroup, i);
                    ObjectListTableBodyGroup before = null;
                    if (i < tbodies.size()) {
                        before = (ObjectListTableBodyGroup)tbodies.get(i);
                    }
                    add(rowGroup, before, i);
                }

            } else if (listEvent.isRemoved()) {
                for (int i = listEvent.getIndexEnd() - 1; i >= listEvent.getIndexStart(); i--) {
                    final ObjectListTableBodyGroup rowGroup = (ObjectListTableBodyGroup)tbodies.get(i);
                    detach(rowGroup);
                    remove(rowGroup);
                }

            } else if (listEvent.isChanged()) {
                if (true) { // unoptimized
                    for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                        final Object obj = objects.get(i);
                        ObjectListTableBodyGroup rowGroup = (ObjectListTableBodyGroup)tbodies.get(i);

                        // test if really different
                        if (obj != rowGroup.getObject()) {
                            // XXX: reposition rows instead of just remove/add them

                            // remove old
                            detach(rowGroup);
                            remove(rowGroup);

                            // insert new
                            rowGroup = new ObjectListTableBodyGroup(obj);
                            renderer.render(obj, rowGroup);

                            ObjectListTableBodyGroup before = null;
                            if (i < tbodies.size()) {
                                before = (ObjectListTableBodyGroup)tbodies.get(i);
                            }
                            add(rowGroup, before, i);
                        }

                    }
                } else { // untested
                    final Map rows = new HashMap(listEvent.getIndexEnd() - listEvent.getIndexStart());
                    int k = listEvent.getIndexStart();
                    for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                        ObjectListTableBodyGroup rowGroup = (ObjectListTableBodyGroup)tbodies.get(k);
                        rows.put(rowGroup.getObject(), rowGroup);
                        detach(rowGroup);
                    }
                    for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                        final Object obj = objects.get(i);
                        ObjectListTableBodyGroup rowGroup = (ObjectListTableBodyGroup)rows.remove(obj);
                        if (rowGroup == null) {
                            rowGroup = new ObjectListTableBodyGroup(obj);
                            renderer.render(obj, rowGroup);
                        }
                        ObjectListTableBodyGroup before = null;
                        if (i < tbodies.size()) {
                            before = (ObjectListTableBodyGroup)tbodies.get(i);
                        }
                        add(rowGroup, before, i);
                    }

                    Iterator keyIter = rows.keySet().iterator();
                    while (keyIter.hasNext()) {
                        Object key = keyIter.next();
                        ObjectListTableBodyGroup rowGroup = (ObjectListTableBodyGroup)rows.get(key);
                        keyIter.remove();
                        remove(rowGroup);
                    }
                }
            }
        }
    }

    class ObjectListTableBodyGroup extends TableBodyGroup {
        private final Object obj;

        ObjectListTableBodyGroup(final Object obj) {
            this.obj = obj;
            addStyleName(CLASS_GWTSTUFF_OBJECTLISTTABLE + "-ObjectListTableBodyGroup");
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
            addStyleName(CLASS_GWTSTUFF_OBJECTLISTTABLE + "-ObjectListTableHeaderGroup");
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
            addStyleName(CLASS_GWTSTUFF_OBJECTLISTTABLE + "-ObjectListTableFooterGroup");
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
            addStyleName(CLASS_GWTSTUFF_OBJECTLISTTABLE + "-ObjectListTableRow");
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
            addStyleName(CLASS_GWTSTUFF_OBJECTLISTTABLE + "-ObjectListTableDataCell");
        }
    }

    private class ObjectListTableHeaderCell extends TableHeaderCell {
        public ObjectListTableHeaderCell() {
            addStyleName(CLASS_GWTSTUFF_OBJECTLISTTABLE + "-ObjectListTableHeaderCell");
        }
    }


    protected void onAttach() {
        // Create the header and footers if they haven't been created yet.
        createThead();
        createTfoot();

        super.onAttach();

        if (renderer instanceof AttachRenderer) {
            final AttachRenderer attachRenderer = (AttachRenderer)renderer;

            final TableHeaderGroup thead = getThead();
            assert thead != null;
            attachRenderer.onAttach(thead);

            final List tbodies = getTbodies();
            final Iterator iter=tbodies.iterator();
            while (iter.hasNext()) {
                final ObjectListTableBodyGroup tbody = (ObjectListTableBodyGroup)iter.next();
                attachRenderer.onAttach(tbody.getObject(), tbody);
            }

            final TableFooterGroup tfoot = getTfoot();
            assert tfoot != null;
            attachRenderer.onAttach(tfoot);
        }
    }

    protected void onDetach() {
        super.onDetach();

        if (renderer instanceof AttachRenderer) {
            final AttachRenderer attachRenderer = (AttachRenderer)renderer;

            final TableHeaderGroup thead = getThead();
            assert thead != null;
            attachRenderer.onDetach(thead);

            final List tbodies = getTbodies();
            final Iterator iter=tbodies.iterator();
            while (iter.hasNext()) {
                final ObjectListTableBodyGroup tbody = (ObjectListTableBodyGroup)iter.next();
                assert tbody != null;
                attachRenderer.onDetach(tbody.getObject(), tbody);
            }

            final TableFooterGroup tfoot = getTfoot();
            assert tfoot != null;
            attachRenderer.onDetach(tfoot);
        }
    }

    public void onBrowserEvent(final Event event) {
        super.onBrowserEvent(event);

        final Element tableElement = getElement();
        Element target = DOM.eventGetTarget(event);

        // if the event is on the table element don't search table row groups.
        if (!DOM.compare(tableElement, target)) {

            // find the parent of the event target that is a row group.
            Element targetParent = DOM.getParent(target);
            while (target != null && !DOM.compare(tableElement, targetParent)) {
                target = targetParent;
                targetParent = DOM.getParent(target);
            }

            // fire the onBrowserEvent for the row group that the event came from.
            if (DOM.compare(target, getThead().getElement())) {
                getThead().onBrowserEvent(event);

            } else if (DOM.compare(target, getTfoot().getElement())) {
                getTfoot().onBrowserEvent(event);

            } else {
                final Iterator iter = tbodies.iterator();
                while (iter.hasNext()) {
                    final ObjectListTableBodyGroup rowGroup = (ObjectListTableBodyGroup)iter.next();
                    if (DOM.compare(target, rowGroup.getElement())) {
                        rowGroup.onBrowserEvent(event);
                        break;
                    }
                }
            }
        }

        if (mouseListeners != null) {
            final int eventType = DOM.eventGetType(event);
            switch (eventType) {
                case Event.ONMOUSEDOWN:
                case Event.ONMOUSEUP:
                case Event.ONMOUSEMOVE:
                case Event.ONMOUSEOVER:
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
            if (mouseListeners.isEmpty()) {
                mouseListeners = null;
            }
        }
    }

    private class ColSpecListEventListener implements ListEventListener {
        public void listChanged(final ListEvent listEvent) {
            final List colSpec = listEvent.getSourceList();
            if (listEvent.isAdded()) {
                for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                    final TableColSpec col = (TableColSpec)colSpec.get(i);
                    DOM.insertChild(getElement(), col.getElement(), i);
                }
            } else if (listEvent.isChanged()) {
                for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                    final TableColSpec col = (TableCol)colSpec.get(i);
                    final Element oldChild = DOM.getChild(getElement(), i);
                    if (!DOM.compare(oldChild, col.getElement())) {
                        DOM.removeChild(getElement(), oldChild);
                        DOM.insertChild(getElement(), col.getElement(), i);
                    }
                }
            } else if (listEvent.isRemoved()) {
                for (int i = listEvent.getIndexEnd()-1; i >= listEvent.getIndexStart(); i--) {
                    final Element oldChild = DOM.getChild(getElement(), i);
                    DOM.removeChild(getElement(), oldChild);
                }
            }
        }
    }
}
