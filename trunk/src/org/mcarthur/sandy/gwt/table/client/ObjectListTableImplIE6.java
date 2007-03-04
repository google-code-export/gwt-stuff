package org.mcarthur.sandy.gwt.table.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.UIObject;

import java.util.ArrayList;
import java.util.List;

/**
 * IE specific behaviors.
 */
class ObjectListTableImplIE6 extends ObjectListTableImpl {

    private final Element tbe = DOM.createElement("tbody");
    private final List unusedTbodies = new ArrayList();

    /**
     * Called in the ObjectListTable's constructor.
     *
     * @param olt the ObjectListTable in the midst of construction.
     */
    protected void init(final ObjectListTable olt) {
        super.init(olt);
        //Create an end tbody we can use for insertBefore
        UIObject.setVisible(tbe, false);
        DOM.appendChild(olt.getElement(), tbe);
    }

    protected ObjectListTable.ObjectListTableBodyGroup takeBodyGroup(final ObjectListTable olt) {
        if (unusedTbodies.isEmpty()) {
            return super.takeBodyGroup(olt);
        } else {
            final ObjectListTable.ObjectListTableBodyGroup bodyGroup =
                    (ObjectListTable.ObjectListTableBodyGroup)unusedTbodies.remove(0);
            assert bodyGroup.getObject() == null;
            bodyGroup.setVisible(true);
            return bodyGroup;
        }
    }

    protected void releaseBodyGroup(final ObjectListTable olt, final ObjectListTable.ObjectListTableBodyGroup bodyGroup) {
        if (olt.getRenderer() instanceof ObjectListTable.ConcealRenderer) {
            final ObjectListTable.ConcealRenderer concealRenderer = (ObjectListTable.ConcealRenderer)olt.getRenderer();
            concealRenderer.conceal(olt.getObjects(), bodyGroup);
        }

        bodyGroup.setObject(null);
        bodyGroup.setVisible(false);
        bodyGroup.reset();
        unusedTbodies.add(bodyGroup);
    }

    protected void insert(final ObjectListTable olt, final ObjectListTable.ObjectListTableBodyGroup bodyGroup, final int index) {
        final List/*<ObjectListTableBodyGroup>*/ tbodies = olt.getTbodies();
        final Element beforeElement;
        if (index < tbodies.size()) {
            final ObjectListTable.ObjectListTableBodyGroup beforeTbody = (ObjectListTable.ObjectListTableBodyGroup)tbodies.get(index);
            beforeElement = beforeTbody.getElement();
        } else {
            beforeElement = tbe;
        }
        insertBefore(olt.getElement(), bodyGroup.getElement(), beforeElement);
    }
}
