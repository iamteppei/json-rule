# json-rule

[![CI/CD Pipeline](https://github.com/iamteppei/json-rule/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/iamteppei/json-rule/actions/workflows/ci.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.iamteppei/json-rule)](https://search.maven.org/artifact/io.github.iamteppei/json-rule)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Java](https://img.shields.io/badge/java-21-blue.svg)](https://openjdk.org/projects/jdk/21/)
[![Coverage Gate](https://img.shields.io/badge/coverage%20gate-line%2060%25%20%7C%20branch%2040%25-brightgreen.svg)](#development)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=iamteppei_json-rule&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=iamteppei_json-rule)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=iamteppei_json-rule&metric=coverage)](https://sonarcloud.io/summary/new_code?id=iamteppei_json-rule)

JSON-based rule engine for decision processing.

This library lets you define rules in JSON and evaluate them against JSON-like input maps. It is designed for policy checks, event filtering, and lightweight decision flows where rules should be data, not code.

## Features

- JSON rule parser with nested object support
- Logical composition with $all and $any
- Rich predicate set for numbers, strings, lists, existence, null checks, and regex
- Expression helper support for ${path.to.value}-style extraction
- JUnit 5 test suite with JaCoCo coverage checks
- GitHub Actions pipeline with build, test, Sonar scan, and Maven Central publish job

## Requirements

- Java 21
- Maven 3.9+

## Installation

If you consume from Maven Central, add this dependency:

```xml
<dependency>
	<groupId>io.github.iamteppei</groupId>
	<artifactId>json-rule</artifactId>
	<version>LATEST_RELEASE</version>
</dependency>
```

For local development:

```bash
mvn -B -ntp clean install
```

## Quick Start

```java
import io.abc.feature.decision_engine.core.rule.api.Rule;
import io.abc.feature.decision_engine.core.rule.api.RuleParser;
import io.abc.feature.decision_engine.core.rule.impl.MapParser;

import java.util.Map;

public class QuickStart {
		public static void main(String[] args) {
				String ruleJson = """
						{
							"detail": {
								"state": ["in", ["running", "initializing"]]
							},
							"source": ["eq_ignore_case", "aws.ec2"]
						}
						""";

				String inputJson = """
						{
							"source": "AWS.EC2",
							"detail": {
								"state": "running"
							}
						}
						""";

				RuleParser ruleParser = new RuleParser();
				MapParser mapParser = new MapParser();

				Rule rule = ruleParser.parse(ruleJson);
				Map<String, Object> input = mapParser.parse(inputJson);

				boolean matched = rule.test(input);
				System.out.println("Matched: " + matched);
		}
}
```

## Rule Syntax

### Simple condition

```json
{
  "detail": {
    "state": ["eq", "running"]
  }
}
```

### Any/All composition

```json
{
  "$all": [
    {
      "source": ["eq", "aws.ec2"]
    },
    {
      "$any": [
        { "detail": { "state": ["eq", "running"] } },
        { "detail": { "state": ["eq", "initializing"] } }
      ]
    }
  ]
}
```

### Supported operators

| Operator            | Meaning                                   |
| ------------------- | ----------------------------------------- |
| eq                  | Equals                                    |
| ne                  | Not equals                                |
| gt                  | Greater than (BigDecimal)                 |
| ge                  | Greater than or equal (BigDecimal)        |
| lt                  | Less than (BigDecimal)                    |
| le                  | Less than or equal (BigDecimal)           |
| in                  | Source is in expected list                |
| not_in              | Source is not in expected list            |
| contains            | Collection/String contains expected value |
| not_contains        | Negated contains                          |
| start_with          | String starts with expected prefix        |
| end_with            | String ends with expected suffix          |
| anything_start_with | String starts with any expected prefix    |
| anything_end_with   | String ends with any expected suffix      |
| eq_ignore_case      | Case-insensitive equals                   |
| matches             | Regex full match                          |
| email               | Valid email and expected boolean true     |
| exists              | Property exists and expected boolean true |
| non_null            | Source is not null and not unknown        |
| is_null             | Source is null                            |

## Development

Run compile:

```bash
mvn -B -ntp clean compile
```

Run tests:

```bash
mvn -B -ntp test
```

Run tests with coverage gate:

```bash
mvn -B -ntp verify
```

JaCoCo rules currently enforce:

- Line coverage >= 60%
- Branch coverage >= 40%

## Community and Governance

- Code of Conduct: see [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md)
- Security Policy: see [SECURITY.md](SECURITY.md)
- License: see [LICENSE](LICENSE)

## Contributing

Contributions are welcome.

1. Fork the repository.
2. Create a feature branch.
3. Add or update tests for your change.
4. Run verify locally with Java 21.
5. Open a pull request with a clear description.

## License

This project is licensed under Apache License 2.0. See [LICENSE](LICENSE) for details.
