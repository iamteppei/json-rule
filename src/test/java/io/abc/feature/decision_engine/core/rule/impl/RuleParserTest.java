package io.abc.feature.decision_engine.core.rule.impl;

import io.abc.feature.decision_engine.core.rule.api.ParserException;
import io.abc.feature.decision_engine.core.rule.api.Rule;
import io.abc.feature.decision_engine.core.rule.api.RuleParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RuleParserTest {

    private final RuleParser parser = new RuleParser();

    @Test
    void givenValidJsonRule_whenParse_thenReturnNoError() {
        // given
        final String jsonRule = """
                {
                  "$all": [
                    {
                      "resources": {
                        "id": [
                          "non_null"
                        ],
                        "properties": {
                          "created_by": [
                            "eq",
                            "foo"
                          ]
                        }
                      }
                    },
                    {
                      "principal": {
                        "id": [
                          "non_null"
                        ]
                      }
                    }
                  ]
                }
                """;

        // when
        final Rule rule = parser.parse(jsonRule);

        // then
        Assertions.assertNotNull(rule);
    }

    @Test
    void givenNullJsonRule_whenParse_thenThrowParserException() {
        final ParserException exception = Assertions.assertThrows(ParserException.class, () -> parser.parse(null));
        Assertions.assertEquals("Failed to parse rule json", exception.getMessage());
    }

    @Test
    void givenInvalidTopLevelJson_whenParse_thenThrowParserException() {
        // given
        final String jsonRule = "[]";

        // when
        final ParserException exception = Assertions.assertThrows(ParserException.class, () -> parser.parse(jsonRule));

        // then
        Assertions.assertEquals("Failed to parse rule json", exception.getMessage());
    }

    @Test
    void givenUnknownConditionOperator_whenParse_thenThrowParserException() {
        // given
        final String jsonRule = """
                {
                  "email": [
                    "unknown",
                    true
                  ]
                }
                """;

        // when
        final ParserException exception = Assertions.assertThrows(ParserException.class, () -> parser.parse(jsonRule));

        // then
        Assertions.assertEquals("Failed to add rule [unknown] for property [email]", exception.getMessage());
    }

    @Test
    void givenAllRuleWithInvalidElement_whenParse_thenThrowParserException() {
        // given
        final String jsonRule = """
                {
                  "$all": [
                    1
                  ]
                }
                """;

        // when
        final ParserException exception = Assertions.assertThrows(ParserException.class, () -> parser.parse(jsonRule));

        // then
        Assertions.assertEquals("invalid $all or $any json rule", exception.getMessage());
    }

    @Test
    void givenMixedTypeSimpleArray_whenParse_thenThrowParserException() {
        // given
        final String jsonRule = """
                {
                  "detail": {
                    "state": [
                      "in",
                      [
                        "running",
                        1
                      ]
                    ]
                  }
                }
                """;

        // when
        final ParserException exception = Assertions.assertThrows(ParserException.class, () -> parser.parse(jsonRule));

        // then
        Assertions.assertEquals("invalid array element value type", exception.getMessage());
    }

    @Test
    void givenInvalidConditionToken_whenParse_thenThrowParserException() {
        // given
        final String jsonRule = """
                {
                  "detail": {
                    "state": [
                      {
                        "op": "eq"
                      }
                    ]
                  }
                }
                """;

        // when
        final ParserException exception = Assertions.assertThrows(ParserException.class, () -> parser.parse(jsonRule));

        // then
        Assertions.assertEquals("invalid json condition", exception.getMessage());
    }
}
