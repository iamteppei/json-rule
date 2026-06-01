package io.abc.feature.decision_engine.core.rule.impl;

import io.abc.feature.decision_engine.core.rule.api.Condition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class ConditionTest {

    @Test
    void givenUnknownOperator_whenAdd_thenReturnFalse() {
        // given
        final Condition condition = new Condition();

        // when
        final boolean added = condition.add("unknown");

        // then
        Assertions.assertFalse(added);
    }

    @Test
    void givenValidOperatorAndExpectedValue_whenAdd_thenAcceptOnlyTwoElements() {
        // given
        final Condition condition = new Condition();

        // when
        final boolean firstAdded = condition.add(Condition.EQ_RULE);
        final boolean secondAdded = condition.add("running");
        final boolean thirdAdded = condition.add("unexpected");

        // then
        Assertions.assertTrue(firstAdded);
        Assertions.assertTrue(secondAdded);
        Assertions.assertFalse(thirdAdded);
    }

    @Test
    void givenEqCondition_whenTest_thenEvaluateCorrectly() {
        // given
        final Condition condition = new Condition();
        condition.add(Condition.EQ_RULE);
        condition.add("running");

        // then
        Assertions.assertTrue(condition.test("running"));
        Assertions.assertFalse(condition.test("stopped"));
    }

    @Test
    void givenGtCondition_whenTest_thenEvaluateNumericComparison() {
        // given
        final Condition condition = new Condition();
        condition.add(Condition.GT_RULE);
        condition.add(BigDecimal.valueOf(3));

        // then
        Assertions.assertTrue(condition.test(BigDecimal.valueOf(4)));
        Assertions.assertFalse(condition.test(BigDecimal.valueOf(3)));
    }

    @Test
    void givenNonNullConditionWithoutExpectedValue_whenTest_thenEvaluateSourceOnly() {
        // given
        final Condition condition = new Condition();
        condition.add(Condition.NON_NULL_RULE);

        // then
        Assertions.assertTrue(condition.test("value"));
        Assertions.assertFalse(condition.test(null));
    }
}
