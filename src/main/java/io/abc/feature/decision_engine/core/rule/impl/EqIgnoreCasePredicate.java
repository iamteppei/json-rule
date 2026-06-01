package io.abc.feature.decision_engine.core.rule.impl;

import java.util.Optional;
import java.util.function.BiPredicate;

/**
 * Checks case-insensitive equality for string sources.
 */
public class EqIgnoreCasePredicate implements BiPredicate<Object, Object> {

    /**
     * Evaluates case-insensitive equality.
     *
     * @param source source value, expected to be a string
     * @param target expected value converted to string when non-null
     * @return {@code true} when values are equal ignoring case
     */
    @Override
    public boolean test(Object source, Object target) {
        if (source instanceof String sourceString) {
            return sourceString.equalsIgnoreCase(Optional.ofNullable(target).map(Object::toString).orElse(null));
        }
        return false;
    }
}
