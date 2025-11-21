package com.openai.chatbot.service

import com.openai.chatbot.exception.ChatServiceException
import com.theokanning.openai.completion.chat.ChatCompletionRequest
import com.theokanning.openai.completion.chat.ChatCompletionResult
import com.theokanning.openai.completion.chat.ChatMessage
import com.theokanning.openai.completion.chat.ChatCompletionChoice
import com.theokanning.openai.service.OpenAiService
import spock.lang.Specification
import spock.lang.Subject

/**
 * Unit tests for ChatService using Spock framework.
 */
class ChatServiceSpec extends Specification {

    OpenAiService openAiService = Mock()
    ConversationService conversationService = Mock()

    @Subject
    ChatService chatService

    def setup() {
        chatService = new ChatService(openAiService, conversationService)
        chatService.modelName = "gpt-3.5-turbo"
    }

    def "chat should return ChatResponse when OpenAI returns valid response"() {
        given: "a user message"
        def userMessage = "Hello, how are you?"

        and: "conversation service creates a new conversation"
        def conversationId = UUID.randomUUID()
        def conversation = Mock(com.openai.chatbot.entity.Conversation)
        conversation.getPublicId() >> conversationId

        and: "OpenAI service returns a valid response"
        def chatMessage = new ChatMessage("assistant", "I'm doing well, thank you!")
        def choice = new ChatCompletionChoice()
        choice.setMessage(chatMessage)

        def result = new ChatCompletionResult()
        result.setChoices([choice])

        when: "chat method is called"
        def response = chatService.chat(userMessage)

        then: "conversation is created"
        1 * conversationService.createConversation(_) >> conversation

        and: "OpenAI service is called with correct parameters"
        1 * openAiService.createChatCompletion(_ as ChatCompletionRequest) >> result

        and: "messages are saved to conversation"
        1 * conversationService.saveMessagePair(conversationId, userMessage, "I'm doing well, thank you!")

        and: "response contains expected data"
        response.response == "I'm doing well, thank you!"
        response.model == "gpt-3.5-turbo"
        response.conversationId == conversationId
    }

    def "chat should throw ChatServiceException when OpenAI returns null result"() {
        given: "a user message"
        def userMessage = "Hello"

        and: "conversation service creates a new conversation"
        def conversationId = UUID.randomUUID()
        def conversation = Mock(com.openai.chatbot.entity.Conversation)
        conversation.getPublicId() >> conversationId
        conversationService.createConversation(_) >> conversation

        and: "OpenAI service returns null"
        openAiService.createChatCompletion(_ as ChatCompletionRequest) >> null

        when: "chat method is called"
        chatService.chat(userMessage)

        then: "ChatServiceException is thrown"
        def exception = thrown(ChatServiceException)
        exception.message == "No response generated from OpenAI"
    }

    def "chat should throw ChatServiceException when OpenAI returns empty choices"() {
        given: "a user message"
        def userMessage = "Hello"

        and: "conversation service creates a new conversation"
        def conversationId = UUID.randomUUID()
        def conversation = Mock(com.openai.chatbot.entity.Conversation)
        conversation.getPublicId() >> conversationId
        conversationService.createConversation(_) >> conversation

        and: "OpenAI service returns result with empty choices"
        def result = new ChatCompletionResult()
        result.setChoices([])
        openAiService.createChatCompletion(_ as ChatCompletionRequest) >> result

        when: "chat method is called"
        chatService.chat(userMessage)

        then: "ChatServiceException is thrown"
        def exception = thrown(ChatServiceException)
        exception.message == "No response generated from OpenAI"
    }

    def "chat should throw ChatServiceException when OpenAI returns null choices"() {
        given: "a user message"
        def userMessage = "Hello"

        and: "conversation service creates a new conversation"
        def conversationId = UUID.randomUUID()
        def conversation = Mock(com.openai.chatbot.entity.Conversation)
        conversation.getPublicId() >> conversationId
        conversationService.createConversation(_) >> conversation

        and: "OpenAI service returns result with null choices"
        def result = new ChatCompletionResult()
        result.setChoices(null)
        openAiService.createChatCompletion(_ as ChatCompletionRequest) >> result

        when: "chat method is called"
        chatService.chat(userMessage)

        then: "ChatServiceException is thrown"
        def exception = thrown(ChatServiceException)
        exception.message == "No response generated from OpenAI"
    }

