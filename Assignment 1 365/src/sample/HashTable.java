package sample;

import java.util.ArrayList;
import java.util.List;

// A node of chains
class HashNode<K, V> {
    K key;
    V value;

    // Reference to next node
    HashNode<K, V> next;

    // Constructor
    public HashNode(K key, V value) {
        this.key = key;
        this.value = value;
        HashNode next = null;
    }

}

// Class to represent entire hash table
class Map2<K, V> {
    // bucketArray is used to store array of chains
    private HashNode<K, V>[] bucketArray;

    // Current capacity of array list
    private int numBuckets;

    // Current size of array list
    private int size;

    // Constructor (Initializes capacity, size and
    // empty chains.
    public Map2() {
        numBuckets = 32;
        bucketArray = new HashNode[numBuckets];
        size = 0;

        // Create empty chains
        // for (int i = 0; i < numBuckets; i++)
        //   bucketArray.add();
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    // This implements hash function to find index
    // for a key
    private int getBucketIndex(K key) {
        int hashCode = key.hashCode();
        int index = (hashCode % bucketArray.length);
        return index;
    }

    // Returns value for a key
    public V get(K key) {
        // Find head of chain for given key
        int bucketIndex = getBucketIndex(key);
        HashNode<K, V> head = bucketArray[bucketIndex];

        for (HashNode n = head; n != null; n = n.next) {
            if (n.key.equals(key))
                return (V) n.value;
            n = n.next;
        }
        // If key not found
        return null;
    }

    public List<V> values() {
        List<V> list = new ArrayList<>();
        for (int i = 0; i < bucketArray.length; ++i) {
            for (HashNode p = bucketArray[i]; p != null; p = p.next) {
                list.add((V) p.key);
            }
        }
        return list;
    }

    public List<K> keys() {
        List<K> list = new ArrayList<>();
        for (int i = 0; i < bucketArray.length; ++i) {
            for (HashNode p = bucketArray[i]; p != null; p = p.next) {
                list.add((K) p.key);
            }
        }
        return list;
    }

    // Adds a key value pair to hash
    public void add(K key, V value) {
        // Find head of chain for given key
        int bucketIndex = getBucketIndex(key);
        HashNode<K, V> head = bucketArray[bucketIndex];

        // Check if key is already present
        while (head != null) {
            if (head.key.equals(key)) {
                head.value = value;
                return;
            }
            head = head.next;
        }
        // Insert key in chain
        size++;
        head = bucketArray[bucketIndex];
        HashNode<K, V> newNode = new HashNode<K, V>(key, value);
        newNode.next = head;
        //bucketArray.set(bucketIndex, newNode)[bucketIndex];

        // If load factor goes beyond threshold, then
        // double hash table size
        if ((1.0 * size) / numBuckets >= 0.7) {
            HashNode<K, V> temp = bucketArray[bucketIndex];
            bucketArray = new HashNode[bucketIndex];
            numBuckets = 2 * numBuckets;
            size = 0;
            for (HashNode<K, V> headNode : temp) {
                while (headNode != null) {
                    add(headNode.key, headNode.value);
                    headNode = headNode.next;
                }
            }
        }
    }
}


