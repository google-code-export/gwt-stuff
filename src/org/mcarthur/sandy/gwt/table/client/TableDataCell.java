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

import java.util.Arrays;
import java.util.List;

/**
 * Base class for an HTML Table Data Cell, th.
 *
 * <h3>CSS Style Rules</h3>
 * <ul class="css">
 * <li>inherited css classes</li>
 * <li>.gwtstuff-TableDataCell { }</li>
 * </ul>
 *
 * @author Sandy McArthur
 * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#h-11.2.6">HTML Table Cell</a>
 */
public class TableDataCell extends TableCell {
    protected TableDataCell() {
        super(DOM.createTD());
        addStyleName("gwtstuff-TableDataCell");
    }

    /**
     * Get the list of header cells id's that provide header information for the current data cell.
     *
     * @return the table cell's header cells id's as a List of Strings.
     * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#adef-headers">HTML Table Data Cell Headers</a>
     */
    public List getHeaders() {
        final String axis = DOM.getAttribute(getElement(), "headers");
        return Arrays.asList(axis.split(" "));
    }

    /**
     * Set the list of header cells id's that provide header information for the current data cell.
     *
     * @param headers the table cell's header cells id's as a List of Strings, null to clear the headers list.
     * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#adef-headers">HTML Table Data Cell Headers</a>
     */
    public void setHeaders(final List headers) {
        DOM.setAttribute(getElement(), "headers", join(headers, " "));
    }

    /**
     * Set the header cell that provides header information for the current data cell.
     *
     * @param header the header cell's id, null to clear the headers attribute.
     * @see #setHeaders(java.util.List)
     * @see #getHeaders()
     * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#adef-headers">HTML Table Data Cell Headers</a>
     */
    public void setHeader(final String header) {
        DOM.setAttribute(getElement(), "headers", header != null ? header : "");
    }
}
