package org.mcarthur.sandy.gwt.table.client;

import com.google.gwt.user.client.DOM;

/**
 * Table Header Row Group, thead.
 *
 * @author Sandy McArthur
 * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#h-11.2.3">HTML Row Group</a>
 */
class TableHeaderGroup extends TableRowGroup {
    public TableHeaderGroup() {
        super(DOM.createElement("thead"));
    }
}
