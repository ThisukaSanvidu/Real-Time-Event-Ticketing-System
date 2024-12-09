package com.thisuka.rtets.service;

import com.thisuka.rtets.entity.Ticket;
import com.thisuka.rtets.entity.Vendor;
import com.thisuka.rtets.repository.VendorRepository;


/**
 * Represents a vendor task that adds tickets to the pool
 * Implements Runnable to run as a separate thread
 */
public class VendorTask implements Runnable{

    private final TicketPool ticketPool;
    private final int releaseInterval;
    private final int ticketsToRelease;
    private final VendorRepository vendorRepository;
    private final Vendor vendor;

    private volatile boolean running = true;  //controls task execution


    /**
     * Constructor for VendorTask
     *
     * @param ticketPool     Shared ticket pool
     * @param releaseInterval Time interval for ticket releases
     * @param ticketsToRelease Total tickets to be released by this vendor
     * @param vendor         Vendor entity associated with this task
     * @param vendorRepository Repository for saving vendor data
     */
    public VendorTask(TicketPool ticketPool, int releaseInterval, int ticketsToRelease, Vendor vendor, VendorRepository vendorRepository){
        this.ticketPool = ticketPool;
        this.releaseInterval = releaseInterval;
        this.ticketsToRelease = ticketsToRelease;
        this.vendor = vendor;
        this.vendorRepository = vendorRepository;
    }


    @Override
    public void run(){

        int releasedTickets = 0;  //Track the no of tickets released

        while (running && releasedTickets < ticketsToRelease){
            try {
                Thread.sleep(releaseInterval);

                synchronized (ticketPool){
                    if (ticketPool.getCurrentTicketCount() < ticketPool.getMaxCapacity()){
                        Ticket ticket = ticketPool.generateNextTicket();  //Create new ticket
                        boolean added = ticketPool.addTicket(ticket);  //Attempt to add the ticket to the pool

                        if (added){
                            vendorRepository.save(vendor); //save vendor details
                            releasedTickets++;
                            System.out.println("Vendor-" + vendor.getId() + " added " + ticket.getTicketId() +
                                    " to the pool. Tickets currently available: " + ticketPool.getCurrentTicketCount());
                            ticketPool.notifyAll();
                        }
                        else{
                            System.out.println("Vendor-" + vendor.getId() + " could not add " + ticket.getTicketId() + " as the pool is full.");
                        }
                    }
                    else{
                        System.out.println("Vendor-" + vendor.getId() + " found the pool full. Waiting...");
                        ticketPool.wait();
                    }
                }

            }
            catch (InterruptedException e){
                Thread.currentThread().interrupt();
                System.err.println("Vendor-" + vendor.getId() + " interrupted.");
                break;
            }
        }

        System.out.println("Vendor-" + vendor.getId() + " has added all assigned tickets. Vendor-" + vendor.getId() + " stopped.");
    }

    public void stop(){
        running = false;
    }
}
