package io.abc.feature.decision_engine.core.rule.impl;

import java.math.BigDecimal;
import java.util.function.BiPredicate;

/**
 * Checks that a numeric source is less than or equal to expected value.
 */
public class LePredicate implements BiPredicate<Object, Object> {
    /**
     * Evaluates less-than-or-equal for BigDecimal inputs.
     *
     * @param target source value
     * @param expected expected value
     * @return {@code true} when target is less than or equal to expected
     */
    @Override
    public boolean test(Object target, Object expected) {
        if (target instanceof BigDecimal targetBigDecimal && expected instanceof BigDecimal expectedBigDecimal) {
            return targetBigDecimal.compareTo(expectedBigDecimal) <= 0;
        }
        return false;
    }
}
