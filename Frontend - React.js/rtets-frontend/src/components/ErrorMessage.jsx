import React from 'react'


function ErrorMessage({ message }){
  // Display a red alert box for error
  return(
    <div className="alert alert-danger mt-2" role="alert">
      <strong>Error:</strong> {message}
    </div>
  )
}

export default ErrorMessage
