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
            //TODO Vedere se è possible inserire piu valori insieme
            for (Room room : rooms) {
                statement.setString(paramIndex++, room.getId());
                statement.setString(paramIndex++, room.getType().toString());
                statement.setString(paramIndex++, room.getDescription());
                statement.setString(paramIndex++, hotelID);
            }
            statement.executeUpdate();
        }
    }

    public ArrayList<Room> getRoomsByID(String hotelID) throws SQLException {
        ArrayList<Room> rooms = new ArrayList<>();

        String sql = "SELECT * FROM Room WHERE hotel_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, hotelID);

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id");
                String type = rs.getString("type");
                String description = rs.getString("description");
                rooms.add(new Room(id, RoomType.valueOf(type), description));
            }
            return rooms;
        }
    }
}
