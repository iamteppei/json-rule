package io.abc.feature.decision_engine.core.rule.api;

import io.abc.feature.decision_engine.core.rule.impl.Unknown;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * A parsed rule node that can contain nested property predicates and logical groups.
 */
public final class Rule implements Predicate {

    public static final String ANY_KEY = "$any";
    public static final String ALL_KEY = "$all";

    private static final Logger logger = LoggerFactory.getLogger(Rule.class);
    private final Map<String, Predicate> predicateMap = new HashMap<>();
    private final String root;

    /**
     * Creates a rule node with a logical root path used for diagnostics.
     *
     * @param root logical path for this rule node
     */
    public Rule(String root) {
        this.root = root;
    }

    private void checkingFailedReason(String key, String reason) {
        logger.debug("<<< [{}] checking FAILED. Reason: {}", String.join(".", root, key), reason);
    }

    private void checkingFailed(String key) {
        logger.debug("<<< [{}] checking FAILED", String.join(".", root, key));
    }

    /**
     * Adds a leaf condition bound to a property name.
     *
     * @param property property key
     * @param condition condition to evaluate
     */
    public void withCondition(String property, Condition condition) {
        predicateMap.put(property, condition);
    }

    /**
     * Adds a nested rule bound to a property name.
     *
     * @param property property key
     * @param rule nested rule
     */
    public void withRule(String property, Rule rule) {
        predicateMap.put(property, rule);
    }

    /**
     * Adds an all-match logical group for child rules.
     *
     * @param rules child rules that must all pass
     */
    public void withAllRules(List<Rule> rules) {
        predicateMap.put(ALL_KEY, new AllRule<>(rules));
    }

    /**
     * Adds an any-match logical group for child rules.
     *
     * @param rules child rules where any pass is sufficient
     */
    public void withAnyRules(List<Rule> rules) {
        predicateMap.put(ANY_KEY, new AnyRule<>(rules));
    }

    /**
     * Evaluates this rule against a map or collection input.
     *
     * @param value map or collection input
     * @return {@code true} when all configured checks for this node pass
     */
    @Override
    public boolean test(Object value) {
        if (value instanceof Map<?, ?> valueMap) {
            return testMap(valueMap);
        } else if (value instanceof Collection<?> valueCollection) {
            return testList(valueCollection);
        }
        return false;
    }

    private boolean testList(Collection<?> values) {
        for (Map.Entry<String, Predicate> predicateEntry : predicateMap.entrySet()) {
            final String key = predicateEntry.getKey();
            final Predicate predicate = predicateEntry.getValue();
            if (ANY_KEY.equalsIgnoreCase(key) || ALL_KEY.equalsIgnoreCase(key)) {
                if (!predicate.test(values)) {
                    checkingFailed(key);
                    return false;
                }
            } else {
                for (final Object value : values) {
                    if (!predicate.test(value)) {
                        checkingFailed(key);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean testMap(Map<?, ?> values) {
        for (Map.Entry<String, Predicate> entry : predicateMap.entrySet()) {
            final String property = entry.getKey();
            final Predicate predicate = entry.getValue();
            if (predicate == null) {
                checkingFailedReason(property, "rule is null");
                return false;
            }

            Object propertyValue = values;
            if (!ALL_KEY.equalsIgnoreCase(property) && !ANY_KEY.equalsIgnoreCase(property)) {
                if (values.containsKey(property)) {
                    propertyValue = values.get(property);
                } else {
                    // use Unknown to support exist rule
                    propertyValue = Unknown.INSTANCE;
                }
            }

            if (!predicate.test(propertyValue)) {
                if (Objects.equals(Unknown.INSTANCE, propertyValue)) {
                    checkingFailedReason(property, "input value is null");
                } else {
                    checkingFailed(property);
                }
                return false;
            }
        }
        return true;
    }
}
