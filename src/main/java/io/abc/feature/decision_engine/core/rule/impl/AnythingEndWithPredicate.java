package io.abc.feature.decision_engine.core.rule.impl;

import java.util.Collection;
import java.util.function.BiPredicate;

public class AnythingEndWithPredicate implements BiPredicate<Object, Object> {

    @Override
    public boolean test(Object source, Object expectedValue) {
        if (source instanceof String sourceString) {
            if (expectedValue instanceof Collection<?> expectedValueCollection) {
                for (final Object value : expectedValueCollection) {
                    if (sourceString.endsWith(value.toString())) {
                        return true;
                    }
                }
            } else if (expectedValue instanceof String expectedValueString) {
                return sourceString.endsWith(expectedValueString);
            }
        }
        return false;
    }
}
