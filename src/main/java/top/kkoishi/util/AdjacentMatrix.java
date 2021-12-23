/*
 * Copyright (c) 2021. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package top.kkoishi.util;

import top.kkoishi.util.graph.Graph;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class AdjacentMatrix<V> implements Graph<V> {
    class Vex implements Point {
        V value;
        boolean visited;

        public Vex (V value, boolean visited) {
            this.value = value;
            this.visited = visited;
        }

        public Vex () {
            this(null, false);
        }
    }

    private transient int vexAmount;
    private transient int[][] edges;
    private transient Object[] vexes;
    private transient int pointer;

    private final boolean hasWeight;

    public static final int UNLINK_SYMBOL = 0x7fffffff;

    public AdjacentMatrix (boolean hasWeight) {
        this.hasWeight = hasWeight;
        vexAmount = 0;
        edges = new int[0][0];
        vexes = new Object[0];
        pointer = 0;
    }

    public AdjacentMatrix (boolean hasWeight, int initSize) {
        this.hasWeight = hasWeight;
        vexAmount = initSize;
        edges = new int[initSize][initSize];
        vexes = new Object[initSize];
        pointer = 0;
    }

    public static <V> Graph<V> getInstance (boolean hasWeight) {
        return new AdjacentMatrix<>(hasWeight);
    }

    final void resize () {
        int symbol = 0;
        if (hasWeight) {
            symbol = UNLINK_SYMBOL;
        }
        ++vexAmount;
        int[][] copy = edges;
        Object[] temp = vexes;
        vexes = new Object[vexAmount];
        edges = new int[vexAmount][vexAmount];
        for (int i = 0; i < vexAmount - 1; i++) {
            System.arraycopy(copy[i], 0, edges[i], 0, vexAmount - 1);
            edges[i][vexAmount - 1] = symbol;
        }
        System.arraycopy(temp, 0, vexes, 0, vexAmount - 1);
        //help gc
        copy = null;
        temp = null;
        Arrays.fill(edges[vexAmount - 1], symbol);
    }

    final void desize (int index) {
        if (vexAmount == 0) {
            throw new NullPointerException();
        }
        --vexAmount;
        int[][] copy = edges;
        Object[] temp = vexes;
        edges = new int[vexAmount][vexAmount];
        for (int i = 0; i < vexAmount; i++) {
            System.arraycopy(copy[i], 0, edges[i], 0, index);
            System.arraycopy(copy[i], index + 1, edges[i], index, vexAmount - index);
        }
        System.arraycopy(temp, 0, vexes, 0, index);
        System.arraycopy(temp, index + 1 ,vexes, index, vexAmount - index);
        //help gc
        copy = null;
        temp = null;
    }

    final int index (V v) {
        if (vexAmount == 0) {
            throw new NullPointerException();
        } else {
            for (int i = 0; i < vexAmount; i++) {
                if (vexes[i].equals(v)) {
                    return i;
                }
            }
            return -1;
        }
    }

    protected boolean contains (V v) {
        if (vexAmount == 0) {
            throw new NullPointerException();
        } else {
            for (int i = 0; i < vexAmount; i++) {
                if (vexes[i].equals(v)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Add a void point to the graph.
     * The point does not have any sides, you can add side
     * with invoking add (from, sideWeight, to);
     * You should make sure next when override the method:
     * <p>
     *
     * The adjacent-matrix does not allow same value exists,the original value
     * remains when the value existed.
     *
     * @param value the void point's value.
     * @return true if added,or false
     */
    @Override
    public boolean add (V value) {
        if (vexAmount != 0 && contains(value)) {
            return false;
        }
        if (pointer == vexAmount) {
            resize();
        }
        edges[pointer][pointer] = 0;
        vexes[pointer++] = value;
        return true;
    }

    /**
     * An empty method which is used to build a side between
     * the node with a value of the first param,
     * and the one with the last param.
     * The implement method should take next:
     * <p>
     * The frontValue's point must be not null,or a {@code NosuchElementException}
     * will be thrown.
     * Further, a node will be created if the point with the value of
     * the last param does not exist.<br>
     * Like this:frontValue -> sideWeight -> nextValue.
     *
     * @param from       the front node' value.
     * @param sideWeight the side's weight
     * @param to         the node which the side will point at.
     */
    @Override
    public void add (V from, int sideWeight, V to) {
        if (!hasWeight) {
            sideWeight = 1;
        }
        int v1 = -1, v2 = -1;
        for (int i = 0; i < vexAmount; i++) {
            if (vexes[i].equals(from)) {
                v1 = i;
            }
            if (vexes[i].equals(to)) {
                v2 = i;
            }
        }
        if (v1 == -1 || v2 == -1) {
            throw new NoSuchElementException();
        } else {
            edges[v1][v2] = sideWeight;
            edges[v2][v1] = sideWeight;
        }
    }

    /**
     * Directly clear the whole graph.
     * Recommend way:<br>
     * re-initialize the data-structure which
     * stores the data.
     */
    @Override
    public void clear () {
        this.edges = new int[0][0];
        this.vexes = new Object[0];
        this.vexAmount = 0;
        this.pointer = 0;
    }

    /**
     * Remove a point by providing its value.
     * <p>
     * You should throw an exception when the point with the value is null.
     * The graph points' value should be only, that makes the removed
     * element is the last one.
     *
     * @param value the value of the node which you'd like to remove.
     */
    @Override
    public void remove (V value) {

    }

    /**
     * Remove a side between two points.
     *
     * @param from from point
     * @param to   next point
     */
    @Override
    public void remove (V from, V to) {

    }

    /**
     * Remove all sides that match the sideWeight
     *
     * @param sideWeight the sides' weight
     */
    @Override
    public void removeAll (int sideWeight) {

    }

    /**
     * Get the only point which its value is the param.
     * if the point is {@code null},the return should be null.
     * the return type is inner interface Point.
     *
     * @param value the value which is searched.
     * @return a Point.
     */
    @Override
    public Point getPoint (V value) {
        return null;
    }

    @Override
    public List<?> getSides (V value) {
        return null;
    }

    @Override
    public List<?> getPoints () {
        return null;
    }

    @Override
    public List<?> getPoints (V value) {
        return null;
    }

    @Override
    public List<?> getSides (int sideWeight) {
        return null;
    }

    @Override
    public Graph<V> deepCopy () {
        return null;
    }

    @Override
    public int size () {
        return 0;
    }

    @Override
    public int pointAmount () {
        return 0;
    }

    @Override
    public int sides () {
        return 0;
    }

    /**
     * Get the side amount which its side-weight is value of the param.
     *
     * @param sideWeight the side-weight of the sides
     * @return amount
     */
    @Override
    public int sides (int sideWeight) {
        return 0;
    }

    /**
     * Get a list of connection component.
     * A connection component is a child graph that is not linked
     * with the other connection component.
     *
     * @return List.
     * @throws NullPointerException when the graph is empty.
     */
    @Override
    public List<Graph<V>> getConnectionComponent () {
        return null;
    }

    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder("AdjacentMatrix-noDirection");
        sb.append((hasWeight) ? "[hasWeight]" : "[noWeight]")
                .append("\n");
        for (int[] e : this.edges) {
            sb.append(Arrays.toString(e))
                    .append("\n");
        }
        return sb.toString();
    }
}

class AdjacentMatrixText {
    public static void main (String[] args) {
        Graph<String> graph = new AdjacentMatrix<>(false);
        graph.add("start");
        graph.add("s1");
        graph.add("s2");
        graph.add("s3");
        graph.add("end");
        graph.add("start", 1, "s2");
        graph.add("start", 1, "s1");
        graph.add("start", 1, "s3");
        graph.add("s1", 1, "end");
        graph.add("s2", 1, "s3");
        graph.add("start", 1, "end");
        System.out.println(graph);
    }
}
