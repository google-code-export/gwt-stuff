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

/**
 * BETA: Base class for a HTML Column, col.
 * 
 * <p>
 * <b>Note:</b> This class is part of an alpha API and is likely to change in incompatible ways
 * between releases.
 * </p>
 *
 * <h3>CSS Style Rules</h3>
 * <ul class="css">
 * <li>.gwtstuff-TableCol { /&#042; table column element (col) &#042;/ }</li>
 * </ul>
 *
 * @author Sandy McArthur
 * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#edef-COL">HTML Table Column</a>
 */
public final class TableCol extends TableColSpec {

    public TableCol() {
        super(DOM.createCol());
        addStyleName(Constants.GWTSTUFF + "-TableCol");
    }

    /**
     * Get the number of columns "spanned" by this <code>TableCol</code> element;
     * this <code>TableCol</code> element shares its attributes with all the columns it spans.
     * The default value for this attribute is 1
     * (i.e., this <code>TableCol</code> element refers to a single column).
     * If the span attribute is set to N &gt; 1,
     * this <code>TableCol</code> element shares its attributes with the next N-1 columns.
     *
     * @return the number of columns "spanned" by this <code>TableCol</code> element.
     */
    public int getSpan() {
        return super.getSpan();
    }

    /**
     * Set the number of columns "spanned" by this <code>TableCol</code> element;
     * this <code>TableCol</code> element shares its attributes with all the columns it spans.
     * The default value for this attribute is 1
     * (i.e., this <code>TableCol</code> element refers to a single column).
     * If the span attribute is set to N &gt; 1,
     * this <code>TableCol</code> element shares its attributes with the next N-1 columns.
     *
     * @param span the number of columns "spanned" by this <code>TableCol</code> element.
     * @throws IllegalArgumentException when <code>span</code> is non-positive.
     */
    public void setSpan(final int span) throws IllegalArgumentException {
        super.setSpan(span);
    }

    /**
     * Get the default width for each column spanned by this <code>TableCol</code> element.
     * It has the same meaning as the {@link TableColGroup#getWidth() TableColGroup width attribute} and overrides it.
     *
     * @return the default width for this column.
     */
    public String getWidth() {
        return super.getWidth();
    }

    /**
     * Set the default width for each column spanned by this <code>TableCol</code> element.
     * It has the same meaning as the {@link TableColGroup#setWidth(String) TableColGroup width attribute} and overrides it.
     *
     * @param width the default width for this column.
     */
    public void setWidth(final String width) {
        super.setWidth(width);
    }
}
