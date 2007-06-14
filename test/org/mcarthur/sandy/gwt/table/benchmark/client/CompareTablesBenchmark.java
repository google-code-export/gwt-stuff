package org.mcarthur.sandy.gwt.table.benchmark.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.Benchmark;
import com.google.gwt.junit.client.IntRange;
import com.google.gwt.junit.client.Operator;
import com.google.gwt.junit.client.Range;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import org.mcarthur.sandy.gwt.table.client.ObjectListTable;
import org.mcarthur.sandy.gwt.table.client.TableBodyGroup;
import org.mcarthur.sandy.gwt.table.client.TableDataCell;
import org.mcarthur.sandy.gwt.table.client.TableFooterGroup;
import org.mcarthur.sandy.gwt.table.client.TableHeaderGroup;
import org.mcarthur.sandy.gwt.table.client.TableRow;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Compares various dynamic table operations on various table widgets.
 *
 * @author Sandy McArthur
 */
public class CompareTablesBenchmark extends Benchmark {

    /**
     * Captures the table type.
     */
    protected static class Table {
        public static final Table GRID = new Table("Grid");
        public static final Table FLEX_TABLE = new Table("FlexTable");
        public static final Table OBJECT_LIST_TABLE = new Table("ObjectListTable");

        public static final Range types = new Range() {
          public Iterator iterator() {
            return Arrays.asList( new Table[] {GRID, FLEX_TABLE, OBJECT_LIST_TABLE} ).iterator();
          }
        };

        private final String label;

        public Table(final String label) {
            this.label = label;
        }

        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || !GWT.getTypeName(this).equals(GWT.getTypeName(o))) return false;
            final Table table = (Table)o;
            return !(label != null ? !label.equals(table.label) : table.label != null);
        }

        public int hashCode() {
            return (label != null ? label.hashCode() : 0);
        }

        public String toString() {
            return label;
        }
    }

    public final IntRange appendRowsRange = new IntRange(1, Integer.MAX_VALUE, Operator.MULTIPLY, 2);

    private Widget table;

    public String getModuleName() {
        return "org.mcarthur.sandy.gwt.table.benchmark.Benchmark";
    }

    /**
     * Test performance of appending rows to a table widget.
     * @gwt.benchmark.param type = Table.types
     * @gwt.benchmark.param rows -limit = appendRowsRange
     */
    public void testTableAppendRow(final Table type, final Integer rows) {
        appendTableRows(type, rows);
    }

    public void testTableAppendRow() {
    }

    public void beginTableAppendRow(final Table type, final Integer rows) {
        if (Table.GRID.equals(type)) {
            final Grid grid;
            table = grid = new Grid();
            grid.resizeColumns(1);

        } else if (Table.FLEX_TABLE.equals(type)) {
            table = new FlexTable();

        } else if (Table.OBJECT_LIST_TABLE.equals(type)) {
            table = new ObjectListTable(new NumberRenderer());
        }
    }

    public void endTableAppendRow() {
    }

    /**
     * Test performance of appending rows to an attached table widget.
     * @gwt.benchmark.param type = Table.types
     * @gwt.benchmark.param rows -limit = appendRowsRange
     */
    public void testTableAppendRowAttached(final Table type, final Integer rows) {
        appendTableRows(type, rows);
    }

    public void testTableAppendRowAttached() {
    }

    public void beginTableAppendRowAttached(final Table type, final Integer rows) {
        beginTableAppendRow(type, rows);
        RootPanel.get().add(table);
    }

    public void endTableAppendRowAttached() {
        endTableAppendRow();
        RootPanel.get().remove(table);
    }


    /**
     * Test performance of inserting rows at the start of a table widget.
     * @gwt.benchmark.param type = Table.types
     * @gwt.benchmark.param rows -limit = appendRowsRange
     */
    public void testTableInsertRow(final Table type, final Integer rows) {
        insertTableRows(type, rows);
    }

    public void testTableInsertRow() {
    }

    public void beginTableInsertRow(final Table type, final Integer rows) {
        if (Table.GRID.equals(type)) {
            final Grid grid;
            table = grid = new Grid();
            grid.resizeColumns(1);

        } else if (Table.FLEX_TABLE.equals(type)) {
            table = new FlexTable();

        } else if (Table.OBJECT_LIST_TABLE.equals(type)) {
            table = new ObjectListTable(new NumberRenderer());
        }
    }

    public void endTableInsertRow() {
    }

    /**
     * Test performance of inserting rows at the start of a table widget.
     * @gwt.benchmark.param type = Table.types
     * @gwt.benchmark.param rows -limit = appendRowsRange
     */
    public void testTableInsertRowAttached(final Table type, final Integer rows) {
        insertTableRows(type, rows);
    }

    public void testTableInsertRowAttached() {
    }

    public void beginTableInsertRowAttached(final Table type, final Integer rows) {
        beginTableInsertRow(type, rows);
        RootPanel.get().add(table);
    }

    public void endTableInsertRowAttached() {
        endTableInsertRow();
        RootPanel.get().remove(table);
    }



    private void appendTableRows(final Table type, final Integer r) {
        final int rows = r.intValue();

        if (Table.GRID.equals(type)) {
            final Grid grid = (Grid)table;

            for (int i=0; i < rows; i++) {
                final Number num = new Integer(i);
                grid.resizeRows(i+1);
                grid.setWidget(i, 0, makeLabel(num));
            }

        } else if (Table.FLEX_TABLE.equals(type)) {
            final FlexTable flexTable = (FlexTable)table;

            for (int i=0; i < rows; i++) {
                final Number num = new Integer(i);
                flexTable.setWidget(i, 0, makeLabel(num));
            }


        } else if (Table.OBJECT_LIST_TABLE.equals(type)) {
            final ObjectListTable olt = (ObjectListTable)table;
            for (int i=0; i < rows; i++) {
                final Number num = new Integer(i);
                olt.getObjects().add(num);
            }
        }
    }

    private void insertTableRows(final Table type, final Integer r) {
        final int rows = r.intValue();

        if (Table.GRID.equals(type)) {
            final Grid grid = (Grid)table;

            for (int i=0; i < rows; i++) {
                final Number num = new Integer(i);
                grid.resizeRows(i+1);
                for (int j=i; j > 0; j--) {
                    grid.setWidget(j, 0, grid.getWidget(j-1, 0));
                }
                grid.setWidget(0, 0, makeLabel(num));
            }

        } else if (Table.FLEX_TABLE.equals(type)) {
            final FlexTable flexTable = (FlexTable)table;

            for (int i=0; i < rows; i++) {
                final Number num = new Integer(i);
                flexTable.insertRow(0);
                flexTable.setWidget(0, 0, makeLabel(num));
            }

        } else if (Table.OBJECT_LIST_TABLE.equals(type)) {
            final ObjectListTable olt = (ObjectListTable)table;

            for (int i=0; i < rows; i++) {
                final Number num = new Integer(i);
                olt.getObjects().add(0, num);
            }
        }
    }


    private static Label makeLabel(final Number number) {
        return new Label(number.toString());
    }

    private static class NumberRenderer implements ObjectListTable.Renderer {
        public void render(final Object obj, final TableBodyGroup bodyGroup) {
            final Number num = (Number)obj;
            final TableRow tr = bodyGroup.newTableRow();
            final TableDataCell td = tr.newTableDataCell();
            td.add(makeLabel(num));
            tr.add(td);
            bodyGroup.add(tr);
        }

        public void renderHeader(final TableHeaderGroup headerGroup) {
        }

        public void renderFooter(final TableFooterGroup footerGroup) {
        }
    }
}
