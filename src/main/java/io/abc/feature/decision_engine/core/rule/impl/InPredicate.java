package io.abc.feature.decision_engine.core.rule.impl;

import java.util.Collection;
import java.util.function.BiPredicate;

/**
 * Checks whether the source value is present in the expected collection.
 */
public class InPredicate implements BiPredicate<Object, Object> {

    /**
     * Evaluates membership.
     *
     * @param source source value
     * @param expectedValue expected collection of values
     * @return {@code true} when expectedValue contains source
     */
    @Override
    public boolean test(Object source, Object expectedValue) {
        if (source != null && expectedValue instanceof Collection<?> expectedValues) {
            return expectedValues.contains(source);
        }
        return false;
    }
}
