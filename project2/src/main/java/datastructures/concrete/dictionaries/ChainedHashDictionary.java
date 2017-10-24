package datastructures.concrete.dictionaries;

import datastructures.concrete.DoubleLinkedList;
import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * See the spec and IDictionary for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;

    // total number of key-value pairs in this
    private int size = 0;
    // the load threshold for resize operation
    private static final double LOADFACTOR = 0.75;

    // You're encouraged to add extra fields (and helper methods) though!

    public ChainedHashDictionary() {
        this.chains = this.makeArrayOfChains(11);
    }

    /**
     * This method will return a new, empty array of the given size that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int arraySize) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[arraySize];
    }

    /**
     * Helper method to produce hashCode for a given a key to hash and the length of this being the max threshold
     * 
     * @param key
     *            the object for which hashCode needs to be found
     * @return the hash code of the key from [0, this.Length)
     */
    private int hashValue(K key) {
        return this.hashValue(key, this.chains.length);
    }

    /**
     * Helper method to produce hashCode, given the max threshold and the key to hash
     * 
     * @param key
     *            the object for which hashCode needs to be found
     * @param length
     *            the maximum threshold until which the hashCode is desired.
     * @return the hash code of the key from [0, this.Length)
     */
    private int hashValue(K key, int length) {
        // the resulting hash needs to be mod by array length to prevent index out of bound
        return (key == null ? 0 : Math.abs(3 * key.hashCode() % length));
    }

    @Override
    public V get(K key) {
        IDictionary<K, V> dict = this.chains[this.hashValue(key)];
        if (dict == null) {
            throw new NoSuchKeyException();
        }
        return dict.get(key);
    }

    @Override
    public void put(K key, V value) {
        this.grow();
        putPairIn(key, value, this.chains);
    }

    /**
     * Helper method to put key value pairs in the given array. Will do maintenance if the array passed is this.chains
     * 
     * @param key
     *            the object which is the key in this
     * @param value
     *            the object which is the value for the key in this
     * @param table
     *            the array object for which the pair needs to be added.
     */
    private void putPairIn(K key, V value, IDictionary<K, V>[] table) {
        int index = this.hashValue(key, table.length);
        IDictionary<K, V> dict = table[index];

        // initialize index if not already and perform size maintenance if table = this.chains
        if (dict == null) {
            IDictionary<K, V> data = new ArrayDictionary<>();
            data.put(key, value);
            table[index] = data;
            if (table == this.chains) {
                this.size++;
            }
        } else {
            int sizeBefore = dict.size();
            dict.put(key, value);
            // update the size of this if we added a new pair.
            if (table == this.chains && sizeBefore < dict.size()) {
                this.size++;
            }
        }
    }

    /**
     * The method resizes and re-hashes data in this if we have surpassed the LOADFACTOR.
     */
    private void grow() {
        if (((double) this.size) / this.chains.length > LOADFACTOR) {
            IDictionary<K, V>[] temp = this.makeArrayOfChains(this.chains.length * 2 + 1);
            for (int i = 0; i < this.chains.length; i++) {
                IDictionary<K, V> data = this.chains[i];
                if (data != null) {
                    for (KVPair<K, V> pairs : data) {
                        putPairIn(pairs.getKey(), pairs.getValue(), temp);
                    }
                }
            }
            this.chains = temp;
        }
    }

    @Override
    public V remove(K key) {
        if (!this.containsKey(key)) {
            throw new NoSuchKeyException();
        }
        int index = this.hashValue(key);
        this.size--;
        return this.chains[index].remove(key);
    }

    @Override
    public boolean containsKey(K key) {
        if (this.size != 0) {
            int index = this.hashValue(key);
            IDictionary<K, V> temp = this.chains[index];
            return (temp == null ? false : temp.containsKey(key));
        }
        return false;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration state. You can add as many fields as you want. If
     * it helps, our reference implementation uses three (including the one we gave you).
     *
     * 2. Before you try and write code, try designing an algorithm using pencil and paper and run through a few
     * examples by hand.
     *
     * 3. Think about what exactly your *invariants* are. An *invariant* is something that must *always* be true once
     * the constructor is done setting up the class AND must *always* be true both before and after you call any method
     * in your class.
     *
     * Once you've decided, write them down in a comment somewhere to help you remember.
     *
     * You may also find it useful to write a helper method that checks your invariants and throws an exception if
     * they're violated. You can then call this helper method at the start and end of each method if you're running into
     * issues while debugging.
     *
     * (Be sure to delete this method once your iterator is fully working.)
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators are meant to be lightweight and so should not be
     * copying the data contained in your dictionary to some other data structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary instance inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;

        // all the indices where we can find data for the key-value pairs
        private IList<Integer> indices;

        // iterate over the arrayDictionary objects, needs to maintained properly
        private Iterator<KVPair<K, V>> iterateDict;

        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            this.indices = this.getIterableIndices();
            this.iterateDict = this.getIterator();
        }

        /**
         * returns the iterator of object in this.chains
         * 
         * @return iterator of IDictionary object in this.chains otherwise null.
         */
        private Iterator<KVPair<K, V>> getIterator() {
            if (!this.indices.isEmpty()) {
                // get the index of the element we are still left to iterate.
                int index = this.indices.remove();
                return this.chains[index].iterator();
            }
            return null;
        }

        /**
         * Returns a list of all indices which have been initialized in 
         * this.chains i.e. contains an IDicitonary object
         */
        private IList<Integer> getIterableIndices() {
            IList<Integer> temp = new DoubleLinkedList<>();
            for (int i = 0; i < chains.length; i++) {
                if (chains[i] != null) {
                    temp.add(i);
                }
            }
            return temp;
        }

        @Override
        public boolean hasNext() {
            if (this.iterateDict != null) {
                if (!this.iterateDict.hasNext() && !this.indices.isEmpty()) {
                    // change the current iterator which has no elements left to
                    // the next iterable index Iterator.
                    this.iterateDict = this.getIterator();
                }
                return this.iterateDict.hasNext();
            }
            return false;
        }

        @Override
        public KVPair<K, V> next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.iterateDict.next();
        }
    }
}
