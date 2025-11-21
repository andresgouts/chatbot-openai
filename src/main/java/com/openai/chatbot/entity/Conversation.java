package com.openai.chatbot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entity representing a conversation with messages.
 * Serves as the aggregate root for the conversation domain.
 */
@Entity
@Table(name = "conversations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", nullable = false, unique = true, updatable = false)
    private UUID publicId;

    @Column(name = "user_uuid", nullable = false)
    private UUID userUuid;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("createdAt ASC")
    @Builder.Default
    private List<Message> messages = new ArrayList<>();

    /**
     * Lifecycle callback to set timestamps and generate UUID before persist.
     */
    @PrePersist
    protected void onCreate() {
        if (publicId == null) {
            publicId = UUID.randomUUID();
        }
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    /**
     * Lifecycle callback to update the timestamp before update.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Business method to add a message to the conversation.
     * Maintains bidirectional relationship consistency.
     *
     * @param message the message to add
     */
    public void addMessage(Message message) {
        messages.add(message);
        message.setConversation(this);
    }

    /**
     * Business method to generate a title from the first message in the conversation.
     * Title is the first 50 Unicode code points of the first message content.
     * Sanitizes control characters and handles multi-byte characters correctly.
     */
    public void generateTitleFromFirstMessage() {
        if (messages.isEmpty()) {
            this.title = "New Conversation";
            return;
        }

        String firstMessage = messages.get(0).getContent();
        if (firstMessage == null || firstMessage.isBlank()) {
            this.title = "New Conversation";
            return;
        }

        // Sanitize: remove control characters (except spaces/tabs) and normalize whitespace
        String sanitized = firstMessage.replaceAll("\\p{Cntrl}&&[^\\t]", " ")
                .replaceAll("\\s+", " ")
                .trim();

        if (sanitized.isBlank()) {
            this.title = "New Conversation";
            return;
        }

        // Take first 50 Unicode code points (handles emojis and multi-byte characters correctly)
        int codePointCount = sanitized.codePointCount(0, sanitized.length());
        if (codePointCount > 50) {
            int endIndex = sanitized.offsetByCodePoints(0, 50);
            this.title = sanitized.substring(0, endIndex).trim() + "...";
        } else {
            this.title = sanitized;
        }
    }
}
