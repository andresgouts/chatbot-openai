/**
 * Type definitions for the chat application
 * Matches backend DTOs and extends with frontend-specific types
 */

/**
 * Request DTO matching backend ChatRequest
 * @see com.openai.chatbot.dto.ChatRequest
 */
export interface ChatRequest {
  message: string;
}

/**
 * Response DTO matching backend ChatResponse
 * @see com.openai.chatbot.dto.ChatResponse
 */
export interface ChatApiResponse {
  response: string;
  model: string;
}

/**
 * Frontend message model for displaying chat messages
 */
export interface Message {
  id: string;
  role: 'user' | 'assistant';
  content: string;
  timestamp: Date;
  isError?: boolean;
}

/**
 * API error response structure
 * @see com.openai.chatbot.exception.GlobalExceptionHandler
 */
export interface ApiError {
  timestamp: string;
  status: number;
  error: string;
  message: string;
}
