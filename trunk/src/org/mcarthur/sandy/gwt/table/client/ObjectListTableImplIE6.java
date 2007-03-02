package org.mcarthur.sandy.gwt.table.client;

import com.google.gwt.user.client.DOM;

/**
 * IE specific behaviors.
 */
class ObjectListTableImplIE6 extends ObjectListTableImpl {

    void add(final ObjectListTable olt, final ObjectListTable.ObjectListTableBodyGroup rowGroup, final ObjectListTable.ObjectListTableBodyGroup beforeGroup, final int beforeIndex) {
        assert beforeIndex >= 0;
        olt.getTbodies().add(beforeIndex, rowGroup);
        int index = beforeIndex;
        if (olt.getThead() != null) index += 1;
        if (olt.getTfoot() != null) index += 1;
        DOM.insertChild(olt.getElement(), rowGroup.getElement(), index);
    }
}
