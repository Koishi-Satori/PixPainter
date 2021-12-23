package top.kkoishi.util.function;

import top.kkoishi.util.Vector;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.Iterator;
import java.util.function.Consumer;

public abstract class Function implements Serializable, Cloneable, Iterable<Double> {
    private transient double x;
    private transient double y;
    private double x1;
    private double x2;
    /**
     * For calculate.
     * examples:
     */
    private String expression;

    public Function (double x1, double x2, String expression) {
        this.x1 = x1;
        this.x2 = x2;
        this.expression = expression;
        x = x1;
        y = Functions.simpleCal(expression, x1);
    }

    public Function (Range range, String expression) {
        this.x1 = range.x1;
        this.x2 = range.x2;
        this.expression = expression;
        x = x1;
        y = Functions.simpleCal(expression, x1);
    }

    public static class Simple extends Function {
        public Simple (double x1, double x2, String expression) {
            super(x1, x2, expression);
        }

        public Simple (Range range, String expression) {
            super(range, expression);
        }

        /**
         * Returns an iterator over elements of type {@code T}.
         *
         * @return an Iterator.
         */
        @Override
        public Iterator<Double> iterator () {
            return super.iterator();
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
        public void forEach (Consumer<? super Double> action) {
            super.forEach(action);
        }

        @Override
        public Function clone () {
            return super.clone();
        }

        @Override
        public Rectangle getBound () {
            return super.getBound();
        }

        @Override
        public Rectangle2D getBound2D () {
            return super.getBound2D();
        }

        @Override
        public Vector<Point2D> getPoints () {
            return super.getPoints();
        }
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<Double> iterator () {
        return new Iterator<>() {
            @Override
            public boolean hasNext () {
                return x - x2 < 0.001;
            }

            @Override
            public Double next () {
                x += 0.25;
                return Functions.simpleCal(expression, x);
            }
        };
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
    public void forEach (Consumer<? super Double> action) {
        Iterable.super.forEach(action);
    }

    @Override
    @SuppressWarnings("all")
    public Function clone () {
        try {
            Function clone = (Function) super.clone();
            //copy mutable state here, so the clone can't change the internals of the original
            clone.expression = this.expression;
            clone.x = this.x;
            clone.y = this.y;
            clone.x1 = this.x1;
            clone.x2 = this.x2;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public Rectangle getBound () {
        return new Rectangle((int) x1, (int) Functions.simpleCal(expression, x1),
                (int) x2, (int) Functions.simpleCal(expression, x2));
    }

    public Rectangle2D getBound2D () {
        return new Rectangle2D.Double(x1, Functions.simpleCal(expression, x1),
                x2, Functions.simpleCal(expression, x2));
    }

    public Vector<Point2D> getPoints () {
        Vector<Point2D> point2ds = new Vector<>();
        for (Double aDouble : this) {
            point2ds.add(new Point2D.Double(this.x, aDouble));
        }
        return point2ds;
    }
}
