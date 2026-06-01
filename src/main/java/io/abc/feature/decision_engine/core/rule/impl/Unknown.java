package io.abc.feature.decision_engine.core.rule.impl;

import java.util.Objects;

public final class Unknown {
    // use unknown to represent a not existed key
    public static final Unknown INSTANCE = new Unknown();

    private Unknown() {
        // private constructor to prevent instantiation
    }

    public static boolean isNotUnknown(Object value) {
        return !Objects.equals(value, INSTANCE);
    }
}
