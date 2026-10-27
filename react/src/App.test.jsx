import { afterEach, expect, test, vi } from 'vitest'
import { cleanup, fireEvent, render, screen, waitFor } from '@testing-library/react'
import App from './App.jsx'

afterEach(() => { cleanup(); vi.unstubAllGlobals() })

const ok = data => Promise.resolve({ ok: true, json: async () => data })

test('zeigt die Antwort von Service-02 und die Bücher über das Gateway', async () => {
  const fetchMock = vi.fn(url => url.endsWith('/call')
    ? ok({
      originalData: { name: 'Hello World from Service 01' },
      message: 'Antwort von service-01 erfolgreich verarbeitet',
      timestamp: '2026-09-23T10:00:00',
    })
    : ok(['1984 (George Orwell)']))
  vi.stubGlobal('fetch', fetchMock)
  render(<App />)
  expect(await screen.findByText('Hello World from Service 01')).toBeInTheDocument()
  expect(await screen.findByText('1984 (George Orwell)')).toBeInTheDocument()
  expect(fetchMock).toHaveBeenCalledWith('http://localhost:8080/api/call', { signal: expect.any(AbortSignal) })
  expect(fetchMock).toHaveBeenCalledWith('http://localhost:8080/api/books', undefined)
})

test('schickt ein neues Buch per POST ans Gateway', async () => {
  const fetchMock = vi.fn(() => ok([]))
  vi.stubGlobal('fetch', fetchMock)
  render(<App />)
  fireEvent.change(screen.getByLabelText('Titel'), { target: { value: 'Harry Potter' } })
  fireEvent.change(screen.getByLabelText('Autor'), { target: { value: 'J.K. Rowling' } })
  fireEvent.click(screen.getByText('Buch hinzufügen'))
  await waitFor(() => expect(fetchMock).toHaveBeenCalledWith('http://localhost:8080/api/books', expect.objectContaining({
    method: 'POST',
    body: JSON.stringify({ id: 1, title: 'Harry Potter', author: 'J.K. Rowling' }),
  })))
})

test('zeigt einen Fehler, wenn das Gateway nicht erreichbar ist', async () => {
  vi.stubGlobal('fetch', vi.fn().mockRejectedValue(new Error('offline')))
  render(<App />)
  expect(await screen.findByRole('alert')).toHaveTextContent('Das Gateway ist nicht erreichbar')
})
