package data_access;

import domain_model.HotelCalendar;
import domain_model.RoomInfo;
import service_layer.HotelManager;
import service_layer.ReservationManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class HotelCalendarDAO {
    private final Connection connection;

    public HotelCalendarDAO() {
        connection = ConnectionManager.connect();
    }
    public void addCalendar(HotelCalendar calendar) throws SQLException {
        if (calendar.getRoomStatusMap().isEmpty())
            return;

        String sql = "INSERT OR IGNORE INTO HotelCalendar (hotel_id, date , room_id, availability, price, minimum_stay) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            for (Map.Entry<LocalDate, Map<String, RoomInfo>> entry : calendar.getRoomStatusMap().entrySet()) {
                LocalDate date = entry.getKey();
                Map<String, RoomInfo> roomInfoMap = entry.getValue();

                for (Map.Entry<String, RoomInfo> roomEntry : roomInfoMap.entrySet()) {
                    RoomInfo info = roomEntry.getValue();
                    String roomID = roomEntry.getKey();

                    statement.setString(1, calendar.getHotelID());
                    statement.setString(2, date.toString());
                    statement.setString(3, roomID);
                    statement.setString(4, info.getAvailabilityToString());
                    statement.setDouble(5, info.getPrice());
                    statement.setInt(6, info.getMinimumStay());
                    statement.addBatch();
                }
            }
            statement.executeBatch(); //fa un inserimento di massa
        }
    }

    public HotelCalendar getCalendar(String hotelID, HotelManager hotelManager, ReservationManager reservationManager) throws SQLException{
        //Funziona che torna il calendario intero... potremmo magari specificarlo per giorni o per roomID
        String sql = "SELECT * FROM HotelCalendar WHERE hotel_id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, hotelID);
            ResultSet rs = statement.executeQuery();
            HotelCalendar hotelCalendar = new HotelCalendar(hotelID, hotelManager, reservationManager);
            Map<LocalDate, Map<String, RoomInfo>> map = new HashMap<>();

            while (rs.next()) {
                LocalDate date = LocalDate.parse(rs.getString("date"));
                String roomID = rs.getString("room_id");
                double price = rs.getDouble("price");
                int minimumStay = rs.getInt("minimum_stay");
                boolean availability = rs.getString("availability").equals("available");

                RoomInfo roomInfo = new RoomInfo(hotelID, roomID, date,price, minimumStay, availability);

                map.computeIfAbsent(date, k -> new HashMap<>()).put(roomID, roomInfo);
            }
            hotelCalendar.setRoomStatusMap(map);
            return hotelCalendar;
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
}
