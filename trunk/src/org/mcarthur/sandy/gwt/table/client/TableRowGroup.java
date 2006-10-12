package org.mcarthur.sandy.gwt.table.client;

import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.DOM;

import java.util.List;
import java.util.ArrayList;

/**
 * Base class for a HTML Row Group.
 *
 * @author Sandy McArthur
 * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#h-11.2.3">HTML Row Group</a>
 */
abstract class TableRowGroup extends UIObject {
    private final List rows = new ArrayList();

    public TableRowGroup(final Element element) {
        setElement(element);
    }

    public void add(final TableRow row) {
        rows.add(row);
        DOM.appendChild(getElement(), row.getElement());
    }

    public List getRows() {
        return rows;
    }
}
