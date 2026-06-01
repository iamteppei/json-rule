package io.abc.feature.decision_engine.core.rule.impl;

import java.util.Objects;
import java.util.function.BiPredicate;

public class NonNullPredicate implements BiPredicate<Object, Object> {
    @Override
    public boolean test(Object source, Object target) {
        return Unknown.isNotUnknown(source) && Objects.nonNull(source);
    }
}
