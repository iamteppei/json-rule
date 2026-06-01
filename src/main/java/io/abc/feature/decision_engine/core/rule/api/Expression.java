package io.abc.feature.decision_engine.core.rule.api;

public final class Expression {

    private Expression() {
        //
    }

    /**
     * Whether a value is an expression. Example ${key1.key2}
     *
     * @param value the value
     * @return true if value is an expression
     */
    public static boolean isExpression(Object value) {
        if (value instanceof String stringValue) {
            return isExpression(stringValue);
        }
        return false;
    }

    private static boolean isExpression(String stringValue) {
        final int length = stringValue.length();
        return length > 3 && stringValue.charAt(0) == '$' && stringValue.charAt(1) == '{' && stringValue.charAt(length - 1) == '}';
    }

    public static String getExpression(Object value) {
        if (value instanceof String stringValue && isExpression(stringValue)) {
            return stringValue.substring(2, stringValue.length() - 1).trim();
        }
        return null;
    }
}
