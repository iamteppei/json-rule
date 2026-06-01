package io.abc.feature.decision_engine.core.rule.impl;

import java.util.Collection;
import java.util.function.BiPredicate;

public class NotInPredicate implements BiPredicate<Object, Object> {

    private final InPredicate in = new InPredicate();

    @Override
    public boolean test(Object source, Object expectedValue) {
        if (expectedValue instanceof Collection) {
            return !in.test(source, expectedValue);
        }
        return false;
    }
}
