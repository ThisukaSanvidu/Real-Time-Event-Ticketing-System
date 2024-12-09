package com.thisuka.rtets.service;

import com.thisuka.rtets.entity.CustomerTicketPurchase;
import com.thisuka.rtets.entity.Ticket;
import com.thisuka.rtets.repository.CustomerTicketPurchaseRepository;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * Represents a customer task that purchases tickets from the pool
 * Implements the Runnable interface to run as a separate thread
 */
public class CustomerTask implements Runnable{

    private final TicketPool ticketPool;  //Shared pool of tickets
    private final int purchaseInterval; //in milliseconds
    private final CustomerTicketPurchaseRepository customerTicketPurchaseRepository; // Repository to store purchases

    private volatile boolean running = true;  //controls task execution

    //Used static AtomicInteger to ensure unique customer numbers across all threads
    private static final AtomicInteger customerNumber = new AtomicInteger(0);


    /**
     * Constructor for CustomerTask
     *
     * @param ticketPool Shared ticket pool
     * @param purchaseInterval Time interval between purchases
     * @param customerTicketPurchaseRepository Repository for customer purchases
     */
    public CustomerTask(TicketPool ticketPool, int purchaseInterval, CustomerTicketPurchaseRepository customerTicketPurchaseRepository){
        this.ticketPool = ticketPool;
        this.purchaseInterval = purchaseInterval;
        this.customerTicketPurchaseRepository = customerTicketPurchaseRepository; // Initialize repository
    }


    @Override
    public void run(){
        //Assign a unique name to the customer
        int currentCustomerNumber = customerNumber.incrementAndGet();
        String customerName = "Customer-" + currentCustomerNumber;

        while (running){
            try {
                Thread.sleep(purchaseInterval);  //Pause for the specified interval

                synchronized (ticketPool){
                    while (ticketPool.isEmpty()){
                        //Stops if all tickets are sold
                        if (ticketPool.getTotalTicketsSold() >= ticketPool.getTotalTicketsToRelease()){
                            running = false;
                            break;
                        }
                        ticketPool.wait(); //wait until tickets are available
                    }

                    if (!ticketPool.isEmpty()){
                        Ticket ticket = ticketPool.purchaseTicket();  //Purchase a ticket from the pool

                        if (ticket != null){
                            //Save the purchase details
                            CustomerTicketPurchase purchase = new CustomerTicketPurchase(customerName, ticket.getId());
                            customerTicketPurchaseRepository.save(purchase);

                            //Logs the purchase
                            System.out.println("Customer-" + currentCustomerNumber + " has bought " + ticket.getTicketId() +
                                    " from the pool. Tickets currently available: " + ticketPool.getCurrentTicketCount());
                            ticketPool.notifyAll(); //Notify other threads
                        }
                    }
                }

            }
            catch (InterruptedException e){
                Thread.currentThread().interrupt();  //Handle thread interruption
                System.err.println("Customer-" + customerName + " interrupted.");
                break;
            }
        }

        //Log the end of the customer's task
        System.out.println("Customer-" + customerName + " stopped.");
    }

    /**
     * Stops the customer's task by updating the running flag.
     */
    public void stop(){
        running = false;
    }
}
