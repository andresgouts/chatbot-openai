package com.openai.chatbot.controller;

import com.openai.chatbot.dto.ConversationDetailDto;
import com.openai.chatbot.dto.ConversationSummaryDto;
import com.openai.chatbot.service.ConversationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for conversation history operations.
 * Provides endpoints for listing and retrieving conversations.
 */
@Slf4j
@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
@Tag(name = "Conversations", description = "Conversation history management operations")
public class ConversationController {

    private final ConversationService conversationService;

    /**
     * Default user UUID for MVP (no authentication yet).
     */
    private static final UUID DEFAULT_USER_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    /**
     * Lists all conversations for a user.
     *
     * @param userId the user UUID (optional, defaults to default user)
     * @return ResponseEntity with list of conversation summaries
     */
    @Operation(
            summary = "List user conversations",
            description = "Retrieves a list of all conversations for a user, ordered by most recently updated. Returns conversation ID and title only."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved conversation list",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ConversationSummaryDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Database connection error",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping
    public ResponseEntity<List<ConversationSummaryDto>> listConversations(
            @Parameter(description = "User UUID (optional, defaults to default user for MVP)")
            @RequestParam(required = false) UUID userId) {
        UUID userUuid = userId != null ? userId : DEFAULT_USER_UUID;
        log.info("Listing conversations for user: {}", userUuid);

        List<ConversationSummaryDto> conversations = conversationService.listConversationsByUser(userUuid);

        log.info("Found {} conversations for user: {}", conversations.size(), userUuid);
        return ResponseEntity.ok(conversations);
    }

    /**
     * Retrieves a conversation by its ID with all messages.
     *
     * @param id the conversation public UUID
     * @return ResponseEntity with conversation detail
     */
    @Operation(
            summary = "Get conversation by ID",
            description = "Retrieves a complete conversation with all messages by its ID. Messages include role, content, and timestamp."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved conversation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConversationDetailDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Conversation not found",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Database connection error",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ConversationDetailDto> getConversation(
            @Parameter(description = "Conversation UUID", required = true)
            @PathVariable UUID id) {
        log.info("Retrieving conversation: {}", id);

        ConversationDetailDto conversation = conversationService.getConversationById(id);

        log.info("Retrieved conversation: {} with {} messages", id, conversation.getMessages().size());
        return ResponseEntity.ok(conversation);
    }
}
