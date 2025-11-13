/**
 * Base Icon component
 * Reusable SVG icon renderer with consistent styling and accessibility
 */

import React from 'react';
import { ICON_PATHS, IconPaths } from '@/utils/iconPaths';

export interface IconProps {
  name: keyof IconPaths;
  size?: string | number;
  color?: string;
  className?: string;
  ariaLabel?: string;
  onClick?: () => void;
  style?: React.CSSProperties;
}

export function Icon({
  name,
  size = 24,
  color = 'currentColor',
  className,
  ariaLabel,
  onClick,
  style,
}: IconProps): JSX.Element {
  const iconData = ICON_PATHS[name];

  if (!iconData) {
    console.warn(`Icon "${name}" not found in ICON_PATHS`);
    return <></>;
  }

  const sizeValue = typeof size === 'number' ? `${size}px` : size;
  const role = onClick ? 'button' : 'img';

  return (
    <svg
      width={sizeValue}
      height={sizeValue}
      viewBox={iconData.viewBox}
      xmlns="http://www.w3.org/2000/svg"
      className={className}
      style={style}
      role={role}
      aria-label={ariaLabel}
      onClick={onClick}
    >
      {ariaLabel && <title>{ariaLabel}</title>}
      <path d={iconData.path} fill={color} />
    </svg>
  );
}
