package data_access;

import domain_model.Room;
import utilities.RoomType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RoomDAO {
    private final Connection connection;

    public RoomDAO() {
        connection = ConnectionManager.connect();
    }

    public void addRooms(ArrayList<Room> rooms, String hotelID) throws SQLException {
        if(rooms.isEmpty())
            return;

        StringBuilder valuesClause = new StringBuilder();
        for(int i = 0; i < rooms.size(); i++) {
            valuesClause.append("(?,?,?,?)");
            if(i < rooms.size() - 1)
                valuesClause.append(",");
        }

        String sql = "INSERT OR IGNORE INTO Room (id, type, description, hotel_id) VALUES " + valuesClause;
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            int paramIndex = 1;
            for (Room room : rooms) {
                statement.setString(paramIndex++, room.getId());
                statement.setString(paramIndex++, room.getType().toString());
                statement.setString(paramIndex++, room.getDescription());
                statement.setString(paramIndex++, hotelID);
            }
            statement.executeUpdate();
        }
    }

    public ArrayList<Room> getRoomsByHotelID(String hotelID) throws SQLException {
        ArrayList<Room> rooms = new ArrayList<>();

        String sql = "SELECT * FROM Room WHERE hotel_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, hotelID);

            try(ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("id");
                    RoomType type = RoomType.getRoomType(rs.getString("type"));
                    String description = rs.getString("description");
                    rooms.add(new Room(id, type, description));
                }
            }
        }
        return rooms;
    }

    public Room getRoomByID(String roomID) throws SQLException {
        Room room = null;

        String sql = "SELECT * FROM Room WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, roomID);

            try(ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String id = rs.getString("id");
                    RoomType type = RoomType.getRoomType(rs.getString("type"));
                    String description = rs.getString("description");
                    room = new Room(id, type, description);
                }
            }
        }
        return room;
    }

    public void deleteRooms(ArrayList<Room> rooms) throws SQLException {
        String sql = "DELETE FROM Room WHERE id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Room room : rooms) {
                statement.setString(1, room.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }


    public boolean canRoomAccomodate(String id, int numOfGuest) throws SQLException {
        boolean canAccomodate = false;


        String sql = "SELECT * FROM Room WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);

            try(ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    RoomType type = RoomType.getRoomType(rs.getString("type"));
                    if(RoomType.getRoomCapacity(type) >= numOfGuest)
                        canAccomodate = true;
                }
            }
        }
        return canAccomodate;
    }
}
