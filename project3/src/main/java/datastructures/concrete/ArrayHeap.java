package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
import misc.exceptions.NotYetImplementedException;

import java.util.Arrays;

/**
 * See IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap; 

    // Feel free to add more fields and constants.
    private int size;

    public ArrayHeap() {
        this.heap = makeArrayOfT(21);
        this.size = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int size) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[size]);
    }

    @Override
    public T removeMin() {
        if (this.isEmpty()) {
            throw new EmptyContainerException();
        }
        T min = this.heap[0];
        this.heap[0] = this.heap[this.size-1];
        percolateDown(this.heap, 1);
        shrink();
        return min;
    }

    private void shrink() {
        if (this.heap.length > 21 && this.size < this.heap.length / 3) {
            T[] temp = makeArrayOfT(this.heap.length / 2);
            System.arraycopy(this.heap, 0, temp, 0, this.size);
            this.heap = temp;
        }
        
    }
    
    //TODO: clean up code.

    private void percolateDown(T[] other, int k) {
        T lastElement = heap[this.size-1];
        heap[--this.size] = null;
        int minChildIndex;
        for (int i = 0; (i*4)+1 < size; i = minChildIndex) {
            // Assuming that the smallest child is the first child
            minChildIndex = (i*4)+1;
            
            // no more children so get out of here!
            if (minChildIndex > size) { 
                break;
            }
            
            //find the smallest child to swap with to preserve min-heap property
            int minIndex = minChildIndex;
            for (int j = 1; j < 4; j++) {
                // if the current index equal to size then we have seen all children.
                if (minChildIndex+j >= size) {
                    break;
                }
                if(heap[minIndex].compareTo(heap[minChildIndex + j]) > 0)
                    minIndex = minChildIndex + j;
            }
            // update the minChildIndex to the index of the child that is new lowest.
            minChildIndex = minIndex;
            
            // if the child we found smaller than the parent, we should swap the two 
            // and continue down to put the parent in the right place.
            if (lastElement.compareTo(heap[minChildIndex]) > 0) {
                swap(heap, i, minChildIndex);
            } else {
                break;
            }
        }
    }

    private void swap(T[] other, int p1, int p2) {
        T temp = other[p1];
        other[p1] = other[p2];
        other[p2] = temp;
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
        this.heap[this.size] = item;
        percolateUp(this.heap, this.size);
        this.size++;
    }

    private void grow() {
        if (this.size + 0 >= this.heap.length) {
            T[] temp = makeArrayOfT(this.heap.length*2);
            System.arraycopy(this.heap, 0, temp, 0, this.size);
            this.heap = temp;
        }
    }

    private void percolateUp(T[] other, int i) {
        while(i > 0 && (i+1)/4 > 1) {
            if (other[i].compareTo(other[(i+1)/4]) < 0) {
                swap(other, i, (i+1)/4);
                i = (i+1)/4; 
            }
            i = 0;
        }
    }

    @Override
    public int size() {
        return this.size;
    }
    
    public String toString() {
        return this.heap.toString();
    }
}
