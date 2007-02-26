package org.mcarthur.sandy.gwt.table.client;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.RootPanel;
import org.mcarthur.sandy.gwt.event.list.client.EventList;
import org.mcarthur.sandy.gwt.event.list.client.EventLists;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link org.mcarthur.sandy.gwt.table.client.ObjectListTable}.
 */
public class ObjectListTableAttachRendererTest extends GWTTestCase {
    public String getModuleName() {
        return "org.mcarthur.sandy.gwt.table.Table";
    }

    private EventList people = EventLists.eventList();

    protected void setUp() throws Exception {
        super.setUp();
        events = new ArrayList();
        people.add(new Person("One", 10));
        //people.add(new Person("Two", 20));
        //people.add(new Person("Three", 30));
    }

    protected void tearDown() throws Exception {
        people.clear();
        events.clear(); events = null;
        super.tearDown();
    }

    public void testCallbacksCalledWhenAppropriate() {
        final List expected = new ArrayList();
        final ObjectListTable olt;

        // Initial items are rendered
        olt = new ObjectListTable(new AttachPersonRenderer(), people);
        expected.add("render");
        assertEquals(expected, events);

        //atcching to browser's document causes the headers/footers to be rendered and rows to be attached
        RootPanel.get().add(olt);
        expected.add("renderHeader");
        expected.add("renderFooter");
        expected.add("onAttachHeader");
        expected.add("onAttach");
        expected.add("onAttachFooter");
        assertEquals(expected, events);

        // adding a element will rendered and attached if the table is attached
        people.add(new Person("Bob", 123));
        expected.add("render");
        expected.add("onAttach");
        assertEquals(expected, events);

        // removing a person should cause a detch
        people.remove(0);
        expected.add("onDetach");
        


        // detaching the table causes headers, rows and footer to be detached.
        RootPanel.get().remove(olt);
        expected.add("onDetachHeader");
        expected.add("onDetach");
        expected.add("onDetachFooter");
        assertEquals(expected, events);

        // should just render
        people.add(new Person("Joe", 321));
        expected.add("render");
        assertEquals(expected, events);

        // removing while detached shouldn't affect anything
        people.remove(0);
        assertEquals(expected, events);
    }

    private List events = new ArrayList();

    class AttachPersonRenderer extends PersonRenderer implements ObjectListTable.AttachRenderer {

        public void onAttach(Object obj, TableBodyGroup rowGroup) {
            events.add("onAttach");
        }

        public void onAttach(TableHeaderGroup rowGroup) {
            events.add("onAttachHeader");
        }

        public void onAttach(TableFooterGroup rowGroup) {
            events.add("onAttachFooter");
        }

        public void onDetach(Object obj, TableBodyGroup rowGroup) {
            events.add("onDetach");
        }

        public void onDetach(TableHeaderGroup rowGroup) {
            events.add("onDetachHeader");
        }

        public void onDetach(TableFooterGroup rowGroup) {
            events.add("onDetachFooter");
        }
    }
    class PersonRenderer implements ObjectListTable.Renderer {

        public void render(Object obj, TableBodyGroup rowGroup) {
            events.add("render");
        }

        public void renderHeader(TableHeaderGroup headerGroup) {
            events.add("renderHeader");
        }

        public void renderFooter(TableFooterGroup footerGroup) {
            events.add("renderFooter");
        }
    }

    static class Person implements Comparable /*, PropertyChangeSource*/ {
        private String name;
        private final int age;

        //private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

        public Person(final String name, final int age) {
            setName(name);
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            final Object old = this.name;
            this.name = name;
            //pcs.firePropertyChange("name", old, name);
        }

        public int getAge() {
            return age;
        }

        public int compareTo(final Object o) {
            final Person person = (Person)o;
            final int ageDiff = person.age - age;
            return ageDiff != 0 ? ageDiff : name.compareTo(person.name);
        }

        public String toString() {
            return name + "{" + age + '}';
        }

        /*
        public void addPropertyChangeListener(final PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(listener);
        }

        public void removePropertyChangeListener(final PropertyChangeListener listener) {
            pcs.removePropertyChangeListener(listener);
        }
        */
    }

}
