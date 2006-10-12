package org.mcarthur.sandy.gwt.table.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetCollection;

import java.util.Iterator;

/**
 * Base class for an HTML Table Row.
 *
 * @author Sandy McArthur
 * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#h-11.2.5">HTML Table Row</a>
*/
abstract class TableRow extends UIObject implements HasWidgets {
    private final Element tr = DOM.createTR();
    //private final List cells = new ArrayList();
    private final WidgetCollection cells = new WidgetCollection(this);

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

    /**
     * Delegate this to {@link Panel#adopt(Widget, Element)}.
     * @see com.google.gwt.user.client.ui.Panel#adopt(Widget, Element)
     */
    protected abstract void adopt(Widget w, Element container);


    public void add(Widget w) {
        if (w instanceof TableCell) {
            add((TableCell)w);
        } else {
            throw new IllegalArgumentException("Only TableCell widgets allowed.");
        }
    }

    public void clear() {
        final Iterator iter = cells.iterator();
        while (iter.hasNext()) {
            iter.next();
            iter.remove();
        }
    }

    public Iterator iterator() {
        return cells.iterator();
    }

    public boolean remove(Widget w) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    //public List getCells() {
    //    return cells;
    //}

    public String toString() {
        return "TableRow";
    }
}
