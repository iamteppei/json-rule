package io.abc.feature.decision_engine.core.rule.impl;

import java.util.Collection;
import java.util.function.BiPredicate;

public class ContainsPredicate implements BiPredicate<Object, Object> {

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
