package io.abc.feature.decision_engine.core.rule.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Composite predicate that requires at least one configured predicate to match.
 *
 * @param <E> predicate type
 */
public final class AnyRule<E extends Predicate> implements Predicate {

    private final List<E> predicates;

    /**
     * Creates an any-match composite.
     *
     * @param predicates predicates where one passing check is sufficient
     */
    public AnyRule(List<E> predicates) {
        this.predicates = predicates;
    }

    /**
     * Evaluates the input.
     *
     * <p>For a map value, at least one predicate must pass for that map. For a collection value,
     * at least one item in the collection must satisfy the configured predicates.</p>
     *
     * @param value map or collection to evaluate
     * @return true when any applicable check passes, otherwise false
     */
    @Override
    public boolean test(Object value) {
        if (value instanceof Collection<?> valueCollection) {
            return valueCollection.stream().filter(this::isValid).findFirst().orElse(null) != null;
        } else if (value instanceof Map) {
            return predicates.stream().filter(it -> it.test(value)).findFirst().orElse(null) != null;
        }
        return false;
    }

    private boolean isValid(Object item) {
        return predicates.stream().filter(it -> it.test(item)).findFirst().orElse(null) != null;
    }
}
