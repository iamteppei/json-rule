package io.abc.feature.decision_engine.core.rule.impl;

import java.util.Objects;
import java.util.function.BiPredicate;

/**
 * Checks whether a value is non-null and not the unknown sentinel.
 */
public class NonNullPredicate implements BiPredicate<Object, Object> {
    /**
     * Evaluates non-null check.
     *
     * @param source source value
     * @param target unused expected value
     * @return {@code true} when source is neither null nor unknown
     */
    @Override
    public boolean test(Object source, Object target) {
        return Unknown.isNotUnknown(source) && Objects.nonNull(source);
    }
}
