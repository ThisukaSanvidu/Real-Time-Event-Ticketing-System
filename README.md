# Real-Time-Event-Ticketing-System

## Introduction
The Real-Time Event Ticketing System is designed to simulate a real-time environment where multiple vendors (producers) release tickets into a shared ticket pool, and multiple customers (consumers) purchase these tickets. It consists of three main parts:

1. CLI (Command-Line Interface):
   
   A standalone Java console application where you can configure the system, start/stop the simulation, and check status.
   
3. Backend (Spring Boot):

   A web-based backend managing the shared TicketPool, Vendors, Customers, and their transactions, storing data in an H2 in-memory database. It provides RESTful endpoints to update configuration, start/stop the system, view status, and fetch logs.
   
5. Frontend (React.js):
   
   A web UI allowing you to configure parameters, start/stop the system, monitor the ticket pool, and view logs in real-time. This UI communicates with the backend via its RESTful endpoints.



   
***



## Setup Instructions

### Prerequisites:
- IntelliJ IDEA (latest version recommended)
- JDK (17 or above) installed and configured in IntelliJ.
- Maven integrated within IntelliJ (IntelliJ can handle Maven projects automatically).
- Node.js and npm installed on your system for the frontend.
- Gson library: Already handled by Maven dependencies in IntelliJ.
- H2 Database: Embedded, no manual setup required.


### Building and Running the CLI in IntelliJ
1. Open CLI Project in IntelliJ:
   - Import the CLI project folder into IntelliJ as a Java project.
   - Ensure you have a Configuration.java, TicketPool.java, Vendor.java, Customer.java, and TicketingSystemApplication.java in your source package.
     
2. Set Main class:
   - In IntelliJ, go to Run > Edit Configurations...
   - Click + and select Application.
   - Set the Main class to TicketingSystemApplication.
   - Make sure the Project SDK is set to your JDK.
     
3. Run the CLI:
   - Click the Run button (the green arrow) in IntelliJ's toolbar.
   - The console output appears in IntelliJ's Run panel.
   - Follow on-screen prompts to configure, start/stop, and exit the system.
  

### Building and Running the Backend (Spring Boot) in IntelliJ
  1. Open Backend project in IntelliJ:
     - Import the Spring Boot backend project (the one with pom.xml) into IntelliJ. IntelliJ will automatically load dependencies.
       
  2. Set Main class:
     - The main class is RealTimeEventTicketingSystemApplication.java.
     - In IntelliJ, go to Run > Edit Configurations..., click + and select Spring Boot.
Set the Main class to com.thisuka.rtets.RealTimeEventTicketingSystemApplication.

  3. Run the Backend:
     - Click the Run button in IntelliJ's toolbar.
     - The backend starts on http://localhost:8080.
     - Access H2 console at http://localhost:8080/h2-console (username: sa, no password by default).

       #### Backend endpoints:
       - POST /api/start
       - POST /api/stop
       - GET /api/status
       - POST /api/config (to update config)
       - GET /api/logs (to retrieve system logs)


### Building and Running the Frontend (React)
1. Open Frontend project in VS Code:
   - Import the React.js frontend project into VS Code as a folder.
   - In VS Code, go to Terminal > New Terminal.
   - Move into the root directory 'rtets-frontend', using command 'cd rtets-frontend/'.
     
2. Install all dependencies:
   - After moving to root directory 'rtets-frontend', in the terminal type 'npm install' to install all dependencies.
   
3. Run the Frontend:
   - In the terminal, type 'npm run dev' to start the development server
       - The frontend will start on http://localhost:3000.



***



## Usage Instructions

### CLI:
- On starting the CLI application, you can configure the system parameters manually or load a previously saved configuration.
- Commands:
    - start: Starts vendor and customer threads.
    - status: Dispay current system status
    - stop: Stops all running threads.
    - exit: Exit the application


### Backend - Spring Boot:
- The backend automatically starts when you run the main class
- Endpoints:
    - POST /api/start : Start the system.
    - POST /api/stop : Stop the system.
    - GET /api/status : Get current system status.
    - POST /api/config : Update configuration dynamically.
    - GET /api/logs : Fetch system logs.


### Frontend - React.js:
- Access the UI at http://localhost:3000.
- Configuration Settings: Enter positive integers in each field and click "Save Configuration".
- Control Panel: Click "Start System" or "Stop System" to control the simulation.
- Ticket Pool Status: Automatically refreshed every 5 seconds.
- System Logs: Automatically fetched every 3 seconds, showing vendor and customer actions.
