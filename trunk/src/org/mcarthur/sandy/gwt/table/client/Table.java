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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * TODO: Write JavaDoc
 *
 * @author Sandy McArthur
 */
public class Table implements EntryPoint {
    private static VerticalPanel vp = new VerticalPanel();

    private static ObjectListTable ot = new ObjectListTable(new OTM());
    private static int pCount = 0;

    public void onModuleLoad() {
        RootPanel.get("log").add(vp);


        RootPanel.get("tableDiv").add(ot);
        final List objects = ot.getObjects();

        objects.add(new Person("Sandy", 28));
        objects.add(new Person("Keebz", 25));
        objects.add(new Person("Bill", 33));
        objects.add(new Person("Ted", 55));


        final FlowPanel fp = new FlowPanel();
        final Button remove2 = new Button("Remove 2");
        remove2.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                final List two = new ArrayList();
                two.add(objects.get(objects.size() - 1));
                two.add(objects.get(0));
                objects.removeAll(two);
            }
        });
        fp.add(remove2);

        final Button addPerson = new Button("Add Person");
        addPerson.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                objects.add(new Person("Person " + (pCount++), (int)(Math.random() * 100)));
            }
        });
        fp.add(addPerson);

        final Button transpose = new Button("Transpose");
        transpose.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                final int a = (int)(Math.random() * objects.size());
                int b;
                do {
                    b = (int)(Math.random() * objects.size());
                } while (a == b);
                final Object oa = objects.get(a);
                final Object ob = objects.get(b);

                objects.set(b, oa);
                objects.set(a, ob);
            }
        });
        fp.add(transpose);

        RootPanel.get("buttons").add(fp);

    }

    private static class OTM implements ObjectListTable.Renderer {

        public void render(final Object obj, final TableBodyGroup rowGroup) {
            final Person person = (Person)obj;

            final TableRow tr = rowGroup.newTableRow();

            final TableCell nameCell = tr.newTableDataCell();
            final TextBox tb = new TextBox();
            tb.setText(person.getName());
            nameCell.add(tb);
            tr.add(nameCell);

            final TableCell ageCell = tr.newTableDataCell();
            //ageCell.add(new Label(Integer.toString(person.getAge())));
            ageCell.add(new AgeLabel(person.getAge()));
            tr.add(ageCell);

            final TableCell removeCell = tr.newTableDataCell();
            final Button button = new Button("Remove");
            button.addClickListener(new ClickListener() {
                public void onClick(final Widget sender) {
                    ot.getObjects().remove(person);
                }
            });
            removeCell.add(button);
            tr.add(removeCell);

            tr.addMouseListener(rml);
            rowGroup.add(tr);

            final TableRow tr2 = rowGroup.newTableRow();
            final TableCell tc2 = tr2.newTableDataCell();
            tc2.add(new Label("Somthing " + Math.random()));
            //tc2.add(makeMenuBar());
            tc2.setColSpan(3);

            tc2.setAxisSingle("summary");
            tr2.add(tc2);
            tr2.addMouseListener(rml);
            rowGroup.add(tr2);

            rowGroup.addMouseListener(rgml);
        }

        public void renderHeader(final TableHeaderGroup headerGroup) {
            render(headerGroup);
        }

        public void renderFooter(final TableFooterGroup footerGroup) {
            render(footerGroup);
        }

        private void render(final TableRowGroup rowGroup) {
            TableRow tr = rowGroup.newTableRow();

            TableHeaderCell th = tr.newTableHeaderCell();
            final MenuBar menu = new MenuBar();

            final MenuBar subMenu = new MenuBar(true);
            final MenuItem sortUp = new MenuItem("Sort Up", (Command)null);
            final MenuItem sortDown = new MenuItem("Sort Down", (Command)null);
            final MenuItem hide = new MenuItem("Hide Column", (Command)null);
            subMenu.addItem(sortUp);
            subMenu.addItem(sortDown);
            subMenu.addItem(hide);

            final MenuItem name = new MenuItem("Name", subMenu);
            menu.addItem(name);
            th.add(menu);
            tr.add(th);

            th = tr.newTableHeaderCell();
            th.add(new Label("Age"));
            tr.add(th);

            th = tr.newTableHeaderCell();
            th.add(new Label("Remove"));
            tr.add(th);

            rowGroup.add(tr);

            tr = rowGroup.newTableRow();

            th = tr.newTableHeaderCell();
            th.setColSpan(3);
            th.add(new Label("Something"));
            tr.add(th);

            rowGroup.add(tr);
        }

        private static RowGroupMouseListener rgml = new RowGroupMouseListener();

        private static class RowGroupMouseListener implements TableRowGroup.MouseListener {
            public void onMouseDown(final TableRowGroup rowGroup, final Event event) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void onMouseOver(final TableRowGroup rowGroup, final Event event) {
                rowGroup.addStyleName("mouseOver");
            }

            public void onMouseOut(final TableRowGroup rowGroup, final Event event) {
                rowGroup.removeStyleName("mouseOver");
            }

            public void onMouseMove(final TableRowGroup rowGroup, final Event event) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void onMouseUp(final TableRowGroup rowGroup, final Event event) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void onClick(final TableRowGroup rowGroup, final Event event) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void onDblClick(final TableRowGroup rowGroup, final Event event) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        }

        private final RowMouseListener rml = new RowMouseListener();

        private static class RowMouseListener implements TableRow.MouseListener {
            public void onMouseDown(final TableRow row, final Event event) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void onMouseOver(final TableRow row, final Event event) {
                row.addStyleName("mouseOver");
            }

            public void onMouseOut(final TableRow row, final Event event) {
                row.removeStyleName("mouseOver");
            }

            public void onMouseMove(final TableRow row, final Event event) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void onMouseUp(final TableRow row, final Event event) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void onClick(final TableRow row, final Event event) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void onDblClick(final TableRow row, final Event event) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        }
    }

    private static class AgeLabel extends Label {
        public AgeLabel(final int age) {
            super(Integer.toString(age));
        }

        protected void onDetach() {
            super.onDetach();
        }
    }

    private static class Person implements Comparable {
        private final String name;
        private final int age;


        public Person(final String name, final int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public int compareTo(final Object o) {
            final Person person = (Person)o;
            final int ageDiff = person.age - age;
            return ageDiff != 0 ? ageDiff : name.compareTo(person.name);
        }
    }

    public static MenuBar makeMenuBar() {
        final MenuBar subMenu = new MenuBar(true);
        subMenu.addItem("<code>Code</code>", true, new MyCommand());
        subMenu.addItem("<strike>Strikethrough</strike>", true, new MyCommand());
        subMenu.addItem("<u>Underlined</u>", true, new MyCommand());

        final MenuBar menu0 = new MenuBar(true);
        menu0.addItem("<b>Bold</b>", true, new MyCommand());
        menu0.addItem("<i>Italicized</i>", true, new MyCommand());
        menu0.addItem("More &#187;", true, subMenu);


        final MenuBar menu = new MenuBar(true);
        menu.addItem(new MenuItem("Style", menu0));

        return menu;
    }

    private static class MyCommand implements Command {
        public void execute() {
            Window.alert("command");
        }
    }

    public static void log(final String str) {
        if (vp == null) {
            vp = new VerticalPanel();
            RootPanel.get("log").add(vp);
        }
        vp.insert(new Label(new Date() + " " + str), 0);
    }
}
