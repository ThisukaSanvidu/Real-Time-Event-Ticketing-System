package com.thisuka.rtets.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;


/**
 * Configuration class for ticketing system properties
 * Reads values from application.properties with the prefix 'ticketing'
 */
@Configuration
@ConfigurationProperties(prefix = "ticketing")
@Validated
public class TicketingConfig{

    @Min(value = 1, message = "max-ticket-capacity must be at least 1")
    private int maxTicketCapacity;

    @Min(value = 1, message = "total-tickets must be at least 1")
    private int totalTickets;

    @Min(value = 0, message = "initial-tickets-in-pool cannot be negative")
    private int initialTicketsInPool;

    @Min(value = 1, message = "ticket-release-rate must be at least 1")
    private int ticketReleaseRate;

    @Min(value = 1, message = "customer-retrieval-rate must be at least 1")
    private int customerRetrievalRate;

    @Min(value = 1, message = "vendor-count must be at least 1")
    private int vendorCount;

    @Min(value = 1, message = "customer-count must be at least 1")
    private int customerCount;


    //getters and setters for accessing and modifying fields
    public int getMaxTicketCapacity(){
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity){
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public int getTotalTickets(){
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets){
        this.totalTickets = totalTickets;
    }

    public int getInitialTicketsInPool(){
        return initialTicketsInPool;
    }

    public void setInitialTicketsInPool(int initialTicketsInPool){
        this.initialTicketsInPool = initialTicketsInPool;
    }

    public int getTicketReleaseRate(){
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(int ticketReleaseRate){
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public int getCustomerRetrievalRate(){
        return customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate){
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public int getVendorCount(){
        return vendorCount;
    }

    public void setVendorCount(int vendorCount){
        this.vendorCount = vendorCount;
    }

    public int getCustomerCount(){
        return customerCount;
    }

    public void setCustomerCount(int customerCount){
        this.customerCount = customerCount;
    }


    /**
     * Custom validation to ensure total tickets do not exceed the maximum capacity
     *
     * @return True if the total tickets are valid, false otherwise
     */
    @AssertTrue(message = "Total Tickets amount cannot exceed Maximum capacity. Please try again!")
    public boolean isTotalTicketsValid(){

        return totalTickets <= maxTicketCapacity;
    }

    /**
     * Custom validation to ensure initial tickets in the pool do not exceed total tickets
     *
     * @return True if the initial tickets are valid, false otherwise
     */
    @AssertTrue(message = "Tickets added to the pool cannot exceed Total tickets amount. Please try again!")
    public boolean isInitialTicketsValid(){

        return initialTicketsInPool <= totalTickets;
    }
}
