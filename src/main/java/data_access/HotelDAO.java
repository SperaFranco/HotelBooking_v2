package data_access;

import domain_model.Hotel;
import domain_model.Room;
import utilities.HotelRating;
import utilities.Research;
import utilities.RoomType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class HotelDAO {
    private final Connection connection;
    private final RoomDAO roomDAO;

    public HotelDAO() {
        connection = ConnectionManager.connect();
        this.roomDAO = new RoomDAO();
    }

    public void addHotel(Hotel hotel) throws SQLException {
        String sql = "INSERT OR IGNORE INTO Hotel (id, name , city, address, telephone, rating, description, director_id) VALUES (?, ?, ?, ?, ?, ?,?,?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, hotel.getId());
            statement.setString(2, hotel.getName());
            statement.setString(3, hotel.getCity());
            statement.setString(4, hotel.getAddress());
            statement.setString(5, hotel.getTelephone());
            statement.setString(6, hotel.getRating().toString());
            statement.setString(7, hotel.getDescription());
            statement.setString(8, hotel.getDirectorID());
            statement.executeUpdate();
        }

        roomDAO.addRooms(hotel.getRooms(), hotel.getId());
    }
    public void removeHotel(Hotel hotel) throws SQLException {
        String sql = "DELETE FROM Hotel WHERE id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, hotel.getId());
            statement.executeUpdate();
        }
        roomDAO.deleteRooms(hotel.getRooms());
    }

    public ArrayList<String> filterHotels(String city) throws SQLException {
        //TODO da implementare filtrando gli hotel secondo i parametri della ricerca che faccio
        //Al momento filtro solo secondo la città
        ArrayList<String> hotels = new ArrayList<>();

        String sql = "SELECT * FROM Hotel WHERE city = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, city);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("id");
                    hotels.add(id);
                }
            }
        }
        return hotels;
    }
    public Hotel getHotelByID(String hotelID) throws SQLException {
        Hotel hotel = null;

        String sql = "SELECT * FROM Hotel WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, hotelID);

            try(ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("id");
                    String name = rs.getString("name");
                    String city = rs.getString("city");
                    String address = rs.getString("address");
                    String telephone = rs.getString("telephone");
                    String rating = rs.getString("rating");
                    String description = rs.getString("description");
                    String director_id = rs.getString("director_id");
                    hotel = new Hotel(id, name, city, address,
                            telephone, HotelRating.getRatingFromString(rating), description, director_id);
                }
            }
        }
        return hotel;
    }
    public ArrayList<Hotel> getHotelsByDirector(String id) throws SQLException {
        ArrayList<Hotel> hotels = new ArrayList<>();

        String sql = "SELECT * FROM Hotel where director_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    String hotel_id = rs.getString("id");
                    String name = rs.getString("name");
                    String city = rs.getString("city");
                    String address = rs.getString("address");
                    String telephone = rs.getString("telephone");
                    String rating = rs.getString("rating");
                    String description = rs.getString("description");
                    String director_id = rs.getString("director_id");
                    Hotel hotel = new Hotel(hotel_id, name, city, address,
                            telephone, HotelRating.getRatingFromString(rating), description, director_id);
                    hotels.add(hotel);
                }
            }
        }
        return hotels;
    }

    //Questa non si usa ma rimane per far vedere quanto era difficile
    public ArrayList<Room> getRoomsAvailable2(String hotelID, Research researchInfo) throws SQLException {
        ArrayList<Room> roomsAvailable = new ArrayList<>();

        String selectQuery = "SELECT r.id AS room_id, r.type AS type, r.description AS description, hc.date " +
                "FROM Room r JOIN HotelCalendar hc on r.id = hc.room_id " +
                "WHERE r.hotel_id = ? AND hc.availability = 'available' " +
                "AND hc.date BETWEEN ? AND ? " +
                "GROUP BY r.id, r.type, hc.date " +
                "HAVING MIN(hc.minimum_stay) <= ? " +
                "AND (r.type = 'Single Room' AND ? <= 1 " +
                "OR r.type = 'Double Room' AND ? <= 2 OR r.type = 'Triple Room' AND ? <= 3)";


        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, hotelID);
            preparedStatement.setString(2, researchInfo.getCheckIn().toString());
            preparedStatement.setString(3, researchInfo.getCheckOut().toString());
            preparedStatement.setInt(4, (int) researchInfo.getCheckIn().until(researchInfo.getCheckOut(), ChronoUnit.DAYS));
            preparedStatement.setInt(5, researchInfo.getNumOfGuest());
            preparedStatement.setInt(6, researchInfo.getNumOfGuest());
            preparedStatement.setInt(7, researchInfo.getNumOfGuest());

            // Esegui la query per ottenere i risultati
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String id = resultSet.getString("room_id");
                    String type = resultSet.getString("type");
                    String description = resultSet.getString("description");
                    roomsAvailable.add(new Room(id, RoomType.getRoomType(type), description));
                }
            }

            return roomsAvailable;
        }
    }

    public RoomDAO getRoomDAO() {
        return roomDAO;
    }


}
