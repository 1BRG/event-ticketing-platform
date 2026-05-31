package repository;

import config.DBConfig;
import model.Customer;
import model.Ticket;
import model.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository implements GenericRepository<Transaction> {

    private static TransactionRepository instance = null;

    private TransactionRepository() {}

    public static TransactionRepository getInstance() {
        if (instance == null) {
            instance = new TransactionRepository();
        }
        return instance;
    }

    @Override
    public void create(Transaction transaction) {
        String sql = """
                INSERT INTO transactions (id, customer_id, ticket_id, transaction_date) 
                VALUES (?, ?, ?, ?)
                """;
        try (Connection connection = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {
             
            ps.setString(1, transaction.getId());
            ps.setString(2, transaction.getCustomer().getId());
            ps.setString(3, transaction.getTicket().getTicketId());
            ps.setTimestamp(4, new Timestamp(transaction.getTransactionDate().getTime()));
            
            ps.executeUpdate();
            System.out.println("Tranzactie inregistrata in DB: " + transaction.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Transaction read(String id) {
        String sql = "SELECT id, customer_id, ticket_id, transaction_date FROM transactions WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {
             
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Customer customer = CustomerRepository.getInstance().read(rs.getString("customer_id"));
                    Ticket ticket = TicketRepository.getInstance().read(rs.getString("ticket_id"));
                    
                    return new Transaction(
                            rs.getString("id"),
                            customer,
                            ticket, 
                            rs.getDate("transaction_date")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Transaction> readAll() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT id, customer_id, ticket_id, transaction_date FROM transactions";
        
        try (Connection connection = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
             
            while (rs.next()) {
                Customer customer = CustomerRepository.getInstance().read(rs.getString("customer_id"));
                Ticket ticket = TicketRepository.getInstance().read(rs.getString("ticket_id"));
                Transaction t = new Transaction(rs.getString("id"), customer, ticket, rs.getDate("transaction_date"));
                transactions.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    @Override
    public void update(Transaction entity) {
        String sql = """
                UPDATE transactions 
                SET customer_id = ?, ticket_id = ?, transaction_date = ? 
                WHERE id = ?
                """;
        try (Connection connection = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {
             
            ps.setString(1, entity.getCustomer().getId());
            ps.setString(2, entity.getTicket().getTicketId());
            ps.setTimestamp(3, new Timestamp(entity.getTransactionDate().getTime()));
            ps.setString(4, entity.getId());
            
            ps.executeUpdate();
            System.out.println("Tranzactie actualizata in DB: " + entity.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM transactions WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
            System.out.println("Tranzactie anulata/stearsa.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}