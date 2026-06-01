package io.abc.feature.decision_engine.core.rule.impl;

import java.util.Objects;
import java.util.function.BiPredicate;

public class NePredicate implements BiPredicate<Object, Object> {
    @Override
    public boolean test(Object o, Object o2) {
        return !Objects.equals(o, o2);
    }
}
