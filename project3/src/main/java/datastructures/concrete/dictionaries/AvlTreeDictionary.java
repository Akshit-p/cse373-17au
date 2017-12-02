package datastructures.concrete.dictionaries;

import java.util.Iterator;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

public class AvlTreeDictionary<K extends Comparable<K>, V> implements IDictionary<K, V> {

    private node root;
    private int size;

    private class node {
        private KVPair<K, V> data;
        private node left;
        private node right;
        private int height;

        public node(K key, V value) {
            this.data = new KVPair<K, V>(key, value);
            this.left = null;
            this.right = null;
            this.height = 0;
        }

        public node(K key, V value, node left, node right) {
            this.data = new KVPair<K, V>(key, value);
            this.left = left;
            this.right = right;
            this.height = left.height + right.height;
        }
    }

    public AvlTreeDictionary() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public V get(K key) {
        return get(key, this.root);
    }

    private V get(K key, node current) {
        if (current == null) {
            throw new NoSuchKeyException();
        }
        if (current.data.getKey().compareTo(key) == 0) {
            return current.data.getValue();
        } else {
            if (current.data.getKey().compareTo(key) > 0) {
                return get(key, current.left);
            } else {
                return get(key, current.right);
            }
        }
    }

    @Override
    public void put(K key, V value) {
        this.root = put(key, value, this.root);
    }

    private node put(K key, V value, node current) {
        if (current == null) {
            return new node(key, value);
        }
        if (current.data.getKey().compareTo(key) == 0) {
            return new node(key, value);
        }
        if (key.compareTo(current.data.getKey()) < 0) {
            current.left = put(key, value, current.left);
        } else {
            current.right = put(key, value, current.right);
        }
        this.size++;
        return current;
    }

    @Override
    public V remove(K key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        try {
            this.get(key, this.root);
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    private node rotate() {
        return null;
    }
}