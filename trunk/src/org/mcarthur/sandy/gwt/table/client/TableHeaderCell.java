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
 * Base class for an HTML Table Header Cell, th.
 *
 * <h3>CSS Style Rules</h3>
 * <ul class="css">
 * <li>inherited css classes</li>
 * <li>.gwtstuff-TableHeaderCell { }</li>
 * </ul>
 *
 * @author Sandy McArthur
 * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#h-11.2.6">HTML Table Cell</a>
 */
public class TableHeaderCell extends TableCell {
    protected TableHeaderCell() {
        super(DOM.createTH());
        addStyleName(Constants.GWTSTUFF + "-TableHeaderCell");
    }

    /**
     * Get the set of data cells for which the current header cell provides header information.
     *
     * @return presumabally one of "row", "col", "rowgroup", or "colgroup".
     * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#adef-scope">HTML Table Header Cell Scope</a>
     */
    public String getScope() {
        return DOM.getElementProperty(getElement(), "scope");
    }

    /**
     * Set the set of data cells for which the current header cell provides header information.
     *
     * @param scope one of "row", "col", "rowgroup", or "colgroup".
     * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#adef-scope">HTML Table Header Cell Scope</a>
     */
    public void setScope(final String scope) {
        DOM.setElementProperty(getElement(), "scope", scope != null ? scope : "");
    }
}
