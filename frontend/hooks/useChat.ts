/**
 * useChat hook
 * Manages chat state and business logic
 */

import { useState } from 'react';
import { chatApi } from '@/services/api/chatApi';
import { Message } from '@/services/types';
import { generateMessageId } from '@/utils/formatters';
import { HttpError } from '@/services/api/httpClient';

interface UseChatReturn {
  messages: Message[];
  inputValue: string;
  setInputValue: (value: string) => void;
  isLoading: boolean;
  error: string | null;
  sendMessage: () => Promise<void>;
  clearError: () => void;
}

/**
 * Hook that manages chat state and provides message sending functionality
 * @returns Object with chat state and control functions
 */
export function useChat(): UseChatReturn {
  const [messages, setMessages] = useState<Message[]>([]);
  const [inputValue, setInputValue] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const sendMessage = async () => {
    const trimmedInput = inputValue.trim();

    if (!trimmedInput || isLoading) {
      return;
    }

    // Clear error from previous attempts
    setError(null);

    // Create and add user message optimistically
    const userMessage: Message = {
      id: generateMessageId(),
      role: 'user',
      content: trimmedInput,
      timestamp: new Date(),
    };

    setMessages((prev) => [...prev, userMessage]);
    setInputValue('');
    setIsLoading(true);

    try {
      // Call backend API
      const response = await chatApi.sendMessage(trimmedInput);

      // Create and add AI response message
      const assistantMessage: Message = {
        id: generateMessageId(),
        role: 'assistant',
        content: response.response,
        timestamp: new Date(),
      };

      setMessages((prev) => [...prev, assistantMessage]);
    } catch (err) {
      // Handle errors
      let errorMessage = 'Failed to send message. Please try again.';

      if (err instanceof HttpError) {
        if (err.status === 400) {
          errorMessage = 'Invalid message. Please check your input.';
        } else if (err.status === 408) {
          errorMessage = 'Request timed out. Please try again.';
        } else if (err.status >= 500) {
          errorMessage = 'Server error. Please try again later.';
        } else if (err.status === 0) {
          errorMessage = 'Network error. Please check your connection.';
        } else {
          errorMessage = err.message;
        }
      }

      setError(errorMessage);

      // Add error message to chat
      const errorMessageObj: Message = {
        id: generateMessageId(),
        role: 'assistant',
        content: errorMessage,
        timestamp: new Date(),
        isError: true,
      };

      setMessages((prev) => [...prev, errorMessageObj]);
    } finally {
      setIsLoading(false);
    }
  };

  const clearError = () => {
    setError(null);
  };

  return {
    messages,
    inputValue,
    setInputValue,
    isLoading,
    error,
    sendMessage,
    clearError,
  };
}
