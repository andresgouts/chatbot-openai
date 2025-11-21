package com.openai.chatbot.repository;

import com.openai.chatbot.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Conversation entity.
 * Provides CRUD operations and custom queries for conversations.
 */
@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    /**
     * Find a conversation by its public UUID.
     *
     * @param publicId the public UUID of the conversation
     * @return Optional containing the conversation if found
     */
    Optional<Conversation> findByPublicId(UUID publicId);

    /**
     * Find a conversation by its public UUID with messages eagerly loaded.
     * Prevents LazyInitializationException when accessing messages outside transaction.
     *
     * @param publicId the public UUID of the conversation
     * @return Optional containing the conversation with messages if found
     */
    @Query("SELECT c FROM Conversation c LEFT JOIN FETCH c.messages WHERE c.publicId = :publicId")
    Optional<Conversation> findByPublicIdWithMessages(@Param("publicId") UUID publicId);

    /**
     * Find all conversations for a specific user, ordered by most recently updated.
     *
     * @param userUuid the user's UUID
     * @return List of conversations ordered by updatedAt descending
     */
    List<Conversation> findByUserUuidOrderByUpdatedAtDesc(UUID userUuid);
}
