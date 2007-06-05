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

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.UIObject;
import org.mcarthur.sandy.gwt.event.list.client.EventList;
import org.mcarthur.sandy.gwt.event.list.client.EventLists;
import org.mcarthur.sandy.gwt.event.list.client.ListEvent;
import org.mcarthur.sandy.gwt.event.list.client.ListEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Base class for an HTML Table Row, tr.
 *
 * <h3>CSS Style Rules</h3>
 * <ul class="css">
 * <li>.gwtstuff-TableRow { }</li>
 * </ul>
 *
 * @author Sandy McArthur
 * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#h-11.2.5">HTML Table Row</a>
 */
public abstract class TableRow extends UIObject implements EventListener {

    private final EventList/*<TableCell>*/ cells = EventLists.eventList();
    private final List/*<TableCell>*/ cellsbk = new ArrayList();
    private List mouseListeners = null;
    private boolean attached = false;

    protected TableRow() {
        setElement(DOM.createTR());
        addStyleName(Constants.GWTSTUFF + "-TableRow");
        cells.addListEventListener(new TableRowListEventListener());
    }

    public final List getCells() {
        return cells;
    }

    /**
     * Add a cell to this table row.
     *
     * @param cell the cell to add.
     * @throws IllegalArgumentException when the <code>cell</code> is not from {@link #newTableDataCell()} or {@link #newTableHeaderCell()}.
     * @see #newTableDataCell()
     * @see #newTableHeaderCell()
     */
    public void add(final TableCell cell) throws IllegalArgumentException {
        cells.add(cell);
    }

    /**
     * Creates a new table data cell that this table row will accept.
     *
     * @return a new table data cell that this table row will accept.
     * @see #add(TableCell)
     */
    public abstract TableDataCell newTableDataCell();

    /**
     * Creates a new table header cell that this table row will accept.
     *
     * @return a new table header cell that this table row will accept.
     * @see #add(TableCell)
     */
    public abstract TableHeaderCell newTableHeaderCell();

    public void setAlignment(final HasHorizontalAlignment.HorizontalAlignmentConstant hAlign, final HasVerticalAlignment.VerticalAlignmentConstant vAlign) {
        setHorizontalAlignment(hAlign);
        setVerticalAlignment(vAlign);
    }

    public void setHorizontalAlignment(final HasHorizontalAlignment.HorizontalAlignmentConstant align) {
        DOM.setElementProperty(getElement(), "align", align.getTextAlignString());
    }

    public void setVerticalAlignment(final HasVerticalAlignment.VerticalAlignmentConstant align) {
        DOM.setStyleAttribute(getElement(), "verticalAlign", align.getVerticalAlignString());
    }

