package top.kkoishi.util;

import top.kkoishi.util.graph.Graph;

import java.util.List;

public class GraphWithoutDirection<V> implements Graph<V>, Cloneable {
    @Override
    @SuppressWarnings("all")
    public GraphWithoutDirection<V> clone () {
        try {
            GraphWithoutDirection clone = (GraphWithoutDirection) super.clone();
            //copy mutable state here, so the clone can't change the internals of the original
            clone.points = this.points;
            clone.nodeAmount = this.nodeAmount;
            clone.sideAmount = this.sideAmount;
            clone.headValue = this.headValue;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /*------------------------------------Field Start-------------------------------------*/

    /**
     * Simple Side class.
     */
    final class Side {
        int weight;
        Point prev;
        Point next;

        public Side (int weight) {
            this.weight = weight;
        }

        public Side (int weight, Point prev, Point next) {
            this.weight = weight;
            this.prev = prev;
            this.next = next;
        }

        @Override
        public String toString () {
            return String.valueOf(weight);
        }
    }

    /**
     * Simple Point class.
     */
    final class Point implements Graph.Point {
        V value;
        LinkedList<Side> sides;

        public Point (V value) {
            this.value = value;
            sides = new LinkedList<>();
        }

        public Point (V value, LinkedList<Side> sides) {
            this.value = value;
            this.sides = sides;
        }

        @Override
        public String toString () {
            return value.toString();
        }
    }

    /**
     * Amount of the points.
     */
    transient int nodeAmount;

    /**
     * Amount of the sides.
     */
    transient int sideAmount;

    /**
     * Transient array of points;
     */
    public transient Vector<Point> points;

    /**
     * The head value.
     */
    private V headValue;

    /*-----------------------------------Field End--------------------------------------*/

    public GraphWithoutDirection () {
        points = new Vector<>();
        sideAmount = 0;
        nodeAmount = 0;
    }

    final boolean contains (V value) {
        for (Point p : points) {
            if (p.value.equals(value)) {
                return true;
            }
        }
        return false;
    }

    final void link (V from, int weight, V to) {
        int i1 = 0;
        int i2 = 0;
        Point p1 = null;
        Point p2 = null;
        for (int i = 0, pointsSize = points.size(); i < pointsSize; i++) {
            Point p = points.get(i);
            if (p.value.equals(from)) {
                p1 = p;
                i1 = i;
            }
            if (p.value.equals(to)) {
                p2 = p;
                i2 = i;
            }
            if (p1 != null && p2 != null) {
                Side s = new Side(weight, p1, p2);
                p1.sides.add(s);
                sideAmount++;
                points.set(i1, p1);
                points.set(i2, p2);
                break;
            }
        }
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
        nodeAmount++;
        if (headValue == null) {
            headValue = value;
            points.add(new Point(headValue));
            return true;
        }
        return contains(value) ? false : points.add(new Point(value));
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
        if (!contains(from)) {
            throw new NullPointerException();
        }
        sideAmount++;
        link(from, sideWeight, to);
    }

    /**
     * Directly clear the whole graph.
     * Recommend way:<br>
     * re-initialize the data-structure which
     * stores the data.
     */
    @Override
    public void clear () {
        this.sideAmount = 0;
        this.nodeAmount = 0;
        this.points.clear();
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

    @Override
    public void remove (V from, V to) {

    }

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
    public Graph.Point getPoint (V value) {
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

    @Override
    public String toString () {
        return "GraphWithoutDirection(无向图):" + toString(points.get(0), 0);
    }

    final String toString (Point point, int i) {
        if (point.value == headValue && i != 0) {
            return point.toString();
        }
        StringBuilder zwcsb = new StringBuilder(point + "\n");
        for (Side s : point.sides) {
            zwcsb.append("  ".repeat(Math.max(0, i))).append("->{")
                    .append(s.weight)
                    .append("->")
                    .append(toString(s.next, i + 1))
                    .append("}\n");
        }
        return zwcsb.toString();
    }
}

class GwdTest {
    public static void main (String[] args) {
        GraphWithoutDirection<Integer> graph = new GraphWithoutDirection<>();
        graph.add(0);
        graph.add(1);
        graph.add(0, -1, 1);
        graph.add(1, -2, 0);
        System.out.println(graph);
    }
}
