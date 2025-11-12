/**
 * useChatScroll hook
 * Automatically scrolls to the bottom of the chat when new messages arrive
 */

import { useEffect, useRef } from 'react';
import { Message } from '@/services/types';

interface UseChatScrollReturn {
  scrollRef: React.RefObject<HTMLDivElement>;
}

/**
 * Hook that provides auto-scroll functionality for chat messages
 * @param messages - Array of messages to monitor for changes
 * @returns Object with scrollRef to attach to the scroll target element
 */
export function useChatScroll(messages: Message[]): UseChatScrollReturn {
  const scrollRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (scrollRef.current && scrollRef.current.scrollIntoView) {
      scrollRef.current.scrollIntoView({ behavior: 'smooth' });
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [messages.length]); // Only trigger on message count change

  return { scrollRef };
}
