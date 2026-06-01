package io.abc.feature.decision_engine.core.rule.impl;

import java.util.Collection;
import java.util.function.BiPredicate;

/**
 * Checks whether a collection or string contains the expected value.
 */
public class ContainsPredicate implements BiPredicate<Object, Object> {

    /**
     * Evaluates containment for collection and string sources.
     *
     * @param source collection or string source
     * @param expectedValue value expected to be contained
     * @return {@code true} when source contains expectedValue
     */
    @Override
    public boolean test(Object source, Object expectedValue) {
        if (source instanceof Collection<?> sourceCollection) {
            return sourceCollection.contains(expectedValue);
        }
        if (source instanceof String sourceString) {
            return sourceString.contains(expectedValue.toString());
        }
        return false;
    }
}
