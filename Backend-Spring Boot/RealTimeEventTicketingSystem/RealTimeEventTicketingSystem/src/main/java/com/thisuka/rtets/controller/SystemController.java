package com.thisuka.rtets.controller;

import com.thisuka.rtets.model.TicketingConfig;
import com.thisuka.rtets.service.LogService;
import com.thisuka.rtets.service.TicketingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")   //Allows cross origin requests from any domain. Used this for frontend communication
@RestController   //REST API controller
@RequestMapping("/api")  //Base URL for all endpoints in this controller
public class SystemController{

    private final TicketingService ticketingService;  //handles the core ticketing logic such as starting/stopping of the system
    private final TicketingConfig config;  //Stores the configuration settings for the system
    private final LogService logService;  //Handles retrieving system logs

    @Autowired   //Automatically inject dependencies into the constructor
    public SystemController(TicketingService ticketingService, TicketingConfig config, LogService logService){
        this.ticketingService = ticketingService;
        this.config = config;
        this.logService = logService;
    }

    @PostMapping("/start")   //endpoint to start the system
    public String startSystem(){
        ticketingService.startSystem();
        return "Ticketing system started with " + config.getVendorCount() + " vendor(s) and " + config.getCustomerCount() + " customer(s) threads started.";
    }

    //Endpoint to stop the system
    @PostMapping("/stop")
    public String stopSystem(){
        ticketingService.stopSystem();
        return "Ticketing system stopped.";
    }

    //Endpoint to retrieve the current system status
    @GetMapping("/status")
    public String getStatus(){
        return ticketingService.getStatus();
    }

    //Endpoint to update config from UI
    @PostMapping("/config")
    public String updateConfig(@RequestBody TicketingConfig newConfigValues) {
        config.setMaxTicketCapacity(newConfigValues.getMaxTicketCapacity());
        config.setTotalTickets(newConfigValues.getTotalTickets());
        config.setInitialTicketsInPool(newConfigValues.getInitialTicketsInPool());
        config.setTicketReleaseRate(newConfigValues.getTicketReleaseRate());
        config.setCustomerRetrievalRate(newConfigValues.getCustomerRetrievalRate());
        config.setVendorCount(newConfigValues.getVendorCount());
        config.setCustomerCount(newConfigValues.getCustomerCount());

        //Validate that the total tickets do not exceed maximum capacity
        if(!config.isTotalTicketsValid()) {
            return "Total Tickets amount cannot exceed Maximum capacity. Please try again!";
        }

        //Validate that the initial tickets in pool does not exceed total tickets
        if(!config.isInitialTicketsValid()) {
            return "Tickets added to the pool cannot exceed Total tickets amount. Please try again!";
        }

        return "Configuration updated successfully.";
    }

    //Endpoint to retrieve system logs
    @GetMapping("/logs")
    public List<String> getLogs(){
        return logService.getLogs();
    }
}
