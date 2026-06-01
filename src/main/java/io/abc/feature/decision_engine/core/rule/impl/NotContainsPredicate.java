package io.abc.feature.decision_engine.core.rule.impl;

import java.util.Collection;
import java.util.function.BiPredicate;

public class NotContainsPredicate implements BiPredicate<Object, Object> {

    private final ContainsPredicate contains = new ContainsPredicate();

    @Override
    public boolean test(Object source, Object expectedValue) {
        if (source instanceof Collection || source instanceof String) {
            return !contains.test(source, expectedValue);
        }
        return false;
    }
}
