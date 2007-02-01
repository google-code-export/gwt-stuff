package org.mcarthur.sandy.gwt.event.list.test;

import junit.framework.TestCase;

/**
 * Created by IntelliJ IDEA.
 * User: sandymac
 * Date: Feb 1, 2007
 * Time: 1:07:27 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class TransformedEventListTest extends TestCase {

    /**
     * {@link org.mcarthur.sandy.gwt.event.list.client.TransformedEventList} can limit the view to
     * a sub set of elements in the backing list. This is to test that the contains method works
     * correctly.
     */
    public abstract void testContains();
}
