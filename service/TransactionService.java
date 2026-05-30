package service;

import model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class TransactionService implements IService<Transaction> {
    private List<Transaction> transactions;

    public TransactionService() {
        this.transactions = new ArrayList<>();
    }

    @Override
    public void add(Transaction entity) {
        transactions.add(entity);
    }

    @Override
    public Transaction findById(String id) {
        for (Transaction t : transactions) {
            if (t.getId().equals(id)) {
                return t;
            }
        }
        return null;
    }

    @Override
    public Collection<Transaction> getAll() {
        return transactions;
    }

    public void buyTicket(String transactionId, Customer customer, Event event, String ticketType, double price) {
        if (customer == null || event == null) {
            System.out.println("Eroare: Clientul sau Evenimentul este invalid.");
            return;
        }

        Ticket ticket;
        if (ticketType.equalsIgnoreCase("VIP")) {
            ticket = new VipTicket(UUID.randomUUID().toString(), event.getId(), price, "VIP-LOUNGE-1");
        } else {
            ticket = new StandardTicket(UUID.randomUUID().toString(), event.getId(), price, "A-10");
        }

        Transaction transaction = new Transaction(transactionId, customer, ticket);
        this.add(transaction);
        System.out.println("Bilet cumparat: " + ticketType + " la " + event.getTitle() + " de catre " + customer.getName());
    }

    public void cancelTransaction(String transactionId) {
        boolean removed = transactions.removeIf(t -> t.getId().equals(transactionId));
        if (removed) {
            System.out.println("Tranzactia " + transactionId + " a fost anulata cu succes.");
        } else {
            System.out.println("Tranzactia " + transactionId + " nu a fost gasita.");
        }
    }

    public void displayCustomerTransactions(String customerId) {
        System.out.println("\n--- Tranzactii pentru Clientul ID: " + customerId + " ---");
        boolean found = false;
        for (Transaction t : transactions) {
            if (t.getCustomer().getId().equals(customerId)) {
                System.out.println("ID: " + t.getId() + " | Bilet: " + t.getTicket().getTicketType() + " | Pret: " + t.getTicket().getPrice());
                found = true;
            }
        }
        if (!found) {
            System.out.println("Nu exista tranzactii pentru acest client.");
        }
    }
}