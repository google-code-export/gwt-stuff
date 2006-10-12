package org.mcarthur.sandy.gwt.table.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Label;

/**
 * TODO: Write JavaDoc
 *
 * @author Sandy McArthur
 */
public class ObjectTable extends Composite {
    private final VerticalPanel vp = new VerticalPanel();
    private final HorizontalPanel hp = new HorizontalPanel();
    private final TextBox colspec = new TextBox();
    private final Label pagination = new Label("0 - 0 of 0");

    public ObjectTable() {
        initWidget(vp);
        vp.setWidth("100%");

        hp.setWidth("100%");
        
        hp.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
        hp.add(colspec);
        hp.setCellWidth(colspec, "80%");

        hp.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
        hp.add(pagination);
        hp.setCellWidth(pagination, "20%");


        vp.add(hp);
    }
}
