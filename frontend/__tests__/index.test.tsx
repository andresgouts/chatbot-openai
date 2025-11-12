import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import Home from '../pages/index'

// Mock the chat API
jest.mock('@/services/api/chatApi', () => ({
  chatApi: {
    sendMessage: jest.fn(),
  },
}))

import { chatApi } from '@/services/api/chatApi'

describe('Home (LepeThat Chat Interface)', () => {
  beforeEach(() => {
    jest.clearAllMocks()
  })

  it('renders the LepeThat title', () => {
    render(<Home />)

    const heading = screen.getByRole('heading', { name: /LepeThat/i })
    expect(heading).toBeInTheDocument()
  })

  it('renders the chat input and send button', () => {
    render(<Home />)

    const textarea = screen.getByPlaceholderText(/Type your message/i)
    const button = screen.getByRole('button', { name: /send/i })

    expect(textarea).toBeInTheDocument()
    expect(button).toBeInTheDocument()
  })

  it('displays empty state message initially', () => {
    render(<Home />)

    const emptyState = screen.getByText(/Start a conversation with LepeThat/i)
    expect(emptyState).toBeInTheDocument()
  })

  it('sends a message when send button is clicked', async () => {
    const mockSendMessage = chatApi.sendMessage as jest.Mock
    mockSendMessage.mockResolvedValue({
      response: 'Hello from AI!',
      model: 'gpt-3.5-turbo',
    })

    const user = userEvent.setup()
    render(<Home />)

    const textarea = screen.getByPlaceholderText(/Type your message/i)
    const button = screen.getByRole('button', { name: /send/i })

    // Type a message
    await user.type(textarea, 'Hello, AI!')

    // Click send
    await user.click(button)

    // Verify the API was called
    expect(mockSendMessage).toHaveBeenCalledWith('Hello, AI!')

    // Wait for the user message to appear
    await waitFor(() => {
      expect(screen.getByText('Hello, AI!')).toBeInTheDocument()
    })

    // Wait for the AI response to appear
    await waitFor(() => {
      expect(screen.getByText('Hello from AI!')).toBeInTheDocument()
    })
  })

  it('disables input and button while loading', async () => {
    const mockSendMessage = chatApi.sendMessage as jest.Mock
    mockSendMessage.mockImplementation(
      () => new Promise((resolve) => setTimeout(() => resolve({ response: 'Response', model: 'gpt-3.5-turbo' }), 100))
    )

    const user = userEvent.setup()
    render(<Home />)

    const textarea = screen.getByPlaceholderText(/Type your message/i) as HTMLTextAreaElement
    const button = screen.getByRole('button', { name: /send/i })

    // Type and send a message
    await user.type(textarea, 'Test message')
    await user.click(button)

    // Check that input and button are disabled
    expect(textarea.disabled).toBe(true)
    expect(button).toBeDisabled()

    // Wait for the request to complete
    await waitFor(() => {
      expect(textarea.disabled).toBe(false)
    })
  })

  it('displays error message when API call fails', async () => {
    const mockSendMessage = chatApi.sendMessage as jest.Mock
    mockSendMessage.mockRejectedValue(new Error('Network error'))

    const user = userEvent.setup()
    render(<Home />)

    const textarea = screen.getByPlaceholderText(/Type your message/i)
    const button = screen.getByRole('button', { name: /send/i })

    // Type and send a message
    await user.type(textarea, 'Test message')
    await user.click(button)

    // Wait for error message to appear
    await waitFor(
      () => {
        expect(screen.getByText(/Failed to send message/i)).toBeInTheDocument()
      },
      { timeout: 3000 }
    )
  })

  it('clears input after sending message', async () => {
    const mockSendMessage = chatApi.sendMessage as jest.Mock
    mockSendMessage.mockResolvedValue({
      response: 'Response',
      model: 'gpt-3.5-turbo',
    })

    const user = userEvent.setup()
    render(<Home />)

    const textarea = screen.getByPlaceholderText(/Type your message/i) as HTMLTextAreaElement
    const button = screen.getByRole('button', { name: /send/i })

    // Type and send a message
    await user.type(textarea, 'Test message')
    expect(textarea.value).toBe('Test message')

    await user.click(button)

    // Input should be cleared
    await waitFor(() => {
      expect(textarea.value).toBe('')
    })
  })
})
