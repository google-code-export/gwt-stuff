package org.mcarthur.sandy.gwt.table.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Label;

/**
 * TODO: Write Class JavaDoc
*
* @author Sandy McArthur
*/
class TableDataCell extends TableCell {
    private static int i = 0;
    public TableDataCell() {
        super(DOM.createTD());
        setWidget(new Label("TableDataCell " + (i++)));
    }
    public String toString() {
        return "TableDataCell";
    }
}
