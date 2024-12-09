public class Vendor implements Runnable{

    private final TicketPool ticketPool;  //Shared pool where tickets are added
    private final int releaseInterval;  //Time interval (ms) between ticket releases
    private final int vendorId;  //Unique ID for vendor
    private final int ticketsToRelease;

    //Constructor to Initialize a vendor with ticket pool and assigned responsibilities
    public Vendor(TicketPool ticketPool, int releaseInterval, int vendorId, int ticketsToRelease){

        this.ticketPool = ticketPool;
        this.releaseInterval = releaseInterval;
        this.vendorId = vendorId;
        this.ticketsToRelease = ticketsToRelease;
    }

    @Override
    public void run(){   //This is the logic that will execute when vendor thread starts
        int releasedTickets = 0;

        while (true){
            try{
                //wait for the release interval before adding tickets
                Thread.sleep(releaseInterval);

                //Synchronize to safely interact with the shared ticket pool
                synchronized (ticketPool){
                    //Stop if the vendor has released all assigned tickets
                    if (releasedTickets >= ticketsToRelease){
                        System.out.println("Vendor-" + vendorId + " has added all assigned tickets. Vendor-" + vendorId + " stopped.");
                        break;  //exit the loop once all tickets are released
                    }

                    while (ticketPool.isFull()){
                        ticketPool.wait(); // Wait if the ticket pool is full
                    }

                    //Add a ticket to the pool
                    String ticket = ticketPool.getNextTicket();
                    ticketPool.addTickets(ticket);
                    releasedTickets++;
                    System.out.println("Vendor-" + vendorId + " added " + ticket + " to the pool. Tickets currently available: " + ticketPool.getCurrentTicketCount());

                    ticketPool.notifyAll(); //Notify customers about the new ticket
                }

            }
            catch (InterruptedException e){
                Thread.currentThread().interrupt();  //Handle interruptions properly
                break;
            }
        }

    }
}
