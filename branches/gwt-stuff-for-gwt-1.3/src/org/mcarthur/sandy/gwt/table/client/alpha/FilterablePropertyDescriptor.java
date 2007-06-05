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

import org.mcarthur.sandy.gwt.event.list.client.FilteredEventList;

import java.util.Collection;

/**
 * TODO: Write Javadoc
*
* @author Sandy McArthur
*/
public interface FilterablePropertyDescriptor extends PropertyDescriptor {
    /**
     * Collection of possible filters.
     */
    public Collection/*<FilterDescriptor>*/ getFilters();

    public interface FilterDescriptor {

        public String getName();
        public String getDisplayName();
        //?: public String getShortDescription();
        public FilteredEventList.Filter getFilter();
    }
}
