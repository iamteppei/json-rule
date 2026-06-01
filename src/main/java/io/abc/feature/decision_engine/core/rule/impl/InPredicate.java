package io.abc.feature.decision_engine.core.rule.impl;

import java.util.Collection;
import java.util.function.BiPredicate;

public class InPredicate implements BiPredicate<Object, Object> {

    /**
     * Whether the source object is in the expectedValue
     *
     * @param source        the source value
     * @param expectedValue the expected list of value
     * @return true if the expectedValue contains the source value
     */
    @Override
    public boolean test(Object source, Object expectedValue) {
        if (source != null && expectedValue instanceof Collection<?> expectedValues) {
            return expectedValues.contains(source);
        }
        return false;
    }
}
