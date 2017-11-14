package datastructures.sorting;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.Set;
import java.util.TreeSet;

import misc.BaseTest;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;

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
    
    private Set<String> testSet;
    
    private IPriorityQueue<String> makeStringHeap(int type){
        IPriorityQueue<String> heap = this.makeInstance();
        if (type == 1) {
            // unique elements
            testSet = new TreeSet<>();
            for (int i = 0; i < 500; i++) {
                heap.insert("e" + i);
                testSet.add("e"+i);
                
            }
        } else if (type == 2) {
            // only duplicated of one element but last element is large
            for (int i = 0; i < 99; i++) {
                heap.insert("e1");
            }
            heap.insert("ee");
        } else if (type == 3) {
            // mix of elements
            heap.insert("e1e");
            heap.insert("e1E");
            heap.insert("e1");
            heap.insert("e11");
            heap.insert("e1");
            heap.insert("ee");
            heap.insert("eee");
            heap.insert("e2e");
            heap.insert("ee2");
            // [e1, e1e, e1E, e11, e1, ee, eee, e2e, ee2]
        }
        return heap;
    }
    
    @Test
    public void testRemoveMinInteger() {
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
    
    @Test(timeout=SECOND)
    public void testRemoveMinStrings(){ 
        IPriorityQueue<String> stringQ = makeStringHeap(3);
        // e1,e1,e11,e1E,e1e,e2e,ee,ee2,eee
        assertEquals("e1", stringQ.removeMin());
        assertEquals("e1", stringQ.removeMin());
        assertEquals("e11", stringQ.removeMin());
        assertEquals("e1E", stringQ.removeMin());
        assertEquals("e1e", stringQ.removeMin());
        assertEquals("e2e", stringQ.removeMin());
        assertEquals("ee", stringQ.removeMin());
        assertEquals("ee2", stringQ.removeMin());
        assertEquals("eee", stringQ.removeMin());
    }
    
    @Test(expected=EmptyContainerException.class, timeout=SECOND)
    public void testRemoveMinException() {
        makeInstance().removeMin();
    }
    
    @Test(timeout=SECOND)
    public void testRemoveMinExhaustive() {
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
        // string inputs
        IPriorityQueue<String> stringQ = makeStringHeap(1);
        qSize = 500;
        for (String ele: testSet) {
            assertEquals(ele, stringQ.removeMin());
            assertEquals(--qSize, stringQ.size());
        }
        
        // duplicate string inputs        
        qSize = 100;
        stringQ = makeStringHeap(2);
        for (int i = 0; i < 99; i++) {
            assertEquals("e1", stringQ.removeMin());
            assertEquals(--qSize, stringQ.size());
        }
        //last element
        assertEquals("ee", stringQ.removeMin());
        assertTrue(stringQ.isEmpty());
        
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
