package io.abc.feature.decision_engine.core.rule.impl;

import com.google.re2j.Pattern;

import java.util.function.BiPredicate;

/**
 * Applies regular expression matching to source strings.
 */
public class MatchPredicate implements BiPredicate<Object, Object> {

    /**
     * Evaluates whether source matches the regex pattern.
     *
     * @param source source value, expected to be a string
     * @param target regex pattern string
     * @return {@code true} when source matches target regex
     */
    @Override
    public boolean test(Object source, Object target) {
        if (source instanceof String sourceString && target instanceof String targetString) {
            final Pattern pattern = Pattern.compile(targetString);
            return pattern.matcher(sourceString).matches();
        }
        return false;
    }
}
