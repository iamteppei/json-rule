package io.abc.feature.decision_engine.core.rule.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class AllRule<E extends Predicate> implements Predicate {

    private final List<E> predicates;

    public AllRule(List<E> predicates) {
        this.predicates = predicates;
    }

    @Override
    public boolean test(Object value) {
        if (value instanceof Collection<?> valueCollection) {
            return valueCollection.stream().filter(item -> !isValid(item)).findFirst().orElse(null) == null;
        } else if (value instanceof Map) {
            return predicates.stream().filter(it -> !it.test(value)).findFirst().orElse(null) == null;
        }
        return false;
    }

    private boolean isValid(Object item) {
        return predicates.stream().filter(it -> !it.test(item)).findFirst().orElse(null) == null;
    }
}
