package org.mcarthur.sandy.gwt.table.client;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Base class for an HTML Table Cell.
 *
 * @author Sandy McArthur
 * @see <a href="http://www.w3.org/TR/html4/struct/tables.html#h-11.2.6">HTML Table Cell</a>
 */
abstract class TableCell extends SimplePanel {
    protected TableCell(final Element cellElement) {
        super(cellElement);
    }
}
