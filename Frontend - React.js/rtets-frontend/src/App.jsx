import React from 'react'
import 'bootstrap/dist/css/bootstrap.min.css'
import ConfigurationForm from './components/ConfigurationForm.jsx'
import TicketStatus from './components/TicketStatus.jsx'
import ControlPanel from './components/ControlPanel.jsx'
import SystemLogs from './components/SystemLogs.jsx'


function App(){
  // Main App layout: shows configuration, controls, status, and logs
  return(
    <div className="container my-4">
      <h1 className="mb-4">Real-Time Event Ticketing System</h1>
      <ConfigurationForm />
      <ControlPanel />
      <TicketStatus />
      <SystemLogs />
    </div>
  )
}

export default App
