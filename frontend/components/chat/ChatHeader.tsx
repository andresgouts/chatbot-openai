/**
 * ChatHeader component
 * Displays the application title with gradient styling
 */

import React from 'react';
import { COLORS, GRADIENTS } from '@/utils/constants';

export function ChatHeader(): JSX.Element {
  return (
    <header style={styles.header}>
      <h1 style={styles.title}>
        <span style={styles.titleText}>LepeThat</span>
      </h1>
    </header>
  );
}

const styles = {
  header: {
    padding: '1rem 2rem',
    borderBottom: `1px solid ${COLORS.border}`,
    backgroundColor: 'rgba(255, 255, 255, 0.02)',
  },
  title: {
    margin: 0,
    fontSize: '1.5rem',
    fontWeight: 600,
    textAlign: 'center' as const,
  },
  titleText: {
    background: GRADIENTS.primary,
    WebkitBackgroundClip: 'text',
    WebkitTextFillColor: 'transparent',
  },
} as const;
