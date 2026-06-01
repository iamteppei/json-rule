package io.abc.feature.decision_engine.core.rule.api;

import com.squareup.moshi.JsonReader;
import okio.Okio;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class RuleParser {

    private static final String ROOT = "$";

    public Rule parse(String jsonRule) throws ParserException {
        try (final JsonReader reader = JsonReader.of(Okio.buffer(Okio.source(new ByteArrayInputStream(jsonRule.getBytes(StandardCharsets.UTF_8)))))) {
            return parseRule(ROOT, reader);
        } catch (ParserException exp) {
            throw exp;
        } catch (Exception exp) {
            throw new ParserException("Failed to parse rule json", exp);
        }
    }

    private Rule parseRule(String root, JsonReader reader) throws IOException {
        reader.beginObject();
        final Rule rule = new Rule(root);
        while (reader.hasNext()) {
            final String name = reader.nextName();
            final String currentRoot = String.join(".", root, name);
            switch (reader.peek()) {
                case BEGIN_OBJECT -> {
                    rule.withRule(name, parseRule(currentRoot, reader.peekJson()));
                    reader.skipValue();
                }
                case BEGIN_ARRAY -> {
                    final JsonReader arrayReader = reader.peekJson();
                    if (name.equalsIgnoreCase(Rule.ALL_KEY)) {
                        rule.withAllRules(parseConditions(currentRoot, arrayReader));
                    } else if (name.equalsIgnoreCase(Rule.ANY_KEY)) {
                        rule.withAnyRules(parseConditions(currentRoot, arrayReader));
                    } else {
                        rule.withCondition(name, parseCondition(name, arrayReader));
                    }
                    reader.skipValue();
                }
                default -> throw new ParserException("invalid json rule");
            }
        }
        reader.endObject();
        return rule;
    }

    private List<Rule> parseConditions(String root, JsonReader reader) throws IOException {
        reader.beginArray();
        final List<Rule> rules = new ArrayList<>();
        while (reader.hasNext()) {
            if (reader.peek() == JsonReader.Token.BEGIN_OBJECT) {
                rules.add(parseRule(root, reader.peekJson()));
                reader.skipValue();
            } else {
                throw new ParserException("invalid $all or $any json rule");
            }
        }
        reader.endArray();
        return rules;
    }

    private Condition parseCondition(String name, JsonReader reader) throws IOException {
        reader.beginArray();
        final Condition condition = new Condition();
        while (reader.hasNext()) {
            switch (reader.peek()) {
                case STRING -> throwIfFailedToAdd(name, condition, reader.nextString());
                case NUMBER -> throwIfFailedToAdd(name, condition, BigDecimal.valueOf(reader.nextDouble()));
                case BOOLEAN -> throwIfFailedToAdd(name, condition, reader.nextBoolean());
                case NULL -> throwIfFailedToAdd(name, condition, reader.nextNull());
                case BEGIN_ARRAY -> {
                    throwIfFailedToAdd(name, condition, readSimpleValueArray(reader.peekJson()));
                    reader.skipValue();
                }
                default -> throw new ParserException("invalid json condition");
            }
        }
        reader.endArray();
        return condition;
    }

    private void throwIfFailedToAdd(String name, Condition condition, Object value) {
        if (!condition.add(value)) {
            throw new ParserException("Failed to add rule [" + value + "] for property [" + name + "]");
        }
    }

    private Collection<?> readSimpleValueArray(JsonReader reader) throws IOException {
        reader.beginArray();
        final List<Object> values = new ArrayList<>();
        final Map<String, JsonReader.Token> firstTokenHolder = new HashMap<>(1);
        while (reader.hasNext()) {
            final JsonReader.Token token = reader.peek();
            switch (token) {
                case STRING -> {
                    throwIfInvalidValueType(firstTokenHolder, token);
                    values.add(reader.nextString());
                }
                case NUMBER -> {
                    throwIfInvalidValueType(firstTokenHolder, token);
                    values.add(BigDecimal.valueOf(reader.nextDouble()));
                }
                case BOOLEAN -> {
                    throwIfInvalidValueType(firstTokenHolder, token);
                    values.add(reader.nextBoolean());
                }
                default -> throw new ParserException("invalid simple value array");
            }
        }
        reader.endArray();
        return values;
    }

    private void throwIfInvalidValueType(Map<String, JsonReader.Token> firstTokenHolder, JsonReader.Token token) {
        if (!Objects.equals(token, firstTokenHolder.computeIfAbsent("first", it -> token))) {
            throw new ParserException("invalid array element value type");
        }
    }
}
