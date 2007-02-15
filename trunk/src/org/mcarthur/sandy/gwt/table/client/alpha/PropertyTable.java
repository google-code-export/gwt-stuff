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

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.mcarthur.sandy.gwt.table.client.ObjectListTable;
import org.mcarthur.sandy.gwt.table.client.TableBodyGroup;
import org.mcarthur.sandy.gwt.table.client.TableFooterGroup;
import org.mcarthur.sandy.gwt.table.client.TableHeaderGroup;

/**
 * TODO: Write Javadoc
 *
 * @author Sandy McArthur
 */
public class PropertyTable extends Composite {
    private final VerticalPanel vp = new VerticalPanel();
    private final HorizontalPanel hp = new HorizontalPanel();
    private final ObjectListTable olt;

    private final PropertyTableInfo pti;

    public PropertyTable(final PropertyTableInfo pti) {
        initWidget(vp);

        this.pti = pti;
        olt = new ObjectListTable(new PropertyRenderer());

        vp.add(hp);
        vp.add(olt);
    }


    private class PropertyRenderer implements ObjectListTable.Renderer {
        public void render(final Object obj, final TableBodyGroup rowGroup) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void renderHeader(final TableHeaderGroup headerGroup) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void renderFooter(final TableFooterGroup footerGroup) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
