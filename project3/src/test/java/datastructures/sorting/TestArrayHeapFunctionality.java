package datastructures.sorting;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import misc.BaseTest;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;

import org.junit.Before;
import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {

    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
    }
    
    @Test
    public void testRemoveMin() {
        //build queue
        IPriorityQueue<Integer> testQ = makeInstance();
        // small full array
        testQ.insert(1);
        testQ.insert(2);
        testQ.insert(3);
        testQ.insert(4);
        testQ.insert(5);
        
        assertEquals(1, testQ.removeMin());
        assertEquals(4, testQ.size());
        
        assertEquals(2, testQ.removeMin());
        assertEquals(3, testQ.size());
        
        assertEquals(3, testQ.removeMin());
        assertEquals(2, testQ.size());
        
        // test duplicate
        testQ.insert(5);
        assertEquals(4, testQ.removeMin());
        assertEquals(2, testQ.size());
        
        assertEquals(5, testQ.removeMin());
        assertEquals(1, testQ.size());
        
        assertEquals(5, testQ.removeMin());
        assertEquals(0, testQ.size());
        assertTrue(testQ.isEmpty());
    }
    
    @Test(expected=EmptyContainerException.class, timeout=SECOND)
    public void testRemoveMinException() {
        makeInstance().removeMin();
    }
    
    @Test
    public void testRemoveMinExhaustive() {
        // can make a setup process that i only test for performance of heap removal.
        IPriorityQueue<Integer> testQ = makeInstance();
        for (int i = 0 ; i < 1000; i++) {
            testQ.insert(i);
        }
        int qSize = 1000;
        for (int i = 0 ; i < 1000; i++) {
            assertEquals(i, testQ.removeMin());
            assertEquals(--qSize, testQ.size());
        }
        try {
            testQ.removeMin();
            fail();
        } catch (EmptyContainerException e) {
            //test passed!
        }
    }
    
    @Test(timeout=SECOND)
    public void testPeekMin() {
      //build queue
        IPriorityQueue<Integer> testQ = makeInstance();
        // small full array
        testQ.insert(1);
        
        assertEquals(1, testQ.peekMin());
        assertEquals(1, testQ.size());
        
        // test duplicate
        testQ.insert(2);
        testQ.insert(2);
        // check for non-min duplication
        assertEquals(1, testQ.peekMin());
        assertEquals(3, testQ.size());
        // check for min duplication
        testQ.insert(1);
        assertEquals(1, testQ.peekMin());
        assertEquals(4, testQ.size());
    }
    
    @Test(expected=EmptyContainerException.class, timeout=SECOND)
    public void testPeekMinException() {
        makeInstance().peekMin();
    }
    
    @Test(timeout=SECOND)
    public void testRemoveMinAndPeekMinCombo() {
        // can make a setup process that i only test for performance of heap removal.
        IPriorityQueue<Integer> testQ = makeInstance();
        for (int i = 1000 ; i > 0; i--) {
            testQ.insert(i);
        }
        int qSize = 1000;
        for(int i = 1; i <= 1000; i++) {
            int min = testQ.peekMin();
            assertEquals(i, min);
            assertEquals(min, testQ.removeMin());
            assertEquals(--qSize, testQ.size());
        }
        //duplication test
        testQ.insert(2);
        testQ.insert(1);
        testQ.insert(1);
        testQ.insert(1);
        testQ.insert(1);
        testQ.insert(2);
        
        int min = testQ.peekMin();
        assertEquals(1, min);
        assertEquals(min, testQ.removeMin());
        assertEquals(5, testQ.size());
        
        min = testQ.peekMin();
        assertEquals(1, min);
        assertEquals(min, testQ.removeMin());
        assertEquals(4, testQ.size());
        
        min = testQ.peekMin();
        assertEquals(1, min);
        assertEquals(min, testQ.removeMin());
        assertEquals(3, testQ.size());
        
        min = testQ.peekMin();
        assertEquals(1, min);
        assertEquals(min, testQ.removeMin());
        assertEquals(2, testQ.size());
        
        min = testQ.peekMin();
        assertEquals(2, min);
        assertEquals(min, testQ.removeMin());
        assertEquals(1, testQ.size());
        
        min = testQ.peekMin();
        assertEquals(2, min);
        assertEquals(min, testQ.removeMin());
        assertEquals(0, testQ.size());
        

        try {
            testQ.peekMin();
            fail();
        } catch (EmptyContainerException e) {
            //test passed!
        }
        try {
            testQ.removeMin();
            fail();
        } catch (EmptyContainerException e) {
            //test passed!
        }
    }
    
    @Test(timeout=SECOND)
    public void testInsert() {
        IPriorityQueue<Integer> qSize10 = makeInstance();
        IPriorityQueue<Integer> qSize500 = makeInstance();
        IPriorityQueue<Integer> qSize1000 = makeInstance();
        IPriorityQueue<Integer> qSize300Reverse = makeInstance();        
        
        for (int i = 0 ; i < 10; i++) {
            qSize10.insert(i);
        }
        assertEquals(10, qSize10.size());
        assertEquals(0, qSize10.peekMin());
        
        for (int i = 0 ; i < 500; i++) {
            qSize500.insert(i);
        }
        assertEquals(500, qSize500.size());
        assertEquals(0, qSize500.peekMin());
        

        for (int i = 0 ; i < 1000; i++) {
            qSize1000.insert(i);
        }
        assertEquals(1000, qSize1000.size());
        assertEquals(0, qSize1000.peekMin());
        
        
        for (int i = 300 ; i > 0; i--) {
            qSize300Reverse.insert(i);
        }
        assertEquals(300, qSize300Reverse.size());
        assertEquals(1, qSize300Reverse.peekMin());
    }
    
    @Test(expected=IllegalArgumentException.class, timeout=SECOND)
    public void testInsertException() {
        makeInstance().insert(null);
    }
    
    @Test(timeout=SECOND)
    public void testSizeUpdate(){
        IPriorityQueue<Integer> testQ = makeInstance();
        assertTrue(testQ.isEmpty());
        
        testQ.insert(1);
        assertFalse(testQ.isEmpty());
        
        testQ.peekMin();
        assertFalse(testQ.isEmpty());
        
        testQ.removeMin();
        assertTrue(testQ.isEmpty());
    }
}
