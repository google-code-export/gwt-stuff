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

/**
 * Safari specific behaviors.
 *
 * @author Sandy McArthur
 */
class ObjectListTableImplSafari extends ObjectListTableImpl {

    /*
     * Safari has a bug such that tbody tags are rendered on top of each other when you insert
     * a tbody as the first tbody child element in the table element. The first fix I found was
     * to add an element (I choose an empty caption) and remove it which I guess triggers a
     * table re-layout.
     *
     * This may not be needed if the inserted element isn't the first one? Need to do more testing.
     */
    private final Element CAPTION = DOM.createElement("caption");
    void add(final ObjectListTable olt, final ObjectListTable.ObjectListTableRowGroup rowGroup, final ObjectListTable.ObjectListTableRowGroup beforeGroup, final int beforeIndex) {
        super.add(olt, rowGroup, beforeGroup, beforeIndex);

        // force table re-layout
        // TODO: Use DeferredCommand to add/remove once at the end of updates.
        DOM.appendChild(olt.getElement(), CAPTION);
        DOM.removeChild(olt.getElement(), CAPTION);
    }
}