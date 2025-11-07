package com.openai.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Response DTO for chat endpoint.
 * Contains the AI-generated response and the model used.
 */
@Data
@AllArgsConstructor
public class ChatResponse {

    private String response;
    private String model;
}
