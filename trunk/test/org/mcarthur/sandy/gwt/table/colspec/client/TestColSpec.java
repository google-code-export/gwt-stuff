package org.mcarthur.sandy.gwt.table.colspec.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import org.mcarthur.sandy.gwt.event.list.client.EventList;
import org.mcarthur.sandy.gwt.event.list.client.EventLists;
import org.mcarthur.sandy.gwt.table.client.ObjectListTable;
import org.mcarthur.sandy.gwt.table.client.TableBodyGroup;
import org.mcarthur.sandy.gwt.table.client.TableCol;
import org.mcarthur.sandy.gwt.table.client.TableColSpec;
import org.mcarthur.sandy.gwt.table.client.TableDataCell;
import org.mcarthur.sandy.gwt.table.client.TableFooterGroup;
import org.mcarthur.sandy.gwt.table.client.TableHeaderGroup;
import org.mcarthur.sandy.gwt.table.client.TableRow;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Test the {@link org.mcarthur.sandy.gwt.table.colspec.client.TestColSpec} code.
 */
public class TestColSpec implements EntryPoint {
    private final EventList messages = EventLists.eventList();

    private ObjectListTable t1 = new ObjectListTable(new Renderer1(), messages);

    private ObjectListTable t2 = new ObjectListTable(new Renderer2(), messages);


    public TestColSpec() {
        setAttribute(t1.getElement(), "width", "100%");
        setAttribute(t1.getElement(), "cellspacing", "0");
        setAttribute(t1.getElement(), "cellpadding", "1");
        //setAttribute(t1.getElement(), "border", "1");
        t1.addStyleName("tlc");

        DOM.setStyleAttribute(t2.getElement(), "width", "100%");
        //setAttribute(t2.getElement(), "width", "100%");
        //setAttribute(t2.getElement(), "cellspacing", "0"); // needed by IE6
        //setAttribute(t2.getElement(), "cellpadding", "1"); // needed by IE6
        t2.addStyleName("tlc");
        
        messages.add(new Message("Bill Gates", true, "Buy Vista Pinata! Buy Vista Pinata! Buy Vista Pinata! ", "Buy buy buy before they break on you.", false, new Date()));
        messages.add(new Message("Steve Jobs", false, "Buy OS X! Buy OS X! ", "It's better than Vista.", true, new Date()));
    }

    public void onModuleLoad() {
        RootPanel.get("t1").add(t1);

        if (false) {
            final TabPanel tp = new TabPanel();
            tp.setWidth("100%");
            tp.add(t2, "t2");
            tp.selectTab(0);
            RootPanel.get("t2").add(tp);
        } else {
            RootPanel.get("t2").add(t2);
        }


        final TextArea ta1 = new TextArea();
        ta1.setWidth("100%");
        ta1.setVisibleLines(7);
        ta1.setText(t1.toString());
        RootPanel.get().add(ta1);

        final TextArea ta2 = new TextArea();
        ta2.setWidth("100%");
        ta2.setVisibleLines(7);
        ta2.setText(t2.toString());
        RootPanel.get().add(ta2);
    }


    private static class Renderer1 implements ObjectListTable.Renderer, ObjectListTable.ColSpecRenderer {

        public void render(final Object obj, final TableBodyGroup bodyGroup) {
            final Message message = (Message)obj;
            final TableRow tr = bodyGroup.newTableRow();
            tr.addStyleName("rr");

            final TableDataCell cb = tr.newTableDataCell();
            cb.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
            cb.setWidth("31px");
            cb.add(new CheckBox());

            final TableDataCell star = tr.newTableDataCell();
            star.setWidth("20px");
            star.addStyleName("sc");
            star.addStyleName("t");

            final TableDataCell from = tr.newTableDataCell();
            from.setWidth("27ex");
            from.add(new Label(message.getFrom()));

            final TableDataCell tome = tr.newTableDataCell();
            tome.setWidth("2ex");
            if (message.isTome()) {
                tome.add(new HTML("&raquo;"));
            }

            final TableDataCell subject = tr.newTableDataCell();
            subject.add(new Label(message.getSubject()));

            final TableDataCell attachment = tr.newTableDataCell();
            attachment.setWidth("17px");
            if (message.isAttachment()) {
                attachment.add(new Image("http://mail.google.com/mail/images/paperclip.gif"));
            }

            final TableDataCell date = tr.newTableDataCell();
            date.setWidth("9.5ex");
            date.add(new Label(message.getDate().toString()));

            tr.add(cb);
            tr.add(star);
            tr.add(from);
            tr.add(tome);
            tr.add(subject);
            tr.add(attachment);
            tr.add(date);

            bodyGroup.add(tr);
        }

        public void renderHeader(final TableHeaderGroup headerGroup) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void renderFooter(final TableFooterGroup footerGroup) {
            //To change body of implemented methods use File | Settings | File Templates.
        }


