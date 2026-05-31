package repository;

import config.DBConfig;
import model.Venue;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VenueRepository implements GenericRepository<Venue> {

    private static VenueRepository instance = null;

    private VenueRepository() {}

    public static VenueRepository getInstance() {
        if (instance == null) {
            instance = new VenueRepository();
        }
        return instance;
    }

    @Override
    public void create(Venue venue) {
        String sql = """
                INSERT INTO venues (id, name, address, capacity) 
                VALUES (?, ?, ?, ?)
                """;
        try (Connection connection = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {
             
            ps.setString(1, venue.getId());
            ps.setString(2, venue.getName());
            ps.setString(3, venue.getAddress());
            ps.setInt(4, venue.getCapacity());
            
            ps.executeUpdate();
            System.out.println("Locatie salvata in DB: " + venue.getName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Venue read(String id) {
        String sql = "SELECT id, name, address, capacity FROM venues WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {
             
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Venue(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("address"),
                            rs.getInt("capacity")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Venue> readAll() {
        List<Venue> venues = new ArrayList<>();
        String sql = "SELECT id, name, address, capacity FROM venues";
        try (Connection connection = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
             
            while (rs.next()) {
                venues.add(new Venue(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getInt("capacity")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return venues;
    }

    @Override
    public void update(Venue venue) {
        String sql = """
                UPDATE venues 
                SET name = ?, address = ?, capacity = ? 
                WHERE id = ?
                """;
        try (Connection connection = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {
             
            ps.setString(1, venue.getName());
            ps.setString(2, venue.getAddress());
            ps.setInt(3, venue.getCapacity());
            ps.setString(4, venue.getId());
            
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM venues WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}