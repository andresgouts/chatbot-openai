package com.openai.chatbot.exception;

import java.util.UUID;

/**
 * Exception thrown when a conversation is not found by its ID.
 * This exception is mapped to HTTP 404 (Not Found) status code.
 */
public class ConversationNotFoundException extends RuntimeException {

    /**
     * Constructs a new ConversationNotFoundException with the conversation ID.
     *
     * @param conversationId the UUID of the conversation that was not found
     */
    public ConversationNotFoundException(UUID conversationId) {
        super("Conversation not found: " + conversationId);
    }
}
