package model;

import java.util.Date;

public class Transaction {
    private String id;
    private Customer customer;
    private Ticket ticket;
    private Date transactionDate;

    public Transaction(String id, Customer customer, Ticket ticket, Date transactionDate) {
        this.id = id;
        this.customer = customer;
        this.ticket = ticket;
        this.transactionDate = transactionDate != null ? transactionDate : new Date();
    }

    // Getters
    public String getId() { 
        return id; 
    }
    public Customer getCustomer() { 
        return customer; 
    }
    public Ticket getTicket() { 
        return ticket; 
    }
    public Date getTransactionDate() { 
        return transactionDate; 
    }
}