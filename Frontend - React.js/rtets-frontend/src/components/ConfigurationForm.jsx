import React, { useState } from 'react'
import { updateConfig } from '../services/api.js'


function ConfigurationForm(){
  // State for form fields
  const [formData, setFormData] = useState({
    maxTicketCapacity: '',
    totalTickets: '',
    initialTicketsInPool: '',
    ticketReleaseRate: '',
    customerRetrievalRate: '',
    vendorCount: '',
    customerCount: ''
  })

  // Messages to show success or error to user
  const [message, setMessage] = useState('')
  const [errorMessage, setErrorMessage] = useState('')


  // Handle changes in input fields
  const handleChange = (e) => {
    const { name, value } = e.target

    // Ensure user inputs only digits or empty
    if (value === '' || /^[0-9]+$/.test(value)){
      setFormData(prev => ({ ...prev, [name]: value }))
      setErrorMessage('') 
    } 
    else{
      // Show error if user enters non-digit characters
      setErrorMessage('Enter positive integers only. Symbols/letters not allowed.')
    }
  }


  // Handle saving configuration
  const handleSave = async () => {
    // Convert strings to integers before sending to backend
    const configValues = {
      maxTicketCapacity: parseInt(formData.maxTicketCapacity || '0',10),
      totalTickets: parseInt(formData.totalTickets||'0',10),
      initialTicketsInPool: parseInt(formData.initialTicketsInPool||'0',10),
      ticketReleaseRate: parseInt(formData.ticketReleaseRate||'0',10),
      customerRetrievalRate: parseInt(formData.customerRetrievalRate||'0',10),
      vendorCount: parseInt(formData.vendorCount||'0',10),
      customerCount: parseInt(formData.customerCount||'0',10)
    }


    // Validate that values are positive integers (except initialTicketsInPool can be zero)
    for (let key in configValues){
      
      // For most fields, must be > 0
      if (configValues[key] <= 0 && key !== 'initialTicketsInPool'){
        setErrorMessage(`Invalid value for ${key}. Must be positive integer.`)
        return
      }
      
      // For initialTicketsInPool, must be >= 0
      if (key === 'initialTicketsInPool' && configValues[key] < 0){
        setErrorMessage(`Invalid value for ${key}. Must be a non-negative integer.`)
        return
      }
    }

    try{
      // Send updated configuration to backend
      const res = await updateConfig(configValues)
      setMessage(res)
      setErrorMessage('')
    } 
    catch (e){
      console.error(e)
      setErrorMessage('Failed to save configuration.')
    }
  }


  // Render the configuration form
  return(
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
