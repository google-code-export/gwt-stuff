package org.mcarthur.sandy.gwt.table.client;

import com.google.gwt.user.client.DOM;

/**
 * Table Footer Row Group, tfoot.
 *
 * @author Sandy McArthur
 * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#h-11.2.3">HTML Row Group</a>
*/
class TableFooterGroup extends TableRowGroup {
    public TableFooterGroup() {
        super(DOM.createElement("tfoot"));
    }
}
