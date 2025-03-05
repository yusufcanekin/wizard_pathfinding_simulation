package data_structures;

import java.util.ArrayList;

/**
 * A generic hash table implementation using separate chaining for collision handling.
 *
 * @param <K> the type of keys maintained by this hash table
 * @param <V> the type of mapped values
 */
public class HashTable<K, V> {
    /**
     * Represents a single node in the hash table's linked list.
     *
     * @param <K> the type of the key
     * @param <V> the type of the value
     */
    private static class Node<K, V> {
        K key; // Key associated with the node
        V value; // Value associated with the key
        Node<K, V> next; // Reference to the next node in the chain

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node<K, V>[] table; // Array of linked list heads
    private int size; // Number of elements in the hash table
    private final int initialCapacity = 1000; // Default capacity of the hash table

    /**
     * Constructs a new hash table with an initial capacity.
     */
    public HashTable() {
        this.table = (Node<K, V>[]) new Node[initialCapacity];
        this.size = 0;
    }

    /**
     * Computes the index in the table array for the given key.
     *
     * @param key the key to hash
     * @return the index for the given key
     */
    private int getBucketIndex(K key) {
        return Math.abs(key.hashCode() % table.length);
    }

    /**
     * Inserts a key-value pair into the hash table. If the key already exists, the operation fails.
     *
     * @param key   the key to insert
     * @param value the value to associate with the key
     * @return {@code true} if the key-value pair was inserted, {@code false} if the key already exists
     */
    public boolean put(K key, V value) {
        int bucketIndex = getBucketIndex(key);
        Node<K, V> head = table[bucketIndex]; // Head of the chain at the relevant bucket index

        // Check if the key already exists in the chain
        while (head != null) {
            if (head.key.equals(key)) {
                head.value = value;
                return true; // If key already exists, fail the operation
            }
            head = head.next;
        }

        // Add a new node to the front of the chain
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = table[bucketIndex];
        table[bucketIndex] = newNode;
        size++;

        // Resize the table if the load factor exceeds 0.5
        if ((1.0 * size) / table.length > 0.5) {
            resize();
        }
        return true;
    }

    /**
     * Retrieves the value associated with the given key.
     *
     * @param key the key to look up
     * @return the value associated with the key, or {@code null} if the key does not exist
     */
    public V get(K key) {
        int bucketIndex = getBucketIndex(key);
        Node<K, V> head = table[bucketIndex]; // Head of the chain at the relevant bucket index

        // Search the chain for the key
        while (head != null) {
            if (head.key.equals(key)) {
                return head.value;
            }
            head = head.next;
        }
        return null; // Key not found
    }

    /**
     * Removes the key-value pair associated with the given key.
     *
     * @param key the key to remove
     * @return {@code true} if the key was found and removed, {@code false} otherwise
     */
    public boolean remove(K key) {
        int bucketIndex = getBucketIndex(key);
        Node<K, V> head = table[bucketIndex];  // Head of the chain at the relevant bucket index
        Node<K, V> prev = null; // Track the previous node during traversal

        // Search the chain for the key
        while (head != null) {
            if (head.key.equals(key)) {
                // Remove the node
                if (prev == null) {
                    table[bucketIndex] = head.next; // Remove the first node
                } else {
                    prev.next = head.next; // Skip the current node
                }
                size--;
                return true; // Key removed successfully
            }
            prev = head;
            head = head.next;
        }
        return false; // Key not found
    }

    /**
     * Checks if the hash table contains the specified key.
     *
     * @param key the key to look for
     * @return {@code true} if the key exists, {@code false} otherwise
     */
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    /**
     * Resizes the hash table when the load factor exceeds 0.5.
     * Doubles the capacity and rehashes all elements.
     */
    private void resize() {
        Node<K, V>[] oldTable = table; // Store the old table
        table = (Node<K, V>[]) new Node[2 * oldTable.length]; // Double the table capacity
        size = 0; // Reset the size

        // Rehash all elements in the old table
        for (Node<K, V> head : oldTable) {
            while (head != null) {
                put(head.key, head.value);
                head = head.next;
            }
        }
    }

    /**
     * Retrieves the value associated with the given key, or returns the specified default value
     * if the key does not exist in the hash table.
     *
     * @param key the key to look up
     * @param defaultValue the default value to return if the key does not exist
     * @return the value associated with the key, or the default value if the key is not found
     */
    public V getOrDefault(K key, V defaultValue){
        V value = this.get(key);
        if (value != null){
            return value;
        }
        return defaultValue;
    }

    /**
     * Retrieves the value associated with the given key, or initializes a new list if the key
     * does not exist. The newly initialized list is associated with the key and stored in the hash table.
     *
     * @param key the key to look up
     * @return the existing or newly created list of nodes associated with the key
     */
    public ArrayList<models.Node> getOrPut(K key){
        // Attempt to retrieve the value associated with the key
        V value = this.get(key);
        if (value != null){
            return (ArrayList<models.Node>)value; // Return the existing value if it exists
        }

        // Create a new list if the key does not already exist, store the new list in HashTable.
        ArrayList<models.Node> listOfNodes = new ArrayList<>();
        this.put(key, (V) listOfNodes);
        return listOfNodes;
    }
}
