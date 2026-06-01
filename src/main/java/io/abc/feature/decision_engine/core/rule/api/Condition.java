package io.abc.feature.decision_engine.core.rule.api;

import io.abc.feature.decision_engine.core.rule.impl.*;

import java.io.Serial;
import java.util.*;
import java.util.function.BiPredicate;

public final class Condition extends ArrayList<Object> implements Predicate {

    public static final String EQ_RULE = "eq";
    public static final String NE_RULE = "ne";
    public static final String GT_RULE = "gt";
    public static final String GE_RULE = "ge";
    public static final String LT_RULE = "lt";
    public static final String LE_RULE = "le";
    public static final String IN_RULE = "in";
    public static final String EMAIL_RULE = "email";
    public static final String EXISTS_RULE = "exists";
    public static final String NOT_IN_RULE = "not_in";
    public static final String NON_NULL_RULE = "non_null";
    public static final String IS_NULL_RULE = "is_null";
    public static final String END_WITH_RULE = "end_with";
    public static final String ANYTHING_END_WITH_RULE = "anything_end_with";
    public static final String START_WITH_RULE = "start_with";
    public static final String ANYTHING_START_WITH_RULE = "anything_start_with";
    public static final String CONTAINS_RULE = "contains";
    public static final String NOT_CONTAINS_RULE = "not_contains";
    public static final String EQ_IGNORE_CASE_RULE = "eq_ignore_case";
    public static final String MATCH_RULE = "matches";

    @Serial
    private static final long serialVersionUID = -8366114604357499894L;
    private static final Map<String, BiPredicate<Object, Object>> opMap = new HashMap<>();

    private static final Set<String> allowedRules = Set.of(
            EQ_RULE,
            NE_RULE,
            GT_RULE,
            GE_RULE,
            LT_RULE,
            LE_RULE,
            IN_RULE,
            EMAIL_RULE,
            EXISTS_RULE,
            NOT_IN_RULE,
            END_WITH_RULE,
            ANYTHING_END_WITH_RULE,
            START_WITH_RULE,
            ANYTHING_START_WITH_RULE,
            CONTAINS_RULE,
            NOT_CONTAINS_RULE,
            EQ_IGNORE_CASE_RULE,
            NON_NULL_RULE,
            MATCH_RULE,
            IS_NULL_RULE
    );

    static {
        opMap.put(EQ_RULE, new EqPredicate());
        opMap.put(NE_RULE, new NePredicate());
        opMap.put(GT_RULE, new GtPredicate());
        opMap.put(GE_RULE, new GePredicate());
        opMap.put(LT_RULE, new LtPredicate());
        opMap.put(LE_RULE, new LePredicate());
        opMap.put(IN_RULE, new InPredicate());
        opMap.put(EMAIL_RULE, new EmailPredicate());
        opMap.put(EXISTS_RULE, new ExistPredicate());
        opMap.put(NOT_IN_RULE, new NotInPredicate());
        opMap.put(END_WITH_RULE, new EndWithPredicate());
        opMap.put(ANYTHING_END_WITH_RULE, new AnythingEndWithPredicate());
        opMap.put(START_WITH_RULE, new StartWithPredicate());
        opMap.put(ANYTHING_START_WITH_RULE, new AnythingStartWithPredicate());
        opMap.put(CONTAINS_RULE, new ContainsPredicate());
        opMap.put(NOT_CONTAINS_RULE, new NotContainsPredicate());
        opMap.put(EQ_IGNORE_CASE_RULE, new EqIgnoreCasePredicate());
        opMap.put(NON_NULL_RULE, new NonNullPredicate());
        opMap.put(MATCH_RULE, new MatchPredicate());
        opMap.put(IS_NULL_RULE, new IsNullPredicate());
    }

    public Condition() {
        super(2);
    }

    private String getOperatorName() {
        return Optional.ofNullable(get(0)).map(Object::toString).orElse(null);
    }

    private Object getExpectedValue() {
        if (size() == 2) {
            return get(1);
        }
        return null;
    }

    @Override
    public boolean add(Object value) {
        if (isEmpty()) {
            if (value instanceof String && allowedRules.contains(value)) {
                return super.add(value);
            }
            return false;
        } else if (size() == 2) {
            return false;
        }
        return super.add(value);
    }

    @Override
    public boolean test(Object value) {
        final BiPredicate<Object, Object> predicate = opMap.get(getOperatorName());
        if (predicate != null) {
            return predicate.test(value, getExpectedValue());
        }
        return false;
    }
}
