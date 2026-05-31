package repository;

import config.DBConfig;
import model.StandardTicket;
import model.Ticket;
import model.VipTicket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketRepository implements GenericRepository<Ticket> {

    private static TicketRepository instance = null;

    private TicketRepository() {}

    public static TicketRepository getInstance() {
        if (instance == null) {
            instance = new TicketRepository();
        }
        return instance;
    }

    @Override
    public void create(Ticket ticket) {
        String sql = """
                INSERT INTO tickets (id, event_id, price, ticket_type, seat_number, lounge_access_code) 
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {
             
            ps.setString(1, ticket.getTicketId());
            ps.setString(2, ticket.getEventId());
            ps.setDouble(3, ticket.getPrice());
            
            if (ticket instanceof VipTicket) {
                VipTicket vip = (VipTicket) ticket;
                ps.setString(4, "VIP");
                ps.setNull(5, Types.VARCHAR); // VIP-ul nu are scaun
                ps.setString(6, vip.getLoungeAccessCode());
            } else if (ticket instanceof StandardTicket) {
                StandardTicket standard = (StandardTicket) ticket;
                ps.setString(4, "STANDARD");
                ps.setString(5, standard.getSeatNumber());
                ps.setNull(6, Types.VARCHAR); // Standardul nu are lounge code
            }
            
            ps.executeUpdate();
            System.out.println("Bilet salvat in DB.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Ticket read(String id) {
        String sql = "SELECT * FROM tickets WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {
             
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String type = rs.getString("ticket_type");
                
                    if (type.equals("VIP")) {
                        return new VipTicket(
                                rs.getString("id"), 
                                rs.getString("event_id"), 
                                rs.getDouble("price"), 
                                rs.getString("lounge_access_code")
                        );
                    } else {
                        return new StandardTicket(
                                rs.getString("id"), 
                                rs.getString("event_id"), 
                                rs.getDouble("price"), 
                                rs.getString("seat_number")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Ticket> readAll() {
        String sql = "Select * from tickets";
        List<Ticket> tickets = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME,
            DBConfig.PASSWORD)){
             Statement statement = connection.createStatement();
             try (ResultSet rs = statement.executeQuery(sql)) {
                while (rs.next()) {
                    String type = rs.getString("ticket_type");

                    if (type.equals("VIP")) {
                        tickets.add(new VipTicket(
                                rs.getString("id"),
                                rs.getString("event_id"),
                                rs.getDouble("price"),
                                rs.getString("lounge_access_code")
                        ));
                    } else {
                        tickets.add(new StandardTicket(
                                rs.getString("id"),
                                rs.getString("event_id"),
                                rs.getDouble("price"),
                                rs.getString("seat_number")
                        ));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    @Override
    public void update(Ticket ticket) {
        String sql = """
                UPDATE tickets 
                SET event_id = ?, price = ?, ticket_type = ?, seat_number = ?, lounge_access_code = ?
                WHERE id = ?
                """;
        try (Connection connection = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {
             
            ps.setString(1, ticket.getEventId());
            ps.setDouble(2, ticket.getPrice());
            
            if (ticket instanceof VipTicket) {
                VipTicket vip = (VipTicket) ticket;
                ps.setString(3, "VIP");
                ps.setNull(4, Types.VARCHAR); // VIP-ul nu are scaun
                ps.setString(5, vip.getLoungeAccessCode());
            } else if (ticket instanceof StandardTicket) {
                StandardTicket standard = (StandardTicket) ticket;
                ps.setString(3, "STANDARD");
                ps.setString(4, standard.getSeatNumber());
                ps.setNull(5, Types.VARCHAR); // Standardul nu are lounge code
            }
            ps.setString(6, ticket.getTicketId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM tickets WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}