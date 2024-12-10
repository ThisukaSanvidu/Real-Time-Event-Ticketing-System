package com.thisuka.rtets.controller;

import com.thisuka.rtets.model.TicketingConfig;
import com.thisuka.rtets.service.LogService;
import com.thisuka.rtets.service.TicketingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class SystemController{

    private final TicketingService ticketingService;
    private final TicketingConfig config;
    private final LogService logService;

    @Autowired
    public SystemController(TicketingService ticketingService, TicketingConfig config, LogService logService){
        this.ticketingService = ticketingService;
        this.config = config;
        this.logService = logService;
    }

    @PostMapping("/start")
    public String startSystem(){
        ticketingService.startSystem();
        return "Ticketing system started with " + config.getVendorCount() + " vendor(s) and " + config.getCustomerCount() + " customer(s) threads started.";
    }

    @PostMapping("/stop")
    public String stopSystem(){
        ticketingService.stopSystem();
        return "Ticketing system stopped.";
    }

    @GetMapping("/status")
    public String getStatus(){
        return ticketingService.getStatus();
    }

    // New endpoint to update config from UI
    @PostMapping("/config")
    public String updateConfig(@RequestBody TicketingConfig newConfigValues) {
        config.setMaxTicketCapacity(newConfigValues.getMaxTicketCapacity());
        config.setTotalTickets(newConfigValues.getTotalTickets());
        config.setInitialTicketsInPool(newConfigValues.getInitialTicketsInPool());
        config.setTicketReleaseRate(newConfigValues.getTicketReleaseRate());
        config.setCustomerRetrievalRate(newConfigValues.getCustomerRetrievalRate());
        config.setVendorCount(newConfigValues.getVendorCount());
        config.setCustomerCount(newConfigValues.getCustomerCount());

        if(!config.isTotalTicketsValid()) {
            return "Total Tickets amount cannot exceed Maximum capacity. Please try again!";
        }
        if(!config.isInitialTicketsValid()) {
            return "Tickets added to the pool cannot exceed Total tickets amount. Please try again!";
        }

        return "Configuration updated successfully.";
    }

    // Endpoint to fetch system logs
    @GetMapping("/logs")
    public List<String> getLogs(){
        return logService.getLogs();
    }
}
