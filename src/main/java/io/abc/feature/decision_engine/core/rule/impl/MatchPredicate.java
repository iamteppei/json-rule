package io.abc.feature.decision_engine.core.rule.impl;

import com.google.re2j.Pattern;

import java.util.function.BiPredicate;

public class MatchPredicate implements BiPredicate<Object, Object> {

    @Override
    public boolean test(Object source, Object target) {
        if (source instanceof String sourceString && target instanceof String targetString) {
            final Pattern pattern = Pattern.compile(targetString);
            return pattern.matcher(sourceString).matches();
        }
        return false;
    }
}
