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
import com.google.gwt.user.client.Element;
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
 * Base class for a HTML Row Group.
 *
 * <h3>CSS Style Rules</h3>
 * <ul class="css">
 * <li>.gwtstuff-TableRowGroup { }</li>
 * </ul>
 *
 * @author Sandy McArthur
 * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#h-11.2.3">HTML Row Group</a>
 */
public abstract class TableRowGroup extends UIObject implements EventListener {
    static final int MOUSEEVENTSALL = Event.MOUSEEVENTS | Event.ONCLICK | Event.ONDBLCLICK;

    private final EventList/*<TableRow>*/ rows = EventLists.eventList();
    private final List rowsbk = new ArrayList();
    private List/*<MouseListener>*/ mouseListeners = null;
    private boolean attached = false;

    protected TableRowGroup(final Element element) {
        setElement(element);
        rows.addListEventListener(new TableRowGroupListEventListener());
        reset();
    }

    /**
     * Add another {@link TableRow} to this row group.
     * This has the same effect as <code>getRows().add(row)</code>.
     *
     * @param row the table row to be added.
     * @throws IllegalArgumentException when <code>row</code> is not from {@link #newTableRow()}.
     * @see #newTableRow()
     */
    public void add(final TableRow row) throws IllegalArgumentException {
        rows.add(row);
    }

    /**
     * Get the List of {@link TableRow}s.
     *
     * @return a List of {@link TableRow}s.
     */
    public final List/*<TableRow>*/ getRows() {
        return rows;
    }

    public abstract TableRow newTableRow();

    public boolean isAttached() {
        return attached;
    }

    public void setAlignment(final HasHorizontalAlignment.HorizontalAlignmentConstant hAlign, final HasVerticalAlignment.VerticalAlignmentConstant vAlign) {
        setHorizontalAlignment(hAlign);
        setVerticalAlignment(vAlign);
    }

    public void setHorizontalAlignment(final HasHorizontalAlignment.HorizontalAlignmentConstant align) {
        DOM.setAttribute(getElement(), "align", align.getTextAlignString());
    }

    public void setVerticalAlignment(final HasVerticalAlignment.VerticalAlignmentConstant align) {
        DOM.setStyleAttribute(getElement(), "verticalAlign", align.getVerticalAlignString());
    }

    public final void onBrowserEvent(final Event event) {
        final Element rowGroupElement = getElement();
        Element target = DOM.eventGetTarget(event);

        // if the event is on the row group element don't search for table cells.
        if (!DOM.compare(rowGroupElement, target)) {

            // find the parent of the event target that is a row.
            Element targetParent = DOM.getParent(target);
            while (target != null && !DOM.compare(rowGroupElement, targetParent)) {
                target = targetParent;
                targetParent = DOM.getParent(target);
            }

            // fire the onBrowserEvent for the row that the event came from.
            final Iterator iter = rows.iterator();
            while (iter.hasNext()) {
                final TableRow row = (TableRow)iter.next();
                if (DOM.compare(target, row.getElement())) {
                    row.onBrowserEvent(event);
                    break;
                }
            }
        }


        if (mouseListeners != null) {
            final Iterator mlIter = mouseListeners.iterator();
            final int eventType = DOM.eventGetType(event);
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
     * @see #removeMouseListener(org.mcarthur.sandy.gwt.table.client.TableRowGroup.MouseListener)
     * @see org.mcarthur.sandy.gwt.table.client.ObjectListTable.AttachRenderer#onAttach(Object, TableBodyGroup)
     */
    public void addMouseListener(final MouseListener listener) {
        if (mouseListeners == null) {
            sinkEvents(TableRowGroup.MOUSEEVENTSALL);
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
     * @see #addMouseListener(org.mcarthur.sandy.gwt.table.client.TableRowGroup.MouseListener)
     * @see org.mcarthur.sandy.gwt.table.client.ObjectListTable.AttachRenderer#onDetach(Object, TableBodyGroup)
     */
    public void removeMouseListener(final MouseListener listener) {
        if (mouseListeners != null) {
            mouseListeners.remove(listener);
            if (mouseListeners.isEmpty()) {
                unsinkEvents(TableRowGroup.MOUSEEVENTSALL);
                mouseListeners = null; // this is needed else sinkEvents won't be called onAttach
            }
        }
    }

    protected void adopt(final TableRow row, final int index) {
        // check that the row hasn't been adopted twice
        assert DOM.getParent(row.getElement()) == null : "TableRow cannot be adoped twice. index: " + index;

        rowsbk.add(index, row);

        DOM.insertChild(getElement(), row.getElement(), index);

        // if the table is attached, attach the row group
        if (attached) {
            row.onAttach();
        }
    }

    protected void disown(final TableRow row) {
        // check that the row is owned by this row group
        assert DOM.compare(getElement(), DOM.getParent(row.getElement())) : "TableRow is not owned by this row group.";

        if (attached) {
            row.onDetach();
        }

        DOM.removeChild(getElement(), row.getElement());

        rowsbk.remove(row);
    }

    protected void onAttach() {
        assert !isAttached() : "TableRowGroup cannot be attached twice.";

        attached = true;

        final Iterator/*<TableRow>*/ iter = getRows().iterator();
        while (iter.hasNext()) {
            final TableRow row = (TableRow)iter.next();
            row.onAttach();
        }
    }

    protected void onDetach() {
        assert isAttached() : "TableRowGroup cannot be detached twice.";

        attached = false;

        final Iterator/*<TableRow>*/ iter = getRows().iterator();
        while (iter.hasNext()) {
            final TableRow row = (TableRow)iter.next();
            row.onDetach();
        }
    }

    /**
     * Clears any associated rows or mouse listeners and
     * reinitializes the element's CSS classes.
     */
    protected void reset() {
        assert !isAttached() : "Trying to reset an attached row group!";
        while (mouseListeners != null) {
            removeMouseListener((MouseListener)mouseListeners.get(0));
        }
        setStyleName(Constants.GWTSTUFF + "-TableRowGroup");
        rows.clear();
    }

    private class TableRowGroupListEventListener implements ListEventListener {
        public void listChanged(final ListEvent listEvent) {
            final List/*<TableRow>*/ rows = listEvent.getSourceList();

            if (listEvent.isAdded()) {
                for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                    final TableRow row = (TableRow)rows.get(i);
                    adopt(row, i);
                }

            } else if (listEvent.isChanged()) {
                for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                    final TableRow row = (TableRow)rows.get(i);
                    final TableRow oldRow = (TableRow)rowsbk.get(i);
                    if (row != oldRow) {
                        disown(oldRow);
                        adopt(row, i);
                    }
                }

            } else if (listEvent.isRemoved()) {
                for (int i = listEvent.getIndexEnd()-1; i >= listEvent.getIndexStart(); i--) {
                    final TableRow row = (TableRow)rowsbk.get(i);
                    disown(row);
                }
            }
        }
    }

    /**
     * Event interface for mouse events on a table row group.
     */
    public interface MouseListener extends java.util.EventListener {
        public void onMouseDown(TableRowGroup rowGroup, Event event);

        public void onMouseMove(TableRowGroup rowGroup, Event event);

        public void onMouseOver(TableRowGroup rowGroup, Event event);

        public void onMouseOut(TableRowGroup rowGroup, Event event);

        public void onMouseUp(TableRowGroup rowGroup, Event event);

        public void onClick(TableRowGroup rowGroup, Event event);

        public void onDblClick(TableRowGroup rowGroup, Event event);
    }
}
