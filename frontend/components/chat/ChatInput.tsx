/**
 * ChatInput component
 * Provides textarea input and send button for composing messages
 */

import React from 'react';
import { COLORS, UI_CONSTANTS } from '@/utils/constants';
import { SendIcon } from '@/components/icons';

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
  const textareaRef = React.useRef<HTMLTextAreaElement>(null);

  const handleKeyDown = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      if (!isLoading && value.trim()) {
        onSend();
      }
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    onChange(e.target.value);

    // Auto-grow textarea
    if (textareaRef.current) {
      textareaRef.current.style.height = 'auto';
      textareaRef.current.style.height = `${textareaRef.current.scrollHeight}px`;
    }
  };

  // Reset height when value is cleared
  React.useEffect(() => {
    if (textareaRef.current && !value) {
      textareaRef.current.style.height = 'auto';
    }
  }, [value]);

  const isDisabled = isLoading || !value.trim();

  return (
    <div style={styles.container}>
      <div style={styles.inputWrapper}>
        <textarea
          ref={textareaRef}
          value={value}
          onChange={handleChange}
          onKeyDown={handleKeyDown}
          placeholder="Type a message..."
          disabled={isLoading}
          maxLength={UI_CONSTANTS.maxMessageLength}
          aria-label="Chat message input"
          aria-describedby="input-hint"
          rows={1}
          style={{
            ...styles.textarea,
            ...(isLoading && styles.textareaDisabled),
          }}
        />
        <button
          onClick={onSend}
          disabled={isDisabled}
          aria-label="Send message"
          style={{
            ...styles.iconButton,
            ...(isDisabled && styles.iconButtonDisabled),
          }}
        >
          <SendIcon
            disabled={isDisabled}
            color={COLORS.text}
          />
        </button>
      </div>
    </div>
  );
}

const styles = {
  container: {
    padding: '1rem 2rem',
    borderTop: `1px solid ${COLORS.border}`,
    backgroundColor: 'rgba(255, 255, 255, 0.02)',
  },
  inputWrapper: {
    position: 'relative' as const,
    width: '100%',
  },
  textarea: {
    width: '100%',
    padding: '0.75rem',
    paddingRight: '3rem',
    backgroundColor: COLORS.messageBg,
    border: `1px solid ${COLORS.border}`,
    borderRadius: '10px',
    color: COLORS.text,
    fontSize: '1rem',
    fontFamily: 'system-ui, sans-serif',
    resize: 'none' as const,
    minHeight: 'auto',
    maxHeight: UI_CONSTANTS.inputMaxHeight,
    outline: 'none',
    boxSizing: 'border-box' as const,
    lineHeight: '1.5',
    overflow: 'auto',
  },
  textareaDisabled: {
    opacity: 0.5,
    cursor: 'not-allowed',
  },
  iconButton: {
    position: 'absolute' as const,
    right: '0.5rem',
    bottom: '0.5rem',
    padding: '0.5rem',
    backgroundColor: 'transparent',
    border: 'none',
    borderRadius: '8px',
    cursor: 'pointer',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    transition: 'opacity 0.2s, background-color 0.2s',
  },
  iconButtonDisabled: {
    cursor: 'not-allowed',
  },
} as const;
