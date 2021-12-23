/*
 *
 *
 *
 *
 * Copyright (c) 2021 KKoishi owns. All rights reserved
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package top.kkoishi.util.graph;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class GraphWithDirection<V> implements Graph<V>, Cloneable, Iterable<V> {
    @Override
    @SuppressWarnings("all")
    public GraphWithDirection<V> clone () {
        try {
            GraphWithDirection clone = (GraphWithDirection) super.clone();
            //copy mutable state here, so the clone can't change the internals of the original
            clone.dataMap = this.dataMap;
            clone.headValue = this.headValue;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    class SideNode {
        int weight;
        PointNode next;

        public SideNode (int weight) {
            this.weight = weight;
        }

        public SideNode () {
        }

        @Override
        public String toString () {
            return "side[" + weight + "] is pointed to " + next.value;
        }
    }

    class PointNode implements Point {
        V value;
        Vector<SideNode> sides = new Vector<>();

        public PointNode (V value) {
            this.value = value;
        }

        public PointNode () {
        }

        @Override
        public String toString () {
            return "point[" + value + "]has " + sides.size() + " sides";
        }
    }

    private transient HashMap<V, PointNode> dataMap;
    private V headValue;

    public GraphWithDirection () {
        dataMap = new HashMap<>();
        headValue = null;
    }

    /**
     * Add a void point to the graph.
     * The point does not have any sides, you can add side
     * with invoking add (from, sideWeight, to);
     * You should make sure next when override the method:
     * <p>
     * Even there are multiple parts which do not connect with each other,
     * the search can still normally run.
     * And when the value has existed, the old point's value will be replaced
     *
     * @param value the void point's value.
     * @return true if added,or false
     */
    @Override
    public boolean add (V value) {
        if (dataMap.isEmpty()) {
            headValue = value;
        }
        if (dataMap.containsKey(value)) {
            return false;
        } else {
            dataMap.put(value, new PointNode(value));
            return true;
        }
    }

    /**
     * Build a side between the node with a value of the first param,
     * and the one with the last param.
     * The frontValue's point must be not null,or a {@code NosuchElementException}
     * will be thrown.
     * Further, a node will be created if the point with the value of
     * the last param does not exist.<br>
     * Like this:frontValue -> sideWeight -> nextValue.
     *
     * @param frontValue the front node' value.
     * @param sideWeight the side's weight
     * @param nextValue  the node which the side will point at.
     */
    @Override
    public void add (V frontValue, int sideWeight, V nextValue) {
        addSide(frontValue, sideWeight, nextValue);
    }

    private void addSide (V frontValue, int sideWeight, V nextValue) {
        PointNode node = dataMap.get(frontValue);
        if (node == null) {
            throw new NoSuchElementException();
        }
        PointNode next;
        if (dataMap.containsKey(nextValue)) {
            next = dataMap.get(nextValue);
        } else {
            next = new PointNode(nextValue);
            dataMap.put(nextValue, next);
        }
        SideNode side = new SideNode(sideWeight);
        node.sides.add(side);
        side.next = next;
        dataMap.replace(frontValue, node);
    }

    @Override
    public void clear () {
        dataMap = new HashMap<>();
    }

    @Override
    public void remove (V value) {
        if (!dataMap.containsKey(value)) {
            throw new NullPointerException();
        }
        dataMap.remove(value);
    }

    @Override
    public void remove (V from, V to) {
        if (!dataMap.containsKey(from) || !dataMap.containsKey(to)) {
            throw new NullPointerException();
        }
        for (SideNode side : dataMap.get(from).sides) {
            if (side.next.value.equals(to)) {
                dataMap.get(from).sides.remove(side);
                break;
            }
        }
    }

    @Override
    public void removeAll (int sideWeight) {
        LinkedList<PointNode> points = new LinkedList<>(dataMap.values());
        for (PointNode point : points) {
            while (point.sides.stream().anyMatch(sideNode -> sideNode.weight == sideWeight)) {
                point.sides.removeIf(sideNode -> sideNode.weight == sideWeight);
            }
        }
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
        return dataMap.get(value);
    }

    @Override
    public List<?> getSides (V value) {
        if (!dataMap.containsKey(value)) {
            throw new NullPointerException();
        }
        return new LinkedList<>(dataMap.get(value).sides);
    }

    @Override
    public List<V> getPoints () {
        List<V> ans = new LinkedList<>();
        dataMap.forEach((v, pointNode) -> ans.add(pointNode.value));
        return ans;
    }

    @Override
    public List<V> getPoints (V value) {
        if (!dataMap.containsKey(value)) {
            throw new NullPointerException();
        }
        PointNode node = dataMap.get(value);
        List<V> ans = new LinkedList<>();
        node.sides.forEach(side -> ans.add(side.next.value));
        return ans;
    }

    @Override
    public List<SideNode> getSides (int sideWeight) {
        List<SideNode> ans = new LinkedList<>();
        dataMap.values().forEach(pointNode -> {
            pointNode.sides.forEach(side -> {
                if (side.weight == sideWeight) {
                    ans.add(side);
                }
            });
        });
        return ans;
    }

    @Override
    public Graph<V> deepCopy () {
        return this;
    }

    @Override
    public int size () {
        return dataMap.size();
    }

    @Override
    public int pointAmount () {
        return size();
    }

    @Override
    public int sides () {
        AtomicInteger counter = new AtomicInteger();
        dataMap.forEach((v, point) -> {
            counter.addAndGet(point.sides.size());
        });
        return counter.intValue();
    }

    @Override
    public int sides (int sideWeight) {
        return getSides(sideWeight).size();
    }

    final boolean isConnectionComponent (PointNode point) {
        if (point == null) {
            return false;
        }
        if (point.value.equals(headValue)) {
            return false;
        }
        return partition(point, 0, point.value);
    }

    final boolean partition (PointNode node, int i, V v) {
        if (node.value.equals(headValue)) {
            return false;
        }
        if (i != 0 && node.value.equals(v)) {
            return true;
        }
        boolean flag = false;
        for (SideNode s : node.sides) {
            flag |= partition(s.next, i + 1, v);
        }
        return flag | node.value.equals(v);
    }

    private GraphWithDirection<V> getInstance (PointNode point) {
        return null;
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
        top.kkoishi.util.LinkedList<PointNode> points = new top.kkoishi.util.LinkedList<>(dataMap.values());
        List<Graph<V>> ans = new LinkedList<>();
        for (PointNode point : points) {
            if (isConnectionComponent(point)) {
                if (point.value != headValue) {
                    ans.add(getInstance(point));
                }
            }
        }
        ans.add(this);
        return ans;
    }

    @Deprecated
    public List<V> toList () {
        PointNode head = dataMap.get(headValue);
        return toList(head, head, 0);
    }

    @Deprecated
    private List<V> toList (PointNode node, PointNode start, int endMark) {
        if (endMark == 1) {
            return new ArrayList<>();
        }
        List<V> list = new ArrayList<>();
        list.add(node.value);
        Vector<SideNode> sides = node.sides;
        for (SideNode side : sides) {
            if (side.next == start || side.next.sides.isEmpty()) {
                list.add(side.next.value);
            } else {
                list.add(side.next.value);
                list.addAll(toList(side.next, start, 0));
            }
        }
        return list;
    }

    @Override
    public String toString () {
        return toString(dataMap.get(headValue), 0);
    }

    private String toString (PointNode node, int times) {
        PointNode head = dataMap.get(headValue);
        StringBuilder b = new StringBuilder(node.value.toString());
        if (!node.sides.isEmpty()) {
            b.append("\n");
            b.append("\t".repeat(Math.max(0, times)));
            b.append("->{");
            for (SideNode side : node.sides) {
                b.append(side.weight).append("->");
                if (side.next != head) {
                    b.append(toString(side.next, times + 1)).append("  ");
                } else {
                    b.append(head.value.toString());
                }
            }
            b.append("}");
        }
        return b.toString();
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<V> iterator () {
        return new GraphIterator(dataMap.values());
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
        for (V v : this) {
            action.accept(v);
        }
    }

    final class GraphIterator implements Iterator<V> {
        HashMap<V, Boolean> visited;
        List<PointNode> pointNodes;
        int pointer = 0;

        public GraphIterator (Collection<PointNode> c) {
            visited = new HashMap<>();
            pointNodes = new Vector<>(c);
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
            return pointer + 1 <= pointNodes.size();
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public V next () {
            {
                V v = pointNodes.get(pointer).value;
                while (visited.containsKey(v)) {
                    pointer++;
                    v = pointNodes.get(pointer).value;
                }
            }
            V v = pointNodes.get(pointer++).value;
            visited.put(v, true);
            return v;
        }
    }
}

class GraphTest {
    public static void main (String[] args) {
        GraphWithDirection<String> graph = new GraphWithDirection<>();
        graph.add("start");
        graph.add("end");
        graph.add("rue");
        graph.add("start", 10, "p(1,1)");
        graph.add("p(1,1)", 5, "p(1,2)");
        graph.add("p(1,2)", 6, "p(1,3)");
        graph.add("p(1,3)", 6, "end");
        graph.add("start", 5, "p(2,1)");
        graph.add("p(2,1)", 3, "p(2,2)");
        graph.add("p(2,2)", 10, "p(2,3)");
        graph.add("p(2,3)", 3, "p(2/3,4)");
        graph.add("start", 9, "p(3,1)");
        graph.add("p(3,1)", 7, "p(3,2)");
        graph.add("p(3,2)", 8, "p(3,3)");
        graph.add("p(3,3)", 5, "p(2/3,4)");
        graph.add("p(2/3,4)", 1, "end");
        graph.add("p(1,1)", 9, "p(2,1)");
        graph.add("p(1,2)", 4, "p(2,1)");
        graph.add("p(1,3)", 4, "p(2,2)");
        graph.add("p(2,1)", 12, "p(3,2)");
        graph.add("p(3,1)", 2, "p(2,1)");
        graph.add("end", 0, "start");
        System.out.println(graph);
        System.out.println(graph.getSides("start"));
        System.out.println(graph.getPoints("start"));
        System.out.println(graph.getConnectionComponent().size());
        System.out.println(graph.isConnectionComponent((GraphWithDirection<String>.PointNode) graph.getPoint("rue")));
    }
}
