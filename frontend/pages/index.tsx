import Head from 'next/head'
import { ChatContainer } from '@/components/chat/ChatContainer'
import { ChatHeader } from '@/components/chat/ChatHeader'
import { ChatMessageList } from '@/components/chat/ChatMessageList'
import { ChatInput } from '@/components/chat/ChatInput'
import { useChat } from '@/hooks/useChat'
import { useChatScroll } from '@/hooks/useChatScroll'

export default function Home() {
  const {
    messages,
    inputValue,
    setInputValue,
    isLoading,
    sendMessage,
  } = useChat();

  const { scrollRef } = useChatScroll(messages);

  return (
    <>
      <Head>
        <title>LepeChat</title>
        <meta name="description" content="AI Chat Assistant powered by OpenAI" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="icon" href="/favicon.ico" />
        <style>{`
          @keyframes pulse {
            0%, 100% {
              opacity: 0.4;
            }
            50% {
              opacity: 1;
            }
          }
        `}</style>
      </Head>

      <ChatContainer>
        <ChatHeader />
        <ChatMessageList
          messages={messages}
          isLoading={isLoading}
          scrollRef={scrollRef}
        />
        <ChatInput
          value={inputValue}
          onChange={setInputValue}
          onSend={sendMessage}
          isLoading={isLoading}
        />
      </ChatContainer>
    </>
  )
}
