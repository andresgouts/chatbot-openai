/**
 * Avatar component for displaying user and assistant icons
 */

import React from 'react';
import { COLORS, UI_CONSTANTS } from '@/utils/constants';

interface AvatarProps {
  type: 'user' | 'assistant';
}

export function Avatar({ type }: AvatarProps): JSX.Element {
  const isUser = type === 'user';

  return (
    <div style={{
      ...styles.avatar,
      backgroundColor: isUser ? COLORS.primary : COLORS.secondary,
    }}>
      <span style={styles.icon}>
        {isUser ? '👤' : '🤖'}
      </span>
    </div>
  );
}

const styles = {
  avatar: {
    width: UI_CONSTANTS.avatarSize,
    height: UI_CONSTANTS.avatarSize,
    borderRadius: '50%',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    flexShrink: 0,
  },
  icon: {
    fontSize: '1.25rem',
  },
} as const;
