package model;

public abstract class Ticket {
    protected String ticketId;
    protected String eventId;
    protected double price;

    public Ticket(String ticketId, String eventId, double price) {
        this.ticketId = ticketId;
        this.eventId = eventId;
        this.price = price;
    }

    public abstract String getTicketType();

    // Getters
    public double getPrice() { 
        return price; 
    }
    public String getTicketId() { 
        return ticketId; 
    }
    public String getEventId() { 
        return eventId; 
    }
}