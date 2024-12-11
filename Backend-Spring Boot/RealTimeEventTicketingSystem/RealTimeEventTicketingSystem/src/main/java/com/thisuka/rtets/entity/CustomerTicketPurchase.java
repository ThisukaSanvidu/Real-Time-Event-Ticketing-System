package com.thisuka.rtets.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Customer_Ticket_Purchase")  //Database table name
public class CustomerTicketPurchase{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //Auto generate primary key
    private Long id;

    @Column(name = "customer_name", nullable = false)  //Store customer name
    private String customerName;

    @Column(name = "ticket_id_purchased", nullable = false)  //Store ticket ID purchased
    private Long ticketIdPurchased;


    //Default constructor for JPA
    public CustomerTicketPurchase(){
    }

    //Constructor with customer name and ticket ID
    public CustomerTicketPurchase(String customerName, Long ticketIdPurchased){

        this.customerName = customerName;
        this.ticketIdPurchased = ticketIdPurchased;
    }


    //Getters and Setters for accessing and modifying fields
    public Long getId(){

        return id;
    }

    public String getCustomerName(){

        return customerName;
    }

    public void setCustomerName(String customerName){

        this.customerName = customerName;
    }

    public Long getTicketIdPurchased(){

        return ticketIdPurchased;
    }

    public void setTicketIdPurchased(Long ticketIdPurchased){

        this.ticketIdPurchased = ticketIdPurchased;
    }
}
