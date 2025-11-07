package com.openai.chatbot.exception;

/**
 * Custom exception for chat service errors.
 * This exception is thrown when there are issues communicating with the OpenAI API
 * or processing chat requests.
 */
public class ChatServiceException extends RuntimeException {

    public ChatServiceException(String message) {
        super(message);
    }

    public ChatServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
