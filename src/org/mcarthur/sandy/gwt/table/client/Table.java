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
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;

import java.util.Date;
import java.util.List;

/**
 * TODO: Write JavaDoc
 *
 * @author Sandy McArthur
 */
public class Table implements EntryPoint {
    private static VerticalPanel vp = new VerticalPanel();

    private FooTable ft;
    private ObjectTable ot = new ObjectTable(new OTM());

    public void onModuleLoad() {
        RootPanel.get("log").add(vp);


        RootPanel.get("tableDiv").add(ot);
        final List objects = ot.getObjects();

        objects.add(new Person("Sandy", 28));
        objects.add(new Person("Keebz", 25));
        objects.add(new Person("Bill", 33));
        objects.add(new Person("Ted", 55));
    }

    private static class OTM implements ObjectTable.ObjectTableModel {

        public void render(final Object obj, final TableRowGroup rowGroup) {
            final Person person = (Person)obj;

            final TableRow tr = rowGroup.newTableRow();

            final TableCell nameCell = tr.newTableDataCell();
            TextBox tb = new TextBox();
            tb.setText(person.getName());
            nameCell.add(tb);
            tr.add(nameCell);

            final TableCell ageCell = tr.newTableDataCell();
            ageCell.add(new Label(Integer.toString(person.getAge())));
            tr.add(ageCell);

            rowGroup.add(tr);

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

        public int compareTo(Object o) {
            Person person = (Person)o;
            final int ageDiff = person.age - age;
            return ageDiff != 0 ? ageDiff : name.compareTo(person.name);
        }
    }

    public static MenuBar makeMenuBar() {
        MenuBar subMenu = new MenuBar(true);
        subMenu.addItem("<code>Code</code>", true, new MyCommand());
        subMenu.addItem("<strike>Strikethrough</strike>", true, new MyCommand());
        subMenu.addItem("<u>Underlined</u>", true, new MyCommand());

        MenuBar menu0 = new MenuBar(true);
        menu0.addItem("<b>Bold</b>", true, new MyCommand());
        menu0.addItem("<i>Italicized</i>", true, new MyCommand());
        menu0.addItem("More &#187;", true, subMenu);


        MenuBar menu = new MenuBar(true);
        menu.addItem(new MenuItem("Style",menu0));

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
