import model.*;
import service.CustomerService;
import service.EventService;
import service.TransactionService;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        CustomerService customerService = new CustomerService();
        EventService eventService = new EventService();
        TransactionService transactionService = new TransactionService();

        Customer c1 = new Customer("C2", "Andrei", "andrei@email.com", "0722111222");
        customerService.add(c1);

        Venue v1 = new Venue("V1", "Arenele Romane", "Bucuresti", 5000);
        Organizer o1 = new Organizer("ORG1", "Global", "contact@global", "Global SRL");
        
        eventService.addVenue(v1);
        eventService.addOrganizer(o1);
        

        Date eventDate = new Date(System.currentTimeMillis() + 864000000L);
        eventService.createEvent("E1", "Concert Rock", eventDate, "V1", "ORG1");

        Customer buyer = customerService.findById("C1");
        Event eventToAttend = eventService.findById("E1");

        transactionService.buyTicket("TX-100", buyer, eventToAttend, "VIP", 250.0);
    }
}