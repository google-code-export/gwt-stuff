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
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.mcarthur.sandy.gwt.event.list.client.EventList;
import org.mcarthur.sandy.gwt.event.list.client.EventLists;
import org.mcarthur.sandy.gwt.event.list.client.FilteredEventList;
import org.mcarthur.sandy.gwt.event.list.client.SortedEventList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * TODO: Write JavaDoc
 *
 * @author Sandy McArthur
 */
public class TestTable implements EntryPoint {
    private static VerticalPanel vp = new VerticalPanel();

    private static ObjectListTable ot;
    private static SortedEventList sel;
    private static FilteredEventList fel;
    private static int pCount = 0;

    public void onModuleLoad() {
        EventList el;
        sel = EventLists.sortedEventList(); el = sel;
        fel = EventLists.filteredEventList(sel); el = fel;
        ot = new ObjectListTable(new OLTR(), el);
        //ot = new ObjectListTable(new OLTR(), EventLists.wrap(new ArrayList()));
        RootPanel.get("log").add(vp);


        final List objects = ot.getObjects();

        RootPanel.get("tableDiv").add(ot);

        objects.add(new Person("Sandy", 28));
        objects.add(0, new Person("Keebz", 25));
        objects.add(new Person("Bill", 33));
        objects.add(new Person("Ted", 55));

        if (false) {
            final List l = new ArrayList();
            l.add(objects.get(0));
            l.add(objects.get(1));
            l.add(objects.get(3));
            objects.retainAll(l);
        } else {
            objects.remove(1);
        }

        // TODO: when this is uncommented instead of above, the table rows fail to work in Opera.
        //RootPanel.get("tableDiv").add(ot);

        final FlowPanel fp = new FlowPanel();
        final Button remove2 = new Button("Remove 2");
        remove2.setTitle("Removes the first and last Person from the list.");
        remove2.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                if (objects.size() > 0) {
                    final List two = new ArrayList();
                    if (objects.size() > 1) two.add(objects.get(objects.size() - 1));
                    two.add(objects.get(0));
                    objects.removeAll(two);
                }
            }
        });
        fp.add(remove2);

        final Button addPerson = new Button("Add Person");
        addPerson.setTitle("Add a Person instance to the List.");
        addPerson.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                objects.add(new Person("Person " + (pCount++), (int)(Math.random() * 100)));
            }
        });
        fp.add(addPerson);

        if (sel == null) {
            final Button transpose = new Button("Transpose");
            transpose.setTitle("Switch two Person instances in the List");
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
        }

        final int instances = 500;
        final Button oneK = new Button("" + instances);
        oneK.setTitle("Add " + instances + " Person instances");
        oneK.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                final List l = new ArrayList();
                for (int i=0; i < instances; i++) {
                    objects.add(new Person("Person " + (pCount++), (int)(Math.random() * 100)));
                }
                final long start = System.currentTimeMillis();
                DeferredCommand.add(new Command() {
                    public void execute() {
                        final long end = System.currentTimeMillis();
                        vp.add(new Label("addAll took: " + (end - start)));
                        Window.setTitle("addAll took: " + (end - start));
                    }
                });
                objects.addAll(l);
            }
        });
        fp.add(oneK);

        if (fel != null) {
            final TextBox lower = new TextBox();
            lower.setWidth("3em");
            final TextBox upper = new TextBox();
            upper.setWidth("3em");
            final Button filterButton = new Button("Filter Ages");
            filterButton.addClickListener(new ClickListener() {
                public void onClick(final Widget sender) {
                    final String lowerText = lower.getText().trim();
                    final int l;
                    try {
                        l = Integer.parseInt(lowerText.length() > 0 ? lowerText : "0");
                    } catch (NumberFormatException nfe) {
                        Window.alert("Lower age bound must be empty or a number.");
                        return;
                    }
                    final String upperText = upper.getText().trim();
                    final int u;
                    try {
                        u = Integer.parseInt(upperText.length() > 0 ? upperText : "999999");
                    } catch (NumberFormatException nfe) {
                        Window.alert("Upper age bound must be empty or a number.");
                        return;
                    }
                    fel.setFilter(new FilteredEventList.Filter() {
                        public boolean accept(final Object element) {
                            final Person person = (Person)element;
                            return l < person.getAge() && person.getAge() < u;
                        }
                    });
                }
            });
            lower.addKeyboardListener(new KeyboardListenerAdapter(){
                public void onKeyUp(final Widget sender, final char keyCode, final int modifiers) {
                    super.onKeyUp(sender, keyCode, modifiers);
                    if (KeyboardListener.KEY_ENTER == keyCode) {
                        filterButton.click();
                    }
                }
            });
            upper.addKeyboardListener(new KeyboardListenerAdapter(){
                public void onKeyUp(final Widget sender, final char keyCode, final int modifiers) {
                    super.onKeyUp(sender, keyCode, modifiers);
                    if (KeyboardListener.KEY_ENTER == keyCode) {
                        filterButton.click();
                    }
                }
            });
            final HorizontalPanel hp = new HorizontalPanel();
            hp.add(new Label("From:"));
            hp.add(lower);
            hp.add(new Label("to:"));
            hp.add(upper);
            hp.add(filterButton);
            fp.add(hp);
        }

        RootPanel.get("buttons").add(fp);

    }

    private static class OLTR implements ObjectListTable.Renderer {

        public void render(final Object obj, final TableBodyGroup rowGroup) {
            final Person person = (Person)obj;

            final TableRow tr = rowGroup.newTableRow();

            final TableCell a = tr.newTableDataCell();
            final CheckBox checkBox = new CheckBox();
            checkBox.setEnabled(false);
            checkBox.setTitle("Doesn't do anything.");
            a.add(checkBox);
            a.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
            a.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
            a.setRowSpan(2);
            tr.add(a);

            final TableCell nameCell = tr.newTableDataCell();
            final TextBox tb = new TextBox();
            tb.addChangeListener(new ChangeListener() {
                public void onChange(final Widget sender) {
                    final TextBox tb = (TextBox)sender;
                    // FIXME: BUG: Rows that are added before the table is attached, don't fire events.
                    //Window.setTitle(tb.getText());
                    person.setName(tb.getText());
                    if (sel != null) {
                        sel.sort();
                    }
                }
            });
            tb.addKeyboardListener(new KeyboardListenerAdapter() {
                public void onKeyUp(final Widget sender, final char keyCode, final int modifiers) {
                    super.onKeyUp(sender, keyCode, modifiers);
                    if (KeyboardListener.KEY_ENTER == keyCode) {
                        final TextBox tb = (TextBox)sender;
                        tb.setFocus(false);
                    }
                }
            });
            tb.setText(person.getName());
            nameCell.add(tb);
            tr.add(nameCell);

            final TableCell ageCell = tr.newTableDataCell();
            //ageCell.add(new Label(Integer.toString(person.getAge())));
            ageCell.add(new AgeLabel(person.getAge()));
            ageCell.setWidth("5em");
            ageCell.setAlignment(HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
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
            tc2.add(new Label("Something " + Math.random()));
            //tc2.add(makeMenuBar());
            tc2.setColSpan(3);

            tc2.setAxis("summary");
            tr2.add(tc2);
            tr2.addMouseListener(rml);
            rowGroup.add(tr2);

            rowGroup.addMouseListener(rgml);
        }

        public void renderHeader(final TableHeaderGroup headerGroup) {
            renderHeaderAndFooter(headerGroup);
        }

        public void renderFooter(final TableFooterGroup footerGroup) {
            renderHeaderAndFooter(footerGroup);
        }

        private void renderHeaderAndFooter(final TableRowGroup rowGroup) {
            TableRow tr = rowGroup.newTableRow();

            TableHeaderCell th = tr.newTableHeaderCell();
            th.add(new Label("rowSpan=2", true));
            th.setRowSpan(2);
            tr.add(th);
            {
                th = tr.newTableHeaderCell();
                final MenuBar nameMenu = new MenuBar();

                final MenuBar nameSubMenu = new MenuBar(true);
                final MenuItem nameSortUp = new MenuItem("Sort Up", new Command() {
                    Comparator c = new Comparator() {
                        public int compare(final Object o1, final Object o2) {
                            final Person p1 = (Person)o1;
                            final Person p2 = (Person)o2;
                            return p1.getName().compareTo(p2.getName());
                        }
                    };
                    public void execute() {
                        if (sel != null) {
                            sel.setComparator(c);
                        }
                    }
                });
                final MenuItem nameSortDown = new MenuItem("Sort Down", new Command() {
                    Comparator c = new Comparator() {
                        public int compare(final Object o1, final Object o2) {
                            final Person p1 = (Person)o1;
                            final Person p2 = (Person)o2;
                            return p2.getName().compareTo(p1.getName());
                        }
                    };
                    public void execute() {
                        if (sel != null) {
                            sel.setComparator(c);
                        }
                    }
                });
                nameSubMenu.addItem(nameSortUp);
                nameSubMenu.addItem(nameSortDown);

                final MenuItem nameMenuItem = new MenuItem("Name", nameSubMenu);
                nameMenu.addItem(nameMenuItem);
                th.add(nameMenu);
                tr.add(th);
            }

            {
                th = tr.newTableHeaderCell();
                final MenuBar ageMenu = new MenuBar();

                final MenuBar ageSubMenu = new MenuBar(true);
                final MenuItem ageSortUp = new MenuItem("Sort Up", new Command() {
                    Comparator c = new Comparator() {
                        public int compare(final Object o1, final Object o2) {
                            final Person p1 = (Person)o1;
                            final Person p2 = (Person)o2;
                            return p1.getAge() - p2.getAge();
                        }
                    };
                    public void execute() {
                        if (sel != null) {
                            sel.setComparator(c);
                        }
                    }
                });
                final MenuItem ageSortDown = new MenuItem("Sort Down", new Command() {
                    Comparator c = new Comparator() {
                        public int compare(final Object o1, final Object o2) {
                            final Person p1 = (Person)o1;
                            final Person p2 = (Person)o2;
                            return p2.getAge() - p1.getAge();
                        }
                    };
                    public void execute() {
                        if (sel != null) {
                            sel.setComparator(c);
                        }
                    }
                });
                ageSubMenu.addItem(ageSortUp);
                ageSubMenu.addItem(ageSortDown);

                final MenuItem ageMenuItem = new MenuItem("Age", ageSubMenu);
                ageMenu.addItem(ageMenuItem);
                th.add(ageMenu);
                tr.add(th);
            }
            th = tr.newTableHeaderCell();
            th.add(new Label("Remove"));
            tr.add(th);

            rowGroup.add(tr);

            tr = rowGroup.newTableRow();

            th = tr.newTableHeaderCell();
            th.setColSpan(3);
            th.add(new Label("Random Number"));
            th.setTitle("ColSpan=3");
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
        private final int age;
        public AgeLabel(final int age) {
            super(Integer.toString(age));
            this.age = age;
        }

        protected void onAttach() {
            super.onAttach();
        }

        protected void onDetach() {
            super.onDetach();
        }

        public void removeFromParent() {
            super.removeFromParent();
        }
    }

    private static class Person implements Comparable {
        private String name;
        private final int age;


        public Person(final String name, final int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
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
