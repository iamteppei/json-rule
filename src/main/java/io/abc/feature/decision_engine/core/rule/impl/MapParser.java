package io.abc.feature.decision_engine.core.rule.impl;

import com.squareup.moshi.JsonReader;
import io.abc.feature.decision_engine.core.rule.api.ParserException;
import okio.Okio;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Parses JSON content into nested Java map and list structures.
 */
public class MapParser {

    /**
     * Parses JSON text to a map.
     *
     * @param jsonContent input JSON text
     * @return parsed map representation.
     * @throws ParserException when parsing fails
     */
    public Map<String, Object> parse(String jsonContent) {
        try (final com.squareup.moshi.JsonReader reader = com.squareup.moshi.JsonReader.of(Okio.buffer(Okio.source(new ByteArrayInputStream(jsonContent.getBytes(StandardCharsets.UTF_8)))))) {
            return parseMap(reader);
        } catch (Exception exp) {
            throw new ParserException("parse map error", exp);
        }
    }

    private Map<String, Object> parseMap(JsonReader reader) throws IOException {
        reader.beginObject();
        final Map<String, Object> values = new LinkedHashMap<>();
        while (reader.hasNext()) {
            final String name = reader.nextName();
            switch (reader.peek()) {
                case STRING -> values.put(name, reader.nextString());
                case BOOLEAN -> values.put(name, reader.nextBoolean());
                case NUMBER -> values.put(name, BigDecimal.valueOf(reader.nextDouble()));
                case BEGIN_OBJECT -> {
                    values.put(name, parseMap(reader.peekJson()));
                    reader.skipValue();
                }
                case BEGIN_ARRAY -> {
                    values.put(name, parseListMap(reader.peekJson()));
                    reader.skipValue();
                }
                default -> {
                }
            }
        }
        reader.endObject();
        return values;
    }

    private List<Object> parseListMap(JsonReader reader) throws IOException {
        reader.beginArray();
        final List<Object> valueList = new ArrayList<>();
        while (reader.hasNext()) {
            switch (reader.peek()) {
                case STRING -> valueList.add(reader.nextString());
                case BOOLEAN -> valueList.add(reader.nextBoolean());
                case NUMBER -> valueList.add(BigDecimal.valueOf(reader.nextDouble()));
                case NULL -> valueList.add(null);
                case BEGIN_OBJECT -> {
                    valueList.add(parseMap(reader.peekJson()));
                    reader.skipValue();
                }
                case BEGIN_ARRAY -> {
                    valueList.add(parseListMap(reader.peekJson()));
                    reader.skipValue();
                }
                default -> {
                }
            }
        }
        reader.endArray();
        return valueList;
    }
}
