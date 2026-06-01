package io.abc.feature.decision_engine.core.rule.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Composite predicate that requires all configured predicates to match.
 *
 * @param <E> predicate type
 */
public final class AllRule<E extends Predicate> implements Predicate {

    private final List<E> predicates;

    /**
     * Creates an all-match composite.
     *
     * @param predicates predicates that must all pass
     */
    public AllRule(List<E> predicates) {
        this.predicates = predicates;
    }

    /**
     * Evaluates the input.
     *
     * <p>For a map value, all predicates are evaluated against that map. For a collection
     * value, each item in the collection must satisfy all predicates.</p>
     *
     * @param value map or collection to evaluate
     * @return {@code true} when all checks pass, otherwise false
     */
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
