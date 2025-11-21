/**
 * SendIcon component
 * Paper plane icon for sending messages
 */

import React from 'react';
import { Icon } from './Icon';

export interface SendIconProps {
  size?: string | number;
  color?: string;
  ariaLabel?: string;
  onClick?: () => void;
  disabled?: boolean;
  className?: string;
  style?: React.CSSProperties;
}

export function SendIcon({
  size = 20,
  color,
  ariaLabel = 'Send message',
  onClick,
  disabled = false,
  className,
  style,
}: SendIconProps): JSX.Element {
  const iconStyle: React.CSSProperties = {
    ...style,
    opacity: disabled ? 0.3 : 1,
    cursor: disabled ? 'not-allowed' : onClick ? 'pointer' : 'default',
    transition: 'opacity 0.2s',
  };

  return (
    <Icon
      name="send"
      size={size}
      color={color}
      ariaLabel={ariaLabel}
      onClick={disabled ? undefined : onClick}
      className={className}
      style={iconStyle}
    />
  );
}
