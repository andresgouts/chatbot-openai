/**
 * Logo component for LepeChat branding
 * Combines ChatBubbleIcon with "LepeChat" text
 */

import React from 'react';
import { ChatBubbleIcon } from '@/components/icons';
import { COLORS, BRANDING } from '@/utils/constants';

export interface LogoProps {
  size?: 'small' | 'medium' | 'large';
  className?: string;
}

const sizeConfig = {
  small: {
    iconSize: 20,
    fontSize: '1rem',
    gap: '0.375rem',
  },
  medium: {
    iconSize: 32,
    fontSize: '1.5rem',
    gap: '0.5rem',
  },
  large: {
    iconSize: 48,
    fontSize: '2rem',
    gap: '0.75rem',
  },
} as const;

export function Logo({ size = 'medium', className }: LogoProps): JSX.Element {
  const config = sizeConfig[size];

  return (
    <div style={{ ...styles.container, gap: config.gap }} className={className}>
      <ChatBubbleIcon size={config.iconSize} color={COLORS.primary} />
      <span style={{ ...styles.text, fontSize: config.fontSize }}>{BRANDING.appName}</span>
    </div>
  );
}

const styles = {
  container: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
  },
  text: {
    fontWeight: 600,
    color: COLORS.text,
    fontFamily: 'system-ui, sans-serif',
  },
} as const;
