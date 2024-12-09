import java.util.LinkedList;  //LinkedList is used to manage the ticket queue
import java.util.Queue;  //Queue interface to handle ticket operations

//Manages the ticketpool where tickets are added and removed
public class TicketPool{

    private final Queue<String> tickets = new LinkedList<>();  //Queue to hold tickets
    private final int maxCapacity;  //Maximum number of tickets in the pool
    private final int totalTicketsToRelease;  //Total tickets vendors will release
    private int totalTicketsAdded = 0;  //Tracks tickets added to the pool
    private int totalTicketsSold = 0;  //Tracks tickets sold to customers
    private int ticketCounter = 0; //counter for generating ticket IDs


    //Initialize the ticket pool with max capacity and initial tickets
    public TicketPool(int maxCapacity, int totalTicketsToRelease, int initialTicketsInPool){

        this.maxCapacity = maxCapacity;
        this.totalTicketsToRelease = totalTicketsToRelease;


        //Add initial tickets to pool
        for (int i = 0; i < initialTicketsInPool; i++){
            tickets.add(generateNextTicket());
        }

        totalTicketsAdded = initialTicketsInPool;

    }

    //Add a new ticket to the pool
    public synchronized void addTickets(String ticket){

        tickets.add(ticket);
        totalTicketsAdded++;
    }

    //Remove a ticket from the pool for a customer
    public synchronized String removeTicket(){

        totalTicketsSold++;
        return tickets.poll();
    }

    //check if the pool is full
    public synchronized boolean isFull(){

        return tickets.size() >= maxCapacity;
    }

    //Check if the pool is empty
    public synchronized boolean isEmpty(){

        return tickets.isEmpty();
    }

    //Get the current number of tickets in the pool
    public synchronized int getCurrentTicketCount(){

        return tickets.size();
    }

    //Get the total number of tickets added
    public synchronized int getTotalTicketsAdded(){

        return totalTicketsAdded;
    }

    //Get the total number of tickets sold
    public synchronized int getTotalTicketsSold(){

        return totalTicketsSold;
    }

    //Get the total number of tickets to be released
    public synchronized int getTotalTicketsToRelease(){

        return totalTicketsToRelease;
    }

    //Generate a unique ticket ID
    private synchronized String generateNextTicket(){

        ticketCounter++;
        return "Ticket-" + ticketCounter;
    }

    //Display the next ticket to be added
    public synchronized String getNextTicket(){

        return generateNextTicket();
    }
}
