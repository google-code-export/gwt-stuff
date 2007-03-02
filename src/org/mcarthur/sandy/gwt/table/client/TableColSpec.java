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
import com.google.gwt.user.client.ui.UIObject;

/**
 * <b>ALPHA:</b> Base class for a HTML Column Groups.
 *
 * <p>
 * <b>Note:</b> This class is part of an alpha API and is likely to change in incompatible ways
 * between releases.
 * </p>
 *
 * @author Sandy McArthur
 * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#h-11.2.4">HTML Table Column Groups</a>
 */
public abstract class TableColSpec extends UIObject {

    protected TableColSpec(final Element columnGroup) {
        setElement(columnGroup);
    }

    /**
     * Get the number of columns affected by this element.
     *
     * <p>
     * <i>The exact meaning of this property will vary between column group elements.</i>
     * </p>
     *
     * @return the number of columns affected by this element.
     */
    public int getSpan() {
        return DOM.getIntAttribute(getElement(), "span");
    }

    /**
     * Set the number of columns affected by this element.
     *
     * <p>
     * <i>The exact meaning of this property will vary between column group elements.</i>
     * </p>
     *
     * @param span the number of columns affected by this element.
     * @throws IllegalArgumentException when <code>span</code> is non-positive.
     */
    public void setSpan(final int span) throws IllegalArgumentException {
        if (span <= 0) {
            throw new IllegalArgumentException("span must be positive. was: " + span);
        }
        DOM.setIntAttribute(getElement(), "span", span);
    }

    /**
     * Get the default width for each column in this column group.
     * In addition to the standard pixel, percentage, and relative values, this attribute allows the
     * special form "0*" (zero asterisk) which means that the width of the each column in the group
     * should be the minimum width necessary to hold the column's contents.
     *
     * @return the default width for each column in this column group.
     */
    public String getWidth() {
        return DOM.getAttribute(getElement(), "width");
    }

    /**
     * Set the default width for each column in this column group.
     * In addition to the standard pixel, percentage, and relative values, this attribute allows the
     * special form "0*" (zero asterisk) which means that the width of the each column in the group
     * should be the minimum width necessary to hold the column's contents.
     *
     * <p>
     * <b>Note:</b> unlike {@link com.google.gwt.user.client.ui.UIObject#setWidth(String)} this
     * method sets a "width" attribute as opposed to the "width" CSS Style attribute.
     * </p>
     *
     * @param width the default width for each column in this column group.
     */
    public void setWidth(final String width) {
        DOM.setAttribute(getElement(), "width", width);
    }
}
