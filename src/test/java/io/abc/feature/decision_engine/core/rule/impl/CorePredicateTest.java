package io.abc.feature.decision_engine.core.rule.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

class CorePredicateTest {

    @Test
    void givenEqAndNePredicate_whenTest_thenReturnExpectedResult() {
        final EqPredicate eqPredicate = new EqPredicate();
        final NePredicate nePredicate = new NePredicate();

        Assertions.assertTrue(eqPredicate.test("running", "running"));
        Assertions.assertFalse(eqPredicate.test("running", "stopped"));

        Assertions.assertTrue(nePredicate.test("running", "stopped"));
        Assertions.assertFalse(nePredicate.test("running", "running"));
    }

    @Test
    void givenNumericPredicates_whenTest_thenCoverComparisonBranches() {
        final BigDecimal three = BigDecimal.valueOf(3);
        final BigDecimal four = BigDecimal.valueOf(4);

        Assertions.assertTrue(new GtPredicate().test(four, three));
        Assertions.assertFalse(new GtPredicate().test(three, three));

        Assertions.assertTrue(new GePredicate().test(three, three));
        Assertions.assertFalse(new GePredicate().test(three, four));

        Assertions.assertTrue(new LtPredicate().test(three, four));
        Assertions.assertFalse(new LtPredicate().test(four, three));

        Assertions.assertTrue(new LePredicate().test(three, three));
        Assertions.assertFalse(new LePredicate().test(four, three));
    }

    @Test
    void givenNumericPredicateWithInvalidInputType_whenTest_thenReturnFalse() {
        Assertions.assertFalse(new GtPredicate().test("4", BigDecimal.valueOf(3)));
        Assertions.assertFalse(new GePredicate().test(BigDecimal.valueOf(4), "3"));
        Assertions.assertFalse(new LtPredicate().test(null, BigDecimal.valueOf(3)));
        Assertions.assertFalse(new LePredicate().test(BigDecimal.valueOf(4), null));
    }

    @Test
    void givenInAndNotInPredicate_whenTest_thenReturnExpectedResult() {
        final InPredicate inPredicate = new InPredicate();
        final NotInPredicate notInPredicate = new NotInPredicate();

        Assertions.assertTrue(inPredicate.test("running", List.of("running", "stopped")));
        Assertions.assertFalse(inPredicate.test("pending", List.of("running", "stopped")));
        Assertions.assertFalse(inPredicate.test("running", "not-a-list"));

        Assertions.assertTrue(notInPredicate.test("pending", List.of("running", "stopped")));
        Assertions.assertFalse(notInPredicate.test("running", List.of("running", "stopped")));
        Assertions.assertFalse(notInPredicate.test("pending", "not-a-list"));
    }

    @Test
    void givenContainsAndNotContainsPredicate_whenTest_thenReturnExpectedResult() {
        final ContainsPredicate containsPredicate = new ContainsPredicate();
        final NotContainsPredicate notContainsPredicate = new NotContainsPredicate();

        Assertions.assertTrue(containsPredicate.test(List.of("a", "b"), "a"));
        Assertions.assertFalse(containsPredicate.test(List.of("a", "b"), "c"));

        Assertions.assertTrue(containsPredicate.test("running", "run"));
        Assertions.assertFalse(containsPredicate.test("running", "stop"));

        Assertions.assertTrue(notContainsPredicate.test(List.of("a", "b"), "c"));
        Assertions.assertFalse(notContainsPredicate.test(List.of("a", "b"), "a"));

        Assertions.assertTrue(notContainsPredicate.test("running", "stop"));
        Assertions.assertFalse(notContainsPredicate.test("running", "run"));

        Assertions.assertFalse(containsPredicate.test(5, 1));
        Assertions.assertFalse(notContainsPredicate.test(5, 1));
    }
}
