package io.abc.feature.decision_engine.core.rule.impl;

import java.util.Objects;
import java.util.function.BiPredicate;

/**
 * Checks that source and expected values are not equal.
 */
public class NePredicate implements BiPredicate<Object, Object> {
    /**
     * Evaluates non-equality.
     *
     * @param o source value
     * @param o2 expected value
     * @return {@code true} when values differ
     */
    @Override
    public boolean test(Object o, Object o2) {
        return !Objects.equals(o, o2);
    }
}
