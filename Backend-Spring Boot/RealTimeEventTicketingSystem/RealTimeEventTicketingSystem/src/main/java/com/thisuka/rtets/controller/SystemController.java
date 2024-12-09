package com.thisuka.rtets.controller;

import com.thisuka.rtets.service.TicketingService;
import com.thisuka.rtets.model.TicketingConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SystemController{

    //Service layer to manage ticketing system logic
    private final TicketingService ticketingService;
    //Configuration properties for ticketing system
    private final TicketingConfig config;


    //Constructor based dependency injection
    @Autowired
    public SystemController(TicketingService ticketingService, TicketingConfig config){

        this.ticketingService = ticketingService;
        this.config = config;
    }

    /**
     * Starts the ticketing system and initializes vendor and customers
     *
     * @return confirmation message about the system start
     */
    @PostMapping("/start")
    public String startSystem(){

        ticketingService.startSystem();  //Start the ticketing system
        return "Ticketing system started with " + config.getVendorCount() + " vendor(s) and " + config.getCustomerCount() + " customer(s) threads started.";
    }

    /**
     * Stops the ticketing system by stopping all threads
     *
     * @return confirmation message about the system stop
     */
    @PostMapping("/stop")
    public String stopSystem(){

        ticketingService.stopSystem();  //stop the ticketing system
        return "Ticketing system stopped.";
    }

    /**
     * Retrieves the current state of the ticketing system
     *
     * @return A string describing the current system status
     */
    @GetMapping("/status")
    public String getStatus(){

        return ticketingService.getStatus();  //fetch and return the system status
    }
}
