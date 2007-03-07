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
import org.mcarthur.sandy.gwt.event.list.client.EventList;
import org.mcarthur.sandy.gwt.event.list.client.EventLists;
import org.mcarthur.sandy.gwt.event.list.client.ListEvent;
import org.mcarthur.sandy.gwt.event.list.client.ListEventListener;

import java.util.List;

/**
 * BETA: Base class for a HTML Column Group, colgroup.
 *
 * <p>
 * <b>Note:</b> This class is part of an alpha API and is likely to change in incompatible ways
 * between releases.
 * </p>
 *
 * <h3>CSS Style Rules</h3>
 * <ul class="css">
 * <li>.gwtstuff-TableColGroup { /&#042; table column group element (colgroup) &#042;/ }</li>
 * </ul>
 *
 * @author Sandy McArthur
 * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#edef-COLGROUP">HTML Column Group</a>
 */
public final class TableColGroup extends TableColSpec {

    private EventList/*<TableCol>*/ cols = null;

    public TableColGroup() {
        super(DOM.createElement("colgroup")); // XXX: convert to DOM.createColGroup when available
        addStyleName(Constants.GWTSTUFF + "-TableColGroup");
    }

    /**
     * Append a <code>TableCol</code> to this <code>TableColGroup</code>.
     * 
     * @param col <code>TableCol</code> to append to this <code>TableColGroup</code>.
     */
    public void add(final TableCol col) {
        getCols().add(col);
    }

    /**
     * Get the List of {@link TableCol}s.
     *
     * @return a List of {@link TableCol}s.
     */
    public List/*<TableCol>*/ getCols() {
        if (cols == null) {
            cols = EventLists.eventList();
            cols.addListEventListener(new ColsListEventListener());
        }
        return cols;
    }

    /**
     * Get the number of columns in a column group. Values mean the following:
     * <ul>
     * <li>In the absence of a span attribute, each {@link TableColGroup} defines a column group containing one column.</li>
     * <li>If the span attribute is set to N &gt; 0, this {@link TableColGroup} element defines a column group containing N columns.</li>
     * </ul>
     * User agents must ignore this attribute if the {@link TableColGroup} element contains one or more {@link TableCol} elements.
     *
     * @return the number of columns in a column group.
     */
    public int getSpan() {
        return super.getSpan();
    }

    /**
     * Set the number of columns in a column group. Values mean the following:
     * <ul>
     * <li>In the absence of a span attribute, each {@link TableColGroup} defines a column group containing one column.</li>
     * <li>If the span attribute is set to N &gt; 0, this {@link TableColGroup} element defines a column group containing N columns.</li>
     * </ul>
     * User agents must ignore this attribute if the {@link TableColGroup} element contains one or more {@link TableCol} elements.
     *
     * @param span the number of columns in a column group.
     * @throws IllegalArgumentException when <code>span</code> is non-positive.
     */
    public void setSpan(final int span) throws IllegalArgumentException {
        super.setSpan(span);
    }

    /**
     * Get the default width for each column in this column group.
     * In addition to the standard pixel, percentage, and relative values, this attribute allows the
     * special form "0*" (zero asterisk) which means that the width of the each column in the group
     * should be the minimum width necessary to hold the column's contents.
     *
     * <p>
     * This attribute is overridden for any column in the column group whose width is specified via a {@link TableCol} element.
     * </p>
     *
     * @return the default width for each column in this column group.
     */
    public String getWidth() {
        return super.getWidth();
    }

    /**
     * Set the default width for each column in this column group.
     * In addition to the standard pixel, percentage, and relative values, this attribute allows the
     * special form "0*" (zero asterisk) which means that the width of the each column in the group
     * should be the minimum width necessary to hold the column's contents.
     *
     * <p>
     * This attribute is overridden for any column in the column group whose width is specified via a {@link TableCol} element.
     * </p>
     *
     * @param width the default width for each column in this column group.
     */
    public void setWidth(final String width) {
        super.setWidth(width);
    }

    private class ColsListEventListener implements ListEventListener {
        public void listChanged(final ListEvent listEvent) {
            if (listEvent.isAdded()) {
                for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                    final TableCol col = (TableCol)cols.get(i);
                    DOM.insertChild(getElement(), col.getElement(), i);
                }
            } else if (listEvent.isChanged()) {
                for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                    final TableCol col = (TableCol)cols.get(i);
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
