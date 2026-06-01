package io.abc.feature.decision_engine.core.rule.impl;

import java.util.Objects;
import java.util.function.BiPredicate;

public class ExistPredicate implements BiPredicate<Object, Object> {

    @Override
    public boolean test(Object source, Object expectedValue) {
        if (expectedValue instanceof Boolean expectedValueBoolean) {
            return !Objects.equals(source, Unknown.INSTANCE) && expectedValueBoolean;
        }
        return false;
    }
}
