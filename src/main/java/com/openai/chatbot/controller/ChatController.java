package com.openai.chatbot.controller;

import com.openai.chatbot.dto.ChatRequest;
import com.openai.chatbot.dto.ChatResponse;
import com.openai.chatbot.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for chat operations.
 * Provides endpoints for interacting with the OpenAI chat service.
 */
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Chat", description = "AI chat operations using OpenAI GPT models")
public class ChatController {

    private final ChatService chatService;

    /**
     * Chat endpoint that receives a user message and returns an AI response.
     * Supports continuing existing conversations via conversationId.
     *
     * @param request the chat request containing the user's message and optional conversation ID
     * @return ResponseEntity with the AI response and conversation ID
     */
    @Operation(
            summary = "Send a chat message",
            description = "Sends a message to the AI and receives a response. If conversationId is provided, continues the existing conversation. Otherwise, creates a new conversation."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully received AI response",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChatResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request (e.g., empty message)",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error communicating with OpenAI or database error",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        log.info("Received chat request for conversation: {}", request.getConversationId());

        ChatResponse response = chatService.chat(request.getMessage(), request.getConversationId());

        log.info("Chat request processed successfully, conversationId: {}", response.getConversationId());

        return ResponseEntity.ok(response);
    }
}
