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

package org.mcarthur.sandy.gwt.table.client;

/**
 * A Person object used for testing.
 *
 * @author Sandy McArthur
 */
class Person implements Comparable /*, PropertyChangeSource*/ {
    private String name;
    private final int age;

    //private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public Person(final String name) {
        this(name, (int)(Math.random()*100));
    }

    public Person(final String name, final int age) {
        setName(name);
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        final Object old = this.name;
        this.name = name;
        //pcs.firePropertyChange("name", old, name);
    }

    public int getAge() {
        return age;
    }

    public int compareTo(final Object o) {
        final Person person = (Person)o;
        final int ageDiff = person.age - age;
        return ageDiff != 0 ? ageDiff : name.compareTo(person.name);
    }

    public String toString() {
        return name + "{" + age + '}';
    }

    /*
    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
    */
}
