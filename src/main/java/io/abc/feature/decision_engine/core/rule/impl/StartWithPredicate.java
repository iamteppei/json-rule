package io.abc.feature.decision_engine.core.rule.impl;

import java.util.function.BiPredicate;

/**
 * Checks whether a source string starts with the expected prefix.
 */
public class StartWithPredicate implements BiPredicate<Object, Object> {

    /**
     * Evaluates prefix matching.
     *
     * @param source source value, expected to be a string
     * @param expectedValue expected prefix string
     * @return {@code true} when source starts with expectedValue
     */
    @Override
    public boolean test(Object source, Object expectedValue) {
        return source instanceof String sourceString && expectedValue instanceof String expectedValueString && sourceString.startsWith(expectedValueString);
    }
}
