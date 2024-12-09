public class Customer implements Runnable{

    private final TicketPool ticketPool;
    private final int purchaseInterval;  //Time interval (ms) between ticket buying attempts
    private final int customerId;  //Unique ID for customer

    //Initialize a customer with ticket pool and purchase interval
    public Customer(TicketPool ticketPool, int purchaseInterval, int customerId){

        this.ticketPool = ticketPool;
        this.purchaseInterval = purchaseInterval;
        this.customerId = customerId;
    }

    @Override
    public void run(){

        while (true){
            try{
                //Wait for the purchase interval before attempting to buy tickets
                Thread.sleep(purchaseInterval);

                synchronized (ticketPool){
                    //check if the tickets are sold out
                    while (ticketPool.isEmpty()){
                        if (ticketPool.getTotalTicketsSold() >= ticketPool.getTotalTicketsToRelease()){
                            System.out.println("Customer-" + customerId + " found no available tickets. Customer-" + customerId + " stopped.");
                            return; //Exit when all tickets are sold
                        }

                        ticketPool.wait(); //Wait for tickets to be added to the pool
                    }

                    //Buy a ticket from the pool
                    String ticket = ticketPool.removeTicket();
                    System.out.println("Customer-" + customerId + " has bought " + ticket + " from the pool. Tickets currently available: " + ticketPool.getCurrentTicketCount());

                    ticketPool.notifyAll(); //notify vendors about space in the pool
                }

            }
            catch (InterruptedException e){
                Thread.currentThread().interrupt();  //Handle interruptions properly
                break;
            }
        }

    }
}
