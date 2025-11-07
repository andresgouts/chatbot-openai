package com.openai.chatbot.exception

import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

/**
 * Unit tests for GlobalExceptionHandler using Spock framework.
 */
class GlobalExceptionHandlerSpec extends Specification {

    @Subject
    GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler()

    def "handleValidationException should return BAD_REQUEST with validation errors"() {
        given: "a validation exception with field errors"
        def target = new Object()
        def bindingResult = new BeanPropertyBindingResult(target, "chatRequest")
        bindingResult.addError(new FieldError("chatRequest", "message", "must not be blank"))
        bindingResult.addError(new FieldError("chatRequest", "message", "size must be between 1 and 1000"))

        def method = String.class.getDeclaredMethod("toString")
        def methodParameter = Mock(MethodParameter) {
            getParameterIndex() >> 0
            getExecutable() >> method
        }
        def exception = new MethodArgumentNotValidException(
                methodParameter,
                bindingResult
        )

        when: "exception handler processes the validation exception"
        def response = exceptionHandler.handleValidationException(exception)

        then: "response has BAD_REQUEST status"
        response.statusCode == HttpStatus.BAD_REQUEST

        and: "response body contains error information"
        response.body.status == 400
        response.body.error == "Validation failed"
        response.body.message.contains("message:")
        response.body.timestamp instanceof LocalDateTime
    }

    def "handleValidationException should format multiple field errors correctly"() {
        given: "multiple field validation errors"
        def target = new Object()
        def bindingResult = new BeanPropertyBindingResult(target, "request")
        bindingResult.addError(new FieldError("request", "field1", "error1"))
        bindingResult.addError(new FieldError("request", "field2", "error2"))
        bindingResult.addError(new FieldError("request", "field3", "error3"))

        def method = String.class.getDeclaredMethod("toString")
        def methodParameter = Mock(MethodParameter) {
            getParameterIndex() >> 0
            getExecutable() >> method
        }
        def exception = new MethodArgumentNotValidException(
                methodParameter,
                bindingResult
        )

        when: "exception handler processes the exception"
        def response = exceptionHandler.handleValidationException(exception)

        then: "all errors are included in the message"
        response.body.message.contains("field1: error1")
        response.body.message.contains("field2: error2")
        response.body.message.contains("field3: error3")
    }

    def "handleValidationException should handle single field error"() {
        given: "a single field validation error"
        def target = new Object()
        def bindingResult = new BeanPropertyBindingResult(target, "chatRequest")
        bindingResult.addError(new FieldError("chatRequest", "message", "must not be blank"))

        def method = String.class.getDeclaredMethod("toString")
        def methodParameter = Mock(MethodParameter) {
            getParameterIndex() >> 0
            getExecutable() >> method
        }
        def exception = new MethodArgumentNotValidException(
                methodParameter,
                bindingResult
        )

        when: "exception handler processes the exception"
        def response = exceptionHandler.handleValidationException(exception)

        then: "response contains the single error"
        response.statusCode == HttpStatus.BAD_REQUEST
        response.body.message == "message: must not be blank"
    }

    def "handleChatServiceException should return INTERNAL_SERVER_ERROR with generic message"() {
        given: "a ChatServiceException"
        def exception = new ChatServiceException("Failed to connect to OpenAI API")

        when: "exception handler processes the exception"
        def response = exceptionHandler.handleChatServiceException(exception)

        then: "response has INTERNAL_SERVER_ERROR status"
        response.statusCode == HttpStatus.INTERNAL_SERVER_ERROR

        and: "response body contains error information"
        response.body.status == 500
        response.body.error == "Internal Server Error"
        response.body.message == "Failed to process chat request. Please try again later."
        response.body.timestamp instanceof LocalDateTime
    }

    def "handleChatServiceException should not expose internal error details to client"() {
        given: "a ChatServiceException with sensitive internal details"
        def exception = new ChatServiceException("API Key invalid: sk-abc123xyz")

        when: "exception handler processes the exception"
        def response = exceptionHandler.handleChatServiceException(exception)

        then: "response contains generic message without sensitive details"
        !response.body.message.contains("API Key")
        !response.body.message.contains("sk-abc123xyz")
        response.body.message == "Failed to process chat request. Please try again later."
    }

    def "handleChatServiceException should handle exception with cause"() {
        given: "a ChatServiceException with underlying cause"
        def rootCause = new RuntimeException("Network timeout")
        def exception = new ChatServiceException("Failed to get response", rootCause)

        when: "exception handler processes the exception"
        def response = exceptionHandler.handleChatServiceException(exception)

        then: "response is returned successfully"
        response.statusCode == HttpStatus.INTERNAL_SERVER_ERROR
        response.body.message == "Failed to process chat request. Please try again later."
    }

    def "handleGenericException should return INTERNAL_SERVER_ERROR for unexpected exceptions"() {
        given: "an unexpected exception"
        def exception = new RuntimeException("Unexpected error occurred")

        when: "exception handler processes the exception"
        def response = exceptionHandler.handleGenericException(exception)

        then: "response has INTERNAL_SERVER_ERROR status"
        response.statusCode == HttpStatus.INTERNAL_SERVER_ERROR

        and: "response body contains generic error information"
        response.body.status == 500
        response.body.error == "Internal Server Error"
        response.body.message == "An unexpected error occurred. Please try again later."
        response.body.timestamp instanceof LocalDateTime
    }

    def "handleGenericException should handle NullPointerException"() {
        given: "a NullPointerException"
        def exception = new NullPointerException("Null value encountered")

        when: "exception handler processes the exception"
        def response = exceptionHandler.handleGenericException(exception)

        then: "response is handled gracefully"
        response.statusCode == HttpStatus.INTERNAL_SERVER_ERROR
        response.body.message == "An unexpected error occurred. Please try again later."
    }

    def "handleGenericException should handle IllegalArgumentException"() {
        given: "an IllegalArgumentException"
        def exception = new IllegalArgumentException("Invalid parameter")

        when: "exception handler processes the exception"
        def response = exceptionHandler.handleGenericException(exception)

        then: "response has INTERNAL_SERVER_ERROR status"
        response.statusCode == HttpStatus.INTERNAL_SERVER_ERROR
        response.body.error == "Internal Server Error"
    }

    def "all exception handlers should include timestamp in response"() {
        given: "various types of exceptions"
        def chatServiceException = new ChatServiceException("Service error")
        def genericException = new RuntimeException("Generic error")

        def target = new Object()
        def bindingResult = new BeanPropertyBindingResult(target, "request")
        bindingResult.addError(new FieldError("request", "field", "error"))
        def method = String.class.getDeclaredMethod("toString")
        def methodParameter = Mock(MethodParameter) {
            getParameterIndex() >> 0
            getExecutable() >> method
        }
        def validationException = new MethodArgumentNotValidException(
                methodParameter,
                bindingResult
        )

        when: "each exception is handled"
        def response1 = exceptionHandler.handleChatServiceException(chatServiceException)
        def response2 = exceptionHandler.handleGenericException(genericException)
        def response3 = exceptionHandler.handleValidationException(validationException)

        then: "all responses contain timestamp"
        response1.body.timestamp != null
        response2.body.timestamp != null
        response3.body.timestamp != null
    }

    def "error responses should contain all required fields"() {
        given: "a ChatServiceException"
        def exception = new ChatServiceException("Test error")

        when: "exception is handled"
        def response = exceptionHandler.handleChatServiceException(exception)

        then: "response contains all required fields"
        response.body.containsKey("timestamp")
        response.body.containsKey("status")
        response.body.containsKey("error")
        response.body.containsKey("message")
    }
}
