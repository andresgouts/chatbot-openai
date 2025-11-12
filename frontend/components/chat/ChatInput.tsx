/**
 * ChatInput component
 * Provides textarea input and send button for composing messages
 */

import React from 'react';
import { COLORS, UI_CONSTANTS } from '@/utils/constants';

interface ChatInputProps {
  value: string;
  onChange: (value: string) => void;
  onSend: () => void;
  isLoading: boolean;
}

export function ChatInput({
  value,
  onChange,
  onSend,
  isLoading,
}: ChatInputProps): JSX.Element {
  const handleKeyDown = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      if (!isLoading && value.trim()) {
        onSend();
      }
    }
  };

  const isDisabled = isLoading || !value.trim();

  return (
    <div style={styles.container}>
      <textarea
        value={value}
        onChange={(e) => onChange(e.target.value)}
        onKeyDown={handleKeyDown}
        placeholder="Type your message... (Shift+Enter for new line)"
        disabled={isLoading}
        style={{
          ...styles.textarea,
          ...(isLoading && styles.textareaDisabled),
        }}
      />
      <button
        onClick={onSend}
        disabled={isDisabled}
        style={{
          ...styles.button,
          ...(isDisabled && styles.buttonDisabled),
        }}
      >
        Send
      </button>
    </div>
  );
}

const styles = {
  container: {
    display: 'flex',
    gap: '0.75rem',
    padding: '1rem 2rem',
    borderTop: `1px solid ${COLORS.border}`,
    backgroundColor: 'rgba(255, 255, 255, 0.02)',
  },
  textarea: {
    flex: 1,
    padding: '0.75rem',
    backgroundColor: COLORS.messageBg,
    border: `1px solid ${COLORS.border}`,
    borderRadius: '10px',
    color: COLORS.text,
    fontSize: '1rem',
    fontFamily: 'system-ui, sans-serif',
    resize: 'vertical' as const,
    minHeight: UI_CONSTANTS.inputMinHeight,
    maxHeight: UI_CONSTANTS.inputMaxHeight,
    outline: 'none',
  },
  textareaDisabled: {
    opacity: 0.5,
    cursor: 'not-allowed',
  },
  button: {
    padding: '0.75rem 1.5rem',
    backgroundColor: COLORS.primary,
    color: COLORS.text,
    border: 'none',
    borderRadius: '10px',
    fontSize: '1rem',
    fontWeight: 600,
    cursor: 'pointer',
    transition: 'opacity 0.2s',
    alignSelf: 'flex-end',
  },
  buttonDisabled: {
    opacity: 0.5,
    cursor: 'not-allowed',
  },
} as const;
