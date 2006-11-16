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
 * @author Sandy McArthur
 * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#h-11.2.3">HTML Row Group</a>
 */
abstract class TableRowGroup extends UIObject implements EventListener {
    private final EventList rows = EventLists.wrap(new ArrayList());
    private List mouseListeners = null;

    protected TableRowGroup(final Element element) {
        setElement(element);
        rows.addListEventListener(new TableRowGroupListEventListener());
    }

    /**
     * Add another {@link TableRow} to this row group.
     * This has the same effect as <code>getRows().add(row)</code>.
     *
     * @param row the table row to be added.
     */
    public void add(final TableRow row) {
        rows.add(row);
    }

    /**
     * Get the List of {@link TableRow}s.
     *
     * @return a List of {@link TableRow}s.
     */
    public final List getRows() {
        return rows;
    }

    public abstract TableRow newTableRow();

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
        final Iterator iter = rows.iterator();
        final Element target = DOM.eventGetTarget(event);
        while (iter.hasNext()) {
            final TableRow row = (TableRow)iter.next();
            if (DOM.isOrHasChild(row.getElement(), target)) {
                row.onBrowserEvent(event);
            }
        }

        if (mouseListeners != null) {
            final Iterator mlIter = mouseListeners.iterator();
            while (mlIter.hasNext()) {
                final MouseListener listener = (MouseListener)mlIter.next();
                final int eventType = DOM.eventGetType(event);
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
            sinkEvents(Event.MOUSEEVENTS | Event.ONCLICK | Event.ONDBLCLICK);
            mouseListeners = new ArrayList();
        }
        mouseListeners.add(listener);
    }

    public void removeMouseListener(final MouseListener listener) {
        if (mouseListeners != null) {
            mouseListeners.remove(listener);
        }
    }

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
