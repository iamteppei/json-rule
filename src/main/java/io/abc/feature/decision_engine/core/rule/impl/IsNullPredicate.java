package io.abc.feature.decision_engine.core.rule.impl;

import java.util.Objects;
import java.util.function.BiPredicate;

/**
 * Checks whether a value is null.
 */
public class IsNullPredicate implements BiPredicate<Object, Object> {
    /**
     * Evaluates null check.
     *
     * @param source source value
     * @param target unused expected value
     * @return {@code true} when source is null
     */
    @Override
    public boolean test(Object source, Object target) {
        return Objects.isNull(source);
    }
}
