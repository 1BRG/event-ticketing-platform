package repository;

import config.DBConfig;
import model.Customer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository implements GenericRepository<Customer> {

    private static CustomerRepository instance = null;

    private CustomerRepository() {}

    public static CustomerRepository getInstance() {
        if (instance == null) {
            instance = new CustomerRepository();
        }
        return instance;
    }

    @Override
    public void create(Customer customer) {
        String sql = """
                INSERT INTO customers (id, name, email, phone_number) 
                VALUES (?, ?, ?, ?)
                """;
        // Folosim try-with-resources pentru închiderea automată a conexiunii
        try (Connection connection = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {
             
            ps.setString(1, customer.getId());
            ps.setString(2, customer.getName());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getPhoneNumber());
            
            ps.executeUpdate();
            System.out.println("Client salvat in baza de date: " + customer.getName());
        } catch (SQLException e) {
            System.out.println("Eroare la salvarea clientului in DB!");
            e.printStackTrace();
        }
    }

    @Override
    public Customer read(String id) {
        String sql = """
                SELECT id, name, email, phone_number 
                FROM customers 
                WHERE id = ?
                """;
        try (Connection connection = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {
             
            ps.setString(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone_number")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; 
    }

    @Override
    public List<Customer> readAll() {
        List<Customer> customers = new ArrayList<>();
        String sql = """
                SELECT id, name, email, phone_number 
                FROM customers
                """;
        try (Connection connection = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
             
            while (rs.next()) {
                customers.add(new Customer(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone_number")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    @Override
    public void update(Customer customer) {
        String sql = """
                UPDATE customers 
                SET name = ?, email = ?, phone_number = ? 
                WHERE id = ?
                """;
        try (Connection connection = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {
             
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getPhoneNumber());
            ps.setString(4, customer.getId()); 
            
            ps.executeUpdate();
            System.out.println("Client actualizat in baza de date.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        String sql = """
                DELETE FROM customers 
                WHERE id = ?
                """;
        try (Connection connection = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {
             
            ps.setString(1, id);
            ps.executeUpdate();
            System.out.println("Client sters din baza de date.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}