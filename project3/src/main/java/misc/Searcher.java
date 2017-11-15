package misc;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;

public class Searcher {
    /**
     * This method takes the input list and returns the top k elements in sorted order.
     *
     * So, the first element in the output list should be the "smallest" element; the last element should be the
     * "biggest".
     *
     * If the input list contains fewer then 'k' elements, return a list containing all input.length elements in sorted
     * order.
     *
     * This method must not modify the input list.
     *
     * @throws IllegalArgumentException
     *             if k < 0
     */
    public static <T extends Comparable<T>> IList<T> topKSort(int k, IList<T> input) {
        // Implementation notes:
        //
        // - This static method is a _generic method_. A generic method is similar to
        // the generic methods we covered in class, except that the generic parameter
        // is used only within this method.
        //
        // You can implement a generic method in basically the same way you implement
        // generic classes: just use the 'T' generic type as if it were a regular type.
        //
        // - You should implement this method by using your ArrayHeap for the sake of
        // efficiency.

        if (k < 0) {
            throw new IllegalArgumentException();
        } else if (k == 0) {
            return new DoubleLinkedList<>();
        } else if (k > input.size()) {
            k = input.size();
        }
        
        IPriorityQueue<T> minHeap = new ArrayHeap<>();
        IList<T> list = new DoubleLinkedList<>();
        // iterator needs to be used to ensure O(1) gets.
        int count = 0;
        for (T ele: input) {
            if (count == input.size()) {
                break;
            } else if (count < k) {
                // this method is necessary to ensure O(nlog(k))) runtime as the heap size it at max K.
                minHeap.insert(ele);
            } else {
                T minVal = minHeap.peekMin();
                if (ele.compareTo(minVal) > 0) {
                    minHeap.removeMin();
                    minHeap.insert(ele);
                }
            }
            count++;
        }
        //add the elements to the list in sorted order
        while (!minHeap.isEmpty()) {
            list.add(minHeap.removeMin());
        }
        return list;
    }
}
