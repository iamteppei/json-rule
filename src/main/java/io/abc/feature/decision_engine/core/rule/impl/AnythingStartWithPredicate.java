package io.abc.feature.decision_engine.core.rule.impl;

import java.util.Collection;
import java.util.function.BiPredicate;

/**
 * Checks whether a source string starts with one or more expected prefixes.
 */
public class AnythingStartWithPredicate implements BiPredicate<Object, Object> {

    /**
     * Evaluates prefix matching.
     *
     * @param source source value, expected to be a string
     * @param expectedValue expected prefix string or collection of prefixes
     * @return {@code true} when source starts with any expected prefix
     */
    @Override
    public boolean test(Object source, Object expectedValue) {
        if (source instanceof String sourceString) {
            if (expectedValue instanceof Collection<?> expectedValueCollection) {
                for (final Object value : expectedValueCollection) {
                    if (sourceString.startsWith(value.toString())) {
                        return true;
                    }
                }
            } else if (expectedValue instanceof String expectedValueString) {
                return sourceString.startsWith(expectedValueString);
            }
        }
        return false;
    }
}
