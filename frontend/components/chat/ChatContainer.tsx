/**
 * ChatContainer component
 * Provides the full-screen layout wrapper for the chat interface
 */

import React from 'react';
import { COLORS } from '@/utils/constants';

interface ChatContainerProps {
  children: React.ReactNode;
}

export function ChatContainer({ children }: ChatContainerProps): JSX.Element {
  return (
    <main style={styles.container}>
      {children}
    </main>
  );
}

const styles = {
  container: {
    display: 'flex',
    flexDirection: 'column' as const,
    height: '100vh',
    backgroundColor: COLORS.background,
    color: COLORS.text,
    fontFamily: 'system-ui, sans-serif',
  },
} as const;
