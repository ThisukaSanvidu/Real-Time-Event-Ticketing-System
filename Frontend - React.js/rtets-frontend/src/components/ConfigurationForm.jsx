// src/components/ConfigurationForm.jsx
import React, { useState } from 'react'
import { updateConfig } from '../services/api.js'

function ConfigurationForm() {
  const [formData, setFormData] = useState({
    maxTicketCapacity: '',
    totalTickets: '',
    initialTicketsInPool: '',
    ticketReleaseRate: '',
    customerRetrievalRate: '',
    vendorCount: '',
    customerCount: ''
  })
  const [message, setMessage] = useState('')
  const [errorMessage, setErrorMessage] = useState('')

  const handleChange = (e) => {
    const { name, value } = e.target
    // Ensure numeric only
    if (value === '' || /^[0-9]+$/.test(value)) {
      setFormData(prev => ({ ...prev, [name]: value }))
      setErrorMessage('') 
    } else {
      setErrorMessage('Enter positive integers only. Symbols/letters not allowed.')
    }
  }

  const handleSave = async () => {
    // Convert formData fields to numbers before sending
    const configValues = {
      maxTicketCapacity: parseInt(formData.maxTicketCapacity || '0',10),
      totalTickets: parseInt(formData.totalTickets||'0',10),
      initialTicketsInPool: parseInt(formData.initialTicketsInPool||'0',10),
      ticketReleaseRate: parseInt(formData.ticketReleaseRate||'0',10),
      customerRetrievalRate: parseInt(formData.customerRetrievalRate||'0',10),
      vendorCount: parseInt(formData.vendorCount||'0',10),
      customerCount: parseInt(formData.customerCount||'0',10)
    }

    // Validation check: ensure positive integers
    for (let key in configValues) {
      if (configValues[key] <= 0 && key !== 'initialTicketsInPool') {
        setErrorMessage(`Invalid value for ${key}. Must be positive integer.`)
        return
      }
      if (key === 'initialTicketsInPool' && configValues[key] < 0) {
        setErrorMessage(`Invalid value for ${key}. Must be a non-negative integer.`)
        return
      }
    }

    try {
      const res = await updateConfig(configValues)
      setMessage(res)
      setErrorMessage('')
    } catch (e) {
      console.error(e)
      setErrorMessage('Failed to save configuration.')
    }
  }

  return (
    <div className="p-3 bg-light border rounded">
      <h2>Configuration Settings</h2>
      {errorMessage && <div className="alert alert-danger">{errorMessage}</div>}
      {message && <div className="alert alert-success">{message}</div>}
      
      <div className="mb-3">
        <label>Maximum Ticket Capacity:</label>
        <input type="text" className="form-control"
          name="maxTicketCapacity"
          value={formData.maxTicketCapacity}
          onChange={handleChange} />
      </div>
      <div className="mb-3">
        <label>Total Tickets:</label>
        <input type="text" className="form-control"
          name="totalTickets"
          value={formData.totalTickets}
          onChange={handleChange} />
      </div>
      <div className="mb-3">
        <label>Initial Tickets in Pool:</label>
        <input type="text" className="form-control"
          name="initialTicketsInPool"
          value={formData.initialTicketsInPool}
          onChange={handleChange} />
      </div>
      <div className="mb-3">
        <label>Ticket Release Rate (ms):</label>
        <input type="text" className="form-control"
          name="ticketReleaseRate"
          value={formData.ticketReleaseRate}
          onChange={handleChange} />
      </div>
      <div className="mb-3">
        <label>Customer Retrieval Rate (ms):</label>
        <input type="text" className="form-control"
          name="customerRetrievalRate"
          value={formData.customerRetrievalRate}
          onChange={handleChange} />
      </div>
      <div className="mb-3">
        <label>Number of Vendors:</label>
        <input type="text" className="form-control"
          name="vendorCount"
          value={formData.vendorCount}
          onChange={handleChange} />
      </div>
      <div className="mb-3">
        <label>Number of Customers:</label>
        <input type="text" className="form-control"
          name="customerCount"
          value={formData.customerCount}
          onChange={handleChange} />
      </div>
      <button className="btn btn-primary" onClick={handleSave}>Save Configuration</button>
    </div>
  )
}

export default ConfigurationForm
