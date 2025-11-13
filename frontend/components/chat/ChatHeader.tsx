/**
 * ChatHeader component
 * Displays the application title with gradient styling
 */

import React from 'react';
import { COLORS } from '@/utils/constants';
import { Logo } from '@/components/branding';

export function ChatHeader(): JSX.Element {
  return (
    <header style={styles.header}>
      <h1 style={styles.heading}>
        <Logo size="medium" />
      </h1>
    </header>
  );
}

const styles = {
  header: {
    padding: '1rem 2rem',
    borderBottom: `1px solid ${COLORS.border}`,
    backgroundColor: 'rgba(255, 255, 255, 0.02)',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
  },
  heading: {
    margin: 0,
    fontWeight: 'inherit',
  },
} as const;
