package org.mcarthur.sandy.gwt.table.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;

/**
 * HTML Table Body Group, tbody.
 *
 * @author Sandy McArthur
 * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#h-11.2.3">HTML Row Group</a>
 */
class TableBodyGroup extends TableRowGroup {
    public TableBodyGroup() {
        super(DOM.createTBody());
    }
}
