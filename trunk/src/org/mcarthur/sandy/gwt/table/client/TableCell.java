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
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.SimplePanel;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Base class for an HTML Table Cell.
 *
 * <h3>CSS Style Rules</h3>
 * <ul class="css">
 * <li>.gwtstuff-TableCell { }</li>
 * </ul>
 *
 * @author Sandy McArthur
 * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#h-11.2.6">HTML Table Cell</a>
 */
public abstract class TableCell extends SimplePanel {
    protected TableCell(final Element cellElement) {
        super(cellElement);
        addStyleName("gwtstuff-TableCell");
    }

    /**
     * Get the table cell's abbr attribbute.
     *
     * @return the table cell's abbr attribbute.
     * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#adef-abbr">HTML Table Cell Abbr</a>
     */
    public String getAbbr() {
        return DOM.getAttribute(getElement(), "abbr");
    }

    /**
     * Get the table cell's abbr attribbute.
     *
     * @param abbr the table cell's abbr attribbute.
     * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#adef-abbr">HTML Table Cell Abbr</a>
     */
    public void setAbbr(final String abbr) {
        DOM.setAttribute(getElement(), "abbr", abbr);
    }

    /**
     * Get the table cell's axes as a List of Strings.
     *
     * @return the table cell's axes as a List of Strings.
     * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#adef-axis">HTML Table Cell Axis</a>
     */
    public List getAxes() {
        final String axis = DOM.getAttribute(getElement(), "axis");
        return Arrays.asList(axis.split(","));
    }

    /**
     * Sets the table cell's axes as a List of Strings.
     *
     * @param axes the table cell's axes as a List of Strings, null to clear the axes.
     * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#adef-axis">HTML Table Cell Axis</a>
     */
    public void setAxes(final List axes) {
        DOM.setAttribute(getElement(), "axis", join(axes, ","));
    }

    /**
     * Sets the table cell's axes to one value.
     * This has the same effect as calling {@link #setAxes(java.util.List)} with one String in the List.
     *
     * @param axis the table cell's axes, null to clear the axis.
     * @see #setAxes(java.util.List)
     * @see #getAxes()
     * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#adef-axis">HTML Table Cell Axis</a>
     */
    public void setAxis(final String axis) {
        DOM.setAttribute(getElement(), "axis", axis != null ? axis : "");
    }

    /**
     * Get the number of columns spanned by the current cell.
     *
     * @return the number of columns spanned by the current cell.
     * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#adef-colspan">HTMl Table Cell ColSpan</a>
     */
    public int getColSpan() {
        return DOM.getIntAttribute(getElement(), "colSpan");
    }

    /**
     * Set the number of columns spanned by the current cell.
     *
     * @param colSpan the number of columns spanned by the current cell.
     * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#adef-colspan">HTMl Table Cell ColSpan</a>
     */
    public void setColSpan(final int colSpan) {
        DOM.setIntAttribute(getElement(), "colSpan", colSpan);
    }

    /**
     * Get the number of rows spanned by the current cell.
     *
     * @return the number of rows spanned by the current cell.
     * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#adef-rowspan">HTML Table Cell RowSpan</a>
     */
    public int getRowSpan() {
        return DOM.getIntAttribute(getElement(), "rowSpan");
    }

    /**
     * Set the number of rows spanned by the current cell.
     *
     * @param rowSpan the number of rows spanned by the current cell.
     * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#adef-rowspan">HTML Table Cell RowSpan</a>
     */
    public void setRowSpan(final int rowSpan) {
        DOM.setIntAttribute(getElement(), "rowSpan", rowSpan);
    }

    /**
     * Set the horizontal and vertical position of data within a cell.
     *
     * @param hAlign the horizontal position of data within a cell.
     * @param vAlign the vertical position of data within a cell.
     * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#adef-align-TD">HTML Table Cell Align</a>
     * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#adef-valign">HTML Table Cell Valign</a>
     */
    public void setAlignment(final HasHorizontalAlignment.HorizontalAlignmentConstant hAlign, final HasVerticalAlignment.VerticalAlignmentConstant vAlign) {
        setHorizontalAlignment(hAlign);
        setVerticalAlignment(vAlign);
    }

    /**
     * Set the horizontal position of data within a cell.
     *
     * @param align the horizontal position of data within a cell.
     * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#adef-align-TD">HTML Table Cell Align</a>
     */
    public void setHorizontalAlignment(final HasHorizontalAlignment.HorizontalAlignmentConstant align) {
        DOM.setAttribute(getElement(), "align", align.getTextAlignString());
    }

    /**
     * Set the vertical position of data within a cell.
     *
     * @param align the vertical position of data within a cell.
     * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#adef-valign">HTML Table Cell Valign</a>
     */
    public void setVerticalAlignment(final HasVerticalAlignment.VerticalAlignmentConstant align) {
        DOM.setStyleAttribute(getElement(), "verticalAlign", align.getVerticalAlignString());
    }

    /**
     * Convert a List into a string.
     *
     * @param list a list of items to join.
     * @param separator characters to go between items.
     * @return list converted a string.
     */
    String join(final List list, final String separator) {
        final Iterator iter = list.iterator();
        String strs = null;
        while (iter.hasNext()) {
            final Object o = iter.next();
            if (strs == null) {
                strs = o.toString();
            } else {
                strs += separator + o.toString();
            }
        }
        if (strs == null) {
            strs = "";
        }
        return strs;
    }
}
