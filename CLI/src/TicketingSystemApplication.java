import java.util.ArrayList;  //Used for managing threads in a list
import java.util.List;  //Allows working with lists
import java.util.Scanner;

//Initializing the main class
public class TicketingSystemApplication{

    private Configuration config;  //Holds system configuration
    private TicketPool ticketPool;  //Manages the tickets
    private final List<Thread> threads = new ArrayList<>();  //Stores running threads
    private final Scanner scanner = new Scanner(System.in);
    private boolean configurationLoaded = false;  //Flag to check if configuration was loaded

    //Main method to start the system
    public void start(){

        System.out.println("============================================");
        System.out.println("===== Real-Time Event Ticketing System =====");
        System.out.println("============================================");

        config = new Configuration();

        //Check if user wants to load a pre-saved configuration
        if (promptYesNo("Load existing configuration?")){

            String filename = prompt("Enter configuration filename:");
            Configuration loadedConfig = Configuration.loadConfiguration(filename);

            if (loadedConfig != null){   //If loading suceeds
                config = loadedConfig;   //Use the loaded configuration
                configurationLoaded = true;  //set flag to true
            }
            else{
                System.out.println("Configuration load failed! Continuing with a new configuration.");
                config.configure(scanner);  //Go back to manual configuration
            }
        }
        else{
            config.configure(scanner);  //Configure the system manually
        }

        // Optionnally save the configuration if it was not loaded
        if (!configurationLoaded && promptYesNo("Save current configuration?")){

            String filename = prompt("Enter filename to save configuration (eg; filename.json):");
            config.saveConfiguration(filename);
        }

        //Initialize the ticket pool with the configuration
        ticketPool = new TicketPool(config.getMaxTicketCapacity(), config.getTotalTickets(), config.getInitialTicketsInPool());

        System.out.println("\nCommands: start, status, stop, exit");
        handleCommands();
    }

    //Handles user commands in a loop
    private void handleCommands(){

        while (true){

            String command = scanner.nextLine().trim().toLowerCase();

            switch (command){
                case "start":
                    startThreads();  //Start vendors and customers threads
                    break;
                case "status":
                    displayStatus();  //Display current system status
                    break;
                case "stop":
                    stopThreads();  //Stop all running threads
                    break;
                case "exit":
                    stopThreads();
                    System.out.println("Exiting system.");
                    return;  //Exit the loop
                default:
                    System.out.println("Invalid command input! Enter one command (start/status/stop/exit)");
            }
        }
    }

    //Starts vendor and customer threads
    private void startThreads(){

        if (!threads.isEmpty()){  //Check if threads are already running
            System.out.println("System is running in progress.");
            return;
        }

        //Distribute remaining tickets among vendors
        int remainingTickets = config.getTotalTickets() - config.getInitialTicketsInPool();  //Tickets not yet added to the pool
        int ticketsPerVendor = remainingTickets / config.getVendorCount();  //Evenly distribute tickets among vendors
        int extraTickets = remainingTickets % config.getVendorCount();  //Handle any remainder tickets

        // Start vendor threads
        for (int i = 1; i <= config.getVendorCount(); i++){
            //Assign extra tickets to the last vendor
            int ticketsToRelease = ticketsPerVendor + (i == config.getVendorCount() ? extraTickets : 0);
            //Create and star a vendor thread
            Thread vendorThread = new Thread(new Vendor(ticketPool, config.getTicketReleaseRate(), i, ticketsToRelease), "Vendor-" + i);
            threads.add(vendorThread);  //Add the thread to the list
            vendorThread.start();
        }

        // Start customer threads
        for (int i = 1; i <= config.getCustomerCount(); i++){
            //Create and start a customer thread
            Thread customerThread = new Thread(new Customer(ticketPool, config.getCustomerRetrievalRate(), i), "Customer-" + i);
            threads.add(customerThread);
            customerThread.start();
        }

        System.out.println(config.getVendorCount() + " vendor(s) and " + config.getCustomerCount() + " customer(s) threads started.");
    }

    //Stops all active threads
    private void stopThreads(){
        //If no threads are running
        if (threads.isEmpty()){
            System.out.println("System is not running.");
            return;
        }

        //Interrupt all threads
        for (Thread thread : threads){
            thread.interrupt();
        }

        threads.clear();  //Clear the list of threads

        System.out.println("All vendors and customers stopped.");
    }

    //Displays the current stats of the system
    private void displayStatus(){

        System.out.println("========== Current System Status ==========");
        System.out.println("Maximum Ticket Pool Capacity: " + config.getMaxTicketCapacity());
        System.out.println("Total Tickets available: " + config.getTotalTickets());
        System.out.println("Total Tickets Added to the Pool: " + ticketPool.getTotalTicketsAdded());
        System.out.println("Total Tickets Sold: " + ticketPool.getTotalTicketsSold());
        System.out.println("===========================================");
    }

    //Prompts the user for a yes/no input
    private boolean promptYesNo(String question){

        while (true){
            System.out.print(question + " (yes/no): ");
            String response = scanner.nextLine().trim().toLowerCase();
            if ("yes".equals(response)) return true;
            if ("no".equals(response)) return false;
            System.out.println("Invalid input! Please enter (yes/no)");  //Prompt again for invalid input
        }
    }

    //Prompts the user for input with a question
    private String prompt(String question){

        System.out.print(question + " ");  //Display the question
        return scanner.nextLine().trim();  //return the user's input
    }

    //Main entry point of the system
    public static void main(String[] args){

        new TicketingSystemApplication().start();  //Create an instance and start the system
    }
}
