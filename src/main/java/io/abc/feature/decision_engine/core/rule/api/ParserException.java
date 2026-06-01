package io.abc.feature.decision_engine.core.rule.api;

/**
 * Runtime exception thrown when parsing rule JSON fails.
 */
public class ParserException extends RuntimeException {

    /**
     * Creates a parser exception with a message.
     *
     * @param message error description
     */
    public ParserException(String message) {
        super(message);
    }

    /**
     * Creates a parser exception with a message and root cause.
     *
     * @param message error description
     * @param cause root cause
     */
    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
