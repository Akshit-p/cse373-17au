package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;

/**
 * See IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // this implementation is n-heap. and for this assignment we want 4-heap/
    private static final int NUM_CHILDREN = 4;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;

    // Num of elements in the heap
    private int size;
    // the initial capacity of the heap.
    private static final int DEFAULT_CAPACITY = 1 + NUM_CHILDREN + NUM_CHILDREN * NUM_CHILDREN;

    public ArrayHeap() {
        this.heap = makeArrayOfT(DEFAULT_CAPACITY);
        this.size = 0;
    }

    /**
     * This method will return a new, empty array of the given size that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int capacity) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[capacity]);
    }

    @Override
    public T removeMin() {
        if (this.isEmpty()) {
            throw new EmptyContainerException();
        }
        T min = this.heap[0];
        this.heap[0] = this.heap[this.size - 1];
        percolateDown();
        shrink();
        return min;
    }

    private void shrink() {
        if (this.heap.length > DEFAULT_CAPACITY && this.size < this.heap.length / 3) {
            T[] temp = makeArrayOfT(this.heap.length / 2);
            System.arraycopy(this.heap, 0, temp, 0, this.size);
            this.heap = temp;
        }

    }

    private void percolateDown() {
        T lastElement = this.heap[this.size - 1];
        // remove the last element that was shifted up and update the size.
        this.heap[--this.size] = null;

        int minChildIndex;

        // loop through each swap operation between parent and child until children are present
        for (int i = 0; (i * NUM_CHILDREN) + 1 < size; i = minChildIndex) {
            // Assuming that the smallest child is the first child
            minChildIndex = (i * NUM_CHILDREN) + 1;

            // no more children so get out of here!
            if (minChildIndex > size) {
                break;
            }

            // find the smallest child to swap with to preserve min-heap property
            int minIndex = minChildIndex;
            for (int j = 1; j < NUM_CHILDREN; j++) {
                // if the current index equal to size then we have seen all children.
                if (minChildIndex + j >= size) {
                    break;
                }
                if (this.heap[minIndex].compareTo(this.heap[minChildIndex + j]) > 0) {
                    minIndex = minChildIndex + j;
                }
            }
            // update the minChildIndex to the index of the child that is new lowest.
            minChildIndex = minIndex;

            // if the child we found is smaller than the parent, we should swap the two
            // and continue down to put the parent in the right place.
            if (lastElement.compareTo(this.heap[minChildIndex]) > 0) {
                swap(i, minChildIndex);
            } else {
                break;
            }
        }
    }

    private void swap(int index1, int index2) {
        T temp = this.heap[index1];
        this.heap[index1] = this.heap[index2];
        this.heap[index2] = temp;
    }

    @Override
    public T peekMin() {
        if (this.isEmpty()) {
            throw new EmptyContainerException();
        }
        return this.heap[0];
    }

    @Override
    public void insert(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        grow();
        percolateUp(item, this.size);
        this.size++;
    }

    private void grow() {
        if (this.size + 0 >= this.heap.length) {
            T[] temp = makeArrayOfT(this.heap.length * 2);
            System.arraycopy(this.heap, 0, temp, 0, this.size);
            this.heap = temp;
        }
    }

    private void percolateUp(T item, int i) {
        this.heap[this.size] = item;
        // insert elements by adding to the end and then swapping upwards.
        int parent = 0;
        while (i > 0 && parent >= 0) {
            parent = (i - 1) / NUM_CHILDREN;
            if (this.heap[i].compareTo(this.heap[parent]) < 0) {
                swap(i, parent);
                // repeat the process until correct position reached.
                i = parent;
            } else {
                // we have the element in the right position so we can exit.
                break;
            }
        }
    }

    @Override
    public int size() {
        return this.size;
    }
}
