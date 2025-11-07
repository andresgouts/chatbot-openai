package com.openai.chatbot.controller;

import com.openai.chatbot.dto.ChatRequest;
import com.openai.chatbot.dto.ChatResponse;
import com.openai.chatbot.service.ChatService;
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
public class ChatController {

    private final ChatService chatService;

    /**
     * Chat endpoint that receives a user message and returns an AI response.
     *
     * @param request the chat request containing the user's message
     * @return ResponseEntity with the AI response
     */
    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        log.info("Received chat request");

        ChatResponse response = chatService.chat(request.getMessage());

        log.info("Chat request processed successfully");

        return ResponseEntity.ok(response);
    }
}
