package org.mcarthur.sandy.gwt.table.client;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

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

    //private final Element tableHead;

    private final TableBody tb = new TableBody();
    private final TableRow tr = new TableRow();
    private final TableCell tc1 = new TD();
    private final TableCell tc2 = new TH();
    private final TableCell tc3 = new TD();
    public FooTable() {
        tableElem = DOM.createTable();
        //tableHead = DOM.createElement("thead");
        setElement(tableElem);

        DOM.setAttribute(tableElem, "border", "1");
        DOM.setAttribute(tableElem, "cellSpacing", "2");
        DOM.setAttribute(tableElem, "cellPadding", "3");

        DOM.appendChild(tableElem, tb.getElement());

        tb.add(tr);

        tr.add(tc1);
        tr.add(tc2);
        tr.add(tc3);
    }

    private static class TableBody extends UIObject {
        private final Element tbody = DOM.createTBody();
        private final List rows = new ArrayList();

        public TableBody() {
            setElement(tbody);
            sinkEvents(Event.ONCLICK | Event.ONMOUSEOVER | Event.ONMOUSEOUT);
            DOM.setStyleAttribute(getElement(), "border", "solid red thin");
            DOM.setStyleAttribute(getElement(), "padding", "3px");
            DOM.setStyleAttribute(getElement(), "margin", "3px");
        }

        public void add(final TableRow row) {
            rows.add(row);
            DOM.appendChild(tbody, row.getElement());
        }

        public List getRows() {
            return rows;
        }

        public String toString() {
            return "TableBody";
        }
    }

    private class TableRow extends UIObject {
        private final Element tr = DOM.createTR();
        private final List cells = new ArrayList();

        public TableRow() {
            setElement(tr);
            sinkEvents(Event.ONCLICK | Event.ONMOUSEOVER | Event.ONMOUSEOUT);
            DOM.setStyleAttribute(getElement(), "border", "solid green thin");
            DOM.setStyleAttribute(getElement(), "padding", "3px");
            DOM.setStyleAttribute(getElement(), "margin", "3px");
        }

        public void add(final TableCell cell) {
            cells.add(cell);
            //DOM.appendChild(tr, cell.getElement());
            adopt(cell, tr);
        }

        public List getCells() {
            return cells;
        }

        public String toString() {
            return "TableRow";
        }
    }

    private static class TableCell extends SimplePanel {
        private final Element cell;

        protected TableCell(final Element cell) {
            super(cell);
            this.cell = cell;
            sinkEvents(Event.ONCLICK | Event.ONMOUSEOVER | Event.ONMOUSEOUT);
            DOM.setStyleAttribute(getElement(), "border", "solid blue thin");
            DOM.setStyleAttribute(getElement(), "padding", "3px");
            DOM.setStyleAttribute(getElement(), "margin", "3px");
        }
        public String toString() {
            return "TableCell";
        }
    }

    private static class TD extends TableCell {
        private static int i = 0;
        public TD() {
            super(DOM.createTD());
            setWidget(new Label("TD " + (i++)));
        }
        public String toString() {
            return "TD";
        }
    }

    private static class TH extends TableCell {
        private static int i = 0;
        public TH() {
            super(DOM.createTH());
            setWidget(new Label("TH " + (i++)));
        }
        public String toString() {
            return "TH";
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
        if (obj instanceof TableBody) {
            TableBody tableBody = (TableBody)obj;
            List rows = tableBody.getRows();
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
            List cells = tableRow.getCells();
            Iterator iter = cells.iterator();
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
        return null;
    }
    
    /**
     * Removes a child widget.
     *
     * @param w the widget to be removed
     * @return <code>true</code> if the widget was present
     */
    public boolean remove(Widget w) {
        return false;
    }
}
