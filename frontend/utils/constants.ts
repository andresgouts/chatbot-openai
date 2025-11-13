/**
 * Application-wide constants
 * Design tokens and configuration values
 */

/**
 * Color palette for the application
 * Based on existing design system from pages/index.tsx and 404.tsx
 */
export const COLORS = {
  background: '#000',
  text: '#fff',
  textSecondary: '#999',
  primary: '#0070f3',
  secondary: '#00d4ff',
  error: '#ff4444',
  messageBg: 'rgba(255, 255, 255, 0.05)',
  border: 'rgba(255, 255, 255, 0.1)',
  codeBg: 'rgba(255, 255, 255, 0.1)',
} as const;

/**
 * Gradient definitions for accent text
 */
export const GRADIENTS = {
  primary: 'linear-gradient(to right, #0070f3, #00d4ff)',
  error: 'linear-gradient(to right, #ff4444, #ff0000)',
} as const;

/**
 * API configuration
 */
export const API_CONFIG = {
  baseUrl: process.env.NODE_ENV === 'production' ? '' : 'http://localhost:8080', // Point to backend in dev mode
  endpoints: {
    chat: '/api/chat',
  },
  timeout: 60000, // 60 seconds (matches backend OpenAI timeout)
} as const;

/**
 * UI constants
 */
export const UI_CONSTANTS = {
  maxMessageLength: 4000, // Matches backend validation
  messageMaxWidth: '70%',
  avatarSize: '2.5rem',
  inputMinHeight: '3rem',
  inputMaxHeight: '10rem',
} as const;

/**
 * Branding constants
 */
export const BRANDING = {
  appName: 'LepeChat',
  tagline: 'AI Chat Assistant powered by OpenAI',
} as const;
