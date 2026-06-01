package io.abc.feature.decision_engine.core.rule.impl;

import java.util.Objects;

/**
 * Sentinel object representing a missing property during rule evaluation.
 */
public final class Unknown {
    /**
     * Shared sentinel instance.
     */
    public static final Unknown INSTANCE = new Unknown();

    private Unknown() {
        // private constructor to prevent instantiation
    }

    /**
     * Checks whether the given value is not the unknown sentinel.
     *
     * @param value value to test
     * @return {@code true} when value is different from {@link #INSTANCE}
     */
    public static boolean isNotUnknown(Object value) {
        return !Objects.equals(value, INSTANCE);
    }
}
