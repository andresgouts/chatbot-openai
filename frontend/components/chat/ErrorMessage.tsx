/**
 * ErrorMessage component
 * Displays error messages as chat bubbles
 */

import React from 'react';
import { Avatar } from '@/components/common/Avatar';
import { COLORS } from '@/utils/constants';

interface ErrorMessageProps {
  error: string | null;
  onDismiss?: () => void;
}

export function ErrorMessage({ error, onDismiss }: ErrorMessageProps): JSX.Element | null {
  if (!error) return null;

  return (
    <div style={styles.container}>
      <Avatar type="assistant" />
      <div style={styles.bubble}>
        <p style={styles.content}>
          ❌ {error}
        </p>
        {onDismiss && (
          <button onClick={onDismiss} style={styles.dismissButton}>
            Dismiss
          </button>
        )}
      </div>
    </div>
  );
}

const styles = {
  container: {
    display: 'flex',
    flexDirection: 'row' as const,
    alignItems: 'flex-start',
    gap: '0.75rem',
    marginBottom: '1rem',
  },
  bubble: {
    maxWidth: '70%',
    padding: '0.75rem 1rem',
    borderRadius: '10px',
    backgroundColor: 'rgba(255, 68, 68, 0.1)',
    border: `1px solid ${COLORS.error}`,
  },
  content: {
    margin: 0,
    fontSize: '1rem',
    lineHeight: 1.5,
    color: COLORS.text,
  },
  dismissButton: {
    marginTop: '0.5rem',
    padding: '0.25rem 0.75rem',
    backgroundColor: 'transparent',
    border: `1px solid ${COLORS.error}`,
    borderRadius: '5px',
    color: COLORS.error,
    fontSize: '0.875rem',
    cursor: 'pointer',
    transition: 'background-color 0.2s',
  },
} as const;
