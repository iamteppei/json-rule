package io.abc.feature.decision_engine.core.rule.impl;

import com.google.re2j.Pattern;

import java.util.function.BiPredicate;

/**
 * Validates email addresses using a predefined regular expression.
 */
public class EmailPredicate implements BiPredicate<Object, Object> {
    // https://owasp.org/www-community/OWASP_Validation_Regex_Repository
    private static final Pattern email = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,15}$");

    /**
     * Evaluates email validity.
     *
     * @param source source value, expected to be an email string
     * @param target expected boolean flag; only true can pass
     * @return {@code true} when source is a valid email and target is true
     */
    @Override
    public boolean test(Object source, Object target) {
        if (source instanceof String sourceString && target instanceof Boolean targetBoolean) {
            return email.matcher(sourceString).matches() && targetBoolean;
        }
        return false;
    }
}