        public List/*<TableColSpec>*/ getColSpec() {
            final List cols = new ArrayList();

            final TableColSpec cb = new TableCol();
            cb.addStyleName("cc");
            //DOM.setStyleAttribute(cb.getElement(), "width", "20px");
            //setAttribute(cb.getElement(), "style", "width: 20px;");

            final TableColSpec star = new TableCol();
            setAttribute(star.getElement(), "style", "width: 20px;");
            DOM.setStyleAttribute(star.getElement(), "width", "20px");

            final TableColSpec from = new TableCol();
            //from.addStyleName("r1-from");
            setAttribute(from.getElement(), "style", "width: 27ex;");
            DOM.setStyleAttribute(from.getElement(), "width", "27ex");

            final TableColSpec tome = new TableCol();
            setAttribute(tome.getElement(), "style", "width: 2ex;");

            final TableColSpec subject = new TableCol();
            //subject.addStyleName("r1-subject");

            final TableColSpec attachment = new TableCol();
            //attachment.addStyleName("r1-attachment");
            setAttribute(attachment.getElement(), "style", "width: 17px;");

            final TableColSpec date = new TableCol();
            //date.addStyleName("r1-date");
            setAttribute(date.getElement(), "style", "width: 9.5ex;");

            cols.add(cb);
            cols.add(star);
            cols.add(from);
            cols.add(tome);
            cols.add(subject);
            cols.add(attachment);
            cols.add(date);

            return cols;
        }
    }

    private static class Renderer2 implements ObjectListTable.Renderer, ObjectListTable.ColSpecRenderer {

        public void render(final Object obj, final TableBodyGroup bodyGroup) {
            final Message message = (Message)obj;
            final TableRow tr = bodyGroup.newTableRow();
            tr.addStyleName("rr");

            final TableDataCell cb = tr.newTableDataCell();
            cb.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
            //cb.setWidth("31px");
            cb.addStyleName("w31px");
            cb.add(new CheckBox());

            final TableDataCell star = tr.newTableDataCell();
            //star.setWidth("20px");
            star.addStyleName("w20px");
            star.addStyleName("sc");
            star.addStyleName("t");

            final TableDataCell from = tr.newTableDataCell();
            //from.setWidth("27ex");
            from.addStyleName("w27ex");
            from.addStyleName("nooverflow");
            from.add(new Label(message.getFrom()));

            final TableDataCell tome = tr.newTableDataCell();
            //tome.setWidth("2ex");
            tome.addStyleName("w2ex");
            if (message.isTome()) {
                tome.add(new HTML("&raquo;"));
            }

            final TableDataCell subject = tr.newTableDataCell();
            subject.add(new Label(message.getSubject()));
            subject.addStyleName("nooverflow");

            final TableDataCell attachment = tr.newTableDataCell();
            //attachment.setWidth("17px");
            attachment.addStyleName("w17px");
            if (message.isAttachment()) {
                attachment.add(new Image("http://mail.google.com/mail/images/paperclip.gif"));
            }

            final TableDataCell date = tr.newTableDataCell();
            //date.setWidth("9.5ex");
            date.addStyleName("w9p5ex");
            date.addStyleName("nooverflow");
            date.add(new Label(message.getDate().toString()));

            tr.add(cb);
            tr.add(star);
            tr.add(from);
            tr.add(tome);
            tr.add(subject);
            tr.add(attachment);
            tr.add(date);

            bodyGroup.add(tr);
        }

        public void renderHeader(final TableHeaderGroup headerGroup) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void renderFooter(final TableFooterGroup footerGroup) {
            //To change body of implemented methods use File | Settings | File Templates.
        }


        public List/*<TableColSpec>*/ getColSpec() {
            final List cols = new ArrayList();

            final TableColSpec cb = new TableCol();
            cb.addStyleName("cc");
            cb.addStyleName("w31px");
            //DOM.setStyleAttribute(cb.getElement(), "width", "20px");
            //setAttribute(cb.getElement(), "style", "width: 20px;");

            final TableColSpec star = new TableCol();
            //setAttribute(star.getElement(), "style", "width: 20px;");
            //DOM.setStyleAttribute(star.getElement(), "width", "20px");
            star.addStyleName("w20px");

            final TableColSpec from = new TableCol();
            //from.addStyleName("r1-from");
            //setAttribute(from.getElement(), "style", "width: 27ex;");
            //DOM.setStyleAttribute(from.getElement(), "width", "27ex");
            from.addStyleName("w27ex");
            //from.addStyleName("nooverflow");

            final TableColSpec tome = new TableCol();
            //setAttribute(tome.getElement(), "style", "width: 2ex;");
            tome.addStyleName("w2ex");

            final TableColSpec subject = new TableCol();
            //subject.addStyleName("r1-subject");
            //subject.addStyleName("nooverflow");

            final TableColSpec attachment = new TableCol();
            //attachment.addStyleName("r1-attachment");
            //setAttribute(attachment.getElement(), "style", "width: 17px;");
            attachment.addStyleName("w17px");

            final TableColSpec date = new TableCol();
            //date.addStyleName("r1-date");
            //setAttribute(date.getElement(), "style", "width: 9.5ex;");
            date.addStyleName("w9p5ex");
            //date.addStyleName("nooverflow");

            cols.add(cb);
            cols.add(star);
            cols.add(from);
            cols.add(tome);
            cols.add(subject);
            cols.add(attachment);
            cols.add(date);

            return cols;
        }
    }

    protected static native void setAttribute(Element elem, String attr, String value) /*-{
        elem.setAttribute(attr,value);
    }-*/;

    private static class Message {
        private String from;
        private boolean tome;
        private String subject;
        private String sample;
        private boolean attachment;
        private Date date;


        public Message(String from, boolean tome, String subject, final String sample, boolean attachment, Date date) {
            this.attachment = attachment;
            this.date = date;
            this.from = from;
            this.sample = sample;
            this.subject = subject;
        }

        public boolean isAttachment() {
            return attachment;
        }

        public void setAttachment(boolean attachment) {
            this.attachment = attachment;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getSample() {
            return sample;
        }

        public void setSample(String sample) {
            this.sample = sample;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public boolean isTome() {
            return tome;
        }

        public void setTome(boolean tome) {
            this.tome = tome;
        }
    }
}
