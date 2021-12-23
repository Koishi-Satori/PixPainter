/*
 * Copyright (c) 2021. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package top.kkoishi.util;

public class BminusTree<K extends Comparable<K>, V> {
    static class Node<K, V> {
        K key1;
        K key2;
        V val1;
        V val2;
        Node<K, V> p1;
        Node<K, V> p2;
        boolean leaf;

        public Node (K key1, K key2, V val1, V val2, Node<K, V> p1, Node<K, V> p2, boolean leaf) {
            this.key1 = key1;
            this.key2 = key2;
            this.val1 = val1;
            this.val2 = val2;
            this.p1 = p1;
            this.p2 = p2;
            this.leaf = leaf;
        }
    }

    Node<K, V> root;

    public BminusTree (Node<K, V> root) {
        this.root = root;
    }

    final Node<K, V> link (K key, V value, Node<K, V> node) {
        if (node == null) {
            node = new Node<>(key, null, value, null,null, null, true);
        } else {
            if (node.leaf) {
                node.key2 = key;
                node.val2 = value;
            } else {
                //根节点 判断是否需要分裂
                if (node.p1 == null) {
                    node.p1 = link(key, value, node.p1);
                } else if (node.p2 == null) {
                    node.p2 = link(key, value, node.p2);
                } else {
                    //需要分裂

                }
            }
        }
        return node;
    }
}