/*
 * Copyright (c) 2021. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package top.kkoishi.util;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static java.lang.System.out;

/**
 * @author KKoishi
 */
public class LinkedList<V> extends AbstractSequentialList<V>
        implements List<V>, Deque<V>, Cloneable, Serializable {
    @Override
    @SuppressWarnings("all")
    public LinkedList<V> clone () {
        try {
            LinkedList clone = (LinkedList) super.clone();
            //copy mutable state here, so the clone can't change the internals of the original
            clone.size = this.size;
            clone.head = this.head;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /*----------------------------------Field Start----------------------------------*/

    final static class Node<V> {
        V value;
        Node<V> prev;
        Node<V> next;

        public Node (V value) {
            this.value = value;
        }

        public Node (V value, Node<V> prev, Node<V> next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }

    transient Node<V> head;

    transient Node<V> last;

    transient int size;

    /*------------------------------------Field End------------------------------------*/

    public LinkedList () {
        size = 0;
    }

    public LinkedList (Collection<? extends V> c) {
        this.addAll(c);
    }

    final void linkFirst (V value) {
        final Node<V> first = head;
        final Node<V> root = new Node<>(value, null, first);
        head = root;
        if (first == null) {
            last = root;
        } else {
            first.prev = root;
        }
        size++;
        modCount++;
    }

    final void linkLast (V value) {
        final Node<V> pointer = last;
        final Node<V> node = new Node<>(value, pointer, null);
        last = node;
        if (pointer == null) {
            head = node;
        } else {
            pointer.next = node;
        }
        size++;
        modCount++;
    }

    final void linkBefore (V v, Node<V> node) {
        final Node<V> pre = node.prev;
        final Node<V> newNode = new Node<>(v, pre, node);
        node.prev = newNode;
        if (pre == null) {
            head = newNode;
        } else {
            pre.next = newNode;
        }
        size++;
        modCount++;
    }

    private V unlinkFirst (Node<V> f) {
        final V value = f.value;
        final Node<V> newHead = f.next;
        f.value = null;
        f.next = null;
        head = newHead;
        if (newHead == null) {
            last = null;
        } else {
            newHead.prev = null;
        }
        size--;
        modCount++;
        return value;
    }

    private V unlinkLast (Node<V> l) {
        final V value = l.value;
        final Node<V> newLast = l.prev;
        l.value = null;
        l.prev = null;
        last = newLast;
        if (newLast == null) {
            head = null;
        } else {
            newLast.next = null;
        }
        size--;
        modCount++;
        return value;
    }

    private V unlink (Node<V> node) {
        final V value = node.value;
        final Node<V> pre = node.prev;
        final Node<V> nev = node.next;
        if (pre == null) {
            head = nev;
        } else {
            pre.next = nev;
        }

        if (nev == null) {
            last = pre;
        } else {
            nev.prev = pre;
        }
        node.value = null;
        size--;
        modCount++;
        return value;
    }

    /**
     * Returns a list iterator over the elements in this list (in proper
     * sequence).
     *
     * @param index index of first element to be returned from the list
     *              iterator (by a call to the {@code next} method)
     * @return a list iterator over the elements in this list (in proper
     * sequence)
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public ListIterator<V> listIterator (int index) {
        return this.new lit();
    }

    @Override
    public int size () {
        return size;
    }

    /**
     * Inserts the specified element at the front of this deque if it is
     * possible to do so immediately without violating capacity restrictions,
     * throwing an {@code IllegalStateException} if no space is currently
     * available.  When using a capacity-restricted deque, it is generally
     * preferable to use method {@link #offerFirst}.
     *
     * @param v the element to add
     * @throws IllegalStateException    if the element cannot be added at this
     *                                  time due to capacity restrictions
     * @throws ClassCastException       if the class of the specified element
     *                                  prevents it from being added to this deque
     * @throws NullPointerException     if the specified element is null and this
     *                                  deque does not permit null elements
     * @throws IllegalArgumentException if some property of the specified
     *                                  element prevents it from being added to this deque
     */
    @Override
    public void addFirst (V v) {
        linkFirst(v);
    }

    /**
     * Inserts the specified element at the end of this deque if it is
     * possible to do so immediately without violating capacity restrictions,
     * throwing an {@code IllegalStateException} if no space is currently
     * available.  When using a capacity-restricted deque, it is generally
     * preferable to use method {@link #offerLast}.
     *
     * <p>This method is equivalent to {@link #add}.
     *
     * @param v the element to add
     * @throws IllegalStateException    if the element cannot be added at this
     *                                  time due to capacity restrictions
     * @throws ClassCastException       if the class of the specified element
     *                                  prevents it from being added to this deque
     * @throws NullPointerException     if the specified element is null and this
     *                                  deque does not permit null elements
     * @throws IllegalArgumentException if some property of the specified
     *                                  element prevents it from being added to this deque
     */
    @Override
    public void addLast (V v) {
        linkLast(v);
    }

    /**
     * Inserts the specified element at the front of this deque unless it would
     * violate capacity restrictions.  When using a capacity-restricted deque,
     * this method is generally preferable to the {@link #addFirst} method,
     * which can fail to insert an element only by throwing an exception.
     *
     * @param v the element to add
     * @return {@code true} if the element was added to this deque, else
     * {@code false}
     * @throws ClassCastException       if the class of the specified element
     *                                  prevents it from being added to this deque
     * @throws NullPointerException     if the specified element is null and this
     *                                  deque does not permit null elements
     * @throws IllegalArgumentException if some property of the specified
     *                                  element prevents it from being added to this deque
     */
    @Override
    public boolean offerFirst (V v) {
        addFirst(v);
        return true;
    }

    /**
     * Inserts the specified element at the end of this deque unless it would
     * violate capacity restrictions.  When using a capacity-restricted deque,
     * this method is generally preferable to the {@link #addLast} method,
     * which can fail to insert an element only by throwing an exception.
     *
     * @param v the element to add
     * @return {@code true} if the element was added to this deque, else
     * {@code false}
     * @throws ClassCastException       if the class of the specified element
     *                                  prevents it from being added to this deque
     * @throws NullPointerException     if the specified element is null and this
     *                                  deque does not permit null elements
     * @throws IllegalArgumentException if some property of the specified
     *                                  element prevents it from being added to this deque
     */
    @Override
    public boolean offerLast (V v) {
        addLast(v);
        return true;
    }

    /**
     * Retrieves and removes the first element of this deque.  This method
     * differs from {@link #pollFirst pollFirst} only in that it throws an
     * exception if this deque is empty.
     *
     * @return the head of this deque
     * @throws NoSuchElementException if this deque is empty
     */
    @Override
    public V removeFirst () {
        final Node<V> node = head;
        if (node == null) {
            throw new NoSuchElementException();
        }
        return unlinkFirst(node);
    }

    /**
     * Retrieves and removes the last element of this deque.  This method
     * differs from {@link #pollLast pollLast} only in that it throws an
     * exception if this deque is empty.
     *
     * @return the tail of this deque
     * @throws NoSuchElementException if this deque is empty
     */
    @Override
    public V removeLast () {
        final Node<V> node = last;
        if (node == null) {
            throw new NoSuchElementException();
        }
        return unlinkLast(node);
    }

    /**
     * Retrieves and removes the first element of this deque,
     * or returns {@code null} if this deque is empty.
     *
     * @return the head of this deque, or {@code null} if this deque is empty
     */
    @Override
    public V pollFirst () {
        return removeFirst();
    }

    /**
     * Retrieves and removes the last element of this deque,
     * or returns {@code null} if this deque is empty.
     *
     * @return the tail of this deque, or {@code null} if this deque is empty
     */
    @Override
    public V pollLast () {
        return removeLast();
    }

    /**
     * Retrieves, but does not remove, the first element of this deque.
     * <p>
     * This method differs from {@link #peekFirst peekFirst} only in that it
     * throws an exception if this deque is empty.
     *
     * @return the head of this deque
     * @throws NoSuchElementException if this deque is empty
     */
    @Override
    public V getFirst () {
        if (head == null) {
            throw new NoSuchElementException();
        }
        return head.value;
    }

    /**
     * Retrieves, but does not remove, the last element of this deque.
     * This method differs from {@link #peekLast peekLast} only in that it
     * throws an exception if this deque is empty.
     *
     * @return the tail of this deque
     * @throws NoSuchElementException if this deque is empty
     */
    @Override
    public V getLast () {
        if (last == null) {
            throw new NoSuchElementException();
        }
        return last.value;
    }

    /**
     * Retrieves, but does not remove, the first element of this deque,
     * or returns {@code null} if this deque is empty.
     *
     * @return the head of this deque, or {@code null} if this deque is empty
     */
    @Override
    public V peekFirst () {
        return (head == null ? null : head.value);
    }

    /**
     * Retrieves, but does not remove, the last element of this deque,
     * or returns {@code null} if this deque is empty.
     *
     * @return the tail of this deque, or {@code null} if this deque is empty
     */
    @Override
    public V peekLast () {
        return (last == null ? null : last.value);
    }

    /**
     * Removes the first occurrence of the specified element from this deque.
     * If the deque does not contain the element, it is unchanged.
     * More formally, removes the first element {@code e} such that
     * {@code Objects.equals(o, e)} (if such an element exists).
     * Returns {@code true} if this deque contained the specified element
     * (or equivalently, if this deque changed as a result of the call).
     *
     * @param o element to be removed from this deque, if present
     * @return {@code true} if an element was removed as a result of this call
     * @throws ClassCastException   if the class of the specified element
     *                              is incompatible with this deque
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null and this
     *                              deque does not permit null elements
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public boolean removeFirstOccurrence (Object o) {
        return remove(o);
    }

    /**
     * Remove the first element of {@code o} from the list.
     * if it is present.  If this list does not contain the element, it is
     * unchanged.  More formally, removes the element with the lowest index
     * {@code i} such that
     * {@code Objects.equals(o, get(i))}
     * (if such an element exists).  Returns {@code true} if this list
     * contained the specified element (or equivalently, if this list
     * changed as a result of the call).
     *
     * @param o element to be removed from this list, if present
     * @return {@code true} if this list contained the specified element
     */
    @Override
    public boolean remove (Object o) {
        if (head == null) {
            throw new NullPointerException();
        }
        if (o == null) {
            for (Node<V> pointer = head; pointer != null; pointer = pointer.next) {
                if (pointer.value == null) {
                    unlink(pointer);
                    return true;
                }
            }
        } else {
            for (Node<V> pointer = head; pointer != null; pointer = pointer.next) {
                if (pointer.value.equals(o)) {
                    unlink(pointer);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Removes the last occurrence of the specified element from this deque.
     * If the deque does not contain the element, it is unchanged.
     * More formally, removes the last element {@code e} such that
     * {@code Objects.equals(o, e)} (if such an element exists).
     * Returns {@code true} if this deque contained the specified element
     * (or equivalently, if this deque changed as a result of the call).
     *
     * @param o element to be removed from this deque, if present
     * @return {@code true} if an element was removed as a result of this call
     * @throws ClassCastException   if the class of the specified element
     *                              is incompatible with this deque
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null and this
     *                              deque does not permit null elements
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public boolean removeLastOccurrence (Object o) {
        if (head == null) {
            throw new NullPointerException();
        }
        if (o == null) {
            for (Node<V> pointer = last; pointer != null; pointer = pointer.prev) {
                if (pointer.value == null) {
                    unlink(pointer);
                    return true;
                }
            }
        } else {
            for (Node<V> pointer = last; pointer != null; pointer = pointer.prev) {
                if (pointer.value.equals(o)) {
                    unlink(pointer);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Inserts the specified element into the queue represented by this deque
     * (in other words, at the tail of this deque) if it is possible to do so
     * immediately without violating capacity restrictions, returning
     * {@code true} upon success and {@code false} if no space is currently
     * available.  When using a capacity-restricted deque, this method is
     * generally preferable to the {@link #add} method, which can fail to
     * insert an element only by throwing an exception.
     *
     * <p>This method is equivalent to {@link #offerLast}.
     *
     * @param v the element to add
     * @return {@code true} if the element was added to this deque, else
     * {@code false}
     * @throws ClassCastException       if the class of the specified element
     *                                  prevents it from being added to this deque
     * @throws NullPointerException     if the specified element is null and this
     *                                  deque does not permit null elements
     * @throws IllegalArgumentException if some property of the specified
     *                                  element prevents it from being added to this deque
     */
    @Override
    public boolean offer (V v) {
        return add(v);
    }

    @Override
    public boolean add (V v) {
        linkLast(v);
        return true;
    }

    /**
     * Retrieves and removes the head of the queue represented by this deque
     * (in other words, the first element of this deque).
     * This method differs from {@link #poll() poll()} only in that it
     * throws an exception if this deque is empty.
     *
     * <p>This method is equivalent to {@link #removeFirst()}.
     *
     * @return the head of the queue represented by this deque
     * @throws NoSuchElementException if this deque is empty
     */
    @Override
    public V remove () {
        return removeLast();
    }

    /**
     * Retrieves and removes the head of the queue represented by this deque
     * (in other words, the first element of this deque), or returns
     * {@code null} if this deque is empty.
     *
     * <p>This method is equivalent to {@link #pollFirst()}.
     *
     * @return the first element of this deque, or {@code null} if
     * this deque is empty
     */
    @Override
    public V poll () {
        return (head == null ? null : removeFirst());
    }

    /**
     * Retrieves, but does not remove, the head of the queue represented by
     * this deque (in other words, the first element of this deque).
     * This method differs from {@link #peek peek} only in that it throws an
     * exception if this deque is empty.
     *
     * <p>This method is equivalent to {@link #getFirst()}.
     *
     * @return the head of the queue represented by this deque
     * @throws NoSuchElementException if this deque is empty
     */
    @Override
    public V element () {
        return (head == null ? null : getFirst());
    }

    /**
     * Retrieves, but does not remove, the head of the queue represented by
     * this deque (in other words, the first element of this deque), or
     * returns {@code null} if this deque is empty.
     *
     * <p>This method is equivalent to {@link #peekFirst()}.
     *
     * @return the head of the queue represented by this deque, or
     * {@code null} if this deque is empty
     */
    @Override
    public V peek () {
        return element();
    }

    /**
     * Pushes an element onto the stack represented by this deque (in other
     * words, at the head of this deque) if it is possible to do so
     * immediately without violating capacity restrictions, throwing an
     * {@code IllegalStateException} if no space is currently available.
     *
     * <p>This method is equivalent to {@link #addFirst}.
     *
     * @param v the element to push
     * @throws IllegalStateException    if the element cannot be added at this
     *                                  time due to capacity restrictions
     * @throws ClassCastException       if the class of the specified element
     *                                  prevents it from being added to this deque
     * @throws NullPointerException     if the specified element is null and this
     *                                  deque does not permit null elements
     * @throws IllegalArgumentException if some property of the specified
     *                                  element prevents it from being added to this deque
     */
    @Override
    public void push (V v) {
        addFirst(v);
    }

    /**
     * Pops an element from the stack represented by this deque.  In other
     * words, removes and returns the first element of this deque.
     *
     * <p>This method is equivalent to {@link #removeFirst()}.
     *
     * @return the element at the front of this deque (which is the top
     * of the stack represented by this deque)
     * @throws NoSuchElementException if this deque is empty
     */
    @Override
    public V pop () {
        return removeFirst();
    }

    /**
     * Returns an iterator over the elements in this deque in reverse
     * sequential order.  The elements will be returned in order from
     * last (tail) to first (head).
     *
     * @return an iterator over the elements in this deque in reverse
     * sequence
     */
    @Override
    public Iterator<V> descendingIterator () {
        return this.new Itr();
    }

    /**
     * Removes all of the elements of this collection that satisfy the given
     * predicate.  Errors or runtime exceptions thrown during iteration or by
     * the predicate are relayed to the caller.
     *
     * @param filter a predicate which returns {@code true} for elements to be
     *               removed
     * @return {@code true} if any elements were removed
     * @throws NullPointerException          if the specified filter is null
     * @throws UnsupportedOperationException if elements cannot be removed
     *                                       from this collection.  Implementations may throw this exception if a
     *                                       matching element cannot be removed or if, in general, removal is not
     *                                       supported.
     * @implSpec The default implementation traverses all elements of the collection using
     * its {@link #iterator}.  Each matching element is removed using
     * {@link Iterator#remove()}.  If the collection's iterator does not
     * support removal then an {@code UnsupportedOperationException} will be
     * thrown on the first matching element.
     * @since 1.8
     */
    @Override
    public boolean removeIf (Predicate<? super V> filter) {
        if (head == null) {
            throw new NullPointerException();
        }
        for (Node<V> pointer = head; pointer != null; pointer = pointer.next) {
            if (filter.test(pointer.value)) {
                unlink(pointer);
                return true;
            }
        }
        return false;
    }

    /**
     * Performs the given action for each element of the {@code Iterable}
     * until all elements have been processed or the action throws an
     * exception.  Actions are performed in the order of iteration, if that
     * order is specified.  Exceptions thrown by the action are relayed to the
     * caller.
     * <p>
     * The behavior of this method is unspecified if the action performs
     * side-effects that modify the underlying source of elements, unless an
     * overriding class has specified a concurrent modification policy.
     *
     * @param action The action to be performed for each element
     * @throws NullPointerException if the specified action is null
     * @implSpec <p>The default implementation behaves as if:
     * <pre>{@code
     *     for (T t : this)
     *         action.accept(t);
     * }</pre>
     * @since 1.8
     */
    @Override
    public void forEach (Consumer<? super V> action) {
        if (head == null) {
            throw new NullPointerException();
        }
        for (Node<V> pointer = head; pointer != null; pointer = pointer.next) {
            action.accept(pointer.value);
        }
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * <p>This implementation first gets a list iterator pointing to the
     * indexed element (with {@code listIterator(index)}).  Then, it gets
     * the element using {@code ListIterator.next} and returns it.
     *
     * @param index the index of the element
     * @return the element
     * @throws IndexOutOfBoundsException {@inheritDoc}
     * @throws NullPointerException      when the list is empty.
     * @throws NoSuchElementException    when the element is not in the list.
     */
    @Override
    public V get (int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (head == null) {
            throw new NullPointerException();
        }
        int counter = 0;
        for (Node<V> pointer = head; pointer != null; pointer = pointer.next) {
            if (counter == index) {
                return pointer.value;
            }
            counter++;
        }
        throw new NoSuchElementException();
    }

    /**
     * Replaces the element at the specified position in this list with the
     * specified element (optional operation).
     *
     * <p>This implementation first gets a list iterator pointing to the
     * indexed element (with {@code listIterator(index)}).  Then, it gets
     * the current element using {@code ListIterator.next} and replaces it
     * with {@code ListIterator.set}.
     *
     * <p>Note that this implementation will throw an
     * {@code UnsupportedOperationException} if the list iterator does not
     * implement the {@code set} operation.
     *
     * @param index   the index of the element that you want to change.
     * @param element the new value.
     * @return old value.
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @throws IllegalArgumentException      {@inheritDoc}
     * @throws IndexOutOfBoundsException     {@inheritDoc}
     */
    @Override
    public V set (int index, V element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (head == null) {
            throw new NullPointerException();
        }
        int counter;
        if (index < (size >> 1)) {
            counter = 0;
            for (Node<V> pointer = head; pointer != null; pointer = pointer.next) {
                if (counter == index) {
                    final V value = pointer.value;
                    pointer.value = element;
                    return value;
                }
                counter++;
            }
        } else {
            counter = size - 1;
            for (Node<V> pointer = last; pointer != null; pointer = pointer.prev) {
                if (counter == index) {
                    final V value = pointer.value;
                    pointer.value = element;
                    return value;
                }
                counter--;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * Inserts the specified element at the specified position in this list
     * (optional operation).  Shifts the element currently at that position
     * (if any) and any subsequent elements to the right (adds one to their
     * indices).
     *
     * <p>This implementation first gets a list iterator pointing to the
     * indexed element (with {@code listIterator(index)}).  Then, it
     * inserts the specified element with {@code ListIterator.add}.
     *
     * <p>Note that this implementation will throw an
     * {@code UnsupportedOperationException} if the list iterator does not
     * implement the {@code add} operation.
     *
     * @param index
     * @param element
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @throws IllegalArgumentException      {@inheritDoc}
     * @throws IndexOutOfBoundsException     {@inheritDoc}
     */
    @Override
    public void add (int index, V element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (head == null) {
            throw new NullPointerException();
        }
        int counter;
        if (index < (size >> 1)) {
            counter = 0;
            for (Node<V> pointer = head; pointer != null; pointer = pointer.next) {
                if (counter == index) {
                    linkBefore(element, pointer);
                    return;
                }
                counter++;
            }
        } else {
            counter = size - 1;
            for (Node<V> pointer = last; pointer != null; pointer = pointer.prev) {
                if (counter == index) {
                    linkBefore(element, pointer);
                    return;
                }
                counter--;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * Removes the element at the specified position in this list (optional
     * operation).  Shifts any subsequent elements to the left (subtracts one
     * from their indices).  Returns the element that was removed from the
     * list.
     *
     * <p>This implementation first gets a list iterator pointing to the
     * indexed element (with {@code listIterator(index)}).  Then, it removes
     * the element with {@code ListIterator.remove}.
     *
     * <p>Note that this implementation will throw an
     * {@code UnsupportedOperationException} if the list iterator does not
     * implement the {@code remove} operation.
     *
     * @param index
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws IndexOutOfBoundsException     {@inheritDoc}
     */
    @Override
    public V remove (int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (head == null) {
            throw new NullPointerException();
        }
        int counter;
        if (index < (size >> 1)) {
            counter = 0;
            for (Node<V> pointer = head; pointer != null; pointer = pointer.next) {
                if (counter == index) {
                    final V value = pointer.value;
                    unlink(pointer);
                    return value;
                }
                counter++;
            }
        } else {
            counter = size - 1;
            for (Node<V> pointer = last; pointer != null; pointer = pointer.prev) {
                if (counter == index) {
                    final V value = pointer.value;
                    unlink(pointer);
                    return value;
                }
                counter--;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * Inserts all of the elements in the specified collection into this
     * list at the specified position (optional operation).  Shifts the
     * element currently at that position (if any) and any subsequent
     * elements to the right (increases their indices).  The new elements
     * will appear in this list in the order that they are returned by the
     * specified collection's iterator.  The behavior of this operation is
     * undefined if the specified collection is modified while the
     * operation is in progress.  (Note that this will occur if the specified
     * collection is this list, and it's nonempty.)
     *
     * <p>This implementation gets an iterator over the specified collection and
     * a list iterator over this list pointing to the indexed element (with
     * {@code listIterator(index)}).  Then, it iterates over the specified
     * collection, inserting the elements obtained from the iterator into this
     * list, one at a time, using {@code ListIterator.add} followed by
     * {@code ListIterator.next} (to skip over the added element).
     *
     * <p>Note that this implementation will throw an
     * {@code UnsupportedOperationException} if the list iterator returned by
     * the {@code listIterator} method does not implement the {@code add}
     * operation.
     *
     * @param index
     * @param c
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @throws IllegalArgumentException      {@inheritDoc}
     * @throws IndexOutOfBoundsException     {@inheritDoc}
     */
    @Override
    public boolean addAll (int index, Collection<? extends V> c) {
        return super.addAll(index, c);
    }

    /**
     * Returns an iterator over the elements in this list (in proper
     * sequence).<p>
     * <p>
     * This implementation merely returns a list iterator over the list.
     *
     * @return an iterator over the elements in this list (in proper sequence)
     */
    @Override
    public Iterator<V> iterator () {
        return this.new Itr();
    }

    /**
     * {@inheritDoc}
     *
     * @param o
     * @throws ClassCastException   {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     * @implSpec This implementation first gets a list iterator (with
     * {@code listIterator()}).  Then, it iterates over the list until the
     * specified element is found or the end of the list is reached.
     */
    @Override
    public int indexOf (Object o) {
        if (size == 0) {
            throw new NullPointerException();
        }
        int counter = 0;
        for (Node<V> pointer = head; pointer != null; pointer = pointer.next) {
            if (o == null && pointer.value == null) {
                return counter;
            } else {
                if (pointer.value.equals(o)) {
                    return counter;
                }
            }
            counter++;
        }
        return -1;
    }

    /**
     * {@inheritDoc}
     *
     * @param o
     * @throws ClassCastException   {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     * @implSpec This implementation first gets a list iterator that points to the end
     * of the list (with {@code listIterator(size())}).  Then, it iterates
     * backwards over the list until the specified element is found, or the
     * beginning of the list is reached.
     */
    @Override
    public int lastIndexOf (Object o) {
        if (size == 0) {
            throw new NullPointerException();
        }
        int counter = size - 1;
        for (Node<V> pointer = head; pointer != null; pointer = pointer.next) {
            if (o == null && pointer.value == null) {
                return counter;
            } else {
                if (pointer.value.equals(o)) {
                    return counter;
                }
            }
            counter--;
        }
        return -1;
    }

    /**
     * Removes all of the elements from this list (optional operation).
     * The list will be empty after this call returns.
     *
     * @throws UnsupportedOperationException if the {@code clear} operation
     *                                       is not supported by this list
     * @implSpec This implementation calls {@code removeRange(0, size())}.
     *
     * <p>Note that this implementation throws an
     * {@code UnsupportedOperationException} unless {@code remove(int
     * index)} or {@code removeRange(int fromIndex, int toIndex)} is
     * overridden.
     */
    @Override
    public void clear () {
        this.size = 0;
        this.head = null;
        this.last = null;
    }

    /**
     * {@inheritDoc}
     *
     * @implSpec This implementation returns {@code listIterator(0)}.
     * @see #listIterator(int)
     */
    @Override
    public ListIterator<V> listIterator () {
        return this.new lit();
    }

    /**
     * {@inheritDoc}
     *
     * @param fromIndex
     * @param toIndex
     * @throws IndexOutOfBoundsException if an endpoint index value is out of range
     *                                   {@code (fromIndex < 0 || toIndex > size)}
     * @throws IllegalArgumentException  if the endpoint indices are out of order
     *                                   {@code (fromIndex > toIndex)}
     * @implSpec This implementation returns a list that subclasses
     * {@code AbstractList}.  The subclass stores, in private fields, the
     * size of the subList (which can change over its lifetime), and the
     * expected {@code modCount} value of the backing list.  There are two
     * variants of the subclass, one of which implements {@code RandomAccess}.
     * If this list implements {@code RandomAccess} the returned list will
     * be an instance of the subclass that implements {@code RandomAccess}.
     *
     * <p>The subclass's {@code set(int, E)}, {@code get(int)},
     * {@code add(int, E)}, {@code remove(int)}, {@code addAll(int,
     * Collection)} and {@code removeRange(int, int)} methods all
     * delegate to the corresponding methods on the backing abstract list,
     * after bounds-checking the index and adjusting for the offset.  The
     * {@code addAll(Collection c)} method merely returns {@code addAll(size,
     * c)}.
     *
     * <p>The {@code listIterator(int)} method returns a "wrapper object"
     * over a list iterator on the backing list, which is created with the
     * corresponding method on the backing list.  The {@code iterator} method
     * merely returns {@code listIterator()}, and the {@code size} method
     * merely returns the subclass's {@code size} field.
     *
     * <p>All methods first check to see if the actual {@code modCount} of
     * the backing list is equal to its expected value, and throw a
     * {@code ConcurrentModificationException} if it is not.
     */
    @Override
    public List<V> subList (int fromIndex, int toIndex) {
        if (fromIndex > toIndex) {
            throw new IllegalArgumentException();
        }
        if (fromIndex < 0 || toIndex >= size) {
            throw new IndexOutOfBoundsException();
        }
        LinkedList<V> sub = new LinkedList<>();
        int i = 0;
        for (Node<V> pointer = head; pointer != null; pointer = pointer.next, i++) {
            if (i >= fromIndex && i <= toIndex) {
                sub.linkLast(pointer.value);
            }
        }
        return sub;
    }

    /**
     * Compares the specified object with this list for equality.  Returns
     * {@code true} if and only if the specified object is also a list, both
     * lists have the same size, and all corresponding pairs of elements in
     * the two lists are <i>equal</i>.  (Two elements {@code e1} and
     * {@code e2} are <i>equal</i> if {@code (e1==null ? e2==null :
     * e1.equals(e2))}.)  In other words, two lists are defined to be
     * equal if they contain the same elements in the same order.
     *
     * @param o the object to be compared for equality with this list
     * @return {@code true} if the specified object is equal to this list
     * @implSpec This implementation first checks if the specified object is this
     * list. If so, it returns {@code true}; if not, it checks if the
     * specified object is a list. If not, it returns {@code false}; if so,
     * it iterates over both lists, comparing corresponding pairs of elements.
     * If any comparison returns {@code false}, this method returns
     * {@code false}.  If either iterator runs out of elements before the
     * other it returns {@code false} (as the lists are of unequal length);
     * otherwise it returns {@code true} when the iterations complete.
     */
    @Override
    public boolean equals (Object o) {
        return (o != null && (o == this || (o instanceof LinkedList<?> &&
                this.size == ((LinkedList<?>) o).size && this.head.equals(((LinkedList<?>) o).head))));
    }

    /**
     * Returns the hash code value for this list.
     *
     * @return the hash code value for this list
     * @implSpec This implementation uses exactly the code that is used to define the
     * list hash function in the documentation for the {@link List#hashCode}
     * method.
     */
    @Override
    public int hashCode () {
        return super.hashCode();
    }

    /**
     * {@inheritDoc}
     *
     * @implSpec This implementation returns {@code size() == 0}.
     */
    @Override
    public boolean isEmpty () {
        return size == 0;
    }

    /**
     * {@inheritDoc}
     *
     * @param o
     * @throws ClassCastException   {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     * @implSpec This implementation iterates over the elements in the collection,
     * checking each element in turn for equality with the specified element.
     */
    @Override
    public boolean contains (Object o) {
        if (size == 0 || head == null) {
            throw new NullPointerException();
        }
        for (Node<V> pointer = head; pointer != null; pointer = pointer.next) {
            if (pointer.value.equals(o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @implSpec This implementation returns an array containing all the elements
     * returned by this collection's iterator, in the same order, stored in
     * consecutive elements of the array, starting with index {@code 0}.
     * The length of the returned array is equal to the number of elements
     * returned by the iterator, even if the size of this collection changes
     * during iteration, as might happen if the collection permits
     * concurrent modification during iteration.  The {@code size} method is
     * called only as an optimization hint; the correct result is returned
     * even if the iterator returns a different number of elements.
     *
     * <p>This method is equivalent to:
     *
     * <pre> {@code
     * List<E> list = new ArrayList<E>(size());
     * for (E e : this)
     *     list.add(e);
     * return list.toArray();
     * }</pre>
     */
    @Override
    public Object[] toArray () {
        if (head == null) {
            return new Object[0];
        }
        Object[] os = new Object[size];
        int counter = 0;
        for (Node<V> pointer = head; pointer != null; pointer = pointer.next) {
            os[counter] = pointer.value;
            counter++;
        }
        return os;
    }

    /**
     * {@inheritDoc}
     *
     * @param c
     * @throws ClassCastException   {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     * @implSpec This implementation iterates over the specified collection,
     * checking each element returned by the iterator in turn to see
     * if it's contained in this collection.  If all elements are so
     * contained {@code true} is returned, otherwise {@code false}.
     * @see #contains(Object)
     */
    @Override
    public boolean containsAll (Collection<?> c) {
        if (c == null) {
            return false;
        }
        if (c.size() != size) {
            return false;
        }
        if (head == null) {
            throw new NullPointerException();
        }
        for (Object value : c) {
            if (!contains(value)) {
                return false;
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @param c
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @throws IllegalArgumentException      {@inheritDoc}
     * @throws IllegalStateException         {@inheritDoc}
     * @implSpec This implementation iterates over the specified collection, and adds
     * each object returned by the iterator to this collection, in turn.
     *
     * <p>Note that this implementation will throw an
     * {@code UnsupportedOperationException} unless {@code add} is
     * overridden (assuming the specified collection is non-empty).
     * @see #add(Object)
     */
    @Override
    public boolean addAll (Collection<? extends V> c) {
        return super.addAll(c);
    }

    /**
     * {@inheritDoc}
     *
     * @param c
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @implSpec This implementation iterates over this collection, checking each
     * element returned by the iterator in turn to see if it's contained
     * in the specified collection.  If it's so contained, it's removed from
     * this collection with the iterator's {@code remove} method.
     *
     * <p>Note that this implementation will throw an
     * {@code UnsupportedOperationException} if the iterator returned by the
     * {@code iterator} method does not implement the {@code remove} method
     * and this collection contains one or more elements in common with the
     * specified collection.
     * @see #remove(Object)
     * @see #contains(Object)
     */
    @Override
    public boolean removeAll (Collection<?> c) {
        return super.removeAll(c);
    }

    /**
     * Returns a string representation of this collection.  The string
     * representation consists of a list of the collection's elements in the
     * order they are returned by its iterator, enclosed in square brackets
     * ({@code "[]"}).  Adjacent elements are separated by the characters
     * {@code ", "} (comma and space).  Elements are converted to strings as
     * by {@link String#valueOf(Object)}.
     *
     * @return a string representation of this collection
     */
    @Override
    public String toString () {
        if (head == null) {
            return null;
        }
        Node<V> pointer = head;
        StringBuilder builder = new StringBuilder("[" + pointer.value.toString());
        while (pointer.next != null) {
            pointer = pointer.next;
            builder.append(", ").append(pointer.value.toString());
        }
        builder.append("]");
        return builder.toString();
    }

    /**
     * Replaces each element of this list with the result of applying the
     * operator to that element.  Errors or runtime exceptions thrown by
     * the operator are relayed to the caller.
     *
     * @param operator the operator to apply to each element
     * @throws UnsupportedOperationException if this list is unmodifiable.
     *                                       Implementations may throw this exception if an element
     *                                       cannot be replaced or if, in general, modification is not
     *                                       supported
     * @throws NullPointerException          if the specified operator is null or
     *                                       if the operator result is a null value and this list does
     *                                       not permit null elements
     *                                       (<a href="Collection.html#optional-restrictions">optional</a>)
     * @implSpec The default implementation is equivalent to, for this {@code list}:
     * <pre>{@code
     *     final ListIterator<E> li = list.listIterator();
     *     while (li.hasNext()) {
     *         li.set(operator.apply(li.next()));
     *     }
     * }</pre>
     * <p>
     * If the list's list-iterator does not support the {@code set} operation
     * then an {@code UnsupportedOperationException} will be thrown when
     * replacing the first element.
     * @since 1.8
     */
    @Override
    public void replaceAll (UnaryOperator<V> operator) {
        super.replaceAll(operator);
    }


    /**
     * Define a simple ListIterator.
     */
    final class lit implements ListIterator<V> {
        int pointer;
        Node<V> copy;

        {
            new Node<>(head.value, null, head.next);
        }

        public lit () {
            pointer = 0;
            copy = head;
        }

        private void init () {
            if (copy == null) {
                copy = head;
            }
        }

        /**
         * Returns {@code true} if this list iterator has more elements when
         * traversing the list in the forward direction. (In other words,
         * returns {@code true} if {@link #next} would return an element rather
         * than throwing an exception.)
         *
         * @return {@code true} if the list iterator has more elements when
         * traversing the list in the forward direction
         */
        @Override
        public boolean hasNext () {
            init();
            return pointer + 1 <= size;
        }

        /**
         * Returns the next element in the list and advances the cursor position.
         * This method may be called repeatedly to iterate through the list,
         * or intermixed with calls to {@link #previous} to go back and forth.
         * (Note that alternating calls to {@code next} and {@code previous}
         * will return the same element repeatedly.)
         *
         * @return the next element in the list
         * @throws NoSuchElementException if the iteration has no next element
         */
        @Override
        public V next () {
            init();
            pointer++;
            final V value = copy.value;
            copy = copy.next;
            return value;
        }

        /**
         * Returns {@code true} if this list iterator has more elements when
         * traversing the list in the reverse direction.  (In other words,
         * returns {@code true} if {@link #previous} would return an element
         * rather than throwing an exception.)
         *
         * @return {@code true} if the list iterator has more elements when
         * traversing the list in the reverse direction
         */
        @Override
        public boolean hasPrevious () {
            return size - 1 > 0;
        }

        /**
         * Returns the previous element in the list and moves the cursor
         * position backwards.  This method may be called repeatedly to
         * iterate through the list backwards, or intermixed with calls to
         * {@link #next} to go back and forth.  (Note that alternating calls
         * to {@code next} and {@code previous} will return the same
         * element repeatedly.)
         *
         * @return the previous element in the list
         * @throws NoSuchElementException if the iteration has no previous
         *                                element
         */
        @Override
        public V previous () {
            final V value = copy.value;
            copy = copy.prev;
            return value;
        }

        /**
         * Returns the index of the element that would be returned by a
         * subsequent call to {@link #next}. (Returns list size if the list
         * iterator is at the end of the list.)
         *
         * @return the index of the element that would be returned by a
         * subsequent call to {@code next}, or list size if the list
         * iterator is at the end of the list
         */
        @Override
        public int nextIndex () {
            return pointer + 1;
        }

        /**
         * Returns the index of the element that would be returned by a
         * subsequent call to {@link #previous}. (Returns -1 if the list
         * iterator is at the beginning of the list.)
         *
         * @return the index of the element that would be returned by a
         * subsequent call to {@code previous}, or -1 if the list
         * iterator is at the beginning of the list
         */
        @Override
        public int previousIndex () {
            return pointer - 1;
        }

        /**
         * Removes from the list the last element that was returned by {@link
         * #next} or {@link #previous} (optional operation).  This call can
         * only be made once per call to {@code next} or {@code previous}.
         * It can be made only if {@link #add} has not been
         * called after the last call to {@code next} or {@code previous}.
         *
         * @throws UnsupportedOperationException if the {@code remove}
         *                                       operation is not supported by this list iterator
         * @throws IllegalStateException         if neither {@code next} nor
         *                                       {@code previous} have been called, or {@code remove} or
         *                                       {@code add} have been called after the last call to
         *                                       {@code next} or {@code previous}
         */
        @Override
        public void remove () {
            LinkedList.this.remove();
        }

        /**
         * Replaces the last element returned by {@link #next} or
         * {@link #previous} with the specified element (optional operation).
         * This call can be made only if neither {@link #remove} nor {@link
         * #add} have been called after the last call to {@code next} or
         * {@code previous}.
         *
         * @param v the element with which to replace the last element returned by
         *          {@code next} or {@code previous}
         * @throws UnsupportedOperationException if the {@code set} operation
         *                                       is not supported by this list iterator
         * @throws ClassCastException            if the class of the specified element
         *                                       prevents it from being added to this list
         * @throws IllegalArgumentException      if some aspect of the specified
         *                                       element prevents it from being added to this list
         * @throws IllegalStateException         if neither {@code next} nor
         *                                       {@code previous} have been called, or {@code remove} or
         *                                       {@code add} have been called after the last call to
         *                                       {@code next} or {@code previous}
         */
        @Override
        public void set (V v) {
            LinkedList.this.set(pointer, v);
        }

        /**
         * Inserts the specified element into the list (optional operation).
         * The element is inserted immediately before the element that
         * would be returned by {@link #next}, if any, and after the element
         * that would be returned by {@link #previous}, if any.  (If the
         * list contains no elements, the new element becomes the sole element
         * on the list.)  The new element is inserted before the implicit
         * cursor: a subsequent call to {@code next} would be unaffected, and a
         * subsequent call to {@code previous} would return the new element.
         * (This call increases by one the value that would be returned by a
         * call to {@code nextIndex} or {@code previousIndex}.)
         *
         * @param v the element to insert
         * @throws UnsupportedOperationException if the {@code add} method is
         *                                       not supported by this list iterator
         * @throws ClassCastException            if the class of the specified element
         *                                       prevents it from being added to this list
         * @throws IllegalArgumentException      if some aspect of this element
         *                                       prevents it from being added to this list
         */
        @Override
        public void add (V v) {
            LinkedList.this.add(v);
        }
    }

    final class Itr implements Iterator<V> {
        int pointer;
        Node<V> copy;

        public Itr () {
            pointer = 0;
            copy = new Node<>(head.value);
            copy.next = head.next;
        }

        private void init () {
            if (copy == null) {
                copy = head;
            }
        }

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext () {
            init();
            return pointer + 1 <= size;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public V next () {
            init();
            pointer++;
            final V value = copy.value;
            copy = copy.next;
            return value;
        }

        /**
         * Removes from the underlying collection the last element returned
         * by this iterator (optional operation).  This method can be called
         * only once per call to {@link #next}.
         * <p>
         * The behavior of an iterator is unspecified if the underlying collection
         * is modified while the iteration is in progress in any way other than by
         * calling this method, unless an overriding class has specified a
         * concurrent modification policy.
         * <p>
         * The behavior of an iterator is unspecified if this method is called
         * after a call to the {@link #forEachRemaining forEachRemaining} method.
         *
         * @throws UnsupportedOperationException if the {@code remove}
         *                                       operation is not supported by this iterator
         * @throws IllegalStateException         if the {@code next} method has not
         *                                       yet been called, or the {@code remove} method has already
         *                                       been called after the last call to the {@code next}
         *                                       method
         * @implSpec The default implementation throws an instance of
         * {@link UnsupportedOperationException} and performs no other action.
         */
        @Override
        public void remove () {
            Iterator.super.remove();
        }
    }
}

class LinkedListTest {
    public static void main (String[] args) {
        LinkedList<Integer> linkedList = new LinkedList<>();
        linkedList.add(1);
        linkedList.add(2);
        linkedList.addFirst(0);
        linkedList.addLast(4);
        linkedList.add(3, 3);
        linkedList.set(3, -3);
        out.println(linkedList);
        for (Integer num : linkedList) {
            out.println(num);
        }
        linkedList.addLast(10);
        linkedList.addLast(10);
        linkedList.addLast(10);
        linkedList.addLast(10);
        linkedList.addLast(10);
    }
}
