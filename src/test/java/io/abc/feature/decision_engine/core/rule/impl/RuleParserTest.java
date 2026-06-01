package io.abc.feature.decision_engine.core.rule.impl;

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
}
