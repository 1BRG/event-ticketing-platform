package model;

public class StandardTicket extends Ticket {
    private String seatNumber;

    public StandardTicket(String ticketId, String eventId, double price, String seatNumber) {
        super(ticketId, eventId, price);
        this.seatNumber = seatNumber;
    }

    @Override
    public String getTicketType() { 
        return "STANDARD"; 
    }

    public String getSeatNumber() { 
        return seatNumber; 
    }
}