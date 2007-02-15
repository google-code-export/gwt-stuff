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

package org.mcarthur.sandy.gwt.table.client.alpha;

import org.mcarthur.sandy.gwt.event.list.client.EventList;

import java.util.Collection;

/**
 * TODO: Write Javadoc
*
* @author Sandy McArthur
*/
public interface PropertyTableInfo {

    public EventList getElements();

    /**
     * Collection of {@link PropertyDescriptor}s
     */
    public Collection/*<PropertyDescriptor>*/ getPropertyDescriptors();

    /**
     * How the row group cells are laid out.
     * A space seperated list of PropertyDescriptor.getName().
     * '|' for next table row
     * '-' for colspan="2", '-#' for colspan="#"
     * '+' for rowspan="2", '+#' for rowspan="#"
     * '*' for colspan adjusted for max row columns
     * '?' for empty td
     */
    public String getRowGroupSpec();
}
