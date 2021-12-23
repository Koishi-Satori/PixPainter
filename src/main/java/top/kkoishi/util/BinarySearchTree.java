/*
 * Copyright (c) 2021. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package top.kkoishi.util;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.function.Consumer;

import static java.lang.System.out;

public class BinarySearchTree<V extends Comparable<V>> {
    /**
     * Node
     * @param <V>
     */
    static class Node<V> {
        V value;
        Node<V> left;
        Node<V> right;

        public Node (V value) {
            this.value = value;
        }

        public Node (V value, Node<V> left, Node<V> right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString () {
            return value.toString();
        }
    }

    Node<V> root;

    public BinarySearchTree (V value) {
        root = new Node<>(value);
    }

    private BinarySearchTree () {
    }

    public static <V extends Comparable<V>> BinarySearchTree<V> getInstance (V[] array) {
        BinarySearchTree<V> instance = new BinarySearchTree<>();
        instance.root = instance.build(array, 0, array.length - 1);
        return instance;
    }

    public void buildUnsorted (V[] array) {
        Arrays.sort(array);
        build(array);
    }

    public void build (V[] array) {
        this.root = build(array, 0, array.length - 1);
    }

    public void setRootValue (V value) {
        if (root == null) {
            root = new Node<>(value);
        } else {
            root.value = value;
        }
    }

    private Node<V> build (V[] sortedArray, int low, int high) {
        if (low == high) {
            return new Node<>(sortedArray[low]);
        } else if (low > high) {
            return null;
        }
        int mid = (low + high) / 2;
        Node<V> node = new Node<>(sortedArray[mid]);
        node.left = build(sortedArray, low, mid - 1);
        node.right = build(sortedArray, mid + 1, high);
        return node;
    }

    public String frontOrderTraversal () {
        return frontOrderTraversal(root);
    }

    private String frontOrderTraversal (Node<V> node) {
        if (node.left == null && node.right == null) {
            return node.toString();
        }
        String ans = node.toString();
        ans += (node.left != null ? " " + frontOrderTraversal(node.left) : " NULL");
        ans += (node.right != null ? " " + frontOrderTraversal(node.right) : " NULL");
        return ans;
    }

    public String midOrderTraversal () {
        return midOrderTraversal(root);
    }

    private String midOrderTraversal (Node<V> node) {
        if (node.left == null && node.right == null) {
            return node.toString();
        }
        String ans = "";
        ans = (node.left != null ? midOrderTraversal(node.left) + " " : "NULL ");
        ans += node.toString();
        ans += (node.right != null ? " " + midOrderTraversal(node.right) : " NULL");
        return ans;
    }

    public String lastOrderTraversal () {
        return lastOrderTraversal(root);
    }

    private String lastOrderTraversal (Node<V> node) {
        if (node.left == null && node.right == null) {
            return node.toString();
        }
        String ans = "";
        ans += (node.left != null ? " " + lastOrderTraversal(node.left) : " NULL");
        ans += (node.right != null ? " " + lastOrderTraversal(node.right) : " NULL");
        ans += " " + node;
        return ans;
    }

    public String levelOrderTraversal () {
        return lvlOrderTraversal()
                .toString()
                .replaceAll("],", "]\n");
    }

    public List<List<String>> lvlOrderTraversal () {
        if (root == null) {
            throw new NullPointerException();
        }
        Queue<Node<V>> queue = new LinkedList<>();
        queue.add(root);
        List<List<String>> ans = new LinkedList<>();
        while (!queue.isEmpty()) {
            List<String> strs = new LinkedList<>();
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                Node<V> temp = queue.poll();
                assert temp != null;
                strs.add(temp.toString());
                if (temp.left != null) {
                    queue.add(temp.left);
                }
                if (temp.right != null) {
                    queue.add(temp.right);
                }
            }
            ans.add(strs);
        }
        return ans;
    }

    public boolean add (V value) {
        if (root == null) {
            root = new Node<>(value);
        } else {
            if (root.value.compareTo(value) > 0) {
                return link(value, root);
            } else if (root.value.compareTo(value) < 0) {
                final V v = root.value;
                root.value = value;
                return link(v, root);
            }
        }
        return true;
    }

    public boolean remove (V value) {
        if (root == null) {
            throw new NullPointerException();
        }
        if (root.value.compareTo(value) == 0) {
            root = null;
            return true;
        }
        return unlink(value, root);
    }

    public void set (V old, V newValue) {
        if (!replace(old, newValue)) {
            throw new NoSuchElementException();
        }
    }

    public boolean replace (V old, V newValue) {
        if (root == null) {
            throw new NullPointerException();
        }
        return replace(old, newValue, root);
    }

    private boolean replace (V value, V newValue, Node<V> node) {
        if (node == null) {
            return false;
        }
        if (value.compareTo(node.value) == 0) {
            remove(value);
            add(newValue);
            return true;
        } else if (value.compareTo(node.value) > 0) {
            return replace(value, newValue, node.right);
        } else {
            return replace(value, newValue, node.left);
        }
    }

    private boolean unlink (V value, Node<V> node) {
        if (node == null) {
            return false;
        }
        if (node.left.value.compareTo(value) == 0) {
            if (node.left.left == null && node.left.right == null) {
                node.left = null;
                return true;
            }
            if (node.left.right == null) {
                node.left = node.left.left;
                return true;
            } else if (node.left.left == null) {
                node.left = node.left.right;
                return true;
            } else {
                replaceAndRemove(node.left);
                return true;
            }
        } else if (node.right.value.compareTo(value) == 0) {
            if (node.right.left == null && node.right.right == null) {
                node.right = null;
                return true;
            }
            if (node.right.right == null) {
                node.right = node.right.left;
            } else if (node.right.left == null) {
                node.right = node.right.right;
            } else {
                replaceAndRemove(node.right);
                return true;
            }
            return false;
        } else {
            return unlink(value, ((value.compareTo(node.value) >= 0 ? node.right : node.left)));
        }
    }

    private void replaceAndRemove (Node<V> node) {
        if (node.left == null && node.right == null) {
            return;
        }
        if (node.right == null) {
            return;
        }
        node.value = node.right.value;
        if (node.right.left == null && node.right.right == null) {
            node.right = null;
            return;
        }
        replaceAndRemove(node.right);
    }

    private boolean link (V value, Node<V> node) {
        if (node.value.compareTo(value) == 0) {
            return false;
        }
        if (node.left == null && node.right == null) {
            if (node.value.compareTo(value) < 0) {
                node.right = new Node<>(value);
            } else {
                node.left = new Node<>(value);
            }
            return true;
        }
        if (node.value.compareTo(value) < 0) {
            if (node.right == null) {
                node.right = new Node<>(value);
                return true;
            } else {
                return link(value, node.right);
            }
        } else if (node.value.compareTo(value) > 0) {
            if (node.left == null) {
                node.left = new Node<>(value);
                return true;
            } else {
                return link(value, node.left);
            }
        }
        return false;
    }

    public void clear () {
        root = null;
    }

    @Override
    public String toString () {
        return "BinarySearchTree:" + toString(root, 1);
    }

    public void forEach (Consumer<V> action) {
        if (root == null) {
            throw new NullPointerException();
        }
        List<V> v = new LinkedList<>();
        Queue<Node<V>> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                Node<V> temp = queue.poll();
                assert temp != null;
                v.add(temp.value);
                if (temp.left != null) {
                    queue.add(temp.left);
                }
                if (temp.right != null) {
                    queue.add(temp.right);
                }
            }
        }
        for (V val : v) {
            action.accept(val);
        }
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

class BstTest {
    public static void main (String[] args) {
        BinarySearchTree<Integer> bst = BinarySearchTree.getInstance(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        bst.add(-1);
        out.println(bst.midOrderTraversal());
        bst.remove(8);
        bst.set(-1, 13);
        out.println(bst.midOrderTraversal());
        bst.clear();
        for (int i = 1; i < 11;i ++) {
            bst.add(i);
        }
        out.println(bst.midOrderTraversal());
    }
}
