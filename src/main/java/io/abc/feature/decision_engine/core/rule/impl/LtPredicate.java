package io.abc.feature.decision_engine.core.rule.impl;

import java.math.BigDecimal;
import java.util.function.BiPredicate;

public class LtPredicate implements BiPredicate<Object, Object> {
    @Override
    public boolean test(Object target, Object expected) {
        if (target instanceof BigDecimal targetBigDecimal && expected instanceof BigDecimal expectedBigDecimal) {
            return targetBigDecimal.compareTo(expectedBigDecimal) < 0;
        }
        return false;
    }
}
