/**
 * ChatBubbleIcon component
 * Chat bubble icon for branding
 */

import React from 'react';
import { Icon } from './Icon';

export interface ChatBubbleIconProps {
  size?: string | number;
  color?: string;
  className?: string;
  style?: React.CSSProperties;
}

export function ChatBubbleIcon({
  size = 32,
  color = '#0070f3',
  className,
  style,
}: ChatBubbleIconProps): JSX.Element {
  return (
    <Icon
      name="chatBubble"
      size={size}
      color={color}
      className={className}
      style={style}
    />
  );
}
