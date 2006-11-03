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

import java.util.List;

/**
 * TODO: Write JavaDoc
 *
 * @author Sandy McArthur
 */
public interface TableModel {
    /**
     *
     * @return a list of Strings that are the property names.
     */
    public List getProperties();

    public List getItems();

    public Object getProperty(Object item, String property);
    
}
