package org.mcarthur.sandy.gwt.table.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;

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

    public static MenuBar makeMenuBar() {
        MenuBar subMenu = new MenuBar(true);
        subMenu.addItem("<code>Code</code>", true, new MyCommand());
        subMenu.addItem("<strike>Strikethrough</strike>", true, new MyCommand());
        subMenu.addItem("<u>Underlined</u>", true, new MyCommand());

        MenuBar menu0 = new MenuBar(true);
        menu0.addItem("<b>Bold</b>", true, new MyCommand());
        menu0.addItem("<i>Italicized</i>", true, new MyCommand());
        menu0.addItem("More &#187;", true, subMenu);


        MenuBar menu = new MenuBar(true);
        menu.addItem(new MenuItem("Style",menu0));

        return menu;
    }

    private static class MyCommand implements Command {
        public void execute() {
            Window.alert("command");
        }
    }

    public static void log(final String str) {
        if (vp == null) {
            vp = new VerticalPanel();
            RootPanel.get("log").add(vp);
        }
        vp.insert(new Label(new Date() + " " + str), 0);
    }
}
