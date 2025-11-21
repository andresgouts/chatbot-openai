package com.openai.chatbot.service;

import com.openai.chatbot.dto.ChatResponse;
import com.openai.chatbot.entity.Conversation;
import com.openai.chatbot.exception.ChatServiceException;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service class for handling chat interactions with OpenAI.
 * Manages the communication with the OpenAI API and processes chat requests.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final OpenAiService openAiService;
    private final ConversationService conversationService;

    @Value("${openai.model}")
    private String modelName;

    /**
     * Default user UUID for conversations when no user is specified.
     */
    private static final UUID DEFAULT_USER_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    /**
     * Processes a chat message and returns the AI response.
     * Creates a new conversation if conversationId is null.
     *
     * @param message the user's message
     * @return ChatResponse containing the AI response and model used
     * @throws ChatServiceException if there's an error communicating with OpenAI
     */
    public ChatResponse chat(String message) {
        return chat(message, null);
    }

    /**
     * Processes a chat message and returns the AI response.
     * Creates a new conversation if conversationId is null, otherwise continues existing conversation.
     * Wrapped in a transaction to ensure atomic operation of conversation creation and message persistence.
     *
     * @param message the user's message
     * @param conversationId the optional conversation ID to continue
     * @return ChatResponse containing the AI response, model used, and conversation ID
     * @throws ChatServiceException if there's an error communicating with OpenAI
     */
    @Transactional
    public ChatResponse chat(String message, UUID conversationId) {
        log.debug("Processing chat request with message: {}, conversationId: {}", message, conversationId);

        try {
            // Create new conversation if needed
            if (conversationId == null) {
                Conversation conversation = conversationService.createConversation(DEFAULT_USER_UUID);
                conversationId = conversation.getPublicId();
                log.debug("Created new conversation: {}", conversationId);
            }

            // Build the chat completion request (still only sends current message to OpenAI)
            ChatCompletionRequest request = ChatCompletionRequest.builder()
                    .model(modelName)
                    .messages(List.of(new ChatMessage("user", message)))
                    .build();

            log.debug("Sending request to OpenAI with model: {}", modelName);

            // Call OpenAI API
            ChatCompletionResult result = openAiService.createChatCompletion(request);

            // Validate response
            if (result == null || result.getChoices() == null || result.getChoices().isEmpty()) {
                log.error("OpenAI returned no choices in response");
                throw new ChatServiceException("No response generated from OpenAI");
            }

            ChatMessage responseMessage = result.getChoices().get(0).getMessage();
            if (responseMessage == null || responseMessage.getContent() == null) {
                log.error("OpenAI returned invalid response format");
                throw new ChatServiceException("Invalid response format from OpenAI");
            }

            // Extract response
            String aiResponse = responseMessage.getContent();

            log.debug("Received response from OpenAI");

            // Save message pair to conversation history
            conversationService.saveMessagePair(conversationId, message, aiResponse);

            return new ChatResponse(aiResponse, modelName, conversationId);

        } catch (ChatServiceException ex) {
            throw ex; // Re-throw chat service exceptions
        } catch (Exception ex) {
            log.error("Error processing chat request: {}", ex.getMessage(), ex);
            throw new ChatServiceException("Failed to get response from OpenAI", ex);
        }
    }
}
