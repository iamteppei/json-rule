package io.abc.feature.decision_engine.core.rule.api;

/**
 * Functional contract for evaluating whether an input value satisfies a rule.
 */
public interface Predicate {

    /**
     * Evaluates the input value.
     *
     * @param value input value to evaluate
     * @return true when the value satisfies this predicate, otherwise false
     */
    boolean test(Object value);
}
