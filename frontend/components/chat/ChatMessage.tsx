/**
 * ChatMessage component
 * Displays a single message bubble with avatar
 */

import React from 'react';
import { Avatar } from '@/components/common/Avatar';
import { Message } from '@/services/types';
import { COLORS, UI_CONSTANTS } from '@/utils/constants';

interface ChatMessageProps {
  message: Message;
}

export function ChatMessage({ message }: ChatMessageProps): JSX.Element {
  const isUser = message.role === 'user';

  return (
    <div style={isUser ? styles.userContainer : styles.assistantContainer}>
      {!isUser && <Avatar type="assistant" />}
      <div style={{
        ...styles.bubble,
        ...(message.isError && styles.errorBubble),
      }}>
        <p style={styles.content}>{message.content}</p>
      </div>
      {isUser && <Avatar type="user" />}
    </div>
  );
}

const styles = {
  userContainer: {
    display: 'flex',
    flexDirection: 'row-reverse' as const,
    alignItems: 'flex-start',
    gap: '0.75rem',
    marginBottom: '1rem',
  },
  assistantContainer: {
    display: 'flex',
    flexDirection: 'row' as const,
    alignItems: 'flex-start',
    gap: '0.75rem',
    marginBottom: '1rem',
  },
  bubble: {
    maxWidth: UI_CONSTANTS.messageMaxWidth,
    padding: '0.75rem 1rem',
    borderRadius: '10px',
    backgroundColor: COLORS.messageBg,
    border: `1px solid ${COLORS.border}`,
  },
  errorBubble: {
    borderColor: COLORS.error,
    backgroundColor: 'rgba(255, 68, 68, 0.1)',
  },
  content: {
    margin: 0,
    fontSize: '1rem',
    lineHeight: 1.5,
    color: COLORS.text,
    whiteSpace: 'pre-wrap' as const,
    wordBreak: 'break-word' as const,
  },
} as const;
