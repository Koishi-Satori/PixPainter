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

import java.util.LinkedList;
import java.util.List;

public class GraphWithoutDirection<V> implements Graph<V> {
    class PointNode implements Point {
        V value;
        LinkedList<SideNode> sides;

        public PointNode (V value) {
            this.value = value;
            sides = new LinkedList<>();
        }
    }

    class SideNode {
        int weight;
        PointNode from;
        PointNode to;

        public SideNode (int weight) {
            this.weight = weight;
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
        return false;
    }

    @Override
    public void add (V from, int sideWeight, V to) {

    }

    @Override
    public void clear () {

    }

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
}
