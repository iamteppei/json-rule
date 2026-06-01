package io.abc.feature.decision_engine.core.rule.impl;

import java.util.Objects;
import java.util.function.BiPredicate;

/**
 * Checks value equality using {@link Objects#equals(Object, Object)}.
 */
public class EqPredicate implements BiPredicate<Object, Object> {

    /**
     * Evaluates equality.
     *
     * @param source source value
     * @param target expected value
     * @return {@code true} when both values are equal
     */
    @Override
    public boolean test(Object source, Object target) {
        return Objects.equals(source, target);
    }
}
