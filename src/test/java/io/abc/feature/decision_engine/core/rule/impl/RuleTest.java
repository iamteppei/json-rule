package io.abc.feature.decision_engine.core.rule.impl;

import io.abc.feature.decision_engine.core.rule.api.Rule;
import io.abc.feature.decision_engine.core.rule.api.RuleParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

class RuleTest {

    private static final MapParser mapParser = new MapParser();
    private static final String event = """
            {
              "email": "foo@gmail.com",
              "version": "0",
              "id": "ddddd4-aaaa-7777-4444-345dd43cc333",
              "detail-type": "EC2 Instance State-change Notification",
              "source": "aws.ec2",
              "account": "012345679012",
              "time": "2017-10-02T16:24:49Z",
              "region": "us-east-1",
              "resources": [
                "arn:aws:ec2:us-east-1:123456789012:instance/i-000000aaaaaa00000"
              ],
              "detail": {
                "c-count": 5,
                "d-count": 3,
                "x-limit": 301.8,
                "source-ip": "10.0.0.33",
                "instance-id": "i-000000aaaaaa00000",
                "state": "running"
              }
            }""";
    private static Map<String, Object> eventMap;
    private final RuleParser ruleParser = new RuleParser();

    @BeforeAll
    static void beforeAll() {
        eventMap = mapParser.parse(event);
    }

    @Test
    void givenJsonContent_whenMatchRule_ExpectReturnTrue() {

        // given
        final String jsonRule = """
                {
                    "total_price": [
                      "gt",
                      500
                    ],
                    "items": {
                      "$any": [
                        {
                          "$all": [
                            {
                              "name": [
                                "eq",
                                "iphone"
                              ]
                            },
                            {
                              "quantity": [
                                "gt",
                                2
                              ]
                            }
                          ]
                        },
                        {
                          "$all": [
                            {
                              "name": [
                                "eq",
                                "samsung"
                              ]
                            },
                            {
                              "quantity": [
                                "gt",
                                3
                              ]
                            }
                          ]
                        }
                      ]
                    }
                  }""";

        final String contentString = """
                {
                    "total_price": 600,
                    "items": [
                      {
                        "name": "iphone",
                        "quantity": 3
                      },
                      {
                        "name": "samsung",
                        "quantity": 1
                      }
                    ]
                  }""";

        // when
        final Rule rule = ruleParser.parse(jsonRule);

        final Map<String, Object> contentOutput = mapParser.parse(contentString);

        // then

        Assertions.assertNotNull(rule);

        Assertions.assertTrue(rule.test(contentOutput));
    }

    @Test
    void givenSimpleRule_whenMatchRule_thenReturnTrue() {
        // given
        final String jsonRule = """
                {
                  "detail-type": [
                    "eq",
                    "EC2 Instance State-change Notification"
                  ],
                  "resources": [
                    "contains",
                    "arn:aws:ec2:us-east-1:123456789012:instance/i-000000aaaaaa00000"
                  ],
                  "detail": {
                    "state": [
                      "in",
                      [
                        "initializing",
                        "running"
                      ]
                    ]
                  }
                }""";

        // when
        final Rule rule = ruleParser.parse(jsonRule);

        // then
        Assertions.assertNotNull(rule);

        // and
        Assertions.assertTrue(rule.test(eventMap));
    }

    @Test
    void givenStartWithRule_whenMatchRule_thenReturnTrue() {
        // given
        final String jsonRule = """
                {
                  "time": [
                    "start_with",
                    "2017-10-02"
                  ]
                }""";

        // when
        final Rule rule = ruleParser.parse(jsonRule);

        // then
        Assertions.assertNotNull(rule);

        // and
        Assertions.assertTrue(rule.test(eventMap));
    }

    @Test
    void givenEndWithRule_whenMatchRule_thenReturnTrue() {
        // given
        final String jsonRule = """
                {
                  "source": [
                    "end_with",
                    "ec2"
                  ]
                }""";

        // when
        final Rule rule = ruleParser.parse(jsonRule);

        // then
        Assertions.assertNotNull(rule);

        // and
        Assertions.assertTrue(rule.test(eventMap));
    }

    @Test
    void givenEqualsIgnoreCaseRule_whenMatchRule_thenReturnTrue() {
        // given
        final String jsonRule = """
                {
                  "source": [
                    "eq_ignore_case",
                    "AWS.ec2"
                  ]
                }""";

        // when
        final Rule rule = ruleParser.parse(jsonRule);

        // then
        Assertions.assertNotNull(rule);

        // and
        Assertions.assertTrue(rule.test(eventMap));
    }

    @Test
    void givenNotContainsRule_whenMatchRule_thenReturnTrue() {
        // given
        final String jsonRule = """
                {
                  "detail": {
                    "state": [
                      "not_contains",
                      "initializing"
                    ]
                  }
                }""";

        // when
        final Rule rule = ruleParser.parse(jsonRule);

        // then
        Assertions.assertNotNull(rule);

        // and
        Assertions.assertTrue(rule.test(eventMap));
    }

