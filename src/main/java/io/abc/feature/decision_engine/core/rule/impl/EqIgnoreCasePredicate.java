package io.abc.feature.decision_engine.core.rule.impl;

import java.util.Optional;
import java.util.function.BiPredicate;

public class EqIgnoreCasePredicate implements BiPredicate<Object, Object> {

    @Override
    public boolean test(Object source, Object target) {
        if (source instanceof String sourceString) {
            return sourceString.equalsIgnoreCase(Optional.ofNullable(target).map(Object::toString).orElse(null));
        }
        return false;
    }
}
