// src/services/api.js
import axios from 'axios'

const BASE_URL = 'http://localhost:8080'

export async function getStatus() {
  const response = await axios.get(`${BASE_URL}/api/status`)
  return response.data
}

export async function startSystem() {
  const response = await axios.post(`${BASE_URL}/api/start`)
  return response.data
}

export async function stopSystem() {
  const response = await axios.post(`${BASE_URL}/api/stop`)
  return response.data
}

export async function updateConfig(configValues) {
  const response = await axios.post(`${BASE_URL}/api/config`, configValues)
  return response.data
}

// Fetch logs
export async function getLogs() {
  const response = await axios.get(`${BASE_URL}/api/logs`)
  return response.data
}
