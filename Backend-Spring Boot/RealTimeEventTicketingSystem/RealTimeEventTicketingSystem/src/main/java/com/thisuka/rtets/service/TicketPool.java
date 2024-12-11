package com.thisuka.rtets.service;

import com.thisuka.rtets.entity.Ticket;
import com.thisuka.rtets.model.TicketingConfig;
import com.thisuka.rtets.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedQueue;


//Manages the ticket pool for the ticketing system
@Service
public class TicketPool{

    private final ConcurrentLinkedQueue<Ticket> availableTickets = new ConcurrentLinkedQueue<>();  //Thread safe queue for tickets
    private final TicketRepository ticketRepository;  //Repository to store tickets data
    private final TicketingConfig config;  //Configuration for the ticketing system
    private int ticketCounter = 0; //Counter for generating unique ticket IDs


    //Constructor to initialize the TicketPool with dependencies
    @Autowired
    public TicketPool(TicketRepository ticketRepository, TicketingConfig config){

        this.ticketRepository = ticketRepository;
        this.config = config;
    }

    //Initializes the pool with the specified number of tickets
    public void initializePool(){

        for (int i = 0; i < config.getInitialTicketsInPool(); i++){
            Ticket ticket = generateNextTicket();
            ticketRepository.save(ticket);  //Save the ticket in the database
            availableTickets.add(ticket);  //Add the ticket to the pool
        }

    }

    //Adds a ticket to the pool if capacity allows
    public synchronized boolean addTicket(Ticket ticket){

        if (availableTickets.size() >= getMaxCapacity()){
            return false;  //Pool is full
        }
        ticketRepository.save(ticket);  //Save the ticket to the database
        availableTickets.add(ticket);  //Add the ticket to the pool
        return true;
    }

    //Removes a ticket from the pool for purchase
    public synchronized Ticket purchaseTicket(){

        Ticket ticket = availableTickets.poll();  //Remove a ticket from the pool

        if (ticket != null){
            ticketRepository.save(ticket); //Update the tickets in the database
        }
        return ticket;
    }

    //Retrieves the current number of tickets in the pool
    public int getCurrentTicketCount(){

        return availableTickets.size();
    }

    //Retrieves the maximum ticket capacity
    public int getMaxCapacity(){

        return config.getMaxTicketCapacity();
    }

    //Generates a unique ticket with a new ID
    public synchronized Ticket generateNextTicket(){

        ticketCounter++;
        String ticketId = "Ticket-" + ticketCounter;  //Generate a unique ticket ID
        return new Ticket(ticketId);  //Return a new ticket object
    }

    //Checks if the pool is empty
    public synchronized boolean isEmpty(){

        return availableTickets.isEmpty();
    }

    //Retrieves the total number of tickets added to the pool
    public long getTotalTicketsAdded(){

        return ticketRepository.count();
    }

    //Retrieves the total number of tickets sold
    public long getTotalTicketsSold(){

        return ticketRepository.count() - availableTickets.size();
    }

    //Retrieves the total number of tickets to be released
    public int getTotalTicketsToRelease(){

        return config.getTotalTickets();
    }
}
