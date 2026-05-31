import model.*;
import service.CustomerService;
import service.EventService;
import service.TransactionService;

import java.util.Date;
import java.util.Scanner;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {
        CustomerService customerService = new CustomerService();
        EventService eventService = new EventService();
        TransactionService transactionService = new TransactionService();

        // Populăm baza de date doar dacă este goală
        if (customerService.getAll().isEmpty()) {
            System.out.println("Baza de date este goala. Se incarca datele initiale (Seed Data)...");
            seedData(customerService, eventService, transactionService);
        }

        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        System.out.println("\n=========================================");
        System.out.println("  Welcome to Event Ticketing Platform!");
        System.out.println("=========================================");

        while (isRunning) {
            System.out.println("\n--- MENIU PRINCIPAL ---");
            System.out.println("1. Adauga un Client nou");
            System.out.println("2. Afiseaza toti Clientii");
            System.out.println("3. Adauga o Locatie (Venue)");
            System.out.println("4. Adauga un Organizator");
            System.out.println("5. Creaza un Eveniment");
            System.out.println("6. Afiseaza Evenimentele Viitoare");
            System.out.println("7. Cumpara un Bilet (Tranzactie)");
            System.out.println("8. Afiseaza Tranzactiile unui Client");
            System.out.println("9. Anuleaza o Tranzactie");
            System.out.println("0. Iesire din aplicatie");
            System.out.print("\nAlege o optiune: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    System.out.print("Introdu Nume: ");
                    String name = scanner.nextLine();
                    System.out.print("Introdu Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Introdu Telefon: ");
                    String phone = scanner.nextLine();
                    
                    // Generăm ID scurt automat
                    String cid = "C-" + UUID.randomUUID().toString().substring(0, 6);
                    customerService.add(new Customer(cid, name, email, phone));
                    System.out.println("✅ Client salvat! Tine minte ID-ul: " + cid);
                    break;

                case 2:
                    System.out.println("\n--- Lista Clienti ---");
                    for (Customer c : customerService.getAll()) {
                        System.out.println(c.getId() + " - " + c.getName() + " (" + c.getEmail() + ")");
                    }
                    break;

                case 3:
                    System.out.print("Introdu Nume Locatie: ");
                    String vname = scanner.nextLine();
                    System.out.print("Introdu Adresa: ");
                    String vaddr = scanner.nextLine();
                    System.out.print("Introdu Capacitate: ");
                    int vcap = scanner.nextInt();
                    scanner.nextLine();
                    
                    String vid = "V-" + UUID.randomUUID().toString().substring(0, 6);
                    eventService.addVenue(new Venue(vid, vname, vaddr, vcap));
                    System.out.println("✅ Locatie salvata! Tine minte ID-ul: " + vid);
                    break;

                case 4:
                    System.out.print("Introdu Nume Reprezentant: ");
                    String oname = scanner.nextLine();
                    System.out.print("Introdu Email: ");
                    String oemail = scanner.nextLine();
                    System.out.print("Introdu Nume Companie: ");
                    String ocompany = scanner.nextLine();
                    
                    String oid = "ORG-" + UUID.randomUUID().toString().substring(0, 6);
                    eventService.addOrganizer(new Organizer(oid, oname, oemail, ocompany));
                    System.out.println("✅ Organizator salvat! Tine minte ID-ul: " + oid);
                    break;

                case 5:
                    System.out.print("Introdu Titlu Eveniment: ");
                    String title = scanner.nextLine();
                    System.out.print("Introdu ID Locatie Existenta: ");
                    String ev_vid = scanner.nextLine();
                    System.out.print("Introdu ID Organizator Existent: ");
                    String ev_oid = scanner.nextLine();
                    
                    String eid = "E-" + UUID.randomUUID().toString().substring(0, 6);
                    Date eventDate = new Date(System.currentTimeMillis() + (15L * 24 * 60 * 60 * 1000));
                    eventService.createEvent(eid, title, eventDate, ev_vid, ev_oid);
                    System.out.println("✅ Eveniment creat! Tine minte ID-ul: " + eid);
                    break;

                case 6:
                    eventService.displayUpcomingEvents();
                    break;

                case 7:
                    System.out.print("Introdu ID Client cumparator: ");
                    String buyerId = scanner.nextLine();
                    System.out.print("Introdu ID Eveniment: ");
                    String eventId = scanner.nextLine();
                    System.out.print("Tip Bilet (VIP / STANDARD): ");
                    String type = scanner.nextLine();
                    System.out.print("Pret: ");
                    double price = scanner.nextDouble();
                    scanner.nextLine();

                    Customer buyer = customerService.findById(buyerId);
                    Event event = eventService.findById(eventId);

                    if (buyer != null && event != null) {
                        String txId = "TX-" + UUID.randomUUID().toString().substring(0, 6);
                        transactionService.buyTicket(txId, buyer, event, type, price);
                        System.out.println("✅ Tranzactie finalizata! ID-ul tranzactiei: " + txId);
                    } else {
                        System.out.println("Eroare: Clientul sau Evenimentul nu a fost gasit in baza de date!");
                    }
                    break;

                case 8:
                    System.out.print("Introdu ID-ul Clientului: ");
                    String searchCid = scanner.nextLine();
                    transactionService.displayCustomerTransactions(searchCid);
                    break;

                case 9:
                    System.out.print("Introdu ID Tranzactie pentru anulare: ");
                    String cancelTxId = scanner.nextLine();
                    transactionService.cancelTransaction(cancelTxId);
                    break;

                case 0:
                    System.out.println("Se inchide aplicatia... Toate datele sunt salvate in PostgreSQL!");
                    isRunning = false;
                    break;

                default:
                    System.out.println("Optiune invalida! Te rog alege un numar intre 0 si 9.");
            }
        }
        scanner.close();
    }

    public static void seedData(CustomerService cs, EventService es, TransactionService ts) {
        cs.add(new Customer("C1", "Andrei Popescu", "andrei@email.com", "0722000111"));
        cs.add(new Customer("C2", "Elena Ionescu", "elena@email.com", "0744000222"));

        es.addVenue(new Venue("V1", "Arenele Romane", "Parcul Carol, Bucuresti", 5000));
        es.addVenue(new Venue("V2", "Cluj Arena", "Aleea Stadionului, Cluj", 30000));

        es.addOrganizer(new Organizer("ORG1", "Global Records", "contact@global.com", "Global SRL"));
        es.addOrganizer(new Organizer("ORG2", "Untold Universe", "hello@untold.com", "Untold SRL"));

        long oZi = 864000000L;
        long now = System.currentTimeMillis();
        
        es.createEvent("E1", "Concert INNA", new Date(now + oZi * 10), "V1", "ORG1");
        es.createEvent("E2", "Untold Festival", new Date(now + oZi * 60), "V2", "ORG2");

        Customer c1 = cs.findById("C1");
        Event e1 = es.findById("E1");
        
        if (c1 != null && e1 != null) {
            ts.buyTicket("TX-1001", c1, e1, "VIP", 350.0);
            ts.buyTicket("TX-1002", c1, e1, "STANDARD", 150.0);
        }
        
        System.out.println("✅ Seed Data a fost incarcat cu succes in PostgreSQL!\n");
    }
}