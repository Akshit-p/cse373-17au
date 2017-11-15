package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestTopKSortFunctionality extends BaseTest {
    @Test(timeout = SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        int ele = 15;
        for (int i : top) {
            assertEquals(ele++, i);
        }
    }

    @Test(timeout = SECOND, expected = IllegalArgumentException.class)
    public void testException() {
        Searcher.topKSort(-1, new DoubleLinkedList<Integer>());
    }

    @Test(timeout = SECOND)
    public void testKLargerThanSize() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 100; i++) {
            list.add(i);
        }
        IList<Integer> top = Searcher.topKSort(101, list);
        assertEquals(100, top.size());
        int ele = 0;
        for (int i : top) {
            assertEquals(ele++, i);
        }
    }

    @Test(timeout = SECOND)
    public void testKEqualsSize() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 100; i++) {
            list.add(i);
        }
        IList<Integer> top = Searcher.topKSort(100, list);
        assertEquals(100, top.size());
        int ele = 0;
        for (int i : top) {
            assertEquals(ele++, i);
        }
    }

    @Test(timeout = SECOND)
    public void testKEqualsZero() {
        IList<Integer> top = Searcher.topKSort(0, new DoubleLinkedList<Integer>());
        assertTrue(top.isEmpty());
    }

    @Test(timeout = SECOND)
    public void testUnmodifiedList() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 20; i > 0; i--) {
            list.add(i);
        }
        IList<Integer> top = Searcher.topKSort(0, list);
        assertTrue(top.isEmpty());
        top = Searcher.topKSort(19, list);
        assertEquals(19, top.size());
        int ele = 2;
        for (int i : top) {
            assertEquals(ele++, i);
        }
        // Check for no modification for the client list
        ele = 20;
        for (int i : list) {
            assertEquals(ele--, i);
        }
    }

    @Test(timeout = SECOND)
    public void testStringListSimple() {
        IList<String> list = new DoubleLinkedList<>();
        for (int i = 0; i < 10; i++) {
            list.add("a" + i);
        }
        IList<String> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        String ele = "a";
        int count = 5;
        for (String i : top) {
            assertEquals(ele + (count++), i);
        }
    }

    @Test(timeout = SECOND)
    public void testStringListComplex() {
        List<String> sortList = new ArrayList<>(500);

        IList<String> list = new DoubleLinkedList<>();
        for (int i = 0; i < 500; i++) {
            list.add("a" + i);
            sortList.add("a" + i);
        }
        Collections.sort(sortList);
        IList<String> top = Searcher.topKSort(200, list);
        assertEquals(200, top.size());
        
        int count = 300;
        for (String i : top) {
            assertEquals(sortList.get(count++), i);
        }
    }
}