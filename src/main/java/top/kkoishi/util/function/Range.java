package top.kkoishi.util.function;

import java.io.Serializable;
import java.util.Iterator;

import static java.lang.StrictMath.abs;

public class Range implements Serializable, Iterable<Integer> {
    double x1;
    double x2;
    double x;

    public Range (double x1, double x2) {
        this.x1 = x1;
        x = x1;
        this.x2 = x2;
    }

    public double getX1 () {
        return x1;
    }

    public double getX2 () {
        return x2;
    }

    public double getX () {
        return x;
    }

    public void next () {
        x++;
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<Integer> iterator () {
        return new Iterator<>() {
            @Override
            public boolean hasNext () {
                return x < x2;
            }

            @Override
            public Integer next () {
                x++;
                return (int) x;
            }
        };
    }
}
