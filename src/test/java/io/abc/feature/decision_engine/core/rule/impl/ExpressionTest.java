package io.abc.feature.decision_engine.core.rule.impl;

import io.abc.feature.decision_engine.core.rule.api.Expression;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class ExpressionTest {

  @Test
  void givenValidExpression_whenCheck_thenReturnTrue() {

    // given
    final String expression = "${key1.key2}";

    // then
    Assertions.assertTrue(Expression.isExpression(expression));
  }

  @Test
  void givenValidExpression_whenGetExpressionBody_thenReturn() {

    // given
    final Map<String, String> expressions = Map.of(
        "${key1.key2}", "key1.key2",
        "${key1}", "key1",
        "${ key1 }", "key1"
    );

    // then
    for (Map.Entry<String, String> entry : expressions.entrySet()) {
      Assertions.assertEquals(entry.getValue(), Expression.getExpression(entry.getKey()));
    }
  }

  @Test
  void givenInValidExpressions_whenCheck_thenReturnFalse() {
    // given
    final List<Object> invalidExpressions = List.of(
        "${}",
        "foo",
        "${foo",
        "foo}",
        "$foo",
        1
    );

    // then
    for (Object expression : invalidExpressions) {
      Assertions.assertFalse(Expression.isExpression(expression));
    }
  }
}
