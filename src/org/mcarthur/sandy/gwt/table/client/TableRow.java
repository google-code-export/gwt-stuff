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
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetCollection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Base class for an HTML Table Row.
 *
 * <h3>CSS Style Rules</h3>
 * <ul class="css">
 * <li>.gwtstuff-TableRow { }</li>
 * </ul>
 *
 * @author Sandy McArthur
 * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#h-11.2.5">HTML Table Row</a>
 */
public abstract class TableRow extends UIObject implements HasWidgets, EventListener {
    private final WidgetCollection cells = new WidgetCollection(this);
    private List mouseListeners = null;

    protected TableRow() {
        setElement(DOM.createTR());
        addStyleName("gwtstuff-TableRow");
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
        adopt(cell, getElement());
    }

    /**
     * Prefer {@link #add(TableCell)} instead.
     *
     * @param w a TableCell to be added.
     * @throws IllegalArgumentException when <code>w</code> is not an instace of TableCell.
     * @see #add(TableCell)
     */
    public void add(final Widget w) throws IllegalArgumentException {
        if (w instanceof TableCell) {
            add((TableCell)w);
        } else {
            throw new IllegalArgumentException("Only TableCell widgets allowed.");
        }
    }

    /**
     * Delegate this to {@link Panel#adopt(Widget,Element)}.
     *
     * @see com.google.gwt.user.client.ui.Panel#adopt(Widget,Element)
     */
    protected abstract void adopt(Widget w, Element container);

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

    public void clear() {
        final Iterator iter = cells.iterator();
        while (iter.hasNext()) {
            iter.next();
            iter.remove();
        }
    }

    public Iterator iterator() {
        return cells.iterator();
    }

    public boolean remove(final Widget w) {
        final boolean removed = cells.contains(w);
        if (removed) {
            cells.remove(w);
        }
        return removed;
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

    public void addMouseListener(final MouseListener listener) {
        if (mouseListeners == null) {
            sinkEvents(TableRowGroup.MOUSEEVENTSALL);
            mouseListeners = new ArrayList();
        }
        mouseListeners.add(listener);
    }

    public void removeMouseListener(final MouseListener listener) {
        if (mouseListeners != null) {
            mouseListeners.remove(listener);
            if (mouseListeners.isEmpty()) {
                unsinkEvents(TableRowGroup.MOUSEEVENTSALL);
                mouseListeners = null;
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
}
