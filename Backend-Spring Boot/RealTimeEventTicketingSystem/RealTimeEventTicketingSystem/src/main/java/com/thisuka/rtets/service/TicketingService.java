package com.thisuka.rtets.service;

import com.thisuka.rtets.entity.Vendor;
import com.thisuka.rtets.model.TicketingConfig;
import com.thisuka.rtets.repository.CustomerTicketPurchaseRepository;
import com.thisuka.rtets.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Service layer to manage the ticketing system logic
 * Handles initialization, thread management, and system status
 */
@Service
public class TicketingService{

    private final TicketingConfig config;  //Configuration for the ticketing system
    private final TicketPool ticketPool;  //Shared pool of tickets
    private final VendorRepository vendorRepository;  //Repository for vendors
    private final CustomerTicketPurchaseRepository customerTicketPurchaseRepository; //Repository for customer purchases
    private final LogService logService; // Log service to record actions

    private final List<VendorTask> vendorTasks = new ArrayList<>();  //Active vendor tasks
    private final List<CustomerTask> customerTasks = new ArrayList<>();  //Active customer tasks
    private final ExecutorService executorService = Executors.newCachedThreadPool();  //Thread pool for tasks


    @Autowired
    public TicketingService(TicketingConfig config, TicketPool ticketPool, VendorRepository vendorRepository,
                            CustomerTicketPurchaseRepository customerTicketPurchaseRepository, LogService logService){
        this.config = config;
        this.ticketPool = ticketPool;
        this.vendorRepository = vendorRepository;
        this.customerTicketPurchaseRepository = customerTicketPurchaseRepository;
        this.logService = logService;
    }

    /**
     * Starts the ticketing system by initializing the ticket pool and threads
     */
    public void startSystem(){
        //Initialize the ticket pool
        ticketPool.initializePool();

        //Calculate the no of tickets each vendor should release
        int remainingTickets = config.getTotalTickets() - config.getInitialTicketsInPool();
        int ticketsPerVendor = remainingTickets / config.getVendorCount();
        int extraTickets = remainingTickets % config.getVendorCount();

        //Create and save vendors
        List<Vendor> vendors = new ArrayList<>();
        for (int i = 1; i <= config.getVendorCount(); i++){
            String vendorName = "Vendor-" + i;
            Vendor vendor = new Vendor(vendorName);
            vendorRepository.save(vendor);
            vendors.add(vendor);
        }

        //Start vendor tasks
        for (int i = 0; i < config.getVendorCount(); i++){
            Vendor vendor = vendors.get(i);
            int ticketsToRelease = ticketsPerVendor + (i == config.getVendorCount() - 1 ? extraTickets : 0);
            VendorTask vendorTask = new VendorTask(ticketPool, config.getTicketReleaseRate(), ticketsToRelease, vendor, vendorRepository, logService);
            vendorTasks.add(vendorTask);
            executorService.submit(vendorTask);
        }

        //start customer tasks
        for (int i = 1; i <= config.getCustomerCount(); i++){
            CustomerTask customerTask = new CustomerTask(ticketPool, config.getCustomerRetrievalRate(), customerTicketPurchaseRepository, logService);
            customerTasks.add(customerTask);
            executorService.submit(customerTask);
        }

        String startMsg = config.getVendorCount() + " vendor(s) and " + config.getCustomerCount() + " customer(s) threads started.";
        System.out.println(startMsg);
        logService.addLog(startMsg);
    }

    /**
     * Stops the ticketing system by interrupting all threads
     */
    public void stopSystem(){
        //stop vendor threads
        for (VendorTask vendorTask : vendorTasks){
            vendorTask.stop();
        }

        //Stop customer threads
        for (CustomerTask customerTask : customerTasks){
            customerTask.stop();
        }

        executorService.shutdownNow();  //Stop all threads immediately

        vendorTasks.clear();
        customerTasks.clear();

        String stopMsg = "All vendors and customers stopped.";
        System.out.println(stopMsg);
        logService.addLog(stopMsg);
    }

    /**
     * Retrieves the current status of the ticketing system
     *
     * @return A string summarizing the system's status
     */
    public String getStatus(){
        long totalTicketsAdded = ticketPool.getTotalTicketsAdded();
        long totalTicketsSold = ticketPool.getTotalTicketsSold();
        int currentPoolSize = ticketPool.getCurrentTicketCount();

        return String.format("Tickets in Pool: %d, Total Tickets Added: %d, Total Tickets Sold: %d, Vendors Running: %d, Customers Running: %d",
                currentPoolSize, totalTicketsAdded, totalTicketsSold, vendorTasks.size(), customerTasks.size());
    }

    /**
     * Ensures all threads are stopped when the application shuts down
     */
    @PreDestroy
    public void onDestroy(){
        stopSystem();
    }
}
