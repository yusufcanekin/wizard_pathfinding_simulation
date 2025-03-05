package data_structures;

import java.util.Arrays;

/**
 * A generic min-heap implementation with decrease-key functionality,
 * utilizing a custom HashTable for tracking element indices.
 *
 * @param <T> the type of elements stored in the heap; must be comparable
 */
public class MinHeap<T extends Comparable<T>> {
    private T[] heap; // 1-based index heap array
    private int size; // Number of elements in the heap
    private int capacity; // Current capacity of the heap
    /**
     * Constructs a new MinHeap with the specified initial capacity.
     *
     * @param capacity the initial capacity of the heap
     */
    public MinHeap(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.heap = (T[]) new Comparable[capacity + 1]; // Index 0 is unused
    }

    /**
     * Swaps two elements in the heap and updates their indices in the indexMap.
     *
     * @param i the index of the first element
     * @param j the index of the second element
     */
    private void swap(int i, int j) {
        T temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    /**
     * Moves the element at the specified index up to its correct position
     * to maintain the min-heap property.
     *
     * @param index the index of the element to adjust
     */
    private void percolateUp(int index) {
        while (index > 1 && heap[index / 2].compareTo(heap[index]) > 0) {
            swap(index, index / 2);
            index = index / 2;
        }
    }

    /**
     * Moves the element at the specified index down to its correct position
     * to maintain the min-heap property.
     *
     * @param index the index of the element to adjust
     */
    private void percolateDown(int index) {
        while (2 * index <= size) {
            int smallest = 2 * index;
            if (smallest < size && heap[smallest].compareTo(heap[smallest + 1]) > 0) {
                smallest++;
            }
            if (heap[index].compareTo(heap[smallest]) <= 0) {
                break;
            }
            swap(index, smallest);
            index = smallest;
        }
    }

    /**
     * Inserts a new element into the heap.
     *
     * @param value the element to insert
     */
    public void insert(T value) {
        if (size >= capacity) {
            resize();
        }
        heap[++size] = value;
        percolateUp(size);
    }

    /**
     * Removes and returns the minimum element (the root) of the heap.
     *
     * @return the minimum element, or {@code null} if the heap is empty
     */
    public T getMin() {
        if (size == 0) return null;
        T min = heap[1];
        heap[1] = heap[size];
        heap[size] = null;
        size--;
        percolateDown(1);
        return min;
    }

    /**
     * Checks if the heap is empty.
     *
     * @return {@code true} if the heap is empty, {@code false} otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Resizes the heap by doubling its capacity.
     */
    private void resize() {
        capacity *= 2;
        heap = Arrays.copyOf(heap, capacity + 1);
    }
}
