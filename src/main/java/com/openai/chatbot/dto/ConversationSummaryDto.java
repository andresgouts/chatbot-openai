package com.openai.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for conversation summary (list view).
 * Contains only basic information without messages for performance.
 */
@Data
@Builder
@AllArgsConstructor
public class ConversationSummaryDto {

    private UUID id;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
