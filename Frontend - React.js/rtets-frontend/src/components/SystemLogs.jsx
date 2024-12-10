import React, { useEffect, useState } from 'react'
import { getLogs } from '../services/api.js'


function SystemLogs(){
  const [logs, setLogs] = useState([])

  useEffect(() => {
    const fetchLogs = async () => {
      try{
        const data = await getLogs()
        setLogs(data)
      } 
      catch (e){
        console.error("Failed to fetch logs:", e)
      }
    }

    // Initial fetch
    fetchLogs()

    // Poll logs every 3 seconds
    const interval = setInterval(fetchLogs, 3000)
    return () => clearInterval(interval)
  }, [])


  return(
    <div className="p-3 bg-light border rounded mt-4">
      <h2>System Logs</h2>
      <ul className="list-group">
        {logs.map((log, index) => (
          <li key={index} className="list-group-item">{log}</li>
        ))}
      </ul>
    </div>
  )
}

export default SystemLogs
