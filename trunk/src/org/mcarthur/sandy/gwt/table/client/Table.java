package org.mcarthur.sandy.gwt.table.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Command;

import java.util.Date;

/**
 * TODO: Write JavaDoc
 *
 * @author Sandy McArthur
 */
public class Table implements EntryPoint {
    private static VerticalPanel vp = new VerticalPanel();

    private FooTable ft;
    //private ObjectTable ot = new ObjectTable();

    public void onModuleLoad() {
        RootPanel.get("log").add(vp);

        DeferredCommand.add(new Command() {
            public void execute() {
                ft = new FooTable();
                RootPanel.get("tableDiv").add(ft);
            }
        });
        //RootPanel.get("tableDiv").add(ot);
    }

    public static void log(final String str) {
        if (vp == null) {
            vp = new VerticalPanel();
            RootPanel.get("log").add(vp);
        }
        vp.insert(new Label(new Date() + " " + str), 0);
    }
}
