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

    /**
     * Called in the ObjectListTable's constructor.
     *
     * @param olt the ObjectListTable in the midst of construction.
     */
    protected void init(final ObjectListTable olt) {
        //Window.setTitle("Loaded ObjectListTableImpl");
    }

    protected ObjectListTable.ObjectListTableBodyGroup takeBodyGroup(final ObjectListTable olt) {
        return new ObjectListTable.ObjectListTableBodyGroup();
    }

    protected void releaseBodyGroup(final ObjectListTable olt, final ObjectListTable.ObjectListTableBodyGroup bodyGroup) {
        DOM.removeChild(olt.getElement(), bodyGroup.getElement());
    }

    protected void insert(final ObjectListTable olt, final ObjectListTable.ObjectListTableBodyGroup bodyGroup, final int index) {
        int insertIndex = index;
        if (olt.getRenderer() instanceof ObjectListTable.ColSpecRenderer) {
            insertIndex += olt.getColSpec().size();
        }
        if (olt.getThead() != null) insertIndex++;
        if (olt.getTfoot() != null) insertIndex++;
        DOM.insertChild(olt.getElement(), bodyGroup.getElement(), insertIndex);
    }

    /*
     * Replace uses of this with DOM.insertBefore once GWT provies one.
     */
    protected final native void insertBefore(Element parent, Element toAdd, Element before) /*-{
        parent.insertBefore(toAdd, before);
    }-*/;
}
