package datastructures.sorting;

import misc.BaseTest;
import org.junit.Test;

import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;

import static org.junit.Assert.assertTrue;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest {
    
    @Test(timeout=10*SECOND)
    public void testArrayHeapStress() {
        int limit = 1000000;
        IPriorityQueue<Integer> heap = makeInstance();
        for (int i = 0; i < limit; i++) {
            heap.insert(i);
            assertEquals(0,heap.peekMin());
            assertEquals(i+1, heap.size());
        }
        
        for (int i = 0; i < limit; i++) {
            int min = heap.peekMin();
            assertEquals(min, heap.removeMin());
        }
        assertTrue(heap.isEmpty());
        
        for (int i = 0; i < limit; i++) {
            heap.insert(i);
            assertEquals(i,heap.peekMin());
            assertEquals(1, heap.size());
            int min = heap.peekMin();
            assertEquals(min, heap.removeMin());
        }
        assertTrue(heap.isEmpty());
        
    }

    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }
}
