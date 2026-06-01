package io.abc.feature.decision_engine.core.rule.impl;

import java.util.function.BiPredicate;

public class StartWithPredicate implements BiPredicate<Object, Object> {

    @Override
    public boolean test(Object source, Object expectedValue) {
        return source instanceof String sourceString && expectedValue instanceof String expectedValueString && sourceString.startsWith(expectedValueString);
    }
}
