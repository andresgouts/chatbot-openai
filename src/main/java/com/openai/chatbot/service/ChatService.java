package com.openai.chatbot.service;

import com.openai.chatbot.dto.ChatResponse;
import com.openai.chatbot.exception.ChatServiceException;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for handling chat interactions with OpenAI.
 * Manages the communication with the OpenAI API and processes chat requests.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final OpenAiService openAiService;

    @Value("${openai.model}")
    private String modelName;

    /**
     * Processes a chat message and returns the AI response.
     *
     * @param message the user's message
     * @return ChatResponse containing the AI response and model used
     * @throws ChatServiceException if there's an error communicating with OpenAI
     */
    public ChatResponse chat(String message) {
        log.debug("Processing chat request with message: {}", message);

        try {
            // Build the chat completion request
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

            return new ChatResponse(aiResponse, modelName);

        } catch (Exception ex) {
            log.error("Error processing chat request: {}", ex.getMessage(), ex);
            throw new ChatServiceException("Failed to get response from OpenAI", ex);
        }
    }
}
