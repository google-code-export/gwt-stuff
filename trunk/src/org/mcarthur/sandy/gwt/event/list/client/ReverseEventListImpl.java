package org.mcarthur.sandy.gwt.event.list.client;

/**
 * A "ReverseEventList". Elements of the backing list are presented in opposite order.
 *
 * @author Sandy McArthur
 */
class ReverseEventListImpl extends AbstractEventList implements EventList {
    private final EventList delegate;
    private final ReverseListEventListener listEventListener = new ReverseListEventListener();

    private int size = 0;

    public ReverseEventListImpl(final EventList delegate) {
        this.delegate = delegate;
        delegate.addListEventListener(listEventListener);

        // fake an add all to initialize this list.
        size = delegate.size();
        //listEventListener.listChanged(new ListEvent(delegate, ListEvent.ADDED, 0, delegate.size()));
    }

    protected int getSourceIndex(final int mutationIndex) {
        return invertIndex(mutationIndex);
        /*
        final int size = size();
        if (mutationIndex > size) {
            return mutationIndex;
        } else if (mutationIndex == size) {
            return 0;
        } else {
            //return (size - 1) - mutationIndex;
            return invertIndex(mutationIndex);
        }
        */
    }

    private int invertIndex(final int index) {
        //return -index + size();
        return size() - index - 1;
    }

    public Object get(final int index) {
        try {
            return delegate.get(invertIndex(index));
        } catch (IndexOutOfBoundsException iobe) {
            final IndexOutOfBoundsException e = new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
            e.initCause(iobe);
            throw e;
        }
    }

    public int size() {
        return size;
    }

    public Object set(final int index, final Object element) {
        try {
            return delegate.set(invertIndex(index), element);
        } catch (IndexOutOfBoundsException iobe) {
            final IndexOutOfBoundsException e = new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
            e.initCause(iobe);
            throw e;
        }
    }

    public void add(final int index, final Object element) {
        try {
            final int invertedIndex = invertIndex(index);
            delegate.add(invertedIndex == -1 ? 0 : invertedIndex, element);
        } catch (IndexOutOfBoundsException iobe) {
            final IndexOutOfBoundsException e = new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
            e.initCause(iobe);
            throw e;
        }
    }

    public Object remove(final int index) {
        try {
            return delegate.remove(invertIndex(index));
        } catch (IndexOutOfBoundsException iobe) {
            final IndexOutOfBoundsException e = new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
            e.initCause(iobe);
            throw e;
        }
    }

    private class ReverseListEventListener implements ListEventListener {
        public void listChanged(final ListEvent listEvent) {
            final ListEvent reverse;
            final int sizeDelta = listEvent.getIndexEnd() - listEvent.getIndexStart();
            if (ListEvent.OTHER.equals(listEvent.getType())) {
                reverse = listEvent.resource(ReverseEventListImpl.this);
            } else if (listEvent.isAdded()) {
                size += sizeDelta;
                final int revStart = invertIndex(listEvent.getIndexStart());
                reverse = new ListEvent(ReverseEventListImpl.this, listEvent.getType(), revStart, revStart + sizeDelta);

            } else if (listEvent.isChanged()) {
                final int revStart = invertIndex(listEvent.getIndexStart());
                reverse = new ListEvent(ReverseEventListImpl.this, listEvent.getType(), revStart, revStart + sizeDelta);

            } else if (listEvent.isRemoved()) {
                final int revStart = invertIndex(listEvent.getIndexStart());
                reverse = new ListEvent(ReverseEventListImpl.this, listEvent.getType(), revStart, revStart + sizeDelta);
                size -= sizeDelta;

            } else {
                // untested?
                final int revStart = invertIndex(listEvent.getIndexStart());
                reverse = new ListEvent(ReverseEventListImpl.this, listEvent.getType(), revStart, revStart + sizeDelta);
            }
            fireListEvent(reverse);
        }
    }
}
