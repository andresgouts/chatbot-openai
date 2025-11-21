/**
 * SVG icon path data
 * Centralized storage for all icon SVG paths and viewBox dimensions
 */

export interface IconPathData {
  viewBox: string;
  path: string;
}

export interface IconPaths {
  send: IconPathData;
  chatBubble: IconPathData;
}

/**
 * Icon path definitions
 * Each icon includes viewBox dimensions and SVG path data
 */
export const ICON_PATHS: IconPaths = {
  /**
   * Paper plane icon for sending messages
   * Simple, recognizable send icon pointing up-right
   */
  send: {
    viewBox: '0 0 24 24',
    path: 'M2.01 21L23 12 2.01 3 2 10l15 2-15 2z',
  },
  /**
   * Chat bubble icon for branding
   * Rounded speech bubble with smiley face
   */
  chatBubble: {
    viewBox: '0 0 24 24',
    path: 'M20 2H4c-1.1 0-2 .9-2 2v18l4-4h14c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zm-7 9h-2V5h2v6zm0 4h-2v-2h2v2z',
  },
} as const;
