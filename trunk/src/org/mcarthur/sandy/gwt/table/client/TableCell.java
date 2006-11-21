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
 * <p/>
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
    public List getAxis() {
        final String axis = DOM.getAttribute(getElement(), "axis");
        return Arrays.asList(axis.split(","));
    }

    /**
     * Sets the table cell's axes as a List of Strings.
     *
     * @param axis the table cell's axes as a List of Strings, null to clear the axis.
     * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#adef-axis">HTML Table Cell Axis</a>
     */
    public void setAxis(final List axis) {
        if (axis != null && axis.size() > 0) {
            final Iterator iter = axis.iterator();
            String ax = null;
            while (iter.hasNext()) {
                final String a = (String)iter.next();
                if (ax == null) {
                    ax = a;
                } else {
                    ax += "," + a;
                }
            }
            DOM.setAttribute(getElement(), "axis", ax);
        } else {
            DOM.setAttribute(getElement(), "axis", "");
        }
    }

    /**
     * Sets the table cell's axes to one value.
     * This has the same effect as calling {@link #setAxis(java.util.List)} with one String in the List.
     *
     * @param axis the table cell's axes, null to clear the axis.
     * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#adef-axis">HTML Table Cell Axis</a>
     */
    public void setAxisSingle(String axis) {
        if (axis == null) {
            axis = "";
        }
        DOM.setAttribute(getElement(), "axis", axis);
    }

    public int getColSpan() {
        return DOM.getIntAttribute(getElement(), "colSpan");
    }

    public void setColSpan(final int colSpan) {
        DOM.setIntAttribute(getElement(), "colSpan", colSpan);
    }

    public int getRowSpan() {
        return DOM.getIntAttribute(getElement(), "rowSpan");
    }

    public void setRowSpan(final int rowSpan) {
        DOM.setIntAttribute(getElement(), "rowSpan", rowSpan);
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
}
