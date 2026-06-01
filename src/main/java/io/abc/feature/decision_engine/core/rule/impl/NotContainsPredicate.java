package io.abc.feature.decision_engine.core.rule.impl;

import java.util.Collection;
import java.util.function.BiPredicate;

/**
 * Checks whether a collection or string does not contain the expected value.
 */
public class NotContainsPredicate implements BiPredicate<Object, Object> {

    private final ContainsPredicate contains = new ContainsPredicate();

    /**
     * Evaluates non-containment.
     *
     * @param source collection or string source
     * @param expectedValue value expected to be absent
     * @return {@code true} when source does not contain expectedValue
     */
    @Override
    public boolean test(Object source, Object expectedValue) {
        if (source instanceof Collection || source instanceof String) {
            return !contains.test(source, expectedValue);
        }
        return false;
    }
}
