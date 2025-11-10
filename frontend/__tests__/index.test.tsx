import { render, screen } from '@testing-library/react'
import Home from '../index'

describe('Home', () => {
  it('renders the welcome heading', () => {
    render(<Home />)

    const heading = screen.getByText(/Welcome to/i)
    expect(heading).toBeInTheDocument()
  })

  it('renders Next.js in the heading', () => {
    render(<Home />)

    const nextJsText = screen.getByText(/Next.js/i)
    expect(nextJsText).toBeInTheDocument()
  })

  it('renders documentation links', () => {
    render(<Home />)

    const docsLink = screen.getByRole('link', { name: /Docs/i })
    expect(docsLink).toBeInTheDocument()
    expect(docsLink).toHaveAttribute('href', 'https://nextjs.org/docs')
  })

  it('renders learn link', () => {
    render(<Home />)

    const learnLink = screen.getByRole('link', { name: /Learn/i })
    expect(learnLink).toBeInTheDocument()
    expect(learnLink).toHaveAttribute('href', 'https://nextjs.org/learn')
  })
})
