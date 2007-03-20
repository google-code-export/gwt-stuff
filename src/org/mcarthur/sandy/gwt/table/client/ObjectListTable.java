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
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;
import org.mcarthur.sandy.gwt.event.list.client.EventList;
import org.mcarthur.sandy.gwt.event.list.client.EventLists;
import org.mcarthur.sandy.gwt.event.list.client.ListEvent;
import org.mcarthur.sandy.gwt.event.list.client.ListEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
 *
 * <h3>Renderers</h3>
 * <p>
 * The {@link org.mcarthur.sandy.gwt.table.client.ObjectListTable.Renderer} interfaces you choose
 * to implement in your project will have an effect on the size and performance of all uses of the
 * ObjectListTable in your project. For example if you do not implement
 * {@link org.mcarthur.sandy.gwt.table.client.ObjectListTable.AttachRenderer} in your project the
 * size increase and speed overhead assoicated with calling
 * {@link org.mcarthur.sandy.gwt.table.client.ObjectListTable.AttachRenderer#onAttach(Object, TableBodyGroup)}
 * or
 * {@link org.mcarthur.sandy.gwt.table.client.ObjectListTable.AttachRenderer#onDetach(Object, TableBodyGroup)}
 * will be eliminated by the GWTCompiler. This is why there are a number of Renderer interfaces for
 * you to pick and choose from and how new functionality can be introduced without a size or
 * performance impact on existing users of the ObjectListTable widget.
 * </p>
 *
 * @author Sandy McArthur
 */
public final class ObjectListTable extends Widget implements SourcesMouseEvents {

    private static final String CLASS_GWTSTUFF_OBJECTLISTTABLE = Constants.GWTSTUFF + "-ObjectListTable";

    private ObjectListTableImpl impl = (ObjectListTableImpl)GWT.create(ObjectListTableImpl.class);

    private final EventList/*<TableColSpec>*/ colSpec;
    private TableHeaderGroup thead;
    private TableFooterGroup tfoot;
    private List/*<ObjectListTableBodyGroup>*/ tbodies = new ArrayList();

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

        impl.init(this);

        if (renderer instanceof ColSpecRenderer) {
            final ColSpecRenderer colSpecRenderer = (ColSpecRenderer)renderer;
            colSpec = EventLists.eventList();
            colSpec.addListEventListener(new ColSpecListEventListener());
            colSpec.addAll(colSpecRenderer.getColSpec());
        } else {
            colSpec = null;
        }

        if (objects.size() > 0) {
            // fake a list changed event to initialize the table rows.
            objectsListener.listChanged(new ListEvent(objects, ListEvent.ADDED, 0, objects.size()));
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

    /**
     * @return List of {@link ObjectListTableBodyGroup}
     */
    final List/*<ObjectListTableBodyGroup>*/ getTbodies() {
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
            adopt(footerGroup);
        }
    }

    private void createThead() {
        if (thead == null) {
            final ObjectListTableHeaderGroup headerGroup = new ObjectListTableHeaderGroup();
            renderer.renderHeader(headerGroup);
            adopt(headerGroup);
        }
    }

    final Renderer getRenderer() {
        return renderer;
    }

    /**
     * Converts objects into table rows.
     *
     * <p>
     * <b>Note:</b> Modifying the EventList backing this table from the renderer is not supported and
     * can lead to undefined behavior.
     * </p>
     */
    public interface Renderer {

        /**
         * Create the table rows and cells for an object.
         *
         * @param obj      the object <code>bodyGroup</code> will represent.
         * @param bodyGroup the row group to be assoiciated with <code>obj</code>.
         * @see org.mcarthur.sandy.gwt.table.client.ObjectListTable.ConcealRenderer#conceal(Object, TableBodyGroup)
         */
        public void render(Object obj, TableBodyGroup bodyGroup);

        /**
         * Create the table rows and cells for the table's header.
         * If you do not want to have a table header then simply do nothing to <code>headerGroup</code>.
         *
         * @param headerGroup the table's header row group.
         * @see org.mcarthur.sandy.gwt.table.client.ObjectListTable.ConcealRenderer#concealHeader(TableHeaderGroup)
         */
        public void renderHeader(TableHeaderGroup headerGroup);

        /**
         * Create the table rows and cells for the table's footer.
         * If you do not want to have a table footer then simply do nothing to the <code>footerGroup</code>.
         *
         * @param footerGroup the table's footer row group.
         * @see org.mcarthur.sandy.gwt.table.client.ObjectListTable.ConcealRenderer#concealFooter(TableFooterGroup)
         */
        public void renderFooter(TableFooterGroup footerGroup);
    }

    /**
     * Opposite of the Renderer interface.
     *
     * <p>
     * If you modify the {@link org.mcarthur.sandy.gwt.table.client.TableBodyGroup}s in any way
     * other than modifying the CSS Style Classes or adding or removeing
     * {@link org.mcarthur.sandy.gwt.table.client.TableRow}s you <b>must</b> undo the effects of
     * those changes.
     * </p>
     *
     * <p>
     * You cannot rely on the methods in this interface for general cleanup.
     * {@link org.mcarthur.sandy.gwt.table.client.ObjectListTable} currenly uses diferent techniques
     * to avoid various browser bugs. Currenly only IE recycles
     * {@link org.mcarthur.sandy.gwt.table.client.TableRowGroup}s because otherwise it will crash.
     * In the future if other browser benchmark to be faster using the same technique as IE to
     * manage {@link org.mcarthur.sandy.gwt.table.client.TableRowGroup}s then other browsers may
     * also make calls back to this interface.
     * </p>
     *
     * <p>
     *  For example: say you also implement
     * {@link org.mcarthur.sandy.gwt.table.client.ObjectListTable.AttachRenderer} and while a
     * {@link org.mcarthur.sandy.gwt.table.client.TableRowGroup} is attached you register a mouse
     * listener that when a row group is clicked may changes the <code>border-color</code> CSS style
     * of the row group's elemnt via
     * {@link com.google.gwt.user.client.DOM#setStyleAttribute(com.google.gwt.user.client.Element, String, String)}.
     * If this might happen you need to reset the <code>border-color</code> style attribute to it's
     * default value. If you do not do this then a future row group might be created with an
     * unexpected initial <code>border-color</code>.
     * </p>
     * <p>
     * If you were to implement the above change via adding a CSS class "clicked" and declaring the
     * new <code>border-color</code> in a stylesheet then you do not need to take such defensive
     * steps. Adding and removing CSS class styles is the prefered method of altering a row group's
     * appearance and that is why it is reset to an initial state for you.
     * </p>
     *
     * @see org.mcarthur.sandy.gwt.table.client.ObjectListTable.Renderer
     */
    public interface ConcealRenderer extends Renderer {
        /**
         * Remove any modifications to a row group since it was rendered.
         *
         * @param obj      the object <code>bodyGroup</code> represented.
         * @param bodyGroup the row group no longer to be assoiciated with <code>obj</code>.
         * @see org.mcarthur.sandy.gwt.table.client.ObjectListTable.Renderer#render(Object, TableBodyGroup)
         */
        public void conceal(Object obj, TableBodyGroup bodyGroup);

        /**
         * Remove any modifications to a header row group since it was rendered.
         *
         * @param headerGroup the table's former header row group.
         * @see org.mcarthur.sandy.gwt.table.client.ObjectListTable.Renderer#renderHeader(TableHeaderGroup)
         */
        public void concealHeader(TableHeaderGroup headerGroup);

        /**
         * Remove any modifications to a footer row group since it was rendered.
         *
         * @param footerGroup the table's former footer row group.
         * @see org.mcarthur.sandy.gwt.table.client.ObjectListTable.Renderer#renderFooter(TableFooterGroup)
         */
        public void concealFooter(TableFooterGroup footerGroup);
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
         * This method should undo anything you've done in the
         * {@link #onAttach(Object, TableBodyGroup)} method.
         * 
         * @param obj the object this row group represents.
         * @param rowGroup the row group representing the object.
         * @see #onAttach(Object, TableBodyGroup)
         */
        public void onDetach(Object obj, TableBodyGroup rowGroup);

        /**
         * Invoked when a TableHeaderGroup is detached from the browser's document.
         * This method should undo anything you've done in the
         * {@link #onAttach(TableHeaderGroup)} method.
         *
         * @param rowGroup the table's header row group.
         */
        public void onDetach(TableHeaderGroup rowGroup);

        /**
         * Invoked when a TableFooterGroup is detached from the browser's document.
         * This method should undo anything you've done in the
         * {@link #onAttach(TableFooterGroup)} method.
         *
         * @param rowGroup the table's footer row group.
         */
        public void onDetach(TableFooterGroup rowGroup);
    }

    /**
     * BETA: A renderer implementing this interface can provide column and column group information
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

    private void adopt(final TableHeaderGroup headerGroup) {
        // check that the headerGroup hasn't been adopted twice
        assert DOM.getParent(headerGroup.getElement()) == null : "headerGroup cannot be adoped twice.";

        assert thead == null : "Table Header cannot be replaced. (yet)";

        thead = headerGroup;

        // attach the thead element to the table element
        int insertIndex = 0;
        if (renderer instanceof ColSpecRenderer) {
            insertIndex += colSpec.size();
        }

        DOM.insertChild(getElement(), headerGroup.getElement(), insertIndex);

        // if the table is attached, attach the row group
        if (isAttached()) {
            attach(headerGroup);
        }
    }

    private void adopt(final TableFooterGroup footerGroup) {
        // check that the footerGroup hasn't been adopted twice
        assert DOM.getParent(footerGroup.getElement()) == null : "footerGroup cannot be adoped twice.";

        assert tfoot == null : "Table Footer cannot be replaced. (yet)";

        tfoot = footerGroup;

        // attach the thead element to the table element
        int insertIndex = 0;
        if (renderer instanceof ColSpecRenderer) {
            insertIndex += colSpec.size();
        }
        if (getThead() != null) insertIndex++;

        DOM.insertChild(getElement(), footerGroup.getElement(), insertIndex);

        // if the table is attached, attach the row group
        if (isAttached()) {
            attach(footerGroup);
        }
    }

    private void adopt(final ObjectListTableBodyGroup bodyGroup, final int index) {
        /*
        // check that the bodyGroup hasn't been adopted twice
        assert DOM.getParent(bodyGroup.getElement()) == null;
        if (DOM.getParent(bodyGroup.getElement()) != null) {
            throw new IllegalStateException("bodyGroup cannot be adoped twice!");
        }
        */

        // attach the tbody element to the table element
        impl.insert(this, bodyGroup, index);

        getTbodies().add(index, bodyGroup);

        /*
        int insertIndex = index;
        if (renderer instanceof ColSpecRenderer) {
            insertIndex += colSpec.size();
        }
        if (getThead() != null) insertIndex++;
        if (getTfoot() != null) insertIndex++;
        DOM.insertChild(getElement(), bodyGroup.getElement(), insertIndex);
        */

        // if the table is attached, attach the row group
        if (isAttached()) {
            renderer.render(bodyGroup.getObject(), bodyGroup);
            bodyGroup.setRendered(true);
            attach(bodyGroup);
        }
    }

    private void disown(final ObjectListTableBodyGroup bodyGroup) {
        // check that the bodyGroup is owned by this table
        assert DOM.compare(getElement(), DOM.getParent(bodyGroup.getElement())) : "bodyGroup is not owned by this table.";

        // if the table is attached, detach the row group
        if (isAttached()) {
            detach(bodyGroup);
        }

        impl.releaseBodyGroup(this, bodyGroup);

        getTbodies().remove(bodyGroup);
    }

    private void attach(final TableHeaderGroup headerGroup) {
        headerGroup.onAttach();

        if (renderer instanceof AttachRenderer) {
            final AttachRenderer attachRenderer = (AttachRenderer)renderer;
            attachRenderer.onAttach(headerGroup);
        }
    }

    private void attach(final TableFooterGroup footerGroup) {
        footerGroup.onAttach();

        if (renderer instanceof AttachRenderer) {
            final AttachRenderer attachRenderer = (AttachRenderer)renderer;
            attachRenderer.onAttach(footerGroup);
        }
    }

    private void attach(final ObjectListTableBodyGroup bodyGroup) {
        bodyGroup.onAttach();

        if (renderer instanceof AttachRenderer) {
            final AttachRenderer attachRenderer = (AttachRenderer)renderer;
            attachRenderer.onAttach(bodyGroup.getObject(), bodyGroup);
        }
    }

    private void detach(final TableHeaderGroup headerGroup) {
        throw new RuntimeException("unfinished method: detach(TableHeaderGroup)");
    }
    
    private void detach(final TableFooterGroup footerGroup) {
        throw new RuntimeException("unfinished method: detach(TableFooterGroup)");
    }

    private void detach(final ObjectListTableBodyGroup bodyGroup) {
        if (renderer instanceof AttachRenderer) {
            final AttachRenderer attachRenderer = (AttachRenderer)renderer;
            attachRenderer.onDetach(bodyGroup.getObject(), bodyGroup);
        }

        bodyGroup.onDetach();
    }

    private class ListTableListEventListener implements ListEventListener {
        public void listChanged(final ListEvent listEvent) {
            final List objects = listEvent.getSourceList();
            final List tbodies = getTbodies();
            
            if (listEvent.isAdded()) {
                for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                    final Object obj = objects.get(i);

                    final ObjectListTableBodyGroup bodyGroup = impl.takeBodyGroup(ObjectListTable.this);
                    bodyGroup.setObject(obj);

                    adopt(bodyGroup, i);
                }

            } else if (listEvent.isChanged()) {
                for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                    final Object obj = objects.get(i);
                    ObjectListTableBodyGroup bodyGroup = (ObjectListTableBodyGroup)tbodies.get(i);

                    // test if really different
                    if (obj != bodyGroup.getObject()) {
                        // remove old
                        disown(bodyGroup);

                        // create new
                        bodyGroup = impl.takeBodyGroup(ObjectListTable.this);
                        bodyGroup.setObject(obj);

                        adopt(bodyGroup, i);
                    }

                }

            } else if (listEvent.isRemoved()) {
                for (int i = listEvent.getIndexEnd() - 1; i >= listEvent.getIndexStart(); i--) {
                    final ObjectListTableBodyGroup bodyGroup = (ObjectListTableBodyGroup)tbodies.get(i);

                    disown(bodyGroup);
                }

            }
        }
    }

    static class ObjectListTableBodyGroup extends TableBodyGroup {
        private Object obj;
        private boolean rendered;

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

        void setObject(final Object obj) {
            this.obj = obj;
        }

        boolean isRendered() {
            return rendered;
        }

        void setRendered(final boolean rendered) {
            this.rendered = rendered;
        }

        protected void reset() {
            super.reset();
            addStyleName(CLASS_GWTSTUFF_OBJECTLISTTABLE + "-ObjectListTableBodyGroup");
            setRendered(false);
        }
    }

    private class ObjectListTableHeaderGroup extends TableHeaderGroup {
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

        protected void reset() {
            super.reset();
            addStyleName(CLASS_GWTSTUFF_OBJECTLISTTABLE + "-ObjectListTableHeaderGroup");
        }
    }

    private class ObjectListTableFooterGroup extends TableFooterGroup {
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

        protected void reset() {
            super.reset();
            addStyleName(CLASS_GWTSTUFF_OBJECTLISTTABLE + "-ObjectListTableFooterGroup");
        }
    }

    private static class ObjectListTableRow extends TableRow {

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

        public TableDataCell newTableDataCell() {
            return new ObjectListTableDataCell();
        }

        public TableHeaderCell newTableHeaderCell() {
            return new ObjectListTableHeaderCell();
        }
    }

    private static class ObjectListTableDataCell extends TableDataCell {
        public ObjectListTableDataCell() {
            addStyleName(CLASS_GWTSTUFF_OBJECTLISTTABLE + "-ObjectListTableDataCell");
        }
    }

    private static class ObjectListTableHeaderCell extends TableHeaderCell {
        public ObjectListTableHeaderCell() {
            addStyleName(CLASS_GWTSTUFF_OBJECTLISTTABLE + "-ObjectListTableHeaderCell");
        }
    }


    protected void onAttach() {
        // Create the header and footers if they haven't been created yet.
        createThead();
        createTfoot();

        super.onAttach();

        final TableHeaderGroup thead = getThead();
        assert thead != null : "Table Headers must be created during onAttach or before onAttach.";
        thead.onAttach();

        final TableFooterGroup tfoot = getTfoot();
        assert tfoot != null : "Table Footers must be created during onAttach or before onAttach.";
        tfoot.onAttach();

        if (renderer instanceof AttachRenderer) {
            final AttachRenderer attachRenderer = (AttachRenderer)renderer;
            attachRenderer.onAttach(thead);
            attachRenderer.onAttach(tfoot);
        }

        final Iterator iter = getTbodies().iterator();
        while (iter.hasNext()) {
            final ObjectListTableBodyGroup tbody = (ObjectListTableBodyGroup)iter.next();
            assert tbody != null : "Broken State: A null tbody got into the list of tbodies.";

            if (!tbody.isRendered()) {
                renderer.render(tbody.getObject(), tbody);
                tbody.setRendered(true);
            }
            tbody.onAttach();

            if (renderer instanceof AttachRenderer) {
                final AttachRenderer attachRenderer = (AttachRenderer)renderer;
                attachRenderer.onAttach(tbody.getObject(), tbody);
            }
        }
    }

    protected void onDetach() {
        // XXX: http://code.google.com/p/google-web-toolkit/issues/detail?id=792
        if (!isAttached()) {
            return;
        }
        //assert isAttached() : "The ObjectListTable is not current attached. The containing Panel/Widget has broken attach/detach logic and needs to be fixed. Using ObjectListTable in a broken widget can lead to unexpected behavior and is not supported.";
        super.onDetach();

        final TableHeaderGroup thead = getThead();
        assert thead != null : "Table Header wasn't created when the table was atached to the browser's document.";
        thead.onDetach();

        final TableFooterGroup tfoot = getTfoot();
        assert tfoot != null : "Table Footer wasn't created when the table was atached to the browser's document.";
        tfoot.onDetach();

        if (renderer instanceof AttachRenderer) {
            final AttachRenderer attachRenderer = (AttachRenderer)renderer;
            attachRenderer.onDetach(thead);
            attachRenderer.onDetach(tfoot);
        }

        final Iterator iter = getTbodies().iterator();
        while (iter.hasNext()) {
            final ObjectListTableBodyGroup tbody = (ObjectListTableBodyGroup)iter.next();
            assert tbody != null : "Broken State: A null tbody got into the list of tbodies.";

            tbody.onDetach();

            if (renderer instanceof AttachRenderer) {
                final AttachRenderer attachRenderer = (AttachRenderer)renderer;
                attachRenderer.onDetach(tbody.getObject(), tbody);
            }
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
                final Iterator iter = getTbodies().iterator();
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
