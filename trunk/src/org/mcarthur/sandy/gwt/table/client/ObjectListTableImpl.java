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

/**
 * Native implementation associated with {@link org.mcarthur.sandy.gwt.table.client.ObjectListTable}.
 *
 * @author Sandy McArthur
 */
class ObjectListTableImpl {

    public void init() {
        //Window.setTitle("Loaded ObjectListTableImpl");
    }

    /*
     * Replace uses of this with DOM.insertBefore once GWT provies one.
     */
    native void insertBefore(Element parent, Element toAdd, Element before) /*-{
        parent.insertBefore(toAdd, before);
    }-*/;

    void add(final ObjectListTable olt, final ObjectListTable.ObjectListTableBodyGroup rowGroup, final ObjectListTable.ObjectListTableBodyGroup beforeGroup, final int beforeIndex) {
        if (beforeGroup != null) {
            final Element beforeElement = beforeGroup.getElement();
            olt.getTbodies().add(beforeIndex, rowGroup);
            insertBefore(olt.getElement(), rowGroup.getElement(), beforeElement);
        } else {
            olt.getTbodies().add(rowGroup);
            DOM.appendChild(olt.getElement(), rowGroup.getElement());
        }
    }

    /**
     * Attach the TableHeaderGroup to the ObjectListTable's element.
     *
     * @param olt the current ObjectListTable.
     * @param headerGroup the thead to attach to <code>olt</code>'s element.
     */
    void attach(final ObjectListTable olt, final TableHeaderGroup headerGroup) {
        //DOM.appendChild(olt.getElement(), headerGroup.getElement());
        DOM.insertChild(olt.getElement(), headerGroup.getElement(), 0);
    }

    /**
     * Attach the TableFooterGroup to the ObjectListTable's element.
     *
     * @param olt the current ObjectListTable.
     * @param footerGroup the tfoot to attach to <code>olt</code>'s element.
     */
    void attach(final ObjectListTable olt, final TableFooterGroup footerGroup) {
        //DOM.appendChild(olt.getElement(), footerGroup.getElement());
        assert olt.getThead() != null;
        DOM.insertChild(olt.getElement(), footerGroup.getElement(), 1);
    }

}
