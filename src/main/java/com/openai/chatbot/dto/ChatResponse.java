package com.openai.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

/**
 * Response DTO for chat endpoint.
 * Contains the AI-generated response and the model used.
 */
@Data
@AllArgsConstructor
public class ChatResponse {

    private String response;
    private String model;
    private UUID conversationId;

    /**
     * Constructor for backward compatibility.
     *
     * @param response the AI response
     * @param model    the model used
     */
    public ChatResponse(String response, String model) {
        this.response = response;
        this.model = model;
        this.conversationId = null;
    }
}
