package org.mcarthur.sandy.gwt.table.client;

import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.*;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import org.mcarthur.sandy.gwt.event.list.client.EventList;
import org.mcarthur.sandy.gwt.event.list.client.EventLists;
import org.mcarthur.sandy.gwt.event.list.client.ListEventListener;
import org.mcarthur.sandy.gwt.event.list.client.ListEvent;

/**
 * TODO: Write JavaDoc
 *
 * @author Sandy McArthur
 */
public class FooTable extends Panel {

    /**
     * Table element.
     */
    private final Element tableElem;
    private final WidgetCollection widgets = new WidgetCollection(this);

    private TableHeaderGroup thead;
    private TableFooterGroup tfoot;
    private List tbodies = new ArrayList();

    private final EventList columns = EventLists.wrap(new ArrayList());
    private final EventList objects = EventLists.wrap(new ArrayList());
    private Renderer renderer;

    private final TableBodyGroup tb = new TableBodyGroup();
    private final TableRow tr = new MyTableRow();
    private final TableCell tc1 = new TableDataCell();
    private final TableCell tc2 = new TableHeaderCell();
    private final TableCell tc3 = new TableDataCell();

    public FooTable() {
        tableElem = DOM.createTable();
        setElement(tableElem);

        MenuBar menu = Table.makeMenuBar();

        TableCell th = new TableHeaderCell();
        th.setWidget(menu);

        columns.add("Foo");
        columns.add("Bar");
        final TableHeaderGroup thead = new HeaderRowGroup(columns);
        setTHead(thead);

        DOM.appendChild(tableElem, tb.getElement());

        tb.add(tr);

        tr.add(tc1);
        tr.add(tc2);
        tr.add(tc3);
    }

    private class HeaderRowGroup extends TableHeaderGroup implements ListEventListener {
        private final EventList columns;
        private TableRow row = new HeaderTableRow();

        public HeaderRowGroup(final EventList columns) {
            this.columns = columns;
            add(row);
            columns.addListEventListener(this);
            init();
        }

        private void init() {
            final Iterator iter = columns.iterator();
            while (iter.hasNext()) {
                final String col = (String)iter.next();
                final TableHeaderCell th = new TableHeaderCell();
                th.setWidget(createMenu(col));
                row.add(th);
            }
        }

        private MenuBar createMenu(final String name) {
            final MenuBar submenu = new MenuBar(true);
            submenu.addItem("Sort Up", new Command() {
                public void execute() {
                    Window.alert("Sort Up");
                }
            });

            submenu.addItem("Sort Down", new Command() {
                public void execute() {
                    Window.alert("Sort Down");
                }
            });
            
            final MenuItem item = new MenuItem(name, submenu);
            final MenuBar bar = new MenuBar();
            bar.addItem(item);
            return bar;
        }

        public void listChanged(final ListEvent listEvent) {
            // TODO Handle events
        }

        private class HeaderTableRow extends TableRow {
            protected void adopt(final Widget w, final Element container) {
                FooTable.this.adopt(w, container);
            }
        }

    }

    public interface Renderer {
        public Widget render(Object obj, String property);
    }

    private void setTHead(final TableHeaderGroup thead) {
        if (this.thead != null) {
            throw new IllegalStateException("Tables can only have one Header at a time.");
        }
        this.thead = thead;
        DOM.appendChild(getElement(), thead.getElement());
        Iterator rowIter = thead.getRows().iterator();
        while (rowIter.hasNext()) {
            TableRow row = (TableRow)rowIter.next();
            Iterator cellIter = row.iterator();
            while (cellIter.hasNext()) {
                TableCell cell = (TableCell)cellIter.next();
                widgets.add(cell);
            }
        }
    }

    public void onBrowserEvent(final Event event) {
        super.onBrowserEvent(event);
        log(event);
    }

    private UIObject find(final UIObject obj, final Element e) {
        if (!DOM.isOrHasChild(obj.getElement(), e)) {
            return null;
        }
        UIObject ui = null;
        if (obj instanceof TableBodyGroup) {
            TableBodyGroup tableBodyGroup = (TableBodyGroup)obj;
            List rows = tableBodyGroup.getRows();
            Iterator iter = rows.iterator();
            UIObject ui2 = null;
            while (iter.hasNext()) {
                TableRow row = (TableRow)iter.next();
                ui2 = find(row, e);
                if (ui2 != null) {
                    ui = ui2;
                }
            }
            return ui;

        } else if (obj instanceof TableRow) {
            TableRow tableRow = (TableRow)obj;
            Iterator iter = tableRow.iterator();
            UIObject ui2 = null;
            while (iter.hasNext()) {
                TableCell cell = (TableCell)iter.next();
                ui2 = find(cell, e);
                if (ui2 != null) {
                    ui = ui2;
                }
            }
            return ui;

        } else if (obj instanceof TableCell) {
            TableCell tableCell = (TableCell)obj;
            if (DOM.isOrHasChild(tableCell.getElement(), e)) {
                return tableCell;
            } else {
                return null;
            }
        } else {
            throw new IllegalArgumentException("dunnno what obj is.");
        }
    }

    private void log(final Event event) {
        Element e = DOM.eventGetTarget(event);
        UIObject item = find(tb, e);
        switch (DOM.eventGetType(event)) {
            case Event.ONCLICK: {
                if (item != null)
                    Table.log("ONCLICK: " + item);
                break;
            }

            case Event.ONMOUSEOVER: {
                if (item != null)
                    Table.log("ONMOUSEOVER: " + item);
                break;
            }

            case Event.ONMOUSEOUT: {
                if (item != null)
                    Table.log("ONMOUSEOUT: " + item);
                break;
            }
        }
        DOM.eventCancelBubble(event, true);
    }

    /**
     * This method is called when the widget becomes attached to the browser's
     * document.
     */
    protected void onLoad() {
        super.onLoad();
        //DOM.appendChild(tableElem, tableHead);
        //Element tr = DOM.createTR();
        //DOM.appendChild(tableHead, tr);
        //
        //Element th = DOM.createTH();
        //DOM.appendChild(tr, th);
        //
        //DOM.setInnerText(th, "Header");
    }

    /**
     * Gets an iterator for the contained widgets. This iterator is required to
     * implement {@link java.util.Iterator#remove()}.
     */
    public Iterator iterator() {
        return widgets.iterator();
    }
    
    /**
     * Removes a child widget.
     *
     * @param w the widget to be removed
     * @return <code>true</code> if the widget was present
     */
    public boolean remove(Widget w) {
        List widgets = new ArrayList();
        widgets.add(tc1.getWidget());
        widgets.add(tc2.getWidget());
        widgets.add(tc3.getWidget());
        return widgets.remove(w);
    }

    private class MyTableRow extends TableRow {
        protected void adopt(final Widget w, final Element container) {
            FooTable.this.adopt(w, container);
        }
    }
}
