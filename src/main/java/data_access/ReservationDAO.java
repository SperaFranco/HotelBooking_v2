package data_access;

import domain_model.Reservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ReservationDAO {
    private final Connection connection;

    public ReservationDAO() {
        connection = ConnectionManager.connect();
    }

    public void addReservation(Reservation reservation) throws SQLException {
        String sql = "INSERT OR IGNORE INTO Reservation (id, check_in, check_out, num_of_guests, notes, hotel_id, room_reserved_id, user_id) VALUES (?, ?, ?, ?, ?, ?,?,?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, reservation.getId());
            statement.setString(2, reservation.getCheckIn().toString());
            statement.setString(3, reservation.getCheckOut().toString());
            statement.setInt(4, reservation.getNumOfGuests());
            statement.setString(5, reservation.getNotes());
            statement.setString(6, reservation.getHotel());
            statement.setString(7, reservation.getRoomReserved());
            statement.setString(8, reservation.getClient());
            statement.executeUpdate();
        }
    }

    public void removeReservation(String id) throws SQLException {
        String sql = "DELETE FROM Reservation WHERE id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        }
    }

    public ArrayList<Reservation> getReservationByGuest(String guest_id) throws SQLException{
        ArrayList<Reservation> reservations = new ArrayList<>();

        String sql = "SELECT * FROM Reservation WHERE user_id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, guest_id);

            try(ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    LocalDate checkIn = LocalDate.parse(resultSet.getString("check_in"));
                    LocalDate checkOut = LocalDate.parse(resultSet.getString("check_out"));
                    int numOfGuests = resultSet.getInt("num_of_guests");
                    String notes = resultSet.getString("notes");
                    String hotelID = resultSet.getString("hotel_id");
                    String roomID = resultSet.getString("room_reserved_id");
                    String userID = resultSet.getString("user_id");

                    reservations.add(new Reservation(id, checkIn, checkOut, numOfGuests, notes, hotelID, roomID, userID));
                }
                return reservations;
            }
        }
    }

    public ArrayList<Reservation> getReservationByHotel(String hotel_id) throws SQLException {
        ArrayList<Reservation> reservations = new ArrayList<>();

        String sql = "SELECT * FROM Reservation WHERE hotel_id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, hotel_id);

            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    LocalDate checkIn = LocalDate.parse(resultSet.getString("check_in"));
                    LocalDate checkOut = LocalDate.parse(resultSet.getString("check_out"));
                    int numOfGuests = resultSet.getInt("num_of_guests");
                    String notes = resultSet.getString("notes");
                    String hotelID = resultSet.getString("hotel_id");
                    String roomID = resultSet.getString("room_reserved_id");
                    String userID = resultSet.getString("user_id");

                    reservations.add(new Reservation(id, checkIn, checkOut, numOfGuests, notes, hotelID, roomID, userID));
                }
                return reservations;
            }
        }
    }

    public void setNotes(String id, String notes) throws SQLException {
        String sql = "UPDATE Reservation SET notes = ? WHERE id = ?";
        try(PreparedStatement statement =  connection.prepareStatement(sql)) {
            statement.setString(1, notes);
            statement.setString(2, id);
            statement.executeUpdate();
        }
    }

    public void setCheckIn(String id, String checkIn) throws SQLException {
        String sql = "UPDATE Reservation SET check_in = ? WHERE id = ?";
        try(PreparedStatement statement =  connection.prepareStatement(sql)) {
            statement.setString(1, checkIn);
            statement.setString(2, id);
            statement.executeUpdate();
        }
    }

    public void setCheckOut(String id, String checkOut) throws SQLException {
        String sql = "UPDATE Reservation SET check_out = ? WHERE id = ?";
        try(PreparedStatement statement =  connection.prepareStatement(sql)) {
            statement.setString(1, checkOut);
            statement.setString(2, id);
            statement.executeUpdate();
        }
    }
}
