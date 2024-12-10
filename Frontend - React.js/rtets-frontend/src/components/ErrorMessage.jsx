// src/components/ErrorMessage.jsx
import React from 'react'

function ErrorMessage({ message }) {
  return (
    <div className="alert alert-danger mt-2" role="alert">
      <strong>Error:</strong> {message}
    </div>
  )
}

export default ErrorMessage
