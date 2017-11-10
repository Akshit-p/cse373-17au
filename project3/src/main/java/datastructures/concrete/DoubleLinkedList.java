package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Note: For more info on the expected behavior of your methods, see the source code for IList.
 * 
 * @author Akshit Patel
 * @author Haobo Zhang
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    @Override
    public void add(T item) {
        if (this.size == 0) {
            this.front = new Node<T>(item);
            this.back = this.front;
        } else {
            this.back = new Node<T>(this.back, item, null);
            this.back.prev.next = this.back;
        }
        this.size++;
    }

    @Override
    public T remove() {
        if (this.isEmpty()) {
            throw new EmptyContainerException();
        }
        T data;
        if (this.size == 1) {
            data = this.front.data;
            this.front = null;
            this.back = null;
        } else {
            data = this.back.data;
            this.back = this.back.prev;
            this.back.next = null;
        }
        this.size--;
        return data;
    }

    @Override
    public T get(int index) {
        indexBoundCheck(index);
        return getNode(index).data;
    }

    @Override
    public void set(int index, T item) {
        indexBoundCheck(index);

        // handle the edge case of changing first element.
        if (index == 0) {
            Node<T> temp = new Node<T>(null, item, this.front.next);
            // update the prev counter of the second element in list if present
            if (this.front.next != null) {
                this.front.next.prev = temp;
            }
            this.front = temp;
            // change back pointer if only one element in whole list.
            if (size == 1) {
                this.back = this.front;
            }
        } else {
            // quickly get the node at the given index.
            Node<T> temp = getNode(index);
            // shove a new point with the requested data and change links.
            temp = new Node<T>(temp.prev, item, temp.next);
            temp.prev.next = temp;
            // update the back pointer if last element was changed.
            if (temp.next != null) {
                temp.next.prev = temp;
            } else {
                this.back = temp;
            }
        }
    }

    /**
     * This methods helps in finding node at given index efficiently by using the 
     * least path to index from front or back.
     * 
     * @param index
     *            the position in the list for which the node is to be found.
     * @return the node at the given index
     */
    private Node<T> getNode(int index) {
        // choose the most closest start point i.e. back or front.
        // go front if difference between the size and index is
        // larger than the half of the list.
        Node<T> temp = this.front;
        if (this.size - index > this.size / 2) {
            int counter = 0;
            while (counter != index && temp != null) {
                temp = temp.next;
                counter++;
            }
        } else {
            temp = this.back;
            int counter = this.size - 1;
            while (counter != index && temp != null) {
                temp = temp.prev;
                counter--;
            }
        }
        return temp;
    }

    @Override
    public void insert(int index, T item) {
        if (index < 0 || index >= this.size + 1) {
            throw new IndexOutOfBoundsException();
        }

        if (index == this.size) {
            this.add(item);
        } else if (index == 0) {
            Node<T> temp = new Node<T>(null, item, this.front);
            this.front.prev = temp;
            this.front = temp;
            this.size++;
        } else {
            // quickly get the node at the given index.
            Node<T> temp = getNode(index);
            // shove a new point with the requested data and change links.
            temp = new Node<T>(temp.prev, item, temp);
            temp.prev.next = temp;
            temp.next.prev = temp;
            this.size++;
        }
    }

    @Override
    public T delete(int index) {
        indexBoundCheck(index);

        if (this.size == 1 && index == 0) {
            return this.remove();
        }
        
        T data;
        if (index == 0) {
            data = this.front.data;
            this.front = this.front.next;
            this.front.prev = null;
        } else {
            Node<T> temp = getNode(index);
            data = temp.data;
            
            // update back pointer for last element.
            if (temp.next == null) {
                this.back = temp.prev;
                this.back.next = null;
            } else {
                temp.prev.next = temp.next;
                temp.next.prev = temp.prev;
            }
        }
        this.size--;
        return data;
    }

    @Override
    public int indexOf(T item) {
        Node<T> temp = this.front;
        int index = 0;
        while (temp != null) {
            if ((item == null && temp.data == item) || temp.data.equals(item)) {
                return index;
            }
            temp = temp.next;
            index++;
        }
        return -1;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean contains(T other) {
        if (this.indexOf(other) > -1) {
            return true;
        }
        return false;
    }
    
    /** 
     * helper method to check for index out of bounds exception.
     * @param index
     */
    private void indexBoundCheck(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at; returns 'false' otherwise.
         */
        public boolean hasNext() {
            if (current != null) {
                return true;
            }
            return false;
        }

        /**
         * Returns the next item in the iteration and internally updates the iterator to advance one element forward.
         *
         * @throws NoSuchElementException
         *             if we have reached the end of the iteration and there are no more elements to look at.
         */
        public T next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            T ans = current.data;
            current = current.next;
            return ans;
        }
    }
}
