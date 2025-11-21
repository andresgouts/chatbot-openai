package com.openai.chatbot.exception;

/**
 * Exception thrown when there's an error in conversation service operations.
 * This exception is mapped to HTTP 500 (Internal Server Error) status code.
 */
public class ConversationServiceException extends RuntimeException {

    /**
     * Constructs a new ConversationServiceException with the specified message.
     *
     * @param message the detail message
     */
    public ConversationServiceException(String message) {
        super(message);
    }

    /**
     * Constructs a new ConversationServiceException with the specified message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public ConversationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
