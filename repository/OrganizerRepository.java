package repository;

import config.DBConfig;
import model.Organizer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrganizerRepository implements GenericRepository<Organizer> {

    private static OrganizerRepository instance = null;

    private OrganizerRepository() {}

    public static OrganizerRepository getInstance() {
        if (instance == null) {
            instance = new OrganizerRepository();
        }
        return instance;
    }

    @Override
    public void create(Organizer organizer) {
        String sql = """
                INSERT INTO organizers (id, name, email, company_name) 
                VALUES (?, ?, ?, ?)
                """;
        try (Connection connection = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {
             
            ps.setString(1, organizer.getId());
            ps.setString(2, organizer.getName());
            ps.setString(3, organizer.getEmail());
            ps.setString(4, organizer.getCompanyName());
            
            ps.executeUpdate();
            System.out.println("Organizator salvat in DB: " + organizer.getName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Organizer read(String id) {
        String sql = "SELECT id, name, email, company_name FROM organizers WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {
             
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Organizer(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("company_name")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Organizer> readAll() {
        List<Organizer> organizers = new ArrayList<>();
        String sql = "SELECT id, name, email, company_name FROM organizers";
        try (Connection connection = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
             
            while (rs.next()) {
                organizers.add(new Organizer(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("company_name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return organizers;
    }

    @Override
    public void update(Organizer organizer) {
        String sql = """
                UPDATE organizers 
                SET name = ?, email = ?, company_name = ? 
                WHERE id = ?
                """;
        try (Connection connection = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {
             
            ps.setString(1, organizer.getName());
            ps.setString(2, organizer.getEmail());
            ps.setString(3, organizer.getCompanyName());
            ps.setString(4, organizer.getId());
            
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM organizers WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}