    @Test
    void givenNotInRule_whenMatchRule_thenReturnTrue() {
        // given
        final String jsonRule = """
                {
                  "detail": {
                    "state": [
                      "not_in",
                      [
                        "stopped",
                        "overloaded"
                      ]
                    ]
                  }
                }""";

        // when
        final Rule rule = ruleParser.parse(jsonRule);

        // then
        Assertions.assertNotNull(rule);

        // and
        Assertions.assertTrue(rule.test(eventMap));
    }

    @Test
    void givenAnythingStartWithListOfStringRule_whenMatchRule_thenReturnTrue() {
        // given
        final String jsonRule = """
                {
                  "detail": {
                    "state": [
                      "anything_start_with",
                      [
                        "stopped",
                        "running"
                      ]
                    ]
                  }
                }""";

        // when
        final Rule rule = ruleParser.parse(jsonRule);

        // then
        Assertions.assertNotNull(rule);

        // and
        Assertions.assertTrue(rule.test(eventMap));
    }

    @Test
    void givenAnythingStartWithListOfStringRule_whenUnMatchRule_thenReturnFalse() {
        // given
        final String jsonRule = """
                {
                  "detail": {
                    "state": [
                      "anything_start_with",
                      [
                        "stopped"
                      ]
                    ]
                  }
                }""";

        // when
        final Rule rule = ruleParser.parse(jsonRule);

        // then
        Assertions.assertNotNull(rule);

        // and
        Assertions.assertFalse(rule.test(eventMap));
    }

    @Test
    void givenAnythingStartWithStringRule_whenMatchRule_thenReturnTrue() {
        // given
        final String jsonRule = """
                {
                  "detail": {
                    "state": [
                      "anything_start_with",
                      "running"
                    ]
                  }
                }""";

        // when
        final Rule rule = ruleParser.parse(jsonRule);

        // then
        Assertions.assertNotNull(rule);

        // and
        Assertions.assertTrue(rule.test(eventMap));
    }

    @Test
    void givenAnythingStartWithStringRule_whenUnMatchRule_thenReturnFalse() {
        // given
        final String jsonRule = """
                {
                  "detail": {
                    "state": [
                      "anything_start_with",
                      "stop"
                    ]
                  }
                }""";

        // when
        final Rule rule = ruleParser.parse(jsonRule);

        // then
        Assertions.assertNotNull(rule);

        // and
        Assertions.assertFalse(rule.test(eventMap));
    }

    //

    @Test
    void givenAnythingEndWithListOfStringRule_whenMatchRule_thenReturnTrue() {
        // given
        final String jsonRule = """
                {
                  "detail": {
                    "state": [
                      "anything_end_with",
                      [
                        "stopped",
                        "running"
                      ]
                    ]
                  }
                }""";

        // when
        final Rule rule = ruleParser.parse(jsonRule);

        // then
        Assertions.assertNotNull(rule);

        // and
        Assertions.assertTrue(rule.test(eventMap));
    }

    @Test
    void givenAnythingEndWithListOfStringRule_whenUnMatchRule_thenReturnFalse() {
        // given
        final String jsonRule = """
                {
                  "detail": {
                    "state": [
                      "anything_end_with",
                      [
                        "stopped"
                      ]
                    ]
                  }
                }""";

        // when
        final Rule rule = ruleParser.parse(jsonRule);

        // then
        Assertions.assertNotNull(rule);

        // and
        Assertions.assertFalse(rule.test(eventMap));
    }

    @Test
    void givenAnythingEndWithStringRule_whenMatchRule_thenReturnTrue() {
        // given
        final String jsonRule = """
                {
                  "detail": {
                    "state": [
                      "anything_end_with",
                      "running"
                    ]
                  }
                }""";

        // when
        final Rule rule = ruleParser.parse(jsonRule);

        // then
        Assertions.assertNotNull(rule);

        // and
        Assertions.assertTrue(rule.test(eventMap));
    }

    @Test
    void givenAnythingEndWithStringRule_whenUnMatchRule_thenReturnFalse() {
        // given
        final String jsonRule = """
                {
                  "detail": {
                    "state": [
                      "anything_end_with",
                      "stop"
                    ]
                  }
                }""";

        // when
        final Rule rule = ruleParser.parse(jsonRule);

        // then
        Assertions.assertNotNull(rule);

        // and
        Assertions.assertFalse(rule.test(eventMap));
    }

    @Test
    void givenExistsRule_whenMatchRule_thenReturnTrue() {
        // given
        final String jsonRule = """
                {
                  "detail": {
                    "c-count": [
                      "exists",
                      true
                    ]
                  }
                }""";

        // when
        final Rule rule = ruleParser.parse(jsonRule);

        // then
        Assertions.assertNotNull(rule);

        // and
        Assertions.assertTrue(rule.test(eventMap));
    }

