package service;

import model.*;
import repository.TicketRepository;
import repository.TransactionRepository;

import java.util.Date;
import java.util.Collection;
import java.util.UUID;

public class TransactionService implements IService<Transaction> {
    
    private TransactionRepository transactionRepository;
    private TicketRepository ticketRepository;

    public TransactionService() {
        this.transactionRepository = TransactionRepository.getInstance();
        this.ticketRepository = TicketRepository.getInstance();
    }

    @Override
    public void add(Transaction entity) {
        transactionRepository.create(entity);
        AuditService.getInstance().logAction("add_transaction");
    }

    @Override
    public Transaction findById(String id) {
        AuditService.getInstance().logAction("find_transaction_by_id");
        return transactionRepository.read(id);
    }

    @Override
    public Collection<Transaction> getAll() {
        AuditService.getInstance().logAction("get_all_transactions");
        return transactionRepository.readAll();
    }

    public void buyTicket(String transactionId, Customer customer, Event event, String ticketType, double price) {
        if (customer == null || event == null) {
            System.out.println("Eroare: Clientul sau Evenimentul nu exista in DB.");
            return;
        }

        Ticket ticket;
        String ticketId = UUID.randomUUID().toString();
        
        if (ticketType.equalsIgnoreCase("VIP")) {
            ticket = new VipTicket(ticketId, event.getId(), price, "VIP-LOUNGE-1");
        } else {
            ticket = new StandardTicket(ticketId, event.getId(), price, "A-10");
        }

        ticketRepository.create(ticket);

        Transaction transaction = new Transaction(transactionId, customer, ticket, new Date());
        this.add(transaction);
        
        System.out.println("Bilet cumparat: " + ticketType + " la " + event.getTitle() + " de catre " + customer.getName());
    }

    public void cancelTransaction(String transactionId) {
        transactionRepository.delete(transactionId);
        AuditService.getInstance().logAction("cancel_transaction");
    }

    public void displayCustomerTransactions(String customerId) {
        System.out.println("\n--- Tranzactii pentru Clientul ID: " + customerId + " ---");
        
        Collection<Transaction> transactionsFromDb = transactionRepository.readAll();
        boolean found = false;
        
        for (Transaction t : transactionsFromDb) {
            if (t.getCustomer().getId().equals(customerId)) {
                System.out.println("ID: " + t.getId() + " | Bilet tip: " + t.getTicket().getTicketType() + " | Pret: " + t.getTicket().getPrice());
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("Nu exista tranzactii pentru acest client in DB.");
        }
        
        AuditService.getInstance().logAction("display_customer_transactions");
    }
}