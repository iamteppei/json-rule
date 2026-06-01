package io.abc.feature.decision_engine.core.rule.impl;

import java.util.Collection;
import java.util.function.BiPredicate;

/**
 * Checks whether the source value is not present in the expected collection.
 */
public class NotInPredicate implements BiPredicate<Object, Object> {

    private final InPredicate in = new InPredicate();

    /**
     * Evaluates non-membership.
     *
     * @param source source value
     * @param expectedValue expected collection of values
     * @return {@code true} when source is not in expectedValue
     */
    @Override
    public boolean test(Object source, Object expectedValue) {
        if (expectedValue instanceof Collection) {
            return !in.test(source, expectedValue);
        }
        return false;
    }
}