    def "chat should throw ChatServiceException when response message is null"() {
        given: "a user message"
        def userMessage = "Hello"

        and: "conversation service creates a new conversation"
        def conversationId = UUID.randomUUID()
        def conversation = Mock(com.openai.chatbot.entity.Conversation)
        conversation.getPublicId() >> conversationId
        conversationService.createConversation(_) >> conversation

        and: "OpenAI service returns result with null message"
        def choice = new ChatCompletionChoice()
        choice.setMessage(null)

        def result = new ChatCompletionResult()
        result.setChoices([choice])
        openAiService.createChatCompletion(_ as ChatCompletionRequest) >> result

        when: "chat method is called"
        chatService.chat(userMessage)

        then: "ChatServiceException is thrown"
        def exception = thrown(ChatServiceException)
        exception.message == "Invalid response format from OpenAI"
    }

    def "chat should throw ChatServiceException when response content is null"() {
        given: "a user message"
        def userMessage = "Hello"

        and: "conversation service creates a new conversation"
        def conversationId = UUID.randomUUID()
        def conversation = Mock(com.openai.chatbot.entity.Conversation)
        conversation.getPublicId() >> conversationId
        conversationService.createConversation(_) >> conversation

        and: "OpenAI service returns message with null content"
        def chatMessage = new ChatMessage("assistant", null)
        def choice = new ChatCompletionChoice()
        choice.setMessage(chatMessage)

        def result = new ChatCompletionResult()
        result.setChoices([choice])
        openAiService.createChatCompletion(_ as ChatCompletionRequest) >> result

        when: "chat method is called"
        chatService.chat(userMessage)

        then: "ChatServiceException is thrown"
        def exception = thrown(ChatServiceException)
        exception.message == "Invalid response format from OpenAI"
    }

    def "chat should throw ChatServiceException when OpenAI service throws exception"() {
        given: "a user message"
        def userMessage = "Hello"

        and: "conversation service creates a new conversation"
        def conversationId = UUID.randomUUID()
        def conversation = Mock(com.openai.chatbot.entity.Conversation)
        conversation.getPublicId() >> conversationId
        conversationService.createConversation(_) >> conversation

        and: "OpenAI service throws an exception"
        def originalException = new RuntimeException("API Error")
        openAiService.createChatCompletion(_ as ChatCompletionRequest) >> { throw originalException }

        when: "chat method is called"
        chatService.chat(userMessage)

        then: "ChatServiceException is thrown with proper message"
        def exception = thrown(ChatServiceException)
        exception.message == "Failed to get response from OpenAI"
        exception.cause == originalException
    }

    def "chat should use configured model name in request"() {
        given: "a custom model name"
        chatService.modelName = "gpt-4"

        and: "a user message"
        def userMessage = "Test message"

        and: "conversation service creates a new conversation"
        def conversationId = UUID.randomUUID()
        def conversation = Mock(com.openai.chatbot.entity.Conversation)
        conversation.getPublicId() >> conversationId
        conversationService.createConversation(_) >> conversation

        and: "OpenAI service returns a valid response"
        def chatMessage = new ChatMessage("assistant", "Response")
        def choice = new ChatCompletionChoice()
        choice.setMessage(chatMessage)

        def result = new ChatCompletionResult()
        result.setChoices([choice])

        ChatCompletionRequest capturedRequest = null
        openAiService.createChatCompletion(_ as ChatCompletionRequest) >> { args ->
            capturedRequest = args[0]
            return result
        }

        when: "chat method is called"
        def response = chatService.chat(userMessage)

        then: "request uses the configured model"
        capturedRequest.model == "gpt-4"
        response.model == "gpt-4"
    }

    def "chat should include user message in OpenAI request"() {
        given: "a specific user message"
        def userMessage = "What is the weather today?"

        and: "conversation service creates a new conversation"
        def conversationId = UUID.randomUUID()
        def conversation = Mock(com.openai.chatbot.entity.Conversation)
        conversation.getPublicId() >> conversationId
        conversationService.createConversation(_) >> conversation

        and: "OpenAI service returns a valid response"
        def chatMessage = new ChatMessage("assistant", "It's sunny")
        def choice = new ChatCompletionChoice()
        choice.setMessage(chatMessage)

        def result = new ChatCompletionResult()
        result.setChoices([choice])

        ChatCompletionRequest capturedRequest = null
        openAiService.createChatCompletion(_ as ChatCompletionRequest) >> { args ->
            capturedRequest = args[0]
            return result
        }

        when: "chat method is called"
        chatService.chat(userMessage)

        then: "request contains the user message"
        capturedRequest.messages.size() == 1
        capturedRequest.messages[0].role == "user"
        capturedRequest.messages[0].content == userMessage
    }
}
