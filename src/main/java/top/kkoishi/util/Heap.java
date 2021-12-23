package top.kkoishi.util;

import java.util.AbstractQueue;
import java.util.Comparator;
import java.util.Iterator;

public class Heap<V> extends AbstractQueue<V> implements java.io.Serializable {
    @java.io.Serial
    private final static long serialVersionUID = -1145141919810L;

    private static final int DEFAULT_INITIAL_CAPACITY = (2 * (1 << 3)) + 1;

    private transient int size;
    private transient Object[] elements;
    private transient int modCount;

    private final Comparator<? extends V> comparator;

    public Heap (int size, Comparator<? extends V> comparator) {
        if (size < 1) {
            throw new IllegalArgumentException();
        }
        this.size = size;
        this.comparator = comparator;
        modCount = 0;
        elements = new Object[size];
    }

    public Heap (int size) {
        this(size, null);
    }

    public Heap (Comparator<? extends V> comparator) {
        this(DEFAULT_INITIAL_CAPACITY, null);
    }

    public Heap () {
        this(DEFAULT_INITIAL_CAPACITY, null);
    }

    /**
     * Returns an iterator over the elements contained in this collection.
     *
     * @return an iterator over the elements contained in this collection
     */
    @Override
    public Iterator<V> iterator () {
        return null;
    }

    @Override
    public int size () {
        return 0;
    }

    /**
     * Inserts the specified element into this queue if it is possible to do
     * so immediately without violating capacity restrictions.
     * When using a capacity-restricted queue, this method is generally
     * preferable to {@link #add}, which can fail to insert an element only
     * by throwing an exception.
     *
     * @param v the element to add
     * @return {@code true} if the element was added to this queue, else
     * {@code false}
     * @throws ClassCastException       if the class of the specified element
     *                                  prevents it from being added to this queue
     * @throws NullPointerException     if the specified element is null and
     *                                  this queue does not permit null elements
     * @throws IllegalArgumentException if some property of this element
     *                                  prevents it from being added to this queue
     */
    @Override
    public boolean offer (V v) {
        return false;
    }

    /**
     * Retrieves and removes the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    @Override
    public V poll () {
        return null;
    }

    /**
     * Retrieves, but does not remove, the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    @Override
    public V peek () {
        return null;
    }
}
