import model.*;
import service.EventBookingService;

import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.UUID;

public class Main {

    public static void seedData(EventBookingService service) {
        service.addVenue(new Venue("V1", "Arenele Romane", "București", 5000));
        service.addVenue(new Venue("V2", "Sala Palatului", "București", 4000));
        service.addVenue(new Venue("V3", "Cluj Arena", "Cluj-Napoca", 30000));
        service.addVenue(new Venue("V4", "Teatrul National", "București", 1000));

        service.addOrganizer(new Organizer("ORG1", "Global Records", "contact@global.com", "Global SRL"));
        service.addOrganizer(new Organizer("ORG2", "Untold Universe", "hello@untold.com", "Untold SRL"));
        service.addOrganizer(new Organizer("ORG3", "Gaggle Events", "info@gaggle.ro", "Gaggle SRL"));

        service.addCustomer(new Customer("C1", "Andrei Popescu", "andrei@email.com", "0722000001"));
        service.addCustomer(new Customer("C2", "Elena Ionescu", "elena@email.com", "0744000002"));
        service.addCustomer(new Customer("C3", "Mihai Radu", "mihai@email.com", "0766000003"));
        service.addCustomer(new Customer("C4", "Ioana Marin", "ioana@email.com", "0777000004"));

        long oZi = 864000000L; // 10 days in milliseconds (Wait, 86400000 * 10. Normal day is 86,400,000ms. Using your exact seed data variable)
        long now = System.currentTimeMillis();

        service.createEvent("E1", "Concert INNA", new Date(now + oZi * 5), "V1", "ORG1"); 
        service.createEvent("E2", "Untold Festival", new Date(now + oZi * 60), "V3", "ORG2"); 
        service.createEvent("E3", "Piesa de Teatru - Dineu cu Prosti", new Date(now + oZi * 2), "V4", "ORG3"); 
        service.createEvent("E4", "Concert Smiley", new Date(now + oZi * 15), "V2", "ORG1");
        service.createEvent("E5", "Stand-up Comedy", new Date(now + oZi * 10), "V1", "ORG3"); 

        service.buyTicket("T-1001", "C1", "E1", "VIP", 300.0);
        service.buyTicket("T-1002", "C2", "E3", "STANDARD", 100.0);
        service.buyTicket("T-1003", "C3", "E2", "VIP", 800.0);
        service.buyTicket("T-1004", "C4", "E1", "STANDARD", 150.0);

        System.out.println("Seed Data completed! \n");
    }

    public static void main(String[] args) {
        EventBookingService service = new EventBookingService();
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        // --- SEED DATA
        seedData(service);
        // -------------------------------------------------------------

        System.out.println("=========================================");
        System.out.println(" Welcome to the Event Ticketing Platform!");
        System.out.println("=========================================");

        while (isRunning) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Add Customer");
            System.out.println("2. Add Organizer");
            System.out.println("3. Add Venue");
            System.out.println("4. Create Event");
            System.out.println("5. Display Upcoming Events");
            System.out.println("6. Buy Ticket");
            System.out.println("7. Display Customer Transactions");
            System.out.println("8. Cancel Transaction");
            System.out.println("9. Display All Transactions");
            System.out.println("10. Display Events by Organizer");
            System.out.println("11. Display Events by Venue");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");

            int choice = -1;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline left-over
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the bad input
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter Customer ID: ");
                    String cId = scanner.nextLine();
                    System.out.print("Enter Name: ");
                    String cName = scanner.nextLine();
                    System.out.print("Enter Email: ");
                    String cEmail = scanner.nextLine();
                    System.out.print("Enter Phone: ");
                    String cPhone = scanner.nextLine();
                    service.addCustomer(new Customer(cId, cName, cEmail, cPhone));
                    break;

                case 2:
                    System.out.print("Enter Organizer ID: ");
                    String oId = scanner.nextLine();
                    System.out.print("Enter Name: ");
                    String oName = scanner.nextLine();
                    System.out.print("Enter Email: ");
                    String oEmail = scanner.nextLine();
                    System.out.print("Enter Company Name: ");
                    String oCompany = scanner.nextLine();
                    service.addOrganizer(new Organizer(oId, oName, oEmail, oCompany));
                    break;

                case 3:
                    System.out.print("Enter Venue ID: ");
                    String vId = scanner.nextLine();
                    System.out.print("Enter Name: ");
                    String vName = scanner.nextLine();
                    System.out.print("Enter Address: ");
                    String vAddress = scanner.nextLine();
                    System.out.print("Enter Capacity: ");
                    int vCapacity = scanner.nextInt();
                    scanner.nextLine();
                    service.addVenue(new Venue(vId, vName, vAddress, vCapacity));
                    break;

                case 4:
                    System.out.print("Enter Event ID: ");
                    String eId = scanner.nextLine();
                    System.out.print("Enter Title: ");
                    String eTitle = scanner.nextLine();
                    System.out.print("Enter Venue ID: ");
                    String eVenueId = scanner.nextLine();
                    System.out.print("Enter Organizer ID: ");
                    String eOrgId = scanner.nextLine();
                    System.out.print("Enter days from today for the event: ");
                    int days = scanner.nextInt();
                    scanner.nextLine();
                    long msInDay = 86400000L;
                    Date eDate = new Date(System.currentTimeMillis() + (days * msInDay));
                    service.createEvent(eId, eTitle, eDate, eVenueId, eOrgId);
                    break;

                case 5:
                    service.displayUpcomingEvents();
                    break;

                case 6:
                    String tId = "T-" + UUID.randomUUID().toString().substring(0, 8); // Autogenerate a short ID
                    System.out.print("Enter Customer ID: ");
                    String tcId = scanner.nextLine();
                    System.out.print("Enter Event ID: ");
                    String teId = scanner.nextLine();
                    System.out.print("Enter Ticket Type (VIP/STANDARD): ");
                    String tType = scanner.nextLine();
                    System.out.print("Enter Price: ");
                    double tPrice = scanner.nextDouble();
                    scanner.nextLine();
                    service.buyTicket(tId, tcId, teId, tType, tPrice);
                    break;

                case 7:
                    System.out.print("Enter Customer ID: ");
                    String custId = scanner.nextLine();
                    service.displayCustomerTransactions(custId);
                    break;

                case 8:
                    System.out.print("Enter Transaction ID to cancel: ");
                    String transIdToCancel = scanner.nextLine();
                    service.cancelTransaction(transIdToCancel);
                    break;

                case 9:
                    service.displayAllTransactions();
                    break;

                case 10:
                    System.out.print("Enter Organizer ID: ");
                    String orgIdSearch = scanner.nextLine();
                    service.displayEventsByOrganizer(orgIdSearch);
                    break;

                case 11:
                    System.out.print("Enter Venue ID: ");
                    String venueIdSearch = scanner.nextLine();
                    service.displayEventsByVenue(venueIdSearch);
                    break;

                case 0:
                    System.out.println("Exiting the platform. Goodbye!");
                    isRunning = false;
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }
}