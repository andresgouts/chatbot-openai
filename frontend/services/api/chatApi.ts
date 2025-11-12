/**
 * Chat API client
 * Handles communication with the backend chat endpoint
 */

import { httpClient } from './httpClient';
import { ChatRequest, ChatApiResponse } from '@/services/types';
import { API_CONFIG } from '@/utils/constants';

/**
 * Chat API service
 */
export const chatApi = {
  /**
   * Sends a chat message to the backend
   * @param message - The user's message text
   * @returns Promise with the AI response
   * @throws HttpError if the request fails
   */
  async sendMessage(message: string): Promise<ChatApiResponse> {
    const request: ChatRequest = { message };

    const response = await httpClient.post<ChatApiResponse>(
      API_CONFIG.endpoints.chat,
      request
    );

    return response;
  },
};
