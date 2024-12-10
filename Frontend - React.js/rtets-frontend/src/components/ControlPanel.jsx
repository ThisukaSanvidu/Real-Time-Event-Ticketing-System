import React, { useState } from 'react'
import ErrorMessage from './ErrorMessage.jsx'
import { startSystem, stopSystem } from '../services/api.js'


function ControlPanel(){
  // Messages and errors to display to user
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')


  // Handle "Start System" button
  async function handleStart(){
    setError('')
    setMessage('')

    try{
      await startSystem()
      setMessage('System started successfully.')
    } 
    catch (err){
      setError(err.message)
    }
  }

  // Handle "Stop System" button
  async function handleStop(){
    setError('')
    setMessage('')

    try{
      await stopSystem()
      setMessage('System stopped successfully.')
    } 
    catch (err) {
      setError(err.message)
    }
  }


  return(
    <div className="p-3 mb-4 bg-light border rounded">
      <h2>Control Panel</h2>

      <div>
        <button onClick={handleStart} className="btn btn-primary me-2">Start System</button>
        <button onClick={handleStop} className="btn btn-secondary">Stop System</button>
      </div>

      {message && <p className="text-success fw-bold mt-2">{message}</p>}
      {error && <ErrorMessage message={error} />}
    </div>
  )
}

export default ControlPanel
