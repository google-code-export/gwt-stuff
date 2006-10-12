package org.mcarthur.sandy.gwt.table.client;

import java.util.List;

/**
 * TODO: Write JavaDoc
 *
 * @author Sandy McArthur
 */
public interface TableModel {
    /**
     *
     * @return a list of Strings that are the property names.
     */
    public List getProperties();

    public List getItems();

    public Object getProperty(Object item, String property);
    
}
