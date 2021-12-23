package top.kkoishi.util;

import javax.management.InstanceAlreadyExistsException;

@Deprecated
public class BalancedBinaryTree<V extends Comparable<V>> {
    final static class Node<V> {
        V value;
        int height;
        Node<V> left;
        Node<V> right;

        public Node (V value, int height, Node<V> left, Node<V> right) {
            this.value = value;
            this.height = height;
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString () {
            return value.toString();
        }
    }

    transient Node<V> root;

    public BalancedBinaryTree () {
        this.root = null;
    }

    final Node<V> link (V value, Node<V> node) throws InstanceAlreadyExistsException {
        if (node == null) {
            node = new Node<>(value, 1, null, null);
            node.height = Math.max(getHeight(node.right), getHeight(node.left)) + 1;
            return node;
        } else {
            node.height = Math.max(getHeight(node.right), getHeight(node.left)) + 1;
            if (node.value.compareTo(value) > 0) {
                node.left = link(value, node.left);
                if (getBalancedFactor(node) >= 2) {
                    if (value.compareTo(node.left.value) < 0) {
                        node = rotateLeft(node);
                    }
                    node = rotateRight(node);
                }
                return node;
            } else if (node.value.compareTo(value) < 0) {
                node.right = link(value, node.right);
                if (getBalancedFactor(node) <= -2) {
                    if (value.compareTo(node.value) < 0) {
                        node = rotateRight(node);
                    }
                    node = rotateLeft(node);
                }
                return node;
            } else {
                throw new InstanceAlreadyExistsException();
            }
        }
    }

    final int getHeight (Node<V> node) {
        return (node == null ? 0 : node.height);
    }

    final int getBalancedFactor (Node<V> node) {
        return (node == null ? 0 : (getHeight(node.left) - getHeight(node.right)));
    }

    final Node<V> rotateRight (Node<V> node) {
        Node<V> mid = node.left;
        Node<V> n = mid.right;
        mid.right = node;
        node.left = n;
        node.height = Math.max(getHeight(node.right), getHeight(node.left)) + 1;
        mid.height = Math.max(getHeight(node.right), getHeight(node.left)) + 1;
        return mid;
    }

    final Node<V> rotateLeft (Node<V> node) {
        Node<V> mid = node.right;
        Node<V> n = mid.left;
        mid.left = node;
        node.right = n;
        node.height = Math.max(getHeight(node.right), getHeight(node.left)) + 1;
        mid.height = Math.max(getHeight(node.right), getHeight(node.left)) + 1;
        return mid;
    }

    public boolean add (V value) {
        try {
            root = link(value, root);
        } catch (InstanceAlreadyExistsException e) {
            return false;
        }
        return true;
    }

    @Override
    public String toString () {
        return "BalancedBinaryTree\n" + toString(root, 0);
    }

    private String toString (Node<V> node, int i) {
        if (node == null) {
            return "NULL";
        }
        if (node.left == null && node.right == null) {
            return "\n" +
                    "    ".repeat(Math.max(0, i)) +
                    "[Node]{value=" +
                    node +
                    "\n" +
                    "    ".repeat(Math.max(0, i - 1)) +
                    "}";
        }
        return "\n" +
                "    ".repeat(Math.max(0, i)) +
                "{value=" +
                node +
                "    ".repeat(Math.max(0, i)) +
                "\nleft=" +
                toString(node.left, i + 1) +
                "\nright=" +
                toString(node.right, i + 1) +
                "\n" +
                "    ".repeat(Math.max(0, i - 1)) +
                "}";
    }
}

