package io.abc.feature.decision_engine.core.rule.impl;

import io.abc.feature.decision_engine.core.rule.api.AllRule;
import io.abc.feature.decision_engine.core.rule.api.AnyRule;
import io.abc.feature.decision_engine.core.rule.api.Condition;
import io.abc.feature.decision_engine.core.rule.api.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class RuleCompositionTest {

    @Test
    void givenUnsupportedInputType_whenRuleTest_thenReturnFalse() {
        final Rule rule = new Rule("$");
        Assertions.assertFalse(rule.test("not-a-map-or-list"));
    }

    @Test
    void givenNullPredicateInRule_whenTest_thenReturnFalse() {
        final Rule rule = new Rule("$");
        rule.withRule("detail", null);

        final boolean result = rule.test(Map.of("detail", Map.of("state", "running")));

        Assertions.assertFalse(result);
    }

    @Test
    void givenMissingPropertyWithEqCondition_whenRuleTest_thenReturnFalse() {
        final Rule rule = new Rule("$");
        final Condition condition = new Condition();
        condition.add(Condition.EQ_RULE);
        condition.add("running");
        rule.withCondition("state", condition);

        final boolean result = rule.test(Map.of("status", "running"));

        Assertions.assertFalse(result);
    }

    @Test
    void givenListWithNonAggregateCondition_whenAllElementsMatch_thenReturnTrue() {
        final Rule rule = new Rule("$");
        final Condition condition = new Condition();
        condition.add(Condition.START_WITH_RULE);
        condition.add("run");
        rule.withCondition("state", condition);

        final boolean result = rule.test(List.of("running", "runner"));

        Assertions.assertTrue(result);
    }

    @Test
    void givenListWithNonAggregateCondition_whenAnyElementFails_thenReturnFalse() {
        final Rule rule = new Rule("$");
        final Condition condition = new Condition();
        condition.add(Condition.START_WITH_RULE);
        condition.add("run");
        rule.withCondition("state", condition);

        final boolean result = rule.test(List.of("running", "stopped"));

        Assertions.assertFalse(result);
    }

    @Test
    void givenListWithAnyRules_whenAnyItemMatches_thenReturnTrue() {
        final Rule onlyRunning = new Rule("$");
        final Condition runningCondition = new Condition();
        runningCondition.add(Condition.EQ_RULE);
        runningCondition.add("running");
        onlyRunning.withCondition("state", runningCondition);

        final Rule rootRule = new Rule("$");
        rootRule.withAnyRules(List.of(onlyRunning));

        final boolean result = rootRule.test(List.of(
                Map.of("state", "stopped"),
                Map.of("state", "running")
        ));

        Assertions.assertTrue(result);
    }

    @Test
    void givenListWithAllRules_whenAnyItemFails_thenReturnFalse() {
        final Rule onlyRunning = new Rule("$");
        final Condition runningCondition = new Condition();
        runningCondition.add(Condition.EQ_RULE);
        runningCondition.add("running");
        onlyRunning.withCondition("state", runningCondition);

        final Rule rootRule = new Rule("$");
        rootRule.withAllRules(List.of(onlyRunning));

        final boolean result = rootRule.test(List.of(
                Map.of("state", "running"),
                Map.of("state", "stopped")
        ));

        Assertions.assertFalse(result);
    }

    @Test
    void givenAllRule_whenMapSatisfiesAllPredicates_thenReturnTrue() {
        final AllRule<io.abc.feature.decision_engine.core.rule.api.Predicate> allRule =
                new AllRule<>(List.of(
                        value -> ((Map<?, ?>) value).containsKey("state"),
                        value -> "running".equals(((Map<?, ?>) value).get("state"))
                ));

        final boolean result = allRule.test(Map.of("state", "running"));

        Assertions.assertTrue(result);
    }

    @Test
    void givenAnyRule_whenMapSatisfiesNoPredicate_thenReturnFalse() {
        final AnyRule<io.abc.feature.decision_engine.core.rule.api.Predicate> anyRule =
                new AnyRule<>(List.of(
                        value -> ((Map<?, ?>) value).containsKey("state"),
                        value -> "running".equals(((Map<?, ?>) value).get("state"))
                ));

        final boolean result = anyRule.test(Map.of("status", "stopped"));

        Assertions.assertFalse(result);
    }

    @Test
    void givenAllAnyRuleWithUnsupportedType_whenTest_thenReturnFalse() {
        final AllRule<io.abc.feature.decision_engine.core.rule.api.Predicate> allRule = new AllRule<>(List.of(value -> true));
        final AnyRule<io.abc.feature.decision_engine.core.rule.api.Predicate> anyRule = new AnyRule<>(List.of(value -> true));

        Assertions.assertFalse(allRule.test("unsupported"));
        Assertions.assertFalse(anyRule.test("unsupported"));
    }
}
