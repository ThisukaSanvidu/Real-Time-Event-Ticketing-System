import java.io.FileReader;  //Used to read configuration files
import java.io.FileWriter;  //Used to write configuration files
import java.io.IOException;  //Handle file i/o exceptions
import java.util.Scanner;
import com.google.gson.Gson;  //Library to handle JSON conversion
import com.google.gson.GsonBuilder;  //For creating formatted JSON


public class Configuration{

    private int maxTicketCapacity;  //Maximum tickets the system can hold
    private int totalTickets;  //Total tickets available in the system
    private int initialTicketsInPool;  //Tracks tickets added to the pool initially
    private int ticketReleaseRate;  //Time interval (milliseconds) for vendors to release tickets
    private int customerRetrievalRate;  //Time interval (milliseconds) for customers to buy tickets
    private int vendorCount;  //Number of vendors in the system
    private int customerCount;  //Number of customers in the system

    //Getters for accessing private variables
    public int getMaxTicketCapacity(){

        return maxTicketCapacity;
    }

    public int getTotalTickets(){

        return totalTickets;
    }

    public int getInitialTicketsInPool(){

        return initialTicketsInPool;
    }

    public int getTicketReleaseRate(){

        return ticketReleaseRate;
    }

    public int getCustomerRetrievalRate(){

        return customerRetrievalRate;
    }

    public int getVendorCount(){

        return vendorCount;
    }

    public int getCustomerCount(){

        return customerCount;
    }

    //Configure the ticketing system based on user input
    public void configure(Scanner scanner){

        System.out.println("============================================");
        System.out.println("===== Real-Time Event Ticketing System =====");
        System.out.println("============================================");

        //Get system parameters from the user
        maxTicketCapacity = promptForPositiveInt(scanner, "Enter the Maximum Ticket Pool Capacity:");
        totalTickets = promptForPositiveInt(scanner, "Enter the Total Tickets available:");

        //Ensure total tickets do not exceed maximum capacity
        while (totalTickets > maxTicketCapacity){
            System.out.println("Total Tickets available cannot exceed the Maximum Ticket Pool Capacity.");
            totalTickets = promptForPositiveInt(scanner, "Enter the Total Tickets available:");
        }

        //Get the number of tickets initially added to the pool
        initialTicketsInPool = promptForNonNegativeInt(scanner, "Enter the amount of Tickets to add to the Pool:");

        while (initialTicketsInPool > totalTickets || initialTicketsInPool > maxTicketCapacity){
            System.out.println("Tickets added cannot exceed Total Tickets or Maximum Ticket Pool Capacity.");
            initialTicketsInPool = promptForNonNegativeInt(scanner, "Enter the amount of Tickets to add to the Pool:");
        }

        //Get ticket release and purchase intervals
        ticketReleaseRate = promptForPositiveInt(scanner, "Enter the Ticket Release Rate (ms):");
        customerRetrievalRate = promptForPositiveInt(scanner, "Enter the Customer Retrieval Rate (ms):");

        //Get the number of vendors and customers
        vendorCount = promptForPositiveInt(scanner, "Enter the Number of Vendors:");
        customerCount = promptForPositiveInt(scanner, "Enter the Number of Customers:");

        System.out.println("Configuration completed.\n");
    }

    //Method to prompt for positive integers
    private int promptForPositiveInt(Scanner scanner, String prompt){
        int value;
        //Loop until valid input in provided
        while (true){
            System.out.print(prompt + " ");
            String input = scanner.nextLine();

            try{
                value = Integer.parseInt(input);  //Converts input to an integer
                if (value > 0) break;  //Ensures the value is positive
                System.out.println("Value must be a positive integer.");
            }
            catch (NumberFormatException e){
                System.out.println("Invalid input! Please enter a positive integer.");
            }
        }
        return value;  //returns the validated input
    }

    //Method to prompt for non-negative integers
    private int promptForNonNegativeInt(Scanner scanner, String prompt){
        int value;
        while (true){
            System.out.print(prompt + " ");
            String input = scanner.nextLine();

            try{
                value = Integer.parseInt(input);
                if (value >= 0) break;
                System.out.println("Value must be a non-negative integer.");
            }
            catch (NumberFormatException e){
                System.out.println("Invalid input! Please enter a non-negative integer.");
            }
        }
        return value;
    }

    //Save the current configuration to a file
    public void saveConfiguration(String filename){

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(filename)){
            gson.toJson(this, writer);
            System.out.println("Saved configuration to " + filename);
        }
        catch (IOException e){
            System.out.println("Failed to save configuration: " + e.getMessage());
        }
    }

    //Load an existing confguration from a file
    public static Configuration loadConfiguration(String filename){

        Gson gson = new Gson();

        try (FileReader reader = new FileReader(filename)){
            Configuration config = gson.fromJson(reader, Configuration.class);
            System.out.println("Loaded configuration from " + filename);
            return config;
        }
        catch (IOException e){
            System.out.println("Failed to load configuration: " + e.getMessage());
            return null;
        }
    }
}
