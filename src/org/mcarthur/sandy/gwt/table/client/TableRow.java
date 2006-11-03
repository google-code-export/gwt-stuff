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
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetCollection;

import java.util.Iterator;

/**
 * Base class for an HTML Table Row.
 *
 * @author Sandy McArthur
 * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#h-11.2.5">HTML Table Row</a>
*/
abstract class TableRow extends UIObject implements HasWidgets {
    private final WidgetCollection cells = new WidgetCollection(this);

    public TableRow() {
        setElement(DOM.createTR());
        sinkEvents(Event.ONCLICK | Event.ONMOUSEOVER | Event.ONMOUSEOUT);
    }

    public void add(final TableCell cell) {
        cells.add(cell);
        adopt(cell, getElement());
    }

    public void add(final Widget w) {
        if (w instanceof TableCell) {
            add((TableCell)w);
        } else {
            throw new IllegalArgumentException("Only TableCell widgets allowed.");
        }
    }

    /**
     * Delegate this to {@link Panel#adopt(Widget, Element)}.
     * @see com.google.gwt.user.client.ui.Panel#adopt(Widget, Element)
     */
    protected abstract void adopt(Widget w, Element container);

    public abstract TableDataCell newTableDataCell();

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

    public String toString() {
        return "TableRow";
    }
}
