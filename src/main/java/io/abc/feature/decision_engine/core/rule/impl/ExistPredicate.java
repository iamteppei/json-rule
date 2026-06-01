package io.abc.feature.decision_engine.core.rule.impl;

import java.util.Objects;
import java.util.function.BiPredicate;

/**
 * Checks property existence semantics using the unknown sentinel.
 */
public class ExistPredicate implements BiPredicate<Object, Object> {

    /**
     * Evaluates existence check.
     *
     * @param source resolved property value
     * @param expectedValue expected boolean flag
     * @return {@code true} when source exists and expectedValue is true
     */
    @Override
    public boolean test(Object source, Object expectedValue) {
        if (expectedValue instanceof Boolean expectedValueBoolean) {
            return !Objects.equals(source, Unknown.INSTANCE) && expectedValueBoolean;
        }
        return false;
    }
}
