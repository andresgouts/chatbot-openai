import Head from 'next/head'
import Link from 'next/link'

export default function Custom404() {
  return (
    <>
      <Head>
        <title>404 - Page Not Found</title>
        <meta name="description" content="Page not found" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
      </Head>
      <main style={styles.main}>
        <div style={styles.container}>
          <h1 style={styles.title}>404</h1>
          <p style={styles.description}>
            Oops! The page you're looking for doesn't exist.
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
    background: 'linear-gradient(to right, #0070f3, #00d4ff)',
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
    backgroundColor: '#0070f3',
    borderRadius: '8px',
    textDecoration: 'none',
    transition: 'background-color 0.2s',
  },
}
