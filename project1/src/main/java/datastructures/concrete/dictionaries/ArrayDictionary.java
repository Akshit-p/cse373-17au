package datastructures.concrete.dictionaries;

import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;
import misc.exceptions.NotYetImplementedException;

/**
 * See IDictionary for more details on what this class should do
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private Pair<K, V>[] pairs;
    
    // number of elements in the dictionary.
    private int size = 0;
    
    
    public ArrayDictionary() {
        this.pairs = makeArrayOfPairs(10);
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);

    }

    @Override
    public V get(K key) {
        int index = getIndexOf(key);
        if (index < 0){
            throw new NoSuchKeyException();
        }
        return this.pairs[index].value;
        
    }

    @Override
    public void put(K key, V value) {
        // grow the array if full else add the pair or update.
        if (this.size == this.pairs.length) {
            grow();
        }
        
        int index = getIndexOf(key);
        // if new key found then add the new pair.
        if (index == -1) {
            this.pairs[this.size] = new Pair<K, V>(key,value);
            this.size++;
        } else {    
            this.pairs[index].value = value;
        }
    }
    
    /**
     * Expands the size of the array to be twice original.
     */
    private void grow() {
        int length = this.pairs.length;
        // double the size for new array
        Pair<K,V>[] copy = makeArrayOfPairs(length*2);
        for (int i = 0; i < length; i++) {
            copy[i] = new Pair<K,V>(this.pairs[i]);
        }
        this.pairs = copy;
    }
    
    
    
    /**
     * Returns the index of the key to be found in this.
     * 
     * @param key the key to be searched in this.
     * @return the index of the key if found, otherwise -1.
     */
    private int getIndexOf(K key) {
        // loop until we have elements and get the index
        for (int i = 0; i < this.size; i++) {
            K checkKey = this.pairs[i].key;
            // check for null keys too.
            if((key != null && checkKey.equals(key)) || key == checkKey) {
                return i;
            }
        }
        
        // no index for given key found
        return -1;
    }

    @Override
    public V remove(K key) {
        int index = getIndexOf(key);
        if (index < 0) {
            throw new NoSuchKeyException();
        }
        V value = this.pairs[index].value;
        shift(index);
        size--;
        return value;
    }

    private void shift(int index) {
        if (this.size > 0) {
            System.arraycopy(this.pairs, index + 1, this.pairs, index, size - index - 1);
        }
        
    }

    @Override
    public boolean containsKey(K key) {
        if (getIndexOf(key) >= 0) {
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return this.size;
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }
        
        /**
         * Copy constructor to create new instance of 
         * pair form other Pair instance.
         * 
         * @param other the Pair object from which to create a new instance
         */
        public Pair(Pair<K, V> other) {
            this.key = other.key;
            this.value = other.value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }
}
