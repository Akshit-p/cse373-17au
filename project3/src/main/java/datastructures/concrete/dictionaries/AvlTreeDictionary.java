package datastructures.concrete.dictionaries;

import java.util.Iterator;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

public class AvlTreeDictionary<K extends Comparable<K>, V> implements IDictionary<K, V> {
    
    private node root;
    private int size;
    
    private class node {
        private KVPair<K,V> data;
        private node left;
        private node right;
        private int height;
        
        public node(K key, V value) {
            this.data = new KVPair<K,V>(key, value);
            this.left = null;
            this.right = null;
            this.height = 0;
        }
        
        public node(node left, node right) {
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
        if (current != null) {
            if (current.data.getKey().equals(key)) {
                return current.data.getValue();
            }
            if (current.data.getKey().compareTo(key) > 0) { 
                get(key, current.left);
            } else {
                get(key, current.right);
            }
        }
        throw new NoSuchKeyException();
    }

    @Override
    public void put(K key, V value) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public V remove(K key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        try{
            this.get(key,this.root);
            return true;
        } catch(NoSuchKeyException e) {
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
}