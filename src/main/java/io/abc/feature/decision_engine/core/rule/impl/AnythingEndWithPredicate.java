package io.abc.feature.decision_engine.core.rule.impl;

import java.util.Collection;
import java.util.function.BiPredicate;

/**
 * Checks whether a source string ends with one or more expected suffixes.
 */
public class AnythingEndWithPredicate implements BiPredicate<Object, Object> {

    /**
     * Evaluates suffix matching.
     *
     * @param source source value, expected to be a string
     * @param expectedValue expected suffix string or collection of suffixes
     * @return {@code true} when source ends with any expected suffix
     */
    @Override
    public boolean test(Object source, Object expectedValue) {
        if (source instanceof String sourceString) {
            if (expectedValue instanceof Collection<?> expectedValueCollection) {
                for (final Object value : expectedValueCollection) {
                    if (sourceString.endsWith(value.toString())) {
                        return true;
                    }
                }
            } else if (expectedValue instanceof String expectedValueString) {
                return sourceString.endsWith(expectedValueString);
            }
        }
        return false;
    }
}
