package com.openai.chatbot.repository.projection;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Projection interface for conversation summaries.
 * Used to fetch only ID and title for list views, improving query performance.
 */
public interface ConversationSummary {

    /**
     * Get the public UUID of the conversation.
     *
     * @return the public UUID
     */
    UUID getPublicId();

    /**
     * Get the conversation title.
     *
     * @return the title
     */
    String getTitle();

    /**
     * Get the conversation creation timestamp.
     *
     * @return the creation timestamp
     */
    LocalDateTime getCreatedAt();

    /**
     * Get the conversation last update timestamp.
     *
     * @return the last update timestamp
     */
    LocalDateTime getUpdatedAt();
}
