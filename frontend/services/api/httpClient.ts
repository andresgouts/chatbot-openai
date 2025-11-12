/**
 * HTTP client wrapper around native fetch API
 * Provides consistent error handling and JSON parsing
 */

import { API_CONFIG } from '@/utils/constants';
import { ApiError } from '@/services/types';

/**
 * Custom error class for HTTP errors
 */
export class HttpError extends Error {
  constructor(
    message: string,
    public status: number,
    public response?: unknown
  ) {
    super(message);
    this.name = 'HttpError';
  }
}

/**
 * HTTP client configuration
 */
interface HttpClientConfig {
  baseUrl?: string;
  timeout?: number;
}

/**
 * Creates an HTTP client with the given configuration
 */
class HttpClient {
  private baseUrl: string;
  private timeout: number;

  constructor(config: HttpClientConfig = {}) {
    this.baseUrl = config.baseUrl || API_CONFIG.baseUrl;
    this.timeout = config.timeout || API_CONFIG.timeout;
  }

  /**
   * Makes a POST request
   * @param url - The endpoint URL
   * @param body - The request body
   * @returns Promise with the parsed JSON response
   * @throws HttpError if the request fails
   */
  async post<T>(url: string, body: unknown): Promise<T> {
    const fullUrl = this.baseUrl + url;
    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), this.timeout);

    try {
      const response = await fetch(fullUrl, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(body),
        signal: controller.signal,
      });

      clearTimeout(timeoutId);

      if (!response.ok) {
        // Try to parse error response from backend
        let errorData: ApiError | undefined;
        try {
          errorData = await response.json();
        } catch {
          // If JSON parsing fails, use generic error
          errorData = undefined;
        }

        throw new HttpError(
          errorData?.message || `HTTP ${response.status}: ${response.statusText}`,
          response.status,
          errorData
        );
      }

      return await response.json();
    } catch (error) {
      clearTimeout(timeoutId); // Ensure cleanup even on error

      if (error instanceof HttpError) {
        throw error;
      }

      if (error instanceof Error && error.name === 'AbortError') {
        throw new HttpError('Request timeout', 408);
      }

      // Network error or other fetch errors
      throw new HttpError(
        error instanceof Error ? error.message : 'Network error',
        0
      );
    }
  }
}

/**
 * Default HTTP client instance
 */
export const httpClient = new HttpClient();
