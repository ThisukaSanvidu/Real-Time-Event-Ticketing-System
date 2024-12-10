import React, { useEffect, useState } from 'react'
import ErrorMessage from './ErrorMessage.jsx'
import { getStatus } from '../services/api.js'


function TicketStatus(){
  const [status, setStatus] = useState('Loading...')
  const [error, setError] = useState('')


  useEffect(() => {
    async function fetchStatus() {
      try{
        const data = await getStatus()
        setStatus(data)
        setError('')
      } 
      catch (err){
        setError(err.message)
      }
    }

    fetchStatus()
    const intervalId = setInterval(fetchStatus, 5000) // Poll status every 5 seconds
    return () => clearInterval(intervalId)
  }, [])


  return(
    <div className="p-3 mb-4 bg-light border rounded">
      <h2>Ticket Pool Status</h2>
      {error ? <ErrorMessage message={error} /> : <p>{status}</p>}
    </div>
  )
}

export default TicketStatus
