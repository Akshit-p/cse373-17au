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

    @Test(timeout = 10 * SECOND)
    public void testArrayHeapStressIntger() {
        int limit = 1000000;
        IPriorityQueue<Integer> heap = makeInstance();
        for (int i = 0; i < limit; i++) {
            heap.insert(i);
            assertEquals(0, heap.peekMin());
            assertEquals(i + 1, heap.size());
        }

        for (int i = 0; i < limit; i++) {
            int min = heap.peekMin();
            assertEquals(min, heap.removeMin());
        }
        assertTrue(heap.isEmpty());

        for (int i = 0; i < limit; i++) {
            heap.insert(i);
            int min = heap.peekMin();
            assertEquals(i, min);
            assertEquals(1, heap.size());
            assertEquals(min, heap.removeMin());
        }
        assertTrue(heap.isEmpty());

    }
    
    @Test(timeout = 10 * SECOND)
    public void testArrayHeapStressStrings() {
        int limit = 1000000;
        List<String> sortList = new ArrayList<>(1000000);
        IPriorityQueue<String> heap = makeInstance();
        for (int i = 0; i < limit; i++) {
            heap.insert("a"+i);
            sortList.add("a"+i);
            assertEquals("a0", heap.peekMin());
            assertEquals(i + 1, heap.size());
        }
        Collections.sort(sortList);

        for (int i = 0; i < limit; i++) {
            String min = heap.peekMin();
            assertEquals(sortList.get(i), min);
            assertEquals(limit - i, heap.size());
            assertEquals(min, heap.removeMin());
        }
        assertTrue(heap.isEmpty());

    }

    //Takes forever to run!
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
        assertEquals(50000, top.size());
        
        int count = 950000;
        for (String i : top) {
            assertEquals(sortList.get(count++), i);
        }
    }
    
    //Takes forever to run!
    @Test(timeout=100*SECOND)
    public void testTopKIntegerListStress() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 1000000; i > 0; i--) {
            list.add(i);
        }
        IList<Integer> top = Searcher.topKSort(50000, list);
        assertEquals(50000, top.size());
        int count = 950001;
        for (int i : top) {            
            assertEquals(count++, i);           
        }
    }

    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }
}
