package io.abc.feature.decision_engine.core.rule.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class StringAndSpecialPredicateTest {

    @Test
    void givenStartAndEndWithPredicate_whenTest_thenReturnExpectedResult() {
        final StartWithPredicate startWithPredicate = new StartWithPredicate();
        final EndWithPredicate endWithPredicate = new EndWithPredicate();

        Assertions.assertTrue(startWithPredicate.test("running", "run"));
        Assertions.assertFalse(startWithPredicate.test("running", "stop"));
        Assertions.assertFalse(startWithPredicate.test(1, "run"));

        Assertions.assertTrue(endWithPredicate.test("running", "ing"));
        Assertions.assertFalse(endWithPredicate.test("running", "run"));
        Assertions.assertFalse(endWithPredicate.test("running", 1));
    }

    @Test
    void givenAnythingStartWithPredicate_whenTestCollectionAndString_thenReturnExpectedResult() {
        final AnythingStartWithPredicate predicate = new AnythingStartWithPredicate();

        Assertions.assertTrue(predicate.test("running", List.of("stop", "run")));
        Assertions.assertFalse(predicate.test("running", List.of("stop", "wait")));

        Assertions.assertTrue(predicate.test("running", "run"));
        Assertions.assertFalse(predicate.test("running", "stop"));

        Assertions.assertFalse(predicate.test(1, List.of("run")));
    }

    @Test
    void givenAnythingEndWithPredicate_whenTestCollectionAndString_thenReturnExpectedResult() {
        final AnythingEndWithPredicate predicate = new AnythingEndWithPredicate();

        Assertions.assertTrue(predicate.test("running", List.of("stop", "ing")));
        Assertions.assertFalse(predicate.test("running", List.of("stop", "wait")));

        Assertions.assertTrue(predicate.test("running", "ing"));
        Assertions.assertFalse(predicate.test("running", "run"));

        Assertions.assertFalse(predicate.test(1, List.of("ing")));
    }

    @Test
    void givenEqIgnoreCasePredicate_whenTest_thenReturnExpectedResult() {
        final EqIgnoreCasePredicate predicate = new EqIgnoreCasePredicate();

        Assertions.assertTrue(predicate.test("AWS.ec2", "aws.EC2"));
        Assertions.assertTrue(predicate.test("123", 123));
        Assertions.assertFalse(predicate.test("running", null));
        Assertions.assertFalse(predicate.test(123, "123"));
    }

    @Test
    void givenMatchPredicate_whenTest_thenReturnExpectedResult() {
        final MatchPredicate predicate = new MatchPredicate();

        Assertions.assertTrue(predicate.test("abc123", "[a-z]+\\d+"));
        Assertions.assertFalse(predicate.test("abc", "[0-9]+"));
        Assertions.assertFalse(predicate.test("abc123", 123));
    }

    @Test
    void givenMatchPredicateWithInvalidRegex_whenTest_thenThrowException() {
        final MatchPredicate predicate = new MatchPredicate();

        Assertions.assertThrows(RuntimeException.class, () -> predicate.test("abc", "("));
    }

    @Test
    void givenEmailPredicate_whenTest_thenReturnExpectedResult() {
        final EmailPredicate predicate = new EmailPredicate();

        Assertions.assertTrue(predicate.test("foo@gmail.com", true));
        Assertions.assertFalse(predicate.test("not-an-email", true));
        Assertions.assertFalse(predicate.test("foo@gmail.com", false));
        Assertions.assertFalse(predicate.test("foo@gmail.com", "true"));
    }

    @Test
    void givenExistPredicate_whenTest_thenRespectCurrentSemantics() {
        final ExistPredicate predicate = new ExistPredicate();

        Assertions.assertTrue(predicate.test("value", true));
        Assertions.assertFalse(predicate.test(Unknown.INSTANCE, true));
        Assertions.assertFalse(predicate.test("value", false));
        Assertions.assertFalse(predicate.test("value", "true"));
    }

    @Test
    void givenIsNullPredicate_whenTest_thenReturnExpectedResult() {
        final IsNullPredicate predicate = new IsNullPredicate();

        Assertions.assertTrue(predicate.test(null, "ignored"));
        Assertions.assertFalse(predicate.test("value", null));
    }

    @Test
    void givenUnknownHelper_whenCheck_thenReturnExpectedValue() {
        Assertions.assertFalse(Unknown.isNotUnknown(Unknown.INSTANCE));
        Assertions.assertTrue(Unknown.isNotUnknown("value"));
        Assertions.assertTrue(Unknown.isNotUnknown(null));
    }
}
