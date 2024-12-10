import axios from 'axios'


// Base backend URL
const BASE_URL = 'http://localhost:8080'


// Get current system status from backend
export async function getStatus(){

  const response = await axios.get(`${BASE_URL}/api/status`)
  return response.data
}

// Start the ticketing system
export async function startSystem(){

  const response = await axios.post(`${BASE_URL}/api/start`)
  return response.data
}

// Stop the ticketing system
export async function stopSystem(){

  const response = await axios.post(`${BASE_URL}/api/stop`)
  return response.data
}

// Update configuration on backend
export async function updateConfig(configValues){

  const response = await axios.post(`${BASE_URL}/api/config`, configValues)
  return response.data
}

// Fetch system logs
export async function getLogs(){
  
  const response = await axios.get(`${BASE_URL}/api/logs`)
  return response.data
}
