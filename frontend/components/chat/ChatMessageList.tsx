/**
 * ChatMessageList component
 * Displays the scrollable list of chat messages
 */

import React from 'react';
import { ChatMessage } from './ChatMessage';
import { TypingIndicator } from './TypingIndicator';
import { Message } from '@/services/types';
import { COLORS } from '@/utils/constants';

interface ChatMessageListProps {
  messages: Message[];
  isLoading: boolean;
  scrollRef: React.RefObject<HTMLDivElement>;
}

export function ChatMessageList({
  messages,
  isLoading,
  scrollRef,
}: ChatMessageListProps): JSX.Element {
  return (
    <div style={styles.container}>
      {messages.length === 0 && (
        <div style={styles.emptyState}>
          <p style={styles.emptyText}>Start a conversation with LepeThat</p>
        </div>
      )}

      {messages.map((message) => (
        <ChatMessage key={message.id} message={message} />
      ))}

      {isLoading && <TypingIndicator />}

      <div ref={scrollRef} />
    </div>
  );
}

const styles = {
  container: {
    flex: 1,
    overflowY: 'auto' as const,
    padding: '2rem',
    display: 'flex',
    flexDirection: 'column' as const,
  },
  emptyState: {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    height: '100%',
  },
  emptyText: {
    fontSize: '1.25rem',
    color: COLORS.textSecondary,
  },
} as const;
