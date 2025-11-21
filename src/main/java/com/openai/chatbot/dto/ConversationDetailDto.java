package com.openai.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO for conversation detail (including all messages).
 * Used for GET /api/conversations/{id} endpoint.
 */
@Data
@Builder
@AllArgsConstructor
public class ConversationDetailDto {

    private UUID id;
    private UUID userId;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<MessageDto> messages;
}
