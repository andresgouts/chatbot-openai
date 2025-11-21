package com.openai.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO for message data.
 * Used for API responses containing message information.
 */
@Data
@Builder
@AllArgsConstructor
public class MessageDto {

    private String role;
    private String content;
    private LocalDateTime timestamp;
}
