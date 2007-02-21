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

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.mcarthur.sandy.gwt.table.client.ObjectListTable;
import org.mcarthur.sandy.gwt.table.client.TableBodyGroup;
import org.mcarthur.sandy.gwt.table.client.TableCell;
import org.mcarthur.sandy.gwt.table.client.TableFooterGroup;
import org.mcarthur.sandy.gwt.table.client.TableHeaderCell;
import org.mcarthur.sandy.gwt.table.client.TableHeaderGroup;
import org.mcarthur.sandy.gwt.table.client.TableRow;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * TODO: Write Javadoc
 *
 * @author Sandy McArthur
 */
public class PropertyTable extends Composite {
    private final VerticalPanel vp = new VerticalPanel();
    private final HorizontalPanel hp = new HorizontalPanel();
    private final ObjectListTable olt;

    private final PropertyTableModel ptm;

    public PropertyTable(final PropertyTableModel ptm) {
        initWidget(vp);

        this.ptm = ptm;
        olt = new ObjectListTable(new PropertyRenderer(), ptm.getElements());

        vp.add(hp);
        vp.add(olt);
    }


    private class PropertyRenderer implements ObjectListTable.Renderer {
        private final Map propertyDescriptorsMap;

        public PropertyRenderer() {
            propertyDescriptorsMap = new HashMap();
            for (Iterator i = ptm.getPropertyDescriptors().iterator(); i.hasNext();) {
                final PropertyDescriptor pd = (PropertyDescriptor)i.next();
                propertyDescriptorsMap.put(pd.getName(), pd);
            }
        }

        public void render(final Object obj, final TableBodyGroup rowGroup) {
            final String[] rgs = ptm.getRowGroupSpec().split(" ");

            TableRow tr = rowGroup.newTableRow();
            for (int i=0; i < rgs.length; i++) {
                final PropertyDescriptor pd = (PropertyDescriptor)propertyDescriptorsMap.get(rgs[i]);
                final TableCell tc = tr.newTableDataCell();
                tc.add(pd.createWidget(obj));
                tr.add(tc);
            }
            rowGroup.add(tr);
        }

        public void renderHeader(final TableHeaderGroup headerGroup) {
            final String[] rgs = ptm.getRowGroupSpec().split(" ");

            TableRow tr = headerGroup.newTableRow();
            for (int i=0; i < rgs.length; i++) {
                final PropertyDescriptor pd = (PropertyDescriptor)propertyDescriptorsMap.get(rgs[i]);
                final TableHeaderCell thc = tr.newTableHeaderCell();

                final MenuBar colMenu = new MenuBar(true);
                if (pd instanceof SortablePropertyDescriptor) {
                    // TODO: sort menu items
                }
                if (pd instanceof FilterablePropertyDescriptor) {
                    // TODO: filter menu items
                }

                colMenu.addItem("Hide Column", new Command() {
                    public void execute() {
                        Window.alert("Hide not yet supported.");
                    }
                });

                final MenuBar menu = new MenuBar();
                menu.addItem(pd.getDisplayName(), colMenu);
                thc.add(menu);
                tr.add(thc);
            }
            headerGroup.add(tr);
        }

        public void renderFooter(final TableFooterGroup footerGroup) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
