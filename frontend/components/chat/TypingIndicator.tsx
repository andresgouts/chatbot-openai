/**
 * TypingIndicator component
 * Displays animated dots while AI is generating a response
 */

import React from 'react';
import { Avatar } from '@/components/common/Avatar';
import { COLORS } from '@/utils/constants';

export function TypingIndicator(): JSX.Element {
  return (
    <div style={styles.container}>
      <Avatar type="assistant" />
      <div style={styles.bubble}>
        <div style={styles.dotsContainer}>
          <span style={styles.dot}>•</span>
          <span style={styles.dot}>•</span>
          <span style={styles.dot}>•</span>
        </div>
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
    padding: '0.75rem 1rem',
    borderRadius: '10px',
    backgroundColor: COLORS.messageBg,
    border: `1px solid ${COLORS.border}`,
  },
  dotsContainer: {
    display: 'flex',
    gap: '0.25rem',
  },
  dot: {
    fontSize: '1.5rem',
    color: COLORS.textSecondary,
    animation: 'pulse 1.5s ease-in-out infinite',
  },
} as const;