    @Test
    void givenExistsRule2_whenUnMatchRule_thenReturnFalse() {
        // given - does not exist c-count field
        final String jsonRule = """
                {
                  "detail": {
                    "c-count": [
                      "exists",
                      false
                    ]
                  }
                }""";

        // when
        final Rule rule = ruleParser.parse(jsonRule);

        // then
        Assertions.assertNotNull(rule);

        // and
        Assertions.assertFalse(rule.test(eventMap));
    }

    @Test
    void givenExistsRule_whenUnMatchRule_thenReturnFalse() {
        // given - expect field exists, but it does not
        final String jsonRule = """
                {
                  "detail": {
                    "not_exists_field": [
                      "exists",
                      true
                    ]
                  }
                }""";

        // when
        final Rule rule = ruleParser.parse(jsonRule);

        // then
        Assertions.assertNotNull(rule);

        // and
        Assertions.assertFalse(rule.test(eventMap));
    }

    @Test
    void givenEmailRule_whenMatchRule_thenReturnTrue() {
        // given
        final String jsonRule = """
                {
                  "email": [
                    "email",
                    true
                  ]
                }""";

        // when
        final Rule rule = ruleParser.parse(jsonRule);

        // then
        Assertions.assertNotNull(rule);

        // and
        Assertions.assertTrue(rule.test(eventMap));
    }

    @Test
    void givenEmailRule_whenUnMatchRule_thenReturnFalse() {
        // given
        final String jsonRule = """
                {
                  "email": [
                    "email",
                    false
                  ]
                }""";

        // when
        final Rule rule = ruleParser.parse(jsonRule);

        // then
        Assertions.assertNotNull(rule);

        // and
        Assertions.assertFalse(rule.test(eventMap));
    }

    @Test
    void givenEmailRule2_whenUnMatchRule_thenReturnFalse() {
        // given
        final String jsonRule = """
                {
                  "email": [
                    "email",
                    0
                  ]
                }""";

        // when
        final Rule rule = ruleParser.parse(jsonRule);

        // then
        Assertions.assertNotNull(rule);

        // and
        Assertions.assertFalse(rule.test(eventMap));
    }

    @Test
    void givenEmailRule3_whenUnMatchRule_thenReturnFalse() {
        // given
        final String jsonRule = """
                {
                  "email": [
                    "email",
                    null
                  ]
                }""";

        // when
        final Rule rule = ruleParser.parse(jsonRule);

        // then
        Assertions.assertNotNull(rule);

        // and
        Assertions.assertFalse(rule.test(eventMap));
    }

    @Test
    void testNonNullRule_withSingleCondition_thenReturnTrue() {
        // given
        final String jsonRule = """
                {
                  "email": [
                    "non_null"
                  ]
                }""";

        // when
        final Rule rule = ruleParser.parse(jsonRule);

        // then
        Assertions.assertNotNull(rule);

        // and
        Assertions.assertTrue(rule.test(eventMap));
    }

    @Test
    void testNonNullRule_withMultipleConditions_thenReturnTrue() {
        // given
        final String jsonRule = """
                {
                  "email": [
                    "non_null"
                  ],
                  "version": [
                    "non_null"
                  ]
                }""";

        // when
        final Rule rule = ruleParser.parse(jsonRule);

        // then
        Assertions.assertNotNull(rule);

        // and
        Assertions.assertTrue(rule.test(eventMap));
    }

    @Test
    void testNonNullRule_withSingleNotExistProperty_thenReturnFalse() {
        // given
        final String jsonRule = """
                {
                  "email": [
                    "non_null"
                  ]
                }""";

        // when
        final Rule rule = ruleParser.parse(jsonRule);

        // then
        Assertions.assertNotNull(rule);

        // and
        Assertions.assertFalse(rule.test(Collections.emptyMap()));
    }

    @Test
    void testNonNullRule_withMultipleNotExistProperty_thenReturnFalse() {
        // given
        final String jsonRule = """
                {
                  "email": [
                    "non_null"
                  ],
                  "version": [
                    "non_null"
                  ]
                }""";

        // when
        final Rule rule = ruleParser.parse(jsonRule);

        // then
        Assertions.assertNotNull(rule);

        // and
        Assertions.assertFalse(rule.test(Collections.emptyMap()));
    }

    @Test
    void testNonNullRule_withNestedAndExistedProperty_thenReturnTrue() {
        // given
        final String jsonRule = """
                {
                  "detail": {
                    "state": [
                      "non_null"
                    ]
                  }
                }""";

        // when
        final Rule rule = ruleParser.parse(jsonRule);

        // then
        Assertions.assertNotNull(rule);

        // and
        Assertions.assertTrue(rule.test(Collections.unmodifiableMap(eventMap)));
    }

    @Test
    void testNonNullRule_withNestedAndNotExistedProperty_thenReturnFalse() {
        // given
        final String jsonRule = """
                {
                  "detail": {
                    "state": [
                      "non_null"
                    ],
                    "time": [
                      "non_null"
                    ]
                  }
                }""";

        // when
        final Rule rule = ruleParser.parse(jsonRule);

        // then
        Assertions.assertNotNull(rule);

        // and
        Assertions.assertFalse(rule.test(Map.of("detail", Map.of("state", "running"))));
    }
}
