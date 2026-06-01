package io.abc.feature.decision_engine.core.rule.impl;

import java.util.function.BiPredicate;

/**
 * Checks whether a source string ends with the expected suffix.
 */
public class EndWithPredicate implements BiPredicate<Object, Object> {

    /**
     * Evaluates suffix matching.
     *
     * @param source source value, expected to be a string
     * @param expectedValue expected suffix string
     * @return {@code true} when source ends with expectedValue
     */
    @Override
    public boolean test(Object source, Object expectedValue) {
        return source instanceof String sourceString && expectedValue instanceof String expectedValueString && sourceString.endsWith(expectedValueString);
    }
}
