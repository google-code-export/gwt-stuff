package org.mcarthur.sandy.gwt.ui.tabs.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabListenerCollection;
import com.google.gwt.user.client.ui.Widget;

/**
 * ALPHA: An alternative TabBar.
 *
 * @author Sandy McArthur
 */
public class TabBar extends Widget implements SourcesTabEvents {

    private final Element table = DOM.createTable();

    // TODO: Need something more, I want Tab Close events too
    private TabListenerCollection tabListeners = new TabListenerCollection();

    public TabBar() {
        setElement(table);
        addStyleName("gwtstuff-TabBar");
    }

    public void addTabListener(final TabListener listener) {
        tabListeners.add(listener);
    }

    public void removeTabListener(final TabListener listener) {
        tabListeners.remove(listener);
    }
}
