package data_access;

import domain_model.HotelCalendar;
import domain_model.Room;
import domain_model.RoomInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class HotelCalendarDAO {
    private final Connection connection;

    public HotelCalendarDAO() {
        connection = ConnectionManager.connect();
    }
    public void addCalendar(HotelCalendar calendar) throws SQLException {
        //TODO anche qui guardare se è possibile inserire più valori insieme
        Map<LocalDate, Map<String, RoomInfo>> roomStatusMap = calendar.getRoomStatusMap();
        Set<LocalDate> dates = roomStatusMap.keySet();
        for(LocalDate date : dates) {
            Map<String, RoomInfo> roomInfoMap = roomStatusMap.get(date);
            Set<String> rooms = roomInfoMap.keySet();

            for(String room : rooms) {
                RoomInfo info = roomInfoMap.get(room);
                String sql = "INSERT OR IGNORE INTO HotelCalendar (hotel_id, date , room_id, availability, price, minimum_stay) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);

                statement.setString(1, calendar.getHotelID());
                statement.setString(2, date.toString());
                statement.setString(3, room);
                statement.setString(4, info.getAvailabilityToString());
                statement.setDouble(5, info.getPrice());
                statement.setInt(6, info.getMinimumStay());
                statement.executeUpdate();
                statement.close();
            }
        }
    }
}
