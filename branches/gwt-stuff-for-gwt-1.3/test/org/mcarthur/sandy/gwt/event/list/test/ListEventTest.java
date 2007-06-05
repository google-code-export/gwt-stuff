/*
 * Copyright 2007 Sandy McArthur, Jr.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.mcarthur.sandy.gwt.event.list.test;

import junit.framework.TestCase;
import org.mcarthur.sandy.gwt.event.list.client.EventLists;
import org.mcarthur.sandy.gwt.event.list.client.ListEvent;

/**
 * Unit tests for {@link org.mcarthur.sandy.gwt.event.list.client.ListEvent}.
 *
 * @author Sandy McArthur
 */
public class ListEventTest extends TestCase {

     public void testNewEvent() {
         // TODO: split this unit test in to a couple of tests
         ListEvent.createOther(EventLists.eventList());

         try {
             ListEvent.createOther(null);
             fail("Expected a IllegalArgumentException.");
         } catch (IllegalArgumentException iae) {
             // expected
         }

         ListEvent e = ListEvent.createChanged(EventLists.eventList(), 0);
         assertTrue(e.getIndexStart() + 1 == e.getIndexEnd());

         try {
             ListEvent.createChanged(null, 0);
             fail("Expected a IllegalArgumentException.");
         } catch (IllegalArgumentException iae) {
             // expected
         }

         e = ListEvent.createChanged(EventLists.eventList(), 10, 0);
         assertEquals(0, e.getIndexStart());
         assertEquals(10, e.getIndexEnd());

         try {
             ListEvent.createChanged(null, 0, 1);
             fail("Expected a IllegalArgumentException.");
         } catch (IllegalArgumentException iae) {
             // expected
         }

         try {
             ListEvent.createChanged(EventLists.eventList(), 0, 0);
             fail("Expected a AssertionError. Did use use -ea?");
         } catch (AssertionError ae) {
             // expected
         }

         try {
             new ListEvent(EventLists.eventList(), null, 0);
             fail("Expected a AssertionError. Did use use -ea?");
         } catch (AssertionError ae) {
             // expected
         }

     }
}
