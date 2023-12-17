package data_access;

import domain_model.HotelCalendar;
import domain_model.Room;
import domain_model.RoomInfo;
import service_layer.CalendarManager;
import service_layer.HotelManager;
import service_layer.ReservationManager;
import utilities.Research;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class HotelCalendarDAO {
    private final Connection connection;

    public HotelCalendarDAO() {
        connection = ConnectionManager.connect();
    }
    public void addCalendar(HotelCalendar calendar, ArrayList<Room> rooms) throws SQLException {
        String sql = "INSERT OR IGNORE INTO HotelCalendar (hotel_id, date , room_id, availability, price, minimum_stay) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            for (LocalDate date = calendar.getStartDate(); !date.isAfter(calendar.getEndDate()); date = date.plusDays(1)) {
                for (Room room :rooms) {
                    statement.setString(1, calendar.getHotelID());
                    statement.setString(2, date.toString());
                    statement.setString(3, room.getId());
                    RoomInfo roomInfo = new RoomInfo(room.getId());
                    statement.setString(4, roomInfo.getAvailabilityToString());
                    statement.setDouble(5, roomInfo.getPrice());
                    statement.setInt(6, roomInfo.getMinimumStay());
                    statement.addBatch();
                }
            }
            statement.executeBatch();
        }
    }

    public void deleteCalendar(HotelCalendar calendar, ArrayList<Room> rooms) throws SQLException {
        String sql = "DELETE FROM HotelCalendar WHERE hotel_id = ? AND date = ? AND room_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            for (LocalDate date = calendar.getStartDate(); !date.isAfter(calendar.getEndDate()); date = date.plusDays(1)) {
                for (Room room : rooms) {
                    statement.setString(1, calendar.getHotelID());
                    statement.setString(2, date.toString());
                    statement.setString(3, room.getId());
                    statement.addBatch();
                }
            }
            statement.executeBatch();
        }
    }

    public void setMinimumStay(String hotelID, String date, String roomID, int minStay) throws SQLException {
        String sql = "UPDATE HotelCalendar SET minimum_stay = ? WHERE hotel_id = ? AND date = ? AND room_id = ?";
        try(PreparedStatement statement =  connection.prepareStatement(sql)) {
            statement.setInt(1, minStay);
            statement.setString(2, hotelID);
            statement.setString(3, date);
            statement.setString(4, roomID);
            statement.executeUpdate();
        }
    }

    public void setAvailability(String hotelID, String date, String roomID, String availability) throws SQLException {
        String sql = "UPDATE HotelCalendar SET availability = ? WHERE hotel_id = ? AND date = ? AND room_id = ?";
        try(PreparedStatement statement =  connection.prepareStatement(sql)) {
            statement.setString(1, availability);
            statement.setString(2, hotelID);
            statement.setString(3, date);
            statement.setString(4, roomID);
            statement.executeUpdate();
        }
    }

    public void setPrice(String hotelID, String date, String roomID, double price) throws SQLException {
        String sql = "UPDATE HotelCalendar SET price = ? WHERE hotel_id = ? AND date = ? AND room_id = ?";
        try(PreparedStatement statement =  connection.prepareStatement(sql)) {
            statement.setDouble(1, price);
            statement.setString(2, hotelID);
            statement.setString(3, date);
            statement.setString(4, roomID);
            statement.executeUpdate();
        }
    }

    public int getMinimumStay(String hotelID, String date, String roomID) {
        String sql = "SELECT minimum_stay FROM HotelCalendar WHERE hotel_id = ? AND date = ? AND room_id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, hotelID);
            statement.setString(2, date);
            statement.setString(3, roomID);
            try(ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("minimum_stay");
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getPrice(String hotelID, String date, String roomID) throws SQLException{
        String sql = "SELECT price FROM HotelCalendar WHERE hotel_id = ? AND date = ? AND room_id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, hotelID);
            statement.setString(2, date);
            statement.setString(3, roomID);
            try(ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("price");
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getAvailability(String hotelID, String date, String roomID) throws SQLException{
        String sql = "SELECT availability FROM HotelCalendar WHERE hotel_id = ? AND date = ? AND room_id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, hotelID);
            statement.setString(2, date);
            statement.setString(3, roomID);

            try(ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("availability");
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }

    public ArrayList<String> getRoomsAvailableIDs(String hotelID, Research research) throws SQLException {
        ArrayList<String> roomIDs = new ArrayList<>();
        LocalDate checkIn = research.getCheckIn();
        LocalDate checkOut = research.getCheckOut();

        String sql = "SELECT DISTINCT room_id " +
                "FROM HotelCalendar " +
                "WHERE hotel_id = ? " +
                "AND date BETWEEN ? AND ? " +
                "AND availability = 'available' " +
                "AND NOT EXISTS (" +
                "SELECT 1 " +
                "FROM HotelCalendar " +
                "WHERE room_id = HotelCalendar.room_id " +
                "AND date BETWEEN ? AND ? " +
                "AND minimum_stay > ?" +
                ")";

        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, hotelID);
            statement.setString(2, checkIn.toString());
            statement.setString(3, checkOut.toString());
            statement.setString(4, checkIn.toString());
            statement.setString(5, checkOut.toString());
            statement.setInt(6, (int) checkIn.until(checkOut, ChronoUnit.DAYS));

            try(ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("room_id");
                    roomIDs.add(id);
                }
            }
            return roomIDs;
        }
    }
}
