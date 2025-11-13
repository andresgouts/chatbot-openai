import type { AppProps } from 'next/app'

export default function App({ Component, pageProps }: AppProps) {
  return (
    <>
      <style jsx global>{`
        html,
        body {
          margin: 0;
          padding: 0;
          overflow: hidden;
          width: 100%;
          height: 100%;
        }
        #__next {
          width: 100%;
          height: 100%;
        }

        /* Custom scrollbar styling */
        * {
          scrollbar-width: thin;
          scrollbar-color: transparent transparent;
        }

        *:hover {
          scrollbar-color: rgba(255, 255, 255, 0.3) transparent;
        }

        /* Webkit browsers (Chrome, Safari, Edge) */
        *::-webkit-scrollbar {
          width: 8px;
          height: 8px;
        }

        *::-webkit-scrollbar-track {
          background: transparent;
        }

        *::-webkit-scrollbar-thumb {
          background-color: transparent;
          border-radius: 4px;
          transition: background-color 0.3s ease;
        }

        *:hover::-webkit-scrollbar-thumb {
          background-color: rgba(255, 255, 255, 0.3);
        }

        *::-webkit-scrollbar-thumb:hover {
          background-color: rgba(255, 255, 255, 0.5);
        }
      `}</style>
      <Component {...pageProps} />
    </>
  )
}
