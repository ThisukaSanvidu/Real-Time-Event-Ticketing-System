package com.thisuka.rtets.model;

import org.springframework.stereotype.Component;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.AssertTrue;

/**
 * Configuration class for ticketing system properties.
 * Now no longer depends on application.properties.
 * Uses defaults and can be updated at runtime via /api/config
 */
@Component
public class TicketingConfig {

    @Min(value = 1, message = "max-ticket-capacity must be at least 1")
    private int maxTicketCapacity = 100;
    @Min(value = 1, message = "total-tickets must be at least 1")
    private int totalTickets = 100;
    @Min(value = 0, message = "initial-tickets-in-pool cannot be negative")
    private int initialTicketsInPool = 15;
    @Min(value = 1, message = "ticket-release-rate must be at least 1")
    private int ticketReleaseRate = 1000;
    @Min(value = 1, message = "customer-retrieval-rate must be at least 1")
    private int customerRetrievalRate = 3000;
    @Min(value = 1, message = "vendor-count must be at least 1")
    private int vendorCount = 10;
    @Min(value = 1, message = "customer-count must be at least 1")
    private int customerCount = 20;

    public int getMaxTicketCapacity() { return maxTicketCapacity; }
    public void setMaxTicketCapacity(int maxTicketCapacity) { this.maxTicketCapacity = maxTicketCapacity; }

    public int getTotalTickets() { return totalTickets; }
    public void setTotalTickets(int totalTickets) { this.totalTickets = totalTickets; }

    public int getInitialTicketsInPool() { return initialTicketsInPool; }
    public void setInitialTicketsInPool(int initialTicketsInPool) { this.initialTicketsInPool = initialTicketsInPool; }

    public int getTicketReleaseRate() { return ticketReleaseRate; }
    public void setTicketReleaseRate(int ticketReleaseRate) { this.ticketReleaseRate = ticketReleaseRate; }

    public int getCustomerRetrievalRate() { return customerRetrievalRate; }
    public void setCustomerRetrievalRate(int customerRetrievalRate) { this.customerRetrievalRate = customerRetrievalRate; }

    public int getVendorCount() { return vendorCount; }
    public void setVendorCount(int vendorCount) { this.vendorCount = vendorCount; }

    public int getCustomerCount() { return customerCount; }
    public void setCustomerCount(int customerCount) { this.customerCount = customerCount; }

    @AssertTrue(message = "Total Tickets amount cannot exceed Maximum capacity. Please try again!")
    public boolean isTotalTicketsValid(){
        return totalTickets <= maxTicketCapacity;
    }

    @AssertTrue(message = "Tickets added to the pool cannot exceed Total tickets amount. Please try again!")
    public boolean isInitialTicketsValid(){
        return initialTicketsInPool <= totalTickets;
    }
}
