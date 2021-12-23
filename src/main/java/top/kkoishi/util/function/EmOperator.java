package top.kkoishi.util.function;

public enum EmOperator {
    /**
     * Simple calculation
     */
    ADD("+", 2, 1,"add"),
    SUB("-", 2, 1, "minus"),
    MULTIPLY("*", 2, 2, "multiply"),
    DIVIDE("/", 2, 2, "divide"),
    MOD("%", 2, 2, "mod"),
    LEFT_BRACKET("(", 0, 114514, "left"),
    RIGHT_BRACKET(")", 0, 114514, "right"),

    EXP("exp", 1, 3, "e^?"),
    POW("^", 2, 3, "?^?"),
    LOG10("log", 1, 3, "log?"),
    SQRT("sqrt", 1, 3, "sqrt?"),
    LN("ln", 1, 3, "ln?"),
    ABS("abs", 1, 3, "abs?"),
    CEIL("ceil", 1, 3, "ceil?"),
    FLOOR("floor", 1, 3, "floor?"),
    MAX("max", 1, 3, "max?"),
    MIN("min", 1, 3, "min?"),
    ;


    private final String expression;

    private final int paramNumber;

    private final int priority;

    private final String desc;
    EmOperator (String expression, int paramNumber, int priority, String desc) {
        this.expression = expression;
        this.paramNumber = paramNumber;
        this.priority = priority;
        this.desc = desc;
    }

    public static boolean isBasicOperation (String expression) {
        return ADD.expression.equals(expression) || SUB.expression.equals(expression)
                || LEFT_BRACKET.expression.equals(expression) || RIGHT_BRACKET.expression.equals(expression)
                || MULTIPLY.expression.equals(expression) || DIVIDE.expression.equals(expression)
                || MOD.expression.equals(expression);
    }

    public static EmOperator get (String expression) {
        for (EmOperator o : EmOperator.values()) {
            if (o.expression.equals(expression)) {
                return o;
            }
        }
        return null;
    }

    public String getExpression () {
        return expression;
    }

    public int getParamNumber () {
        return paramNumber;
    }

    public int getPriority () {
        return priority;
    }

    public String getDesc () {
        return desc;
    }
}
