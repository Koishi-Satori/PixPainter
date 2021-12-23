

package top.kkoishi.util;

import java.nio.InvalidMarkException;
import java.util.*;

/**
 * Hash table based implementation of the {@code Map} interface.  This
 * implementation provides all the optional map operations, and permits
 * {@code null} values and the {@code null} key.  (The {@code HashMap}
 * class is roughly equivalent to {@code Hashtable}, except that it is
 * un-synchronized and permits nulls.)  This class makes no guarantees as to
 * the order of the map; in particular, it does not guarantee that the order
 * will remain constant over time.
 *
 * <br>
 * <br>
 * And if you offer {@code null} key in the HashMap, the value will be all add
 * to the first node.Also,I only use the LinkedList to solve the hash crush.
 * The time to search by key is O(1).
 *
 * @param <K> The key of the key-value entry which is stored in the HashMap.
 * @param <V> The value of the key-value entry which is stored in the HashMap.
 * @author KKoishi
 * @apiNote The first time to write a HashMap for me.
 * @see Object#hashCode()
 * @see Collection
 * @see Map
 * @since java8
 */
public class HashMap<K, V> extends AbstractMap<K, V> implements Map<K, V>, Cloneable {
    /**
     * Returns a copy of the HashMap,which contains all the element of the origial one.
     *
     * @return A new HashMap.
     * @throws AssertionError,CloneNotSupportedException when failed to clone.
     */
    @Override
    @SuppressWarnings("all")
    public HashMap<K, V> clone () throws AssertionError, CloneNotSupportedException {
        try {
            HashMap clone = (HashMap) super.clone();
            clone.data = this.data;
            clone.size = this.size;
            clone.arraySize = this.arraySize;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /**
     * inner static class,the Node of the LinkedList.
     *
     * @param <K> key
     * @param <V> value
     */
    static class Node<K, V> implements Entry<K, V> {
        /**
         * Data field
         */
        K key;
        V value;
        Node<K, V> next;
        int hashcode;
        int size;

        public Node (K key, V value) {
            this();
            this.key = key;
            this.value = value;
        }

        public Node () {
            size = 1;
        }

        @Override
        public final K getKey () {
            return key;
        }

        @Override
        public final V getValue () {
            return value;
        }

        @Override
        public final V setValue (V value) {
            V temp = this.value;
            this.value = value;
            return temp;
        }

        public void add (K key, V value) {
            size++;
            Node<K, V> node = new Node<>(key, value);
            Node<K, V> pointer = this;
            while (pointer.next != null) {
                pointer = pointer.next;
            }
            pointer.next = node;
        }

        /**
         * remove an element from the HashMap's node(LinkedList)
         *
         * @param key the key of the element
         * @return the node which is removed.
         * @throws IndexOutOfBoundsException when the key is not in the HashMap.
         */
        public V removeAndGet (K key) {
            Node<K, V> temp = get(key);
            unlinkElement(key);
            return temp.value;
        }

        public boolean contains (K key) {
            Node<K, V> pointer = this;
            while (pointer.next != null) {
                if (pointer.key.equals(key)) {
                    return true;
                }
            }
            return pointer.key.equals(key);
        }

        public Node<K, V> get (K key) {
            Node<K, V> pointer = this;
            while (pointer.next != null) {
                if (pointer.key.equals(key)) {
                    return pointer;
                }
            }
            return (pointer.key.equals(key) ? pointer : new Node<>());
        }

        public void remove (K key) {
            unlinkElement(key);
        }

        private void unlinkElement (K key) {
            Node<K, V> pointer = this;
            Node<K, V> fasterPointer = this.next;
            while (fasterPointer.next != null) {
                if (fasterPointer.key.equals(key)) {
                    break;
                }
                fasterPointer = fasterPointer.next;
                pointer = pointer.next;
            }
            if (fasterPointer.key.equals(key)) {
                size--;
                pointer.next = fasterPointer.next;
            }
        }

        @Override
        public final boolean equals (Object o) {
            if (this == o) {
                return true;
            }
            if (o instanceof Map.Entry) {
                Node<?, ?> node = (Node<?, ?>) o;
                return Objects.equals(this.value, node.getValue()) &&
                        Objects.equals(this.key, node.getKey());
            }
            return false;
        }

        @Override
        public final int hashCode () {
            hashcode = (key.hashCode() ^ value.hashCode()) << 1 + 5;
            return hashcode;
        }

        public V replace (K key, V value) {
            Node<K, V> pointer = this;
            while (pointer.next != null) {
                if (pointer.key.equals(key)) {
                    return pointer.setValue(value);
                }
            }
            if (pointer.key.equals(key)) {
                return pointer.setValue(value);
            }
            return null;
        }

        public List<Node<K, V>> toList () {
            Node<K, V> pointer = this;
            List<Node<K, V>> l = new ArrayList<>();
            while (pointer.next != null) {
                pointer = pointer.next;
                l.add(pointer);
            }
            l.add(pointer);
            return l;
        }

        @Override
        public String toString () {
            StringBuilder b = new StringBuilder();
            Node<K, V> pointer = this;
            while (pointer.next != null) {
                pointer = pointer.next;
                b.append("key:")
                        .append(pointer.key)
                        .append("&value:")
                        .append(pointer.value)
                        .append(" ,");
            }
            b.append("key:")
                    .append(pointer.key)
                    .append("&value:")
                    .append(pointer.value);
            return b.toString();
        }
    }

    /**
     * The default initial capacity - MUST be a power of two.
     */
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    // aka 16

    /**
     * The maximum capacity, used if a higher value is implicitly specified
     * by either of the constructors with arguments.
     * MUST be a power of two <= 1<<30.
     */
    static final int MAXIMUM_CAPACITY = 1 << 30;

    /*------------------------------------------------------------*/
    //Start Data Field

    /**
     * The size of the HashMap.
     * Actually,the amount of elements in the hashmap.
     */
    transient private int size;

    /**
     * The data of nodes.
     * The length of the array should be smaller than 1<<30.
     */
    transient private Node<K, V>[] data;

    /**
     * The size of the node array.
     */
    transient private int arraySize;

    //End Data Field
    /*------------------------------------------------------------*/

    /**
     * The only and default construct method of the HashMap.
     * <br>
     * The initial capacity of the data array will be 1 << 4(16)
     */
    public HashMap () {
        resize(DEFAULT_INITIAL_CAPACITY);
        arraySize = DEFAULT_INITIAL_CAPACITY;
        size = 0;
    }

    /**
     * reset the size of the bucket.
     * @param newCap new Size.
     * @throws IllegalArgumentException when size < 0.
     * @throws InvalidMarkException when size >1 << 30.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void resize (int newCap) throws IllegalArgumentException, InvalidMarkException {
        if (newCap <= 0) {
            throw new IllegalArgumentException();
        } else if (newCap >= MAXIMUM_CAPACITY) {
            throw new InvalidMarkException();
        }
        arraySize = newCap;
        if (data == null) {
            data = (Node<K, V>[]) new Node[newCap];
        } else {
            Node<K, V>[] oldData = data;
            data = (Node<K, V>[]) new Node[newCap];
            System.arraycopy(oldData, 0, data, 0, oldData.length - 1);
        }
    }

    /**
     * Hash function.
     * @param key the key
     * @return the hash
     */
    private int hash (Object key) {
        int hash;
        return ((key == null) ? 0 : (hash = Objects.hashCode(key)) ^ (hash >>> 16));
    }

    /**
     * Replace the old value of the node(indexed by key) to a new one.
     * @deprecated
     * @param key key
     * @param oldValue old one
     * @param newValue new one
     * @return if replace completed.
     */
    @Override
    @Deprecated
    public boolean replace (K key, V oldValue, V newValue) {
        Node<K, V>[] tab;
        Node<K, V> node;
        int hash;
        if ((tab = data) != null && size > 0) {
            hash = hash(key);
            if ((node = tab[hash]) != null) {
                node.replace(key, newValue);
                return true;
            }
        }
        return false;
    }

    /**
     * Replace the old value of the node(indexed by key) to a new one.
     * <br>
     * You should use {@code replace(K, V)}, for that this method actually
     * invoke that method.
     * @param key key
     * @param newValue new one
     * @return if complete
     */
    public boolean set (K key, V newValue) {
        return replace(key, newValue) != null;
    }

    /**
     * Replace the old value of the node(indexed by key) to a new one.
     * @param key key
     * @param value new one
     * @return if complete
     */
    @Override
    public V replace (K key, V value) {
        Node<K, V>[] tab;
        Node<K, V> node;
        int hash;
        if ((tab = data) != null && size > 0) {
            hash = hash(key);
            if ((node = tab[hash]) != null) {
                return node.replace(key, value);
            }
        }
        return null;
    }

    /**
     * the size of the hashmap(the amount of the elements)
     * @return size
     */
    @Override
    public int size () {
        return size;
    }

    /**
     *
     * @return if the hashmap is empty.
     */
    @Override
    public boolean isEmpty () {
        return size == 0;
    }

    @Override
    public boolean containsKey (Object key) {
        return getNode(key) != null;
    }

    @Override
    public boolean containsValue (Object value) {
        Node<K, V>[] tab;
        V v;
        if ((tab = data) != null && size > 0) {
            for (Node<K, V> node : tab) {
                for (; node != null; node = node.next) {
                    if ((v = node.value) == value || (value != null && value.equals(v))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public V get (Object key) {
        Node<K, V> e;
        return ((e = getNode(key)) != null ? e.getValue() : null);
    }

    final Node<K, V> getNode (Object key) {
        Node<K, V>[] tab;
        Node<K, V> node, pointer;
        int hash;
        K k;
        if ((tab = data) != null && key != null) {
            hash = hash(key);
            if ((node = tab[hash]) != null) {
                if (node.next == null) {
                    return node;
                } else {
                    pointer = node;
                    while (pointer.next != null) {
                        if ((k = pointer.key) == key || key.equals(k)) {
                            return pointer;
                        }
                        pointer = pointer.next;
                    }
                    return ((k = pointer.key) == key || key.equals(k) ? pointer : null);
                }
            }
        }
        return null;
    }

    @Override
    public V put (K key, V value) {
        return putVal(hash(key), key, value);
    }

    final V putVal (int hash, K key, V value) {
        if (hash >= arraySize) {
            resize(hash + 1);
        }

        Node<K, V> pointer = data[hash];
        if (pointer != null) {
            if (pointer.contains(key)) {
                return pointer.replace(key, value);
            } else {
                size++;
                pointer.size++;
                pointer.add(key, value);
            }
        } else {
            size++;
            Node<K, V> node = new Node<>(key, value);
            node.size++;
            data[hash] = node;
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public V remove (Object key) throws NullPointerException {
        Node<K, V>[] tab;
        Node<K, V> node;
        int hash;
        if ((tab = data) != null && size > 0) {
            hash = hash(key);
            if ((node = tab[hash]) != null) {
                return node.removeAndGet((K) key);
            } else {
                return null;
            }
        } else {
            throw new NullPointerException();
        }
    }

    @Override
    @Deprecated
    public void putAll (Map<? extends K, ? extends V> m) {
        int s = m.size();
        if (s > size) {
            resize(s);
        }

        for (Entry<? extends K, ? extends V> e : m.entrySet()) {
            K key = e.getKey();
            V value = e.getValue();
            putVal(hash(key), key, value);
        }
    }

    @Override
    public void clear () {
        data = null;
        resize(DEFAULT_INITIAL_CAPACITY);
        size = 0;
        arraySize = DEFAULT_INITIAL_CAPACITY;
    }

    @Override
    @Deprecated
    public Set<K> keySet () {
        Collection<K> keys = new ArrayList<>();
        for (Node<K, V> node : data) {
            List<Node<K, V>> temp = node.toList();
            for (Node<K, V> n : temp) {
                keys.add(n.key);
            }
        }
        return (Set<K>) keys;
    }

    @Override
    public Collection<V> values () {
        Collection<V> values = new ArrayList<>();
        for (Node<K, V> node : data) {
            List<Node<K, V>> temp = node.toList();
            for (Node<K, V> n : temp) {
                values.add(n.value);
            }
        }
        return values;
    }

    @Override
    @Deprecated
    public Set<Entry<K, V>> entrySet () {
        return null;
    }

    @Override
    @Deprecated
    public boolean equals (Object o) {
        return false;
    }

    @Override
    public String toString () {
        StringBuilder b = new StringBuilder();
        for (Node<K, V> node : data) {
            b.append("[").append(node.toString()).append("]");
        }
        return b.toString();
    }

    @Override
    public int hashCode () {
        return Objects.hashCode(this);
    }

    public List<K> keyList () {
        List<K> ks = new ArrayList<>();
        for (Node<K, V> node : data) {
            if (node != null) {
                Node<K, V> pointer = node;
                ks.add(pointer.key);
                while (pointer.next != null) {
                    pointer = pointer.next;
                    ks.add(pointer.key);
                }
            }
        }
        return ks;
    }
}

class Test {
    public static void main (String[] args) {
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        hashMap.put(1, 1);
        hashMap.put(0, 0);
        hashMap.put(2, 2);
        for (int i = 0; i < 10000; i++) {
            hashMap.put(i, i);
        }
        System.out.println(hashMap.containsKey(999) + " " + hashMap.size());
        System.out.println(hashMap.get(999));
        System.out.println(hashMap);
    }
}
