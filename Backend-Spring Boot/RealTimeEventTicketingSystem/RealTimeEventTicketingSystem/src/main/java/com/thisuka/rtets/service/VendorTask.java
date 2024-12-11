package com.thisuka.rtets.service;

import com.thisuka.rtets.entity.Ticket;
import com.thisuka.rtets.entity.Vendor;
import com.thisuka.rtets.repository.VendorRepository;

//Represents a vendor task that adds tickets to the pool and Implements Runnable to run as a separate thread
public class VendorTask implements Runnable{

    private final TicketPool ticketPool;
    private final int releaseInterval;
    private final int ticketsToRelease;
    private final VendorRepository vendorRepository;
    private final Vendor vendor;
    private final LogService logService; // Log service to record vendor actions

    private volatile boolean running = true;  //controls task execution


    //Constructor for VendorTask
    public VendorTask(TicketPool ticketPool, int releaseInterval, int ticketsToRelease, Vendor vendor, VendorRepository vendorRepository, LogService logService){
        this.ticketPool = ticketPool;
        this.releaseInterval = releaseInterval;
        this.ticketsToRelease = ticketsToRelease;
        this.vendor = vendor;
        this.vendorRepository = vendorRepository;
        this.logService = logService;
    }


    @Override
    public void run(){

        int releasedTickets = 0;  //Track the no of tickets released
        logService.addLog("Vendor-" + vendor.getId() + " started.");

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
                            String msg = "Vendor-" + vendor.getId() + " added " + ticket.getTicketId() +
                                    " to the pool. Tickets currently available: " + ticketPool.getCurrentTicketCount();
                            System.out.println(msg);
                            logService.addLog(msg);
                            ticketPool.notifyAll();
                        }
                        else{
                            String msg = "Vendor-" + vendor.getId() + " could not add " + ticket.getTicketId() + " as the pool is full.";
                            System.out.println(msg);
                            logService.addLog(msg);
                        }
                    }
                    else{
                        String msg = "Vendor-" + vendor.getId() + " found the pool full. Waiting...";
                        System.out.println(msg);
                        logService.addLog(msg);
                        ticketPool.wait();
                    }
                }

            }
            catch (InterruptedException e){
                Thread.currentThread().interrupt();
                String msg = "Vendor-" + vendor.getId() + " interrupted.";
                System.err.println(msg);
                logService.addLog(msg);
                break;
            }
        }

        String stopMsg = "Vendor-" + vendor.getId() + " has added all assigned tickets. Vendor-" + vendor.getId() + " stopped.";
        System.out.println(stopMsg);
        logService.addLog(stopMsg);
    }

    public void stop(){
        running = false;
    }
}
