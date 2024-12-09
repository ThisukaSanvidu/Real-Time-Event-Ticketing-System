package com.thisuka.rtets.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "vendor")  //database table name
public class Vendor{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //Auto generate primary keys
    private Long id;

    private String name;  //Vendor's name


    //default constructor for JPA
    public Vendor(){
    }

    /**
     * Constructor to create a vendor with a specific name
     *
     * @param name The name of the vendor
     */
    public Vendor(String name){
        this.name = name;
    }

    //Getters and Setters for accessing and modifying fields
    public Long getId(){
        return id;
    }


    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

}