    public final void onBrowserEvent(final Event event) {
        if (mouseListeners != null) {
            final int eventType = DOM.eventGetType(event);
            final Iterator mlIter = mouseListeners.iterator();
            while (mlIter.hasNext()) {
                final MouseListener listener = (MouseListener)mlIter.next();
                switch (eventType) {
                    case Event.ONMOUSEDOWN: {
                        listener.onMouseDown(this, event);
                        break;
                    }

                    case Event.ONMOUSEUP: {
                        listener.onMouseUp(this, event);
                        break;
                    }

                    case Event.ONMOUSEMOVE: {
                        listener.onMouseMove(this, event);
                        break;
                    }

                    case Event.ONMOUSEOVER: {
                        listener.onMouseOver(this, event);
                        break;
                    }

                    case Event.ONMOUSEOUT: {
                        listener.onMouseOut(this, event);
                        break;
                    }

                    case Event.ONCLICK: {
                        listener.onClick(this, event);
                        break;
                    }

                    case Event.ONDBLCLICK: {
                        listener.onDblClick(this, event);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Adds a listener to receive mouse events.
     * To avoid memory leaks it's best if you only call this method from an <code>onAttach</code>
     * method.
     *
     * @param listener the mouse listener to add.
     * @see #removeMouseListener(org.mcarthur.sandy.gwt.table.client.TableRow.MouseListener)
     * @see org.mcarthur.sandy.gwt.table.client.ObjectListTable.AttachRenderer#onAttach(Object, TableBodyGroup)
     */
    public void addMouseListener(final MouseListener listener) {
        if (mouseListeners == null) {
            //sinkEvents(TableRowGroup.MOUSEEVENTSALL); // no longer works with GWT 1.4's event model changes
            mouseListeners = new ArrayList();
        }
        mouseListeners.add(listener);
    }

    /**
     * Removes a previously added mouse listener.
     * To avoid memory leaks it's best if you remove any listeners added in an <code>onAttach</code>
     * method in the matching <code>onDetach</code> method.
     *
     * @param listener the mouse listener to remove.
     * @see #addMouseListener(org.mcarthur.sandy.gwt.table.client.TableRow.MouseListener)
     * @see org.mcarthur.sandy.gwt.table.client.ObjectListTable.AttachRenderer#onDetach(Object, TableBodyGroup)
     */
    public void removeMouseListener(final MouseListener listener) {
        if (mouseListeners != null) {
            mouseListeners.remove(listener);
            if (mouseListeners.isEmpty()) {
                //unsinkEvents(TableRowGroup.MOUSEEVENTSALL); // no longer works with GWT 1.4's event model changes
                mouseListeners = null; // this is needed else sinkEvents won't be called onAttach
            }
        }
    }

    /**
     * Event interface for mouse events on a table row.
     */
    public interface MouseListener extends java.util.EventListener {
        public void onMouseDown(TableRow row, Event event);

        public void onMouseMove(TableRow row, Event event);

        public void onMouseOver(TableRow row, Event event);

        public void onMouseOut(TableRow row, Event event);

        public void onMouseUp(TableRow row, Event event);

        public void onClick(TableRow row, Event event);

        public void onDblClick(TableRow row, Event event);
    }

    protected void adopt(final TableCell cell, final int index) {
        // check that the cell hasn't been adopted twice
        assert DOM.getParent(cell.getElement()) == null : "table cell cannot be adoped twice. index: " + index;

        cellsbk.add(index, cell);

        DOM.insertChild(getElement(), cell.getElement(), index);

        // if the row is attached, attach the cell
        if (attached) {
            cell.onAttach();
        }
    }

    protected void disown(final TableCell cell) {
        // check that the cell is owned by this row
        assert DOM.compare(getElement(), DOM.getParent(cell.getElement())) : "table cell is not owned by this row.";

        if (attached) {
            cell.onDetach();
        }

        DOM.removeChild(getElement(), cell.getElement());

        cellsbk.remove(cell);
    }

    protected void onAttach() {
        assert !attached : "TableRow cannot be attached twice.";

        attached = true;

        final Iterator/*<TableCell>*/ iter = getCells().iterator();
        while (iter.hasNext()) {
            final TableCell cell = (TableCell)iter.next();
            cell.onAttach();
        }
    }

    protected void onDetach() {
        assert attached : "TableRow cannot be detached twice.";

        attached = false;

        final Iterator/*<TableCell>*/ iter = getCells().iterator();
        while (iter.hasNext()) {
            final TableCell cell = (TableCell)iter.next();
            cell.onDetach();
        }
    }

    private class TableRowListEventListener implements ListEventListener {
        public void listChanged(final ListEvent listEvent) {
            final List/*<TableCell>*/ cells = listEvent.getSourceList();

            if (listEvent.isAdded()) {
                for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                    final TableCell cell = (TableCell)cells.get(i);
                    adopt(cell, i);
                }

            } else if (listEvent.isChanged()) {
                for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                    final TableCell cell = (TableCell)cells.get(i);
                    final TableCell oldCell = (TableCell)cellsbk.get(i);
                    if (cell != oldCell) {
                        disown(oldCell);
                        adopt(cell, i);
                    }
                }

            } else if (listEvent.isRemoved()) {
                for (int i = listEvent.getIndexEnd()-1; i >= listEvent.getIndexStart(); i--) {
                    final TableCell cell = (TableCell)cellsbk.remove(i);
                    disown(cell);
                }
            }
        }
    }

}
