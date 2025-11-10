import Head from 'next/head'
import Link from 'next/link'

export default function Custom500() {
  return (
    <>
      <Head>
        <title>500 - Internal Server Error</title>
        <meta name="description" content="Internal server error" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
      </Head>
      <main style={styles.main}>
        <div style={styles.container}>
          <h1 style={styles.title}>500</h1>
          <p style={styles.description}>
            Something went wrong on our end. Please try again later.
          </p>
          <Link href="/" style={styles.link}>
            Go back home
          </Link>
        </div>
      </main>
    </>
  )
}

const styles = {
  main: {
    display: 'flex',
    flexDirection: 'column' as const,
    justifyContent: 'center',
    alignItems: 'center',
    padding: '6rem',
    minHeight: '100vh',
    fontFamily: 'system-ui, sans-serif',
    backgroundColor: '#000',
    color: '#fff',
  },
  container: {
    textAlign: 'center' as const,
    maxWidth: '600px',
  },
  title: {
    fontSize: '6rem',
    fontWeight: 700,
    margin: 0,
    background: 'linear-gradient(to right, #ff4444, #ff0000)',
    WebkitBackgroundClip: 'text',
    WebkitTextFillColor: 'transparent',
  },
  description: {
    fontSize: '1.5rem',
    marginTop: '1rem',
    marginBottom: '2rem',
    color: '#999',
  },
  link: {
    display: 'inline-block',
    padding: '0.75rem 1.5rem',
    fontSize: '1rem',
    fontWeight: 600,
    color: '#fff',
    backgroundColor: '#ff4444',
    borderRadius: '8px',
    textDecoration: 'none',
    transition: 'background-color 0.2s',
  },
}
