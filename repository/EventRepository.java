package repository;

import config.DBConfig;
import model.Event;
import model.Organizer;
import model.Venue;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventRepository implements GenericRepository<Event> {

    private static EventRepository instance = null;

    private EventRepository() {}

    public static EventRepository getInstance() {
        if (instance == null) {
            instance = new EventRepository();
        }
        return instance;
    }

    @Override
    public void create(Event event) {
        String sql = """
                INSERT INTO events (id, title, event_date, venue_id, organizer_id) 
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection connection = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {
             
            ps.setString(1, event.getId());
            ps.setString(2, event.getTitle());
            // Transformam util.Date în sql.Timestamp pentru DB
            ps.setTimestamp(3, new Timestamp(event.getDate().getTime())); 
            ps.setString(4, event.getVenue().getId());
            ps.setString(5, event.getOrganizer().getId());
            
            ps.executeUpdate();
            System.out.println("Eveniment salvat in DB: " + event.getTitle());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Event read(String id) {
        String sql = "SELECT id, title, event_date, venue_id, organizer_id FROM events WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {
             
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Venue venue = VenueRepository.getInstance().read(rs.getString("venue_id"));
                    Organizer organizer = OrganizerRepository.getInstance().read(rs.getString("organizer_id"));
                    
                    return new Event(
                            rs.getString("id"),
                            rs.getString("title"),
                            new java.util.Date(rs.getTimestamp("event_date").getTime()),
                            venue,
                            organizer
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Event> readAll() {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT id, title, event_date, venue_id, organizer_id FROM events";
        try (Connection connection = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
             
            while (rs.next()) {
                Venue venue = VenueRepository.getInstance().read(rs.getString("venue_id"));
                Organizer organizer = OrganizerRepository.getInstance().read(rs.getString("organizer_id"));
                
                events.add(new Event(
                        rs.getString("id"),
                        rs.getString("title"),
                        new java.util.Date(rs.getTimestamp("event_date").getTime()),
                        venue,
                        organizer
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    @Override
    public void update(Event event) {
        String sql = """
                UPDATE events 
                SET title = ?, event_date = ?, venue_id = ?, organizer_id = ? 
                WHERE id = ?
                """;
        try (Connection connection = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {
             
            ps.setString(1, event.getTitle());
            ps.setTimestamp(2, new Timestamp(event.getDate().getTime()));
            ps.setString(3, event.getVenue().getId());
            ps.setString(4, event.getOrganizer().getId());
            ps.setString(5, event.getId());
            
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM events WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}