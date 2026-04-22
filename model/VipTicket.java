package model;

public class VipTicket extends Ticket {
    private String loungeAccessCode;

    public VipTicket(String ticketId, String eventId, double price, String loungeAccessCode) {
        super(ticketId, eventId, price);
        this.loungeAccessCode = loungeAccessCode;
    }

    @Override
    public String getTicketType() { 
        return "VIP"; 
    }

    public String getLoungeAccessCode() { 
        return loungeAccessCode; 
    }
}