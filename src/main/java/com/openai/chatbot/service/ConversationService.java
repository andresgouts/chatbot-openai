package com.openai.chatbot.service;

import com.openai.chatbot.dto.ConversationDetailDto;
import com.openai.chatbot.dto.ConversationSummaryDto;
import com.openai.chatbot.dto.MessageDto;
import com.openai.chatbot.entity.Conversation;
import com.openai.chatbot.entity.Message;
import com.openai.chatbot.exception.ConversationNotFoundException;
import com.openai.chatbot.exception.ConversationServiceException;
import com.openai.chatbot.repository.ConversationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for managing conversations and messages.
 * Handles business logic and transaction management for conversation operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;

    /**
     * Creates a new conversation for a user.
     *
     * @param userUuid the UUID of the user
     * @return the created conversation
     */
    @Transactional
    public Conversation createConversation(UUID userUuid) {
        try {
            log.debug("Creating new conversation for user: {}", userUuid);

            Conversation conversation = Conversation.builder()
                    .userUuid(userUuid)
                    .build();

            Conversation saved = conversationRepository.save(conversation);
            log.info("Created conversation with public ID: {}", saved.getPublicId());

            return saved;
        } catch (Exception ex) {
            log.error("Error creating conversation for user {}: {}", userUuid, ex.getMessage(), ex);
            throw new ConversationServiceException("Failed to create conversation", ex);
        }
    }

    /**
     * Saves a pair of messages (user and assistant) to a conversation.
     * Generates the conversation title from the first message if not already set.
     *
     * @param conversationId the public UUID of the conversation
     * @param userMessage    the user's message content
     * @param assistantMessage the assistant's response content
     */
    @Transactional
    public void saveMessagePair(UUID conversationId, String userMessage, String assistantMessage) {
        try {
            log.debug("Saving message pair to conversation: {}", conversationId);

            Conversation conversation = conversationRepository.findByPublicId(conversationId)
                    .orElseThrow(() -> new ConversationNotFoundException(conversationId));

            // Add user message
            Message userMsg = Message.builder()
                    .role("user")
                    .content(userMessage)
                    .build();
            conversation.addMessage(userMsg);

            // Add assistant message
            Message assistantMsg = Message.builder()
                    .role("assistant")
                    .content(assistantMessage)
                    .build();
            conversation.addMessage(assistantMsg);

            // Generate title from first message if not set
            if (conversation.getTitle() == null || conversation.getTitle().isBlank()) {
                conversation.generateTitleFromFirstMessage();
            }

            conversationRepository.save(conversation);
            log.info("Saved message pair to conversation: {}", conversationId);

        } catch (ConversationNotFoundException ex) {
            throw ex; // Re-throw domain exceptions
        } catch (Exception ex) {
            log.error("Error saving messages to conversation {}: {}", conversationId, ex.getMessage(), ex);
            throw new ConversationServiceException("Failed to save messages", ex);
        }
    }

    /**
     * Retrieves a conversation by its public ID with all messages.
     *
     * @param conversationId the public UUID of the conversation
     * @return the conversation detail DTO
     * @throws ConversationNotFoundException if the conversation is not found
     */
    @Transactional(readOnly = true)
    public ConversationDetailDto getConversationById(UUID conversationId) {
        log.debug("Retrieving conversation: {}", conversationId);

        Conversation conversation = conversationRepository.findByPublicIdWithMessages(conversationId)
                .orElseThrow(() -> new ConversationNotFoundException(conversationId));

        List<MessageDto> messages = conversation.getMessages().stream()
                .map(m -> MessageDto.builder()
                        .role(m.getRole())
                        .content(m.getContent())
                        .timestamp(m.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return ConversationDetailDto.builder()
                .id(conversation.getPublicId())
                .userId(conversation.getUserUuid())
                .title(conversation.getTitle())
                .createdAt(conversation.getCreatedAt())
                .updatedAt(conversation.getUpdatedAt())
                .messages(messages)
                .build();
    }

    /**
     * Lists all conversations for a specific user.
     *
     * @param userUuid the UUID of the user
     * @return list of conversation summaries ordered by most recently updated
     */
    @Transactional(readOnly = true)
    public List<ConversationSummaryDto> listConversationsByUser(UUID userUuid) {
        log.debug("Listing conversations for user: {}", userUuid);

        List<Conversation> conversations = conversationRepository.findByUserUuidOrderByUpdatedAtDesc(userUuid);

        return conversations.stream()
                .map(c -> ConversationSummaryDto.builder()
                        .id(c.getPublicId())
                        .title(c.getTitle())
                        .createdAt(c.getCreatedAt())
                        .updatedAt(c.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
