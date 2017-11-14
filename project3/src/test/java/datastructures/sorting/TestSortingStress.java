package datastructures.sorting;

import misc.BaseTest;
import misc.Searcher;

import org.junit.Test;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    
    @Test(timeout=10*SECOND)
    public void testTopKStringListStress() {
        List<String> sortList = new ArrayList<>(1000000);
        
        IList<String> list = new DoubleLinkedList<>();
        for (int i = 0; i < 1000000; i++) {
            list.add("a"+i);
            sortList.add("a"+i);
        }
        Collections.sort(sortList);
        IList<String> top = Searcher.topKSort(50000, list);
        assertEquals(5, top.size());
        for (int i = 950000; i < top.size(); i++) {
            assertEquals(sortList.get(i), top.get(i));           
        }
    }
    
    @Test(timeout=10*SECOND)
    public void testTopKIntegerListStress() {
        List<Integer> sortList = new ArrayList<>(1000000);
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 1000000; i > 0; i--) {
            list.add(i);
            sortList.add(i);
        }
        Collections.sort(sortList);
        IList<Integer> top = Searcher.topKSort(50000, list);
        assertEquals(5, top.size());
        for (int i = 950000; i < top.size(); i++) {
            assertEquals(sortList.get(i), top.get(i));           
        }
    }

    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }
}
