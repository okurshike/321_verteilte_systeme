import { useEffect, useState } from 'react'

// Alle Requests gehen über das Gateway, React kennt die Ports der Services nicht mehr
const GATEWAY = 'http://localhost:8080/api'

export default function App() {
  const [result, setResult] = useState(null)
  const [books, setBooks] = useState([])
  const [error, setError] = useState(false)
  const [title, setTitle] = useState('')
  const [author, setAuthor] = useState('')

  const getJson = (path, options) => fetch(GATEWAY + path, options).then(response => {
    if (!response.ok) throw new Error(`HTTP ${response.status}`)
    return response.json()
  })

  const loadBooks = () => getJson('/books').then(setBooks).catch(() => setError(true))

  useEffect(() => {
    const controller = new AbortController()
    getJson('/call', { signal: controller.signal })
      .then(setResult)
      .catch(err => { if (err.name !== 'AbortError') setError(true) })
    loadBooks()
    return () => controller.abort()
  }, [])

  const addBook = event => {
    event.preventDefault()
    getJson('/books', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ id: books.length + 1, title, author }),
    }).then(() => { setTitle(''); setAuthor(''); loadBooks() })
      .catch(() => setError(true))
  }

  return <main>
    <h1>M321 – Verteilte Systeme</h1>
    <p>React → Gateway → Service-02 → Eureka → Service-01</p>
    {error && <p role="alert">Das Gateway ist nicht erreichbar. Starte zuerst Eureka, beide Services und das Gateway.</p>}
    {!error && !result && <p>Antwort wird geladen …</p>}
    {result && <section>
      <h2>Antwort von Service-02</h2>
      <p>{result.originalData?.name}</p>
      <p>{result.message}</p>
      <small>{result.timestamp}</small>
    </section>}
    <section>
      <h2>Bücher (über Feign)</h2>
      <ul>{books.map(book => <li key={book}>{book}</li>)}</ul>
      <form onSubmit={addBook}>
        <label>Titel <input value={title} onChange={e => setTitle(e.target.value)} required /></label>
        <label>Autor <input value={author} onChange={e => setAuthor(e.target.value)} required /></label>
        <button>Buch hinzufügen</button>
      </form>
    </section>
  </main>
}
