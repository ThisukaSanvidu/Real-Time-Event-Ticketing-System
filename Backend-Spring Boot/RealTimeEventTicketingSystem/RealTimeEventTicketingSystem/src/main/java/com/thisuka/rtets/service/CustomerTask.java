package com.thisuka.rtets.service;

import com.thisuka.rtets.entity.CustomerTicketPurchase;
import com.thisuka.rtets.entity.Ticket;
import com.thisuka.rtets.repository.CustomerTicketPurchaseRepository;

import java.util.concurrent.atomic.AtomicInteger;


//Represents a customer task that purchases tickets from the pool and Implements the Runnable interface to run as a separate thread
public class CustomerTask implements Runnable{

    private final TicketPool ticketPool;  //Shared pool of tickets
    private final int purchaseInterval; //in milliseconds
    private final CustomerTicketPurchaseRepository customerTicketPurchaseRepository; // Repository to store purchases
    private final LogService logService; // For logging

    private volatile boolean running = true;  //controls task execution

    //Used static AtomicInteger to ensure unique customer numbers across all threads
    private static final AtomicInteger customerNumber = new AtomicInteger(0);


    //Constructor for CustomerTask
    public CustomerTask(TicketPool ticketPool, int purchaseInterval, CustomerTicketPurchaseRepository customerTicketPurchaseRepository, LogService logService){
        this.ticketPool = ticketPool;
        this.purchaseInterval = purchaseInterval;
        this.customerTicketPurchaseRepository = customerTicketPurchaseRepository;
        this.logService = logService;
    }


    @Override
    public void run(){
        //Assign a unique name to the customer
        int currentCustomerNumber = customerNumber.incrementAndGet();
        String customerName = "Customer-" + currentCustomerNumber;

        logService.addLog(customerName + " started.");

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
                            String msg = customerName + " has bought " + ticket.getTicketId() +
                                    " from the pool. Tickets currently available: " + ticketPool.getCurrentTicketCount();
                            System.out.println(msg);
                            logService.addLog(msg);
                            ticketPool.notifyAll(); //Notify other threads
                        }
                    }
                }

            }
            catch (InterruptedException e){
                Thread.currentThread().interrupt();
                String msg = customerName + " interrupted.";
                System.err.println(msg);
                logService.addLog(msg);
                break;
            }
        }

        String stopMsg = customerName + " stopped.";
        System.out.println(stopMsg);
        logService.addLog(stopMsg);
    }

    //Stops the customer's task
    public void stop(){
        running = false;
    }
}
