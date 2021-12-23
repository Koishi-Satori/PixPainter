package top.kkoishi.util.function;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import static java.lang.StrictMath.*;

public class Functions {
    private Functions () {
    }

    public static double simpleCal (String expression, double x) {
        String[] strings = expression.split("\\s+");
        if (strings.length == 2) {
            switch (strings[0]) {
                case "sqrt" : {
                    return sqrt(x);
                }
                case "exp" : {
                    return exp(x);
                }
                case "ln" : {
                    return log(x);
                }
                case "log" : {
                    return log10(x);
                }
                case "cube" : {
                    return cbrt(x);
                }
                case "sin" : {
                    return sin(x);
                }
                case "cos" : {
                    return cos(x);
                }
                case "sh" : {
                    return sinh(x);
                }
                case "ch" : {
                    return cosh(x);
                }
                case "tg" : {
                    return tan(x);
                }
                case "tanh" : {
                    return tanh(x);
                }
                case "sec" : {
                    return 1.0 / cos(x);
                }
                case "csc" : {
                    return 1.0 / sin(x);
                }
                case "cot" : {
                    return 1.0 / tan(x);
                }
                case "asin" : {
                    return asin(x);
                }
                case "acos" : {
                    return acos(x);
                }
                case "atan" : {
                    return atan(x);
                }
                default : {
                    return x;
                }
            }
        } else if (strings.length == 3) {
            boolean isFirst = !strings[0].equals(x + "");
            double i = Double.parseDouble((isFirst) ? strings[0] : strings[2]);
            switch (strings[1]) {
                case "+" : {
                    return x + i;
                }
                case "-" : {
                    return (isFirst ? (i - x) : (x - i));
                }
                case "*" : {
                    return x * i;
                }
                case "/" : {
                    return (isFirst ? (i / x) : (x / i));
                }
                default : {
                    return x;
                }
            }
        } else {
            return Double.POSITIVE_INFINITY;
        }
    }

    public static Shape execute (Function function) {
        return new Shape() {
            @Override
            public Rectangle getBounds () {
                return function.getBound();
            }

            @Override
            public Rectangle2D getBounds2D () {
                return function.getBound2D();
            }

            @Override
            public boolean contains (double x, double y) {
                return function.getPoints().contains(new Point2D.Double(x, y));
            }

            @Override
            public boolean contains (Point2D p) {
                return function.getPoints().contains(p);
            }

            @Override
            public boolean intersects (double x, double y, double w, double h) {
                return false;
            }

            @Override
            public boolean intersects (Rectangle2D r) {
                return false;
            }

            @Override
            public boolean contains (double x, double y, double w, double h) {
                return false;
            }

            @Override
            public boolean contains (Rectangle2D r) {
                return false;
            }

            @Override
            public PathIterator getPathIterator (AffineTransform at) {
                return null;
            }

            @Override
            public PathIterator getPathIterator (AffineTransform at, double flatness) {
                return null;
            }
        };
    }
}
