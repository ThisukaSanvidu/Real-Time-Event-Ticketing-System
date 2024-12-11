package com.thisuka.rtets.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ticket")  //Database table name
public class Ticket{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //Auto generated primary key
    private Long id;

    private String ticketId;  //Unique identifier for a ticket


    //default constructor for JPA
    public Ticket(){
    }

    //Constructor to create a ticket with a specific ID
    public Ticket(String ticketId){

        this.ticketId = ticketId;
    }

    // Getters and setters for accessing and modifying fields
    public Long getId(){

        return id;
    }

    public String getTicketId(){

        return ticketId;
    }

    public void setTicketId(String ticketId){

        this.ticketId = ticketId;
    }
}
