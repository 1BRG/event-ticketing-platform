package service;

import model.*;
import java.util.*;

public class EventBookingService {
    
    
    private Map<String, Customer> customers;
    private Map<String, Organizer> organizers;
    private Map<String, Venue> venues;
    private List<Transaction> transactions;
    
    
    private Set<Event> events;

    public EventBookingService() {
        this.customers = new HashMap<>();
        this.organizers = new HashMap<>();
        this.venues = new HashMap<>();
        this.transactions = new ArrayList<>();
        this.events = new TreeSet<>();
    }

    public void addCustomer(Customer customer) {
        customers.put(customer.getId(), customer);
        System.out.println("Customer added: " + customer.getName());
    }

    public void addOrganizer(Organizer organizer) {
        organizers.put(organizer.getId(), organizer);
        System.out.println("Organizer added: " + organizer.getName());
    }

    public void addVenue(Venue venue) {
        venues.put(venue.getId(), venue);
        System.out.println("Venue added: " + venue.getName());
    }

    public void createEvent(String id, String title, Date date, String venueId, String organizerId) {
        Venue venue = venues.get(venueId);
        Organizer organizer = organizers.get(organizerId);
        if (venue == null || organizer == null) {
            System.out.println("Invalid venue or organizer ID.");
            return;
        }
        Event event = new Event(id, title, date, venue, organizer);
        events.add(event);
        System.out.println("Event created: " + title);
    }

    public void displayUpcomingEvents(){
        System.out.println("Upcoming Events:");
        for(Event event : events) {
            System.out.println(event.getTitle() + " at " + event.getVenue().getName() + " on " + event.getDate());
        }
    }

    public void buyTicket(String transactionId, String customerId, String eventId, String ticketType, double price){
        Customer customer = customers.get(customerId);
        Event event = getEventById(eventId);
        if (customer == null || event == null) {
            System.out.println("Invalid customer or event ID.");
            return;
        }
        Ticket ticket;
        if (ticketType.equalsIgnoreCase("VIP")) {
            ticket = new VipTicket(UUID.randomUUID().toString(), eventId, price, "VIP-LOUNGE-ACCESS");
        } else {
            ticket = new StandardTicket(UUID.randomUUID().toString(), eventId, price, "A1");
        }
        Transaction transaction = new Transaction(transactionId, customer, ticket);
        transactions.add(transaction);
        System.out.println("Ticket purchased: " + ticketType + " for " + event.getTitle());
    }

    public Event getEventById(String eventId) {
        for (Event event : events) {
            if (event.getId().equals(eventId)) {
                return event;
            }
        }
        return null;
    }

    public void displayCustomerTransactions(String customerId){
        System.out.println("Transactions for Customer ID: " + customerId);
        for(Transaction transaction : transactions) {
            if(transaction.getCustomer().getId().equals(customerId)) {
                System.out.println("Transaction ID: " + transaction.getId() + ", Event: " + transaction.getTicket().getEventId() + ", Date: " + transaction.getTransactionDate());
            }
        }
    }

    public void cancelTransaction(String transactionId){
        Transaction transaction = null;
        for(Transaction t : transactions) {
            if(t.getId().equals(transactionId)) {
                transaction = t;
                break;
            }
        }
        if(transaction != null) {
            transactions.remove(transaction);
            System.out.println("Transaction cancelled: " + transactionId);
        } else {
            System.out.println("Transaction not found: " + transactionId);
        }
    }

    public void displayAllTransactions(){
        System.out.println("All Transactions:");
        for(Transaction transaction : transactions) {
            System.out.println("Transaction ID: " + transaction.getId() + ", Customer: " + transaction.getCustomer().getName() + ", Event: " + transaction.getTicket().getEventId() + ", Date: " + transaction.getTransactionDate());
        }
    }

    public void displayEventsByOrganizer(String organizerId){
        System.out.println("Events by Organizer ID: " + organizerId);
        for(Event event : events) {
            if(event.getOrganizer().getId().equals(organizerId)) {
                System.out.println("Event: " + event.getTitle() + ", Date: " + event.getDate());
            }
        }
    }

    public void displayEventsByVenue(String venueId){
        System.out.println("Events at Venue ID: " + venueId);
        for(Event event : events) {
            if(event.getVenue().getId().equals(venueId)) {
                System.out.println("Event: " + event.getTitle() + ", Date: " + event.getDate());
            }
        }
    }




}