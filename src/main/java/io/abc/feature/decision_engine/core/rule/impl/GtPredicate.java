package io.abc.feature.decision_engine.core.rule.impl;

import java.math.BigDecimal;
import java.util.function.BiPredicate;

/**
 * Checks that a numeric source is greater than the expected value.
 */
public class GtPredicate implements BiPredicate<Object, Object> {
    /**
     * Evaluates greater-than for BigDecimal inputs.
     *
     * @param target source value
     * @param expected expected value
     * @return {@code true} when target is greater than expected
     */
    @Override
    public boolean test(Object target, Object expected) {
        if (target instanceof BigDecimal targetBigDecimal && expected instanceof BigDecimal expectedBigDecimal) {
            return targetBigDecimal.compareTo(expectedBigDecimal) > 0;
        }
        return false;
    }
}
