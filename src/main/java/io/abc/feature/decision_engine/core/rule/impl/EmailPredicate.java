package io.abc.feature.decision_engine.core.rule.impl;

import com.google.re2j.Pattern;

import java.util.function.BiPredicate;

public class EmailPredicate implements BiPredicate<Object, Object> {
    // https://owasp.org/www-community/OWASP_Validation_Regex_Repository
    private static final Pattern email = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,15}$");

    @Override
    public boolean test(Object source, Object target) {
        if (source instanceof String sourceString && target instanceof Boolean targetBoolean) {
            return email.matcher(sourceString).matches() && targetBoolean;
        }
        return false;
    }
}
