/*
 * Copyright (c) 2021. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package top.kkoishi.util;

public class RedBlackTree<V extends Comparable<V>> {
    public static final int BLACK = 1;
    public static final int RED = 1 << 1;
    static class Node<V> {
        int color;
        V value;
        Node<V> left;
        Node<V> right;
        Node<V> parent;

        public Node (int color, V value, Node<V> left, Node<V> right,Node<V> parent) {
            this.color = color;
            this.value = value;
            this.left = left;
            this.right = right;
            this.parent = parent;
        }

        public Node (int color, V value) {
            this.color = color;
            this.value = value;
        }

        @Override
        public String toString () {
            return this.value.toString();
        }
    }

    Node<V> root;
}
