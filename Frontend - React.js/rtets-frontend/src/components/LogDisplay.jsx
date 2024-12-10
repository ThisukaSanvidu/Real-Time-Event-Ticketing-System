// src/components/LogDisplay.jsx
import React, { useEffect, useState } from 'react'
import ErrorMessage from './ErrorMessage.jsx'

function LogDisplay() {
  const [logs, setLogs] = useState([])
  const [error, setError] = useState('')

  useEffect(() => {
    async function fetchLogs() {
      try {
        const response = await fetch('http://localhost:8080/api/logs')
        if (!response.ok) {
          throw new Error('Failed to fetch logs')
        }
        const data = await response.json()
        setLogs(data)
        setError('')
      } catch (err) {
        setError(err.message)
      }
    }

    fetchLogs()
    const intervalId = setInterval(fetchLogs, 5000)
    return () => clearInterval(intervalId)
  }, [])

  return (
    <div className="p-3 mb-4 bg-light border rounded">
      <h2>System Logs</h2>
      {error && <ErrorMessage message={error} />}
      {!error && logs.length === 0 && <p>No logs available.</p>}
      {!error && logs.length > 0 && (
        <ul className="list-group">
          {logs.map((log, i) => <li className="list-group-item" key={i}>{log}</li>)}
        </ul>
      )}
    </div>
  )
}

export default LogDisplay
