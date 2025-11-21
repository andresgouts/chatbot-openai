package com.openai.chatbot.controller

import com.openai.chatbot.dto.ChatResponse
import com.openai.chatbot.service.ChatService
import org.springframework.http.HttpStatus
import spock.lang.Specification
import spock.lang.Subject

/**
 * Unit tests for ChatController using Spock framework.
 */
class ChatControllerSpec extends Specification {

    ChatService chatService = Mock()

    @Subject
    ChatController chatController

    def setup() {
        chatController = new ChatController(chatService)
    }

    def "chat should return ResponseEntity with ChatResponse when service returns successfully"() {
        given: "a valid chat request with a message"
        def request = new com.openai.chatbot.dto.ChatRequest(message: "Hello, how are you?")

        and: "chat service returns a response"
        def serviceResponse = new ChatResponse("I'm doing well!", "gpt-3.5-turbo")
        chatService.chat(request.message, request.conversationId) >> serviceResponse

        when: "chat endpoint is called"
        def response = chatController.chat(request)

        then: "service is called once with correct parameters"
        1 * chatService.chat(request.message, request.conversationId) >> serviceResponse

        and: "response has OK status"
        response.statusCode == HttpStatus.OK

        and: "response body contains correct data"
        response.body.response == "I'm doing well!"
        response.body.model == "gpt-3.5-turbo"
    }

    def "chat should delegate message processing to ChatService"() {
        given: "a chat request with specific message"
        def userMessage = "What is the meaning of life?"
        def request = new com.openai.chatbot.dto.ChatRequest(message: userMessage)

        and: "chat service is configured to return a response"
        def expectedResponse = new ChatResponse("42", "gpt-4")
        chatService.chat(userMessage, request.conversationId) >> expectedResponse

        when: "chat endpoint is called"
        def response = chatController.chat(request)

        then: "service receives the exact user message and conversation ID"
        1 * chatService.chat(userMessage, request.conversationId) >> expectedResponse

        and: "controller returns the service response"
        response.body == expectedResponse
    }

    def "chat should propagate exceptions from ChatService"() {
        given: "a chat request"
        def request = new com.openai.chatbot.dto.ChatRequest(message: "Test")

        and: "chat service throws an exception"
        def exception = new RuntimeException("Service error")
        chatService.chat(_, _) >> { throw exception }

        when: "chat endpoint is called"
        chatController.chat(request)

        then: "exception is propagated"
        thrown(RuntimeException)
    }

    def "chat should handle empty message from request"() {
        given: "a chat request with empty message"
        def request = new com.openai.chatbot.dto.ChatRequest(message: "")

        and: "chat service is configured"
        def serviceResponse = new ChatResponse("Please provide a message", "gpt-3.5-turbo")
        chatService.chat("", request.conversationId) >> serviceResponse

        when: "chat endpoint is called"
        def response = chatController.chat(request)

        then: "service is called with empty message and conversation ID"
        1 * chatService.chat("", request.conversationId) >> serviceResponse

        and: "response is returned successfully"
        response.statusCode == HttpStatus.OK
        response.body.response == "Please provide a message"
    }

    def "chat should handle long messages"() {
        given: "a chat request with a very long message"
        def longMessage = "A" * 1000
        def request = new com.openai.chatbot.dto.ChatRequest(message: longMessage)

        and: "chat service processes the long message"
        def serviceResponse = new ChatResponse("Processed long message", "gpt-3.5-turbo")
        chatService.chat(longMessage, request.conversationId) >> serviceResponse

        when: "chat endpoint is called"
        def response = chatController.chat(request)

        then: "service is called with the long message and conversation ID"
        1 * chatService.chat(longMessage, request.conversationId) >> serviceResponse

        and: "response is successful"
        response.statusCode == HttpStatus.OK
        response.body == serviceResponse
    }

    def "chat should handle special characters in message"() {
        given: "a chat request with special characters"
        def specialMessage = "Hello! @#\$%^&*()_+ <script>alert('test')</script>"
        def request = new com.openai.chatbot.dto.ChatRequest(message: specialMessage)

        and: "chat service processes the message"
        def serviceResponse = new ChatResponse("Response to special chars", "gpt-3.5-turbo")
        chatService.chat(specialMessage, request.conversationId) >> serviceResponse

        when: "chat endpoint is called"
        def response = chatController.chat(request)

        then: "service is called with the special message and conversation ID"
        1 * chatService.chat(specialMessage, request.conversationId) >> serviceResponse

        and: "response is successful"
        response.statusCode == HttpStatus.OK
        response.body == serviceResponse
    }

    def "chat should return response with correct model information"() {
        given: "a chat request"
        def request = new com.openai.chatbot.dto.ChatRequest(message: "Test")

        and: "chat service returns response with specific model"
        def modelName = "gpt-4-turbo"
        def serviceResponse = new ChatResponse("Response", modelName)
        chatService.chat(_, _) >> serviceResponse

        when: "chat endpoint is called"
        def response = chatController.chat(request)

        then: "response contains correct model information"
        response.body.model == modelName
    }
}